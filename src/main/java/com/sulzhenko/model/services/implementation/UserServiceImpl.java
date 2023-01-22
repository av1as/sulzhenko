package com.sulzhenko.model.services.implementation;

import com.sulzhenko.Util.notifications.Mailer;
import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.DAO.implementation.UserDAOImpl;
import com.sulzhenko.DTO.UserDTO;
import com.sulzhenko.model.entity.User;
import com.sulzhenko.Util.notifications.NotificationFactories;
import com.sulzhenko.Util.notifications.NotificationFactory;
import com.sulzhenko.model.hashingPasswords.Sha;
import com.sulzhenko.model.services.ServiceException;
import com.sulzhenko.model.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.sulzhenko.model.services.validator.InputValidator.*;

public class UserServiceImpl implements UserService {
    private final DataSource dataSource;
    UserDAO userDAO;
    Sha sha;
    public UserServiceImpl(DataSource dataSource){
        this.dataSource = dataSource;
        this.userDAO = new UserDAOImpl(dataSource);
        this.sha = new Sha();
    }
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    @Override
    public User getUser(String login){
        return userDAO.getByLogin(login).orElse(null);
    }
    public UserDTO getUserDTO(String login) {
        User user = getUser(login);
        if (user != null) {
            return new UserDTO.Builder().withAccount(user.getAccount()).withLogin(login)
                    .withPassword(user.getPassword())
                    .withEmail(user.getEmail())
                    .withFirstName(user.getFirstName())
                    .withLastName(user.getLastName())
                    .withRole(user.getRole().getValue())
                    .withStatus(user.getStatus())
                    .withNotification(user.getNotification())
                    .build();
        } else return null;
    }
    @Override
    public void addUser(UserDTO userDTO, String passwordConfirm) {
        User user = new User.Builder()
                .withLogin(userDTO.getLogin())
                .withEmail(userDTO.getEmail())
                .withPassword(userDTO.getPassword())
                .withFirstName(userDTO.getFirstName())
                .withLastName(userDTO.getLastName())
                .withRole(userDTO.getRole().getValue())
                .withStatus(userDTO.getStatus())
                .withNotification(userDTO.getNotification())
                .build();
        try{
            isDataCorrect(user, passwordConfirm);
            userDAO.save(user);
        } catch (DAOException e){
            logger.warn(e.getMessage());
            throw new ServiceException(e);
        }
    }
    @Override
    public void updateUser(UserDTO t, String[] params){
        User user = getUser(t.getLogin());
        try {
            isUpdateCorrect(params, t.getLogin());
            if(Objects.equals(params[2], ""))  params[2] = t.getPassword();
            else params[2] = sha.hashToHex(params[2], Optional.ofNullable(params[0]));
            userDAO.update(user, params);
            notifyAboutUpdate(user);
        } catch (DAOException | UnsupportedEncodingException | NoSuchAlgorithmException e){
            logger.warn(e.getMessage());
            throw new ServiceException(UNKNOWN_ERROR);
        }
    }
    @Override
    public void adminUpdateUser(UserDTO userDTO, String[] params){
        try {
            isAdminUpdateCorrect(params);
            params[2] = userDTO.getPassword();
            User user = getUser(userDTO.getLogin());
            userDAO.update(user, params);
            notifyAboutUpdate(user);
        } catch(DAOException | ServiceException e){
            logger.warn(e);
            throw new ServiceException(e);
        }
    }
    @Override
    public void deleteUser(User user) throws DAOException {
        if(!isLoginAvailable(user.getLogin())) {
            userDAO.delete(user);
        } else {
            logger.warn("wrong.user: {}", user.getLogin());
            throw new ServiceException(WRONG_LOGIN);
        }
    }
    @Override
    public List<UserDTO> viewAllSystemUsers(int startPosition, int size){
        List<UserDTO> list = new ArrayList<>();
        for(int i = startPosition; (i < (startPosition + size))
                && (i < userDAO.getByRole(SYSTEM_USER).size()); i++){
            User user = userDAO.getByRole(SYSTEM_USER).get(i);
            UserDTO userDTO = getUserDTO(user.getLogin());
            list.add(userDTO);
        }
        return list;
    }
    @Override
    public List<UserDTO> viewAllActiveUsers(int startPosition, int size){
        List<UserDTO> list = new ArrayList<>();
        for(int i = startPosition; (i < (startPosition + size))
                && (i < new UserDAOImpl(dataSource).getByStatus(ACTIVE).size()); i++){
            User user = userDAO.getByStatus(ACTIVE).get(i);
            UserDTO userDTO = getUserDTO(user.getLogin());
            list.add(userDTO);
        }
        return list;
    }
    @Override
    public List<UserDTO> viewAllInactiveUsers(int startPosition, int size){
        List<UserDTO> list = new ArrayList<>();
        for(int i = startPosition; (i < (startPosition + size))
                && (i < new UserDAOImpl(dataSource).getByStatus(INACTIVE).size()); i++){
            User user = userDAO.getByStatus(INACTIVE).get(i);
            UserDTO userDTO = getUserDTO(user.getLogin());
            list.add(userDTO);
        }
        return list;
    }
    @Override
    public List<UserDTO> viewAllDeactivatedUsers(int startPosition, int size){
        List<UserDTO> list = new ArrayList<>();
        for(int i = startPosition; (i < (startPosition + size))
                && (i < new UserDAOImpl(dataSource).getByStatus(DEACTIVATED).size()); i++){
            User user = userDAO.getByStatus(DEACTIVATED).get(i);
            UserDTO userDTO = getUserDTO(user.getLogin());
            list.add(userDTO);
        }
        return list;
    }
    public List<UserDTO> getUserList(String status, int page, int recordsPerPage){
        if(Objects.equals(status, ACTIVE)) {
            return viewAllActiveUsers((page-1)*recordsPerPage, recordsPerPage);
        } else if(Objects.equals(status, INACTIVE)) {
            return viewAllInactiveUsers((page-1)*recordsPerPage, recordsPerPage);
        } else if(Objects.equals(status, DEACTIVATED)) {
            return viewAllDeactivatedUsers((page-1)*recordsPerPage, recordsPerPage);
        } else {
            return viewAllSystemUsers((page-1)*recordsPerPage, recordsPerPage);
        }
    }
    private void isDataCorrect(User t, String passwordConfirm) {
        String errorMessage = validateNewUser(t, passwordConfirm);
        if (!isRoleCorrect(t.getRole().value)){
            throw new ServiceException(WRONG_ROLE);
        } else if (!isStatusCorrect(t.getStatus())){
            throw new ServiceException(WRONG_STATUS);
        } else if (t.getLogin() == null || !isLoginAvailable(t.getLogin())){
            throw new ServiceException(WRONG_LOGIN);
        } else if(!Objects.equals(errorMessage, null)){
            throw new ServiceException(errorMessage);
        }
    }
    @Override
    public boolean isLoginAvailable(String login){
        return userDAO.getByLogin(login).isEmpty();
    }
    @Override
    public boolean isRoleCorrect(String role) throws DAOException{
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQLQueries.UserQueries.FIND_ROLE_BY_DESCRIPTION)) {
            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            logger.fatal(e);
            throw new ServiceException(UNKNOWN_ERROR, e);
        }
        return false;
    }
    @Override
    public boolean isStatusCorrect(String status) throws DAOException{
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQLQueries.UserQueries.FIND_STATUS_BY_NAME)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            logger.fatal(e);
            throw new ServiceException(UNKNOWN_ERROR);
        }
        return false;
    }
    @Override
    public void isUpdateCorrect(String[] params, String passwordConfirm) {
        User user = new User.Builder().withLogin(params[0]).withEmail(params[1]).withPassword(params[2]).withFirstName(params[3]).withLastName(params[4]).build();
        String errorMessage = validateUserUpdate(user);
        if (!Objects.equals(errorMessage, null)){
            throw new ServiceException(errorMessage);
        }
    }
    public void isAdminUpdateCorrect(String[] params) {
        User user = new User.Builder().withLogin(params[0]).withEmail(params[1]).withPassword(params[2]).withFirstName(params[3]).withLastName(params[4]).build();
        String errorMessage = validateAdminUserUpdate(user);
        if(!Objects.equals(errorMessage, null)){
            throw new ServiceException(errorMessage);
        }
    }
    public String areFieldsIncorrect(String login, String password) {
        String errorMessage = null;
        if (getUser(login) == null) {
            errorMessage = WRONG_LOGIN;
            logger.warn(errorMessage);
        } else {
            try {
                if (!new Sha().hashToHex(password, Optional.ofNullable(login))
                        .equals(getUser(login).getPassword())) {
                    errorMessage = WRONG_PASSWORD;
                    logger.warn(errorMessage);
                }
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                errorMessage = UNKNOWN_ERROR;
                logger.fatal(e);
                throw new ServiceException(errorMessage);
            }
        }
        return errorMessage;
    }
    public String areFieldsBlank(HttpServletRequest request) {
        String errorMessage = null;
        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);
        if (login == null || login.isEmpty()) {
            errorMessage = EMPTY_LOGIN;
            logger.warn(errorMessage);
        } else if (password == null || password.isEmpty()) {
            errorMessage = EMPTY_PASSWORD;
            logger.warn(errorMessage);
        }
        return errorMessage;
    }
    public int getNumberOfRecords(String status){
        if(Objects.equals(status, ACTIVE)) {
            return userDAO.getByStatus(ACTIVE).size();
        } else if(Objects.equals(status, INACTIVE)) {
            return userDAO.getByStatus(INACTIVE).size();
        } else if(Objects.equals(status, DEACTIVATED)) {
            return userDAO.getByStatus(DEACTIVATED).size();
        } else {
            return userDAO.getByRole(SYSTEM_USER).size();
        }
    }
    public String getErrorMessageUpdate(String login, String password,
                                        String newPassword, String newPasswordConfirm){
        String errorMessage = null;
        if(!Objects.equals(newPassword, newPasswordConfirm)) {
            errorMessage = DIFFERENT_PASSWORDS;
        } else if (login == null ) {
            errorMessage = WRONG_LOGIN;
        } else {
            try {
                if (!sha.hashToHex(password, Optional.of(login)).equals(getUser(login).getPassword())) {
                    errorMessage = WRONG_PASSWORD;
                }
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                logger.fatal(e);
                errorMessage = UNKNOWN_ERROR;
            }
        }
        return errorMessage;
    }
    public String validateNewUser(User user, String passwordConfirm){
        String result;
        if(!isValid(user.getLogin(), LOGIN_REGEX)) {
            result = LOGIN_ERROR;
        } else if(!Objects.equals(user.getPassword(), passwordConfirm)){
            result = DIFFERENT_PASSWORDS;
        } else if(!isValid(user.getPassword(), PASSWORD_REGEX)){
            result = PASSWORD_ERROR;
        } else if(!isValid(user.getEmail(), EMAIL_REGEX)) {
            result = EMAIL_ERROR;
        } else if(!user.getFirstName().isEmpty() && !isValid(user.getFirstName(), NAME_REGEX)){
            result = NAME_ERROR;
        } else if(!user.getLastName().isEmpty() && !isValid(user.getLastName(), NAME_REGEX)){
            result = NAME_ERROR;
        } else result = null;
        return result;
    }
    public String validateUserUpdate(User user){
        String result;
        if(!user.getPassword().isEmpty() && !isValid(user.getPassword(), PASSWORD_REGEX)){
            result = PASSWORD_ERROR;
        } else  if(!isValid(user.getEmail(), EMAIL_REGEX)){
            result = EMAIL_ERROR;
        } else if(!user.getFirstName().isEmpty() && !isValid(user.getFirstName(), NAME_REGEX)){
            result = NAME_ERROR;
        } else if(!user.getLastName().isEmpty() && !isValid(user.getLastName(), NAME_REGEX)){
            result = NAME_ERROR;
        } else result = null;
        return result;
    }
    public String validateAdminUserUpdate(User user){
        String result;
        if(!isValid(user.getEmail(), EMAIL_REGEX)){
            result = EMAIL_ERROR;
        } else if(!user.getFirstName().isEmpty() && !isValid(user.getFirstName(), NAME_REGEX)){
            result = NAME_ERROR;
        } else if(!user.getLastName().isEmpty() && !isValid(user.getLastName(), NAME_REGEX)){
            result = NAME_ERROR;
        } else result = null;
        return result;
    }
    @Override
    public void notifyAboutUpdate(User user){
            NotificationFactory factory = new NotificationFactories().accountUpdateFactory(user);
            String subject = factory.createSubject();
            String body = factory.createBody();
            Mailer.send(user.getEmail(), subject, body);
    }
}
