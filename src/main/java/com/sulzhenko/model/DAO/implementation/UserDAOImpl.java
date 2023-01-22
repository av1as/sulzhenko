package com.sulzhenko.model.DAO.implementation;

import com.sulzhenko.model.Constants;
import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.entity.User;
import com.sulzhenko.model.hashingPasswords.Sha;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class describes CRUD operations with User class entities
 */
public class UserDAOImpl implements UserDAO, Constants {

    private final DataSource dataSource;
    public UserDAOImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }
    private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);
    @Override
    public Optional<User> get(Object parameter, String querySQL){
        User t = null;
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(querySQL)) {
            stmt.setObject(1, parameter);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                t = getUserWithFields(rs);
            }
        } catch (SQLException e){
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
        return Optional.ofNullable(t);
    }
    @Override
    public List<User> getList(Object parameter, String querySQL){
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
    public Optional<User> getById(Long id) {
        return get(id, SQLQueries.UserQueries.GET_USER_BY_ID);
    }

    public Optional<User> getByLogin(String login) {
        return get(login, SQLQueries.UserQueries.GET_USER_BY_LOGIN);
    }

    public List<User> getByEmail(String email) {
        return getList(email, SQLQueries.UserQueries.GET_USERS_BY_EMAIL);
    }
    public List<User> getByFirstName(String firstName) {
        return getList(firstName, SQLQueries.UserQueries.GET_USERS_BY_FIRST_NAME);
    }
    public List<User> getByLastName(String lastName) {
        return getList(lastName, SQLQueries.UserQueries.GET_USERS_BY_LAST_NAME);
    }
    public List<User> getByRole(String role) {
        return getList(role, SQLQueries.UserQueries.GET_USERS_BY_ROLE);
    }
    public List<User> getByStatus(String status) {
        return getList(status, SQLQueries.UserQueries.GET_USERS_BY_STATUS);
    }
    public List<User> getByNotification(String notification) {
        return getList(notification, SQLQueries.UserQueries.GET_USERS_BY_NOTIFICATION);
    }

    @Override
    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQLQueries.UserQueries.GET_ALL_USERS)) {
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
    @Override
    public void save(User t) {
        try(Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(SQLQueries.UserQueries.INSERT_USER)) {
            setUserFields(t, stmt);
        } catch (DAOException e){
            logger.warn(e.getMessage());
            throw e;
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
    }
    @Override
    public void update(User t, String[] params) {
        String oldLogin = t.getLogin();
        try(Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(SQLQueries.UserQueries.UPDATE_USER)) {
            updateUserFields(params, oldLogin, stmt);
        } catch (DAOException e){
            logger.warn(e.getMessage());
            throw e;
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
    }
    @Override
    public void delete(User t) throws DAOException {
            try (Connection con = dataSource.getConnection();
                    PreparedStatement stmt = con.prepareStatement(SQLQueries.UserQueries.DELETE_USER)
            ) {
                stmt.setString(1, t.getLogin());
                stmt.executeUpdate();
            } catch (SQLException e) {
                logger.fatal(e.getMessage());
                throw new DAOException(UNKNOWN_ERROR);
            }
    }
    private static void setUserFields(User t, PreparedStatement stmt) throws SQLException {
        int k = 0;
        stmt.setString(++k, t.getLogin());
        stmt.setString(++k, t.getEmail());
        try {
            stmt.setString(++k, new Sha().hashToHex(t.getPassword(), Optional.ofNullable(t.getLogin())));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new DAOException(UNKNOWN_ERROR);
        }
        stmt.setString(++k, t.getFirstName());
        stmt.setString(++k, t.getLastName());
        stmt.setString(++k, t.getRole().value);
        stmt.setString(++k, t.getStatus());
        stmt.setString(++k, t.getNotification());
        stmt.executeUpdate();
    }
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
    public void addStatus(String statusName) throws DAOException{
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQLQueries.UserQueries.ADD_STATUS)
        ) {
            stmt.setString(1, statusName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
    }
    public void deleteStatus(String statusName) throws DAOException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQLQueries.UserQueries.DELETE_STATUS)) {
            stmt.setString(1, statusName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
    }
}

