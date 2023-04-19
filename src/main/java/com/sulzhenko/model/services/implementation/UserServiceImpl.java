package com.sulzhenko.model.services.implementation;

import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.DAO.implementation.UserDAOImpl;
import com.sulzhenko.DTO.UserDTO;
import com.sulzhenko.model.entity.User;
import com.sulzhenko.Util.notifications.NotificationFactories;
import com.sulzhenko.Util.notifications.NotificationFactory;
import com.sulzhenko.Util.hashingPasswords.Sha;
import com.sulzhenko.model.services.ServiceException;
import com.sulzhenko.model.services.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import static com.sulzhenko.Util.notifications.Mailer.send;
import static com.sulzhenko.Util.validator.InputValidator.*;

/**
 * UserService class for interaction between controller and User DAO
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final Sha sha;
    public UserServiceImpl(DataSource dataSource){
        this.userDAO = new UserDAOImpl(dataSource);
        this.sha = new Sha();
    }
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    /**
     * Gets instance of User by login
     * @param login - value of user login
     * @return instance of User class
     */
    @Override
    public User getUser(String login){
        return userDAO.getByLogin(login).orElse(null);
    }

    /**
     * Gets instance of UserDTO by login
     * @param login - value of user login
     * @return instance of UserDTO class
     */
    @Override
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

    /**
     * Inserts new user to database
     * @param userDTO - UserDTO
     * @param passwordConfirm - confirmation of password
     * @throws ServiceException is wrapper for DAOException
     */
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

    /**
     * Updates user
     * @param userDTO - UserDTO to update
     * @param params - list of new fields
     * @throws ServiceException is wrapper for DAOException
     */
    @Override
    public void updateUser(UserDTO userDTO, String[] params){
        User user = getUser(userDTO.getLogin());
        try {
            isUpdateCorrect(params, userDTO.getLogin());
            if(params[2].isEmpty())  params[2] = userDTO.getPassword();
            else params[2] = sha.hashToHex(params[2], Optional.ofNullable(params[0]));
            userDAO.update(user, params);
            notifyAboutUpdate(user);
        } catch (DAOException | UnsupportedEncodingException | NoSuchAlgorithmException e){
            logger.warn(e.getMessage());
            throw new ServiceException(UNKNOWN_ERROR);
        }
    }

    /**
     * Sends temporary password
     * @param login - user login
     * @throws ServiceException is wrapper for DAOException
     */
    @Override
    public void recoverPassword(String login){
        User user = userDAO.getByLogin(login).orElse(null);
        String randomPassword = RandomStringUtils.randomAlphanumeric(10);
        if(user != null) {
            try {
                String[] param = {login, user.getEmail(),
                        sha.hashToHex(randomPassword, Optional.ofNullable(login)),
                        user.getFirstName(), user.getLastName(), user.getRole().value,
                        user.getStatus(), user.getNotification()};
                userDAO.update(user, param);
                sendTemporaryPassword(user, randomPassword);
            } catch (DAOException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
                logger.warn(e.getMessage());
                throw new ServiceException(UNKNOWN_ERROR);
            }
        } else {
            logger.warn(WRONG_LOGIN + login);
            throw new ServiceException(WRONG_LOGIN);
        }
    }
    /**
     * Updates user by admin
     * @param userDTO - UserDTO to update
     * @param params - new user fields
     * @throws ServiceException is wrapper for DAOException
     */
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

    /**
     * Deletes user record
     * @param user - user
     * @throws ServiceException is custom Exception
     */
    @Override
    public void deleteUser(User user) throws ServiceException {
        if(!isLoginAvailable(user.getLogin())) {
            userDAO.delete(user);
        } else {
            logger.warn("wrong.user: {}", user.getLogin());
            throw new ServiceException(WRONG_LOGIN);
        }
    }

    /**
     * Gets sorted and ordered list of user records in database
     * @param startPosition - offset for list
     * @param size - number of records per page
     * @return List of UserDTO
     */
    @Override
    public List<UserDTO> viewAllSystemUsers(int startPosition, int size){
        List<UserDTO> list = new ArrayList<>();
        List<User> users = userDAO.getByRole(SYSTEM_USER);
        for(int i = startPosition; (i < (startPosition + size)) && (i < users.size()); i++){
            list.add(getUserDTO(users.get(i).getLogin()));
        }
        return list;
    }

    /**
     * Gets sorted and ordered list of user records in database with status 'active'
     * @param startPosition - offset for list
     * @param size - number of records per page
     * @return List of UserDTO
     */
    @Override
    public List<UserDTO> viewAllActiveUsers(int startPosition, int size){
        List<UserDTO> list = new ArrayList<>();
        List<User> users = userDAO.getByStatus(ACTIVE);
        for(int i = startPosition; (i < (startPosition + size)) && (i < users.size()); i++){
            list.add(getUserDTO(users.get(i).getLogin()));
        }
        return list;
    }

    /**
     * Gets sorted and ordered list of user records in database with status 'inactive'
     * @param startPosition - offset for list
     * @param size - number of records per page
     * @return List of UserDTO
     */
    @Override
    public List<UserDTO> viewAllInactiveUsers(int startPosition, int size){
        List<UserDTO> list = new ArrayList<>();
        List<User> users = userDAO.getByStatus(INACTIVE);
        for(int i = startPosition; (i < (startPosition + size)) && (i < users.size()); i++){
            list.add(getUserDTO(users.get(i).getLogin()));
        }
        return list;
    }

    /**
     * Gets sorted and ordered list of user records in database with status 'deactivated'
     * @param startPosition - offset for list
     * @param size - number of records per page
     * @return List of UserDTO
     */
    @Override
    public List<UserDTO> viewAllDeactivatedUsers(int startPosition, int size){
        List<UserDTO> list = new ArrayList<>();
        List<User> users = userDAO.getByStatus(DEACTIVATED);
        for(int i = startPosition; (i < (startPosition + size)) && (i < users.size()); i++){
            list.add(getUserDTO(users.get(i).getLogin()));
        }
        return list;
    }

    /**
     * Gets sorted and ordered list of user records in database according to filter
     * @param status - set filter for list
     * @param page - number of page in list
     * @param recordsPerPage - number of records per page
     * @return List of UserDTO
     */
    @Override
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

    /**
     * Auxiliary method to check if input data are correct
     * @param user - user to check
     * @param passwordConfirm - confirmation of password
     * @throws ServiceException is custom exception
     */
    private void isDataCorrect(User user, String passwordConfirm) {
        String errorMessage = validateNewUser(user, passwordConfirm);
        if (!userDAO.isRoleCorrect(user.getRole().value)){
            throw new ServiceException(WRONG_ROLE);
        } else if (!userDAO.isStatusCorrect(user.getStatus())){
            throw new ServiceException(WRONG_STATUS);
        } else if (user.getLogin() == null){
            throw new ServiceException(WRONG_LOGIN);
        } else if (!isLoginAvailable(user.getLogin())){
            throw new ServiceException(DUPLICATE_LOGIN);
        } else if(!Objects.equals(errorMessage, null)){
            throw new ServiceException(errorMessage);
        }
    }
    /**
     * Checks if user login is not already used in database
     * @param login - login to check
     * @return true if login is not used, false otherwise
     */
    @Override
    public boolean isLoginAvailable(String login){
        return userDAO.getByLogin(login).isEmpty();
    }

    /**
     * Checks if input data for user update are correct
     * @param params - set of new user fields
     * @param passwordConfirm - confirmation of password
     * @throws ServiceException if data are incorrect
     */
    @Override
    public void isUpdateCorrect(String[] params, String passwordConfirm) {
        User user = new User.Builder()
                .withLogin(params[0])
                .withEmail(params[1])
                .withPassword(params[2])
                .withFirstName(params[3])
                .withLastName(params[4])
                .build();
        String errorMessage = validateUserUpdate(user);
        if (!Objects.equals(errorMessage, null)){
            throw new ServiceException(errorMessage);
        }
    }

    /**
     * Checks if input data for user update by admin are correct
     * @param params - set of new user fields
     * @throws ServiceException if data are incorrect
     */
    @Override
    public void isAdminUpdateCorrect(String[] params) {
        User user = new User.Builder()
                .withLogin(params[0])
                .withEmail(params[1]).withPassword(params[2])
                .withFirstName(params[3])
                .withLastName(params[4])
                .build();
        String errorMessage = validateAdminUserUpdate(user);
        if(!Objects.equals(errorMessage, null)){
            throw new ServiceException(errorMessage);
        }
    }

    /**
     * Checks if input data are correct
     * @param login - user login
     * @param password - user password
     * @return error message if fields are incorrect, null otherwise
     * @throws ServiceException is wrapper for NoSuchAlgorithmException, UnsupportedEncodingException
     */
    @Override
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

    /**
     * Checks if input data are blank
     * @param login - user login
     * @param password - user password
     * @return error message if fields are blank, null otherwise
     */

    public static String areFieldsBlank(String login, String password) {
        String errorMessage = null;
        if (login == null || login.isEmpty()) {
            errorMessage = EMPTY_LOGIN;
            logger.warn(errorMessage);
        } else if (password == null || password.isEmpty()) {
            errorMessage = EMPTY_PASSWORD;
            logger.warn(errorMessage);
        }
        return errorMessage;
    }

    /**
     * Gets number of records in database according to filter
     * @param status - filter for accounting records (user status)
     * @return number of records
     */
    @Override
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

    /**
     * Checks if input data for update are correct
     * @param login - user login
     * @param password - user password
     * @param newPassword - new user password
     * @param newPasswordConfirm - confirmation of new user password
     * @return error message if data are incorrect, null otherwise
     */
    @Override
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

    /**
     * Checks if input data for new user record are valid
     * @param user - user entity
     * @param passwordConfirm - user password confirmation
     * @return error message if fields are not valid, null otherwise
     */

    public static String validateNewUser(User user, String passwordConfirm){
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

    /**
     * Checks if input data for user update are valid
     * @param user - user entity
     * @return error message if fields are not valid, null otherwise
     */

    public static String validateUserUpdate(User user){
        String errorMessage;
        if(!user.getPassword().isEmpty() && !isValid(user.getPassword(), PASSWORD_REGEX)){
            errorMessage = PASSWORD_ERROR;
        } else if(!isValid(user.getEmail(), EMAIL_REGEX)){
            errorMessage = EMAIL_ERROR;
        } else if(!user.getFirstName().isEmpty() && !isValid(user.getFirstName(), NAME_REGEX)){
            errorMessage = NAME_ERROR;
        } else if(!user.getLastName().isEmpty() && !isValid(user.getLastName(), NAME_REGEX)){
            errorMessage = NAME_ERROR;
        } else errorMessage = null;
        return errorMessage;
    }

    /**
     * Checks if input data for user update by admin are valid
     * @param user - user entity
     * @return error message if fields are not valid, null otherwise
     */

    public static String validateAdminUserUpdate(User user){
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

    /**
     * Creates and sends emails to certain user about update their account
     * @param user - user
     */

    public static void notifyAboutUpdate(User user){
            NotificationFactory factory = new NotificationFactories().accountUpdateFactory(user);
            String subject = factory.createSubject();
            String body = factory.createBody();
            send(user.getEmail(), subject, body);
    }

    /**
     * Creates and sends emails with temporary password to user who asked for recovering password
     * @param user - user
     * @param temporaryPassword - temporary password
     */
    private static void sendTemporaryPassword(User user, String temporaryPassword){
        NotificationFactory factory = new NotificationFactories().recoverPasswordFactory(user, temporaryPassword);
        String subject = factory.createSubject();
        String body = factory.createBody();
        send(user.getEmail(), subject, body);
    }
}
