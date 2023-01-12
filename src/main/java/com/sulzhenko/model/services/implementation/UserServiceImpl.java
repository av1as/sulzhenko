package com.sulzhenko.model.services.implementation;

import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.DAO.implementation.UserDAOImpl;
import com.sulzhenko.model.entity.User;
import com.sulzhenko.model.hashingPasswords.Sha;
import com.sulzhenko.model.services.ServiceException;
import com.sulzhenko.model.services.UserService;
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

import static com.sulzhenko.model.services.validator.InputValidator.validateUser;

public class UserServiceImpl implements UserService {
    public static final String SOMETHING_WENT_WRONG = "unknown.error";
    private final DataSource dataSource;
    UserDAO userDAO;
    public UserServiceImpl(DataSource dataSource){
        this.dataSource = dataSource;
        this.userDAO = new UserDAOImpl(dataSource);
    }
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    @Override
    public User getUser(String login){
        return userDAO.getByLogin(login).orElse(null);
    }
    @Override
    public void addUser(User t) {
        try{
            isDataCorrect(t);
            userDAO.save(t);
        } catch (DAOException e){
            logger.warn(e.getMessage());
            throw new ServiceException(e);
        }
    }
    @Override
    public void updateUser(User t, String[] params){
        String oldLogin = t.getLogin();
        try {
            isUpdateCorrect(params, oldLogin);
            params[2] = new Sha().hashToHex(params[2], Optional.ofNullable(params[0]));
            userDAO.update(t, params);
            notifyAboutUpdate(t, "updated");
        } catch (DAOException | UnsupportedEncodingException | NoSuchAlgorithmException e){
            logger.warn(e.getMessage());
            throw new ServiceException(e);
        }
    }
    @Override
    public void adminUpdateUser(User t, String[] params){
        String oldLogin = t.getLogin();
        isAdminUpdateCorrect(params, oldLogin);
        userDAO.update(t, params);
        notifyAboutUpdate(t, "updated");
    }
    @Override
    public void deleteUser(User t) throws DAOException {
        if(!isLoginAvailable(t.getLogin())) {
            userDAO.delete(t);
        } else {
            logger.info("wrong.user: {}", t.getLogin());
            throw new ServiceException("wrong.login");
        }
    }
    @Override
    public List<User> viewAllSystemUsers(int startPosition, int size){
        List<User> list = new ArrayList<>();
        for(int i = startPosition; (i < (startPosition + size)) && (i < new UserDAOImpl(dataSource).getByRole("system user").size()); i++){
            list.add(userDAO.getByRole("system user").get(i));
        }
        return list;
    }
    @Override
    public List<User> viewAllActiveUsers(int startPosition, int size){
        List<User> list = new ArrayList<>();
        for(int i = startPosition; (i < (startPosition + size)) && (i < new UserDAOImpl(dataSource).getByStatus("active").size()); i++){
            list.add(userDAO.getByStatus("active").get(i));
        }
        return list;
    }
    @Override
    public List<User> viewAllInactiveUsers(int startPosition, int size){
        List<User> list = new ArrayList<>();
        for(int i = startPosition; (i < (startPosition + size)) && (i < new UserDAOImpl(dataSource).getByStatus("inactive").size()); i++){
            list.add(userDAO.getByStatus("inactive").get(i));
        }
        return list;
    }
    @Override
    public List<User> viewAllDeactivatedUsers(int startPosition, int size){
        List<User> list = new ArrayList<>();
        for(int i = startPosition; (i < (startPosition + size)) && (i < new UserDAOImpl(dataSource).getByStatus("deactivated").size()); i++){
            list.add(userDAO.getByStatus("deactivated").get(i));
        }
        return list;
    }
    private void isDataCorrect(User t) {
        if (!isRoleCorrect(t.getRole().value)){
            throw new ServiceException("wrong.role");
        } else if (!isStatusCorrect(t.getStatus())){
            throw new ServiceException("wrong.status");
        } else if (!isLoginAvailable(t.getLogin())){
            throw new ServiceException("wrong.login");
        } else if(!Objects.equals(validateUser(t), "")){
            throw new ServiceException(validateUser(t));
        }
    }
    @Override
    public boolean isLoginAvailable(String login){
        return userDAO.getByLogin(login).isEmpty();
    }
    @Override
    public boolean isRoleCorrect(String role) throws DAOException{
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQLQueries.UserQueries.FIND_ROLE_BY_DESCRIPTION)
        ) {
            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            logger.fatal("unknown exception when checking role");
            throw new ServiceException(SOMETHING_WENT_WRONG, e);
        }
        return false;
    }
    @Override
    public boolean isStatusCorrect(String status) throws DAOException{
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQLQueries.UserQueries.FIND_STATUS_BY_NAME)
        ) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            logger.fatal("unknown exception when checking status");
            throw new ServiceException(SOMETHING_WENT_WRONG, e);
        }
        return false;
    }
    @Override
    public void isUpdateCorrect(String[] params, String oldLogin) {
        User user = new User.Builder().withLogin(params[0]).withEmail(params[1]).withPassword(params[2]).build();
        if (!isRoleCorrect(params[5])){
            throw new ServiceException("wrong.role");
        } else if (!isStatusCorrect(params[6])){
            throw new ServiceException("wrong.status");
        } else if (!Objects.equals(params[0], oldLogin) && !isLoginAvailable(params[0])){
            throw new ServiceException("wrong.login");
        } else if (isLoginAvailable(oldLogin)){
            throw new ServiceException("wrong.login");
        } else if(!Objects.equals(validateUser(user), "")){
            throw new ServiceException(validateUser(user));
        }
    }
    private void isAdminUpdateCorrect(String[] params, String oldLogin) {
        if (!isRoleCorrect(params[5])){
            throw new ServiceException("wrong.role");
        } else if (!isStatusCorrect(params[6])){
            throw new ServiceException("wrong.status");
        } else if (!Objects.equals(params[0], oldLogin) && !isLoginAvailable(params[0])){
            throw new ServiceException("wrong.login");
        } else if (isLoginAvailable(oldLogin)){
            throw new ServiceException("wrong.login");
        }
    }
    public String areFieldsIncorrect(String login, String password) {
        String errorMessage = null;
        if (getUser(login) == null) {
            errorMessage = "wrong.login";
        } else {
            try {
                if (!new Sha().hashToHex(password, Optional.ofNullable(login)).equals(getUser(login).getPassword())) {
                    errorMessage = "wrong.password";
                }
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                errorMessage = SOMETHING_WENT_WRONG;
                throw new ServiceException(errorMessage);
            }
        }
        logger.warn(errorMessage);
        return errorMessage;
    }
    public int getNumberOfRecords(String status){
        if(Objects.equals(status, "active")) {
            return userDAO.getByStatus("active").size();
        } else if(Objects.equals(status, "inactive")) {
            return userDAO.getByStatus("inactive").size();
        } else if(Objects.equals(status, "deactivated")) {
            return userDAO.getByStatus("deactivated").size();
        } else {
            return userDAO.getByRole("system user").size();
        }
    }
    public void notifyAboutUpdate(User u, String description){

        // create method to notify connected users



    }
}
