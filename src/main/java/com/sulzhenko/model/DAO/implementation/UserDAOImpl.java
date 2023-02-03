package com.sulzhenko.model.DAO.implementation;

import com.sulzhenko.model.Constants;
import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.entity.User;
import com.sulzhenko.Util.hashingPasswords.Sha;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static com.sulzhenko.model.DAO.SQLQueries.UserQueries.*;

/**
 * User DAO class for CRUD operations with database. Matches tables 'user', 'user_status',
 * 'role' in database.
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class UserDAOImpl implements UserDAO, Constants {
    /** An instance of datasource to provide connection to database */
    private final DataSource dataSource;
    public UserDAOImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }
    private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);

    /**
     * Gets instance of User from database by some parameter
     * @param parameter - value of some field in database
     * @param querySQL - String representation for SQL query
     * @return Optional.ofNullable - user is null if there is no user
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public Optional<User> get(Object parameter, String querySQL) throws DAOException{
        User user = null;
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(querySQL)) {
            stmt.setObject(1, parameter);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = getUserWithFields(rs);
            }
        } catch (SQLException e){
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
        return Optional.ofNullable(user);
    }

    /**
     * Gets list of Users from database by some parameter
     * @param parameter - value of some field in database
     * @param querySQL - String representation for SQL query
     * @return List of User entities
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public List<User> getList(Object parameter, String querySQL) throws DAOException{
        List<User> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(querySQL)) {
            stmt.setObject(1, parameter);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getUserWithFields(rs));
            }
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
        return list;
    }

    /**
     * Gets instance of User from database by id
     * @param id - value of account field in database
     * @return Optional.ofNullable - user is null if there is no user
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public Optional<User> getById(Long id) throws DAOException {
        return get(id, GET_USER_BY_ID);
    }

    /**
     * Gets instance of User from database by login
     * @param login - value of login field in database
     * @return Optional.ofNullable - user is null if there is no user
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public Optional<User> getByLogin(String login) throws DAOException {
        return get(login, GET_USER_BY_LOGIN);
    }

    /**
     * Gets list of Users from database by email
     * @param email - value of email field in database
     * @return List of User entities
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public List<User> getByEmail(String email) throws DAOException {
        return getList(email, GET_USERS_BY_EMAIL);
    }

    /**
     * Gets list of Users from database by first name
     * @param firstName - value of first_name field in database
     * @return List of User entities
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public List<User> getByFirstName(String firstName) throws DAOException {
        return getList(firstName, GET_USERS_BY_FIRST_NAME);
    }

    /**
     * Gets instance of User from database by id
     * @param lastName - value of last_name field in database
     * @return List of User entities
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public List<User> getByLastName(String lastName) throws DAOException {
        return getList(lastName, GET_USERS_BY_LAST_NAME);
    }

    /**
     * Gets list of Users from database by role
     * @param role - value from 'role' table that matches role_id field in database
     * @return List of User entities
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public List<User> getByRole(String role) throws DAOException {
        return getList(role, GET_USERS_BY_ROLE);
    }

    /**
     * Gets list of Users from database by status
     * @param status - value from 'user_status' table that matches status_id field in database
     * @return List of User entities
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public List<User> getByStatus(String status) throws DAOException {
        return getList(status, GET_USERS_BY_STATUS);
    }

    /**
     * Gets list of Users from database by notification
     * @param notification - value of notification field in database
     * @return List of User entities
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public List<User> getByNotification(String notification) throws DAOException {
        return getList(notification, GET_USERS_BY_NOTIFICATION);
    }

    /**
     * Gets list of all users from database
     * @return users list
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public List<User> getAll() throws DAOException {
        List<User> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_ALL_USERS)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getUserWithFields(rs));
            }
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
        return list;
    }
    /**
     * This method reads user fields from result set
     */
    private static User getUserWithFields(ResultSet rs) throws SQLException {
        return new User.Builder()
                .withAccount(rs.getLong(1))
                .withLogin(rs.getString(2))
                .withEmail(rs.getString(3))
                .withPassword(rs.getString(4))
                .withFirstName(rs.getString(5))
                .withLastName(rs.getString(6))
                .withRole(rs.getString(7))
                .withStatus(rs.getString(8))
                .withNotification(rs.getString(9))
                .build();
    }
    /**
     * Inserts new user to database
     * @param user - id will be generated by database. Email and login cannot be null
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void save(User user) throws DAOException {
        try(Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(INSERT_USER)) {
            setUserFields(user, stmt);
        } catch (DAOException e){
            logger.warn(e.getMessage());
            throw e;
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
    }

    /**
     * Updates user
     * @param user contains User entity to be updated
     * @param params should contain all user fields except account to be updated
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void update(User user, String[] params) throws DAOException {
        String oldLogin = user.getLogin();
        try(Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(UPDATE_USER)) {
            updateUserFields(params, oldLogin, stmt);
        } catch (DAOException e){
            logger.warn(e.getMessage());
            throw e;
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
    }

    /**
     * Deletes user record in database
     * @param user - User entity
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void delete(User user) throws DAOException {
            try (Connection con = dataSource.getConnection();
                    PreparedStatement stmt = con.prepareStatement(DELETE_USER)){
                stmt.setString(1, user.getLogin());
                stmt.executeUpdate();
            } catch (SQLException e) {
                logger.fatal(e.getMessage());
                throw new DAOException(UNKNOWN_ERROR);
            }
    }

    /**
     * Auxiliary method to set user fields from prepared statement
     * @param user - User entity
     * @param stmt - prepared statement
     * @throws DAOException is wrapper for SQLException
     */
    private static void setUserFields(User user, PreparedStatement stmt) throws SQLException {
        int k = 0;
        stmt.setString(++k, user.getLogin());
        stmt.setString(++k, user.getEmail());
        try {
            stmt.setString(++k, new Sha().hashToHex(user.getPassword(), Optional.ofNullable(user.getLogin())));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
        stmt.setString(++k, user.getFirstName());
        stmt.setString(++k, user.getLastName());
        stmt.setString(++k, user.getRole().value);
        stmt.setString(++k, user.getStatus());
        stmt.setString(++k, user.getNotification());
        stmt.executeUpdate();
    }

    /**
     * Auxiliary method to update user fields from prepared statement
     * @param params - user fields after update
     * @param oldLogin - user login before update
     * @param stmt - prepared statement
     * @throws DAOException is wrapper for SQLException
     */
    private static void updateUserFields(String[] params, String oldLogin, PreparedStatement stmt)
            throws SQLException {
        int k = 0;
        stmt.setString(++k, params[k - 1]);
        stmt.setString(++k, params[k - 1]);
        stmt.setString(++k, params[k - 1]);
        stmt.setString(++k, params[k - 1]);
        stmt.setString(++k, params[k - 1]);
        stmt.setString(++k, params[k - 1]);
        stmt.setString(++k, params[k - 1]);
        stmt.setString(++k, params[k - 1]);
        stmt.setString(++k, oldLogin);
        stmt.executeUpdate();
    }

    /**
     * Adds user status to database
     * @param statusName - name of status
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void addStatus(String statusName) throws DAOException{
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(ADD_STATUS)){
            stmt.setString(1, statusName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
    }

    /**
     * Deletes user status from database
     * @param statusName - name of status
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void deleteStatus(String statusName) throws DAOException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_STATUS)){
            stmt.setString(1, statusName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
    }

    /**
     * Checks if user role exists
     * @param role - user role to check
     * @return true if role exists, false otherwise
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public boolean isRoleCorrect(String role) throws DAOException{
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(FIND_ROLE_BY_DESCRIPTION)) {
            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            logger.fatal(e);
            throw new DAOException(UNKNOWN_ERROR);
        }
        return false;
    }

    /**
     * Checks if user status exists
     * @param status - user status to check
     * @return true if status exists, false otherwise
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public boolean isStatusCorrect(String status) throws DAOException{
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(FIND_STATUS_BY_NAME)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            logger.fatal(e);
            throw new DAOException(UNKNOWN_ERROR);
        }
        return false;
    }
}

