package com.sulzhenko.model.DAO.implementation;

import com.sulzhenko.model.Constants;
import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static com.sulzhenko.model.DAO.SQLQueries.RequestQueries.*;
import static com.sulzhenko.model.DAO.SQLQueries.UserActivityQueries.IF_USER_HAS_ACTIVITY;

/**
 * Request DAO class for CRUD operations with database. Matches tables 'request', 'action_with_request'
 * in database.
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class RequestDAOImpl implements RequestDAO, Constants {
    /** An instance of datasource to provide connection to database */
    private final DataSource dataSource;
    private static final Logger logger = LogManager.getLogger(RequestDAOImpl.class);
    public RequestDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Gets instance of Request from database by some parameter
     * @param parameter - value of some field in database
     * @param querySQL - String representation for SQL query
     * @return Optional.ofNullable - request is null if there is no request
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public Optional<Request> get(Object parameter, String querySQL) throws DAOException{
        Request request = null;
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(querySQL)){
            stmt.setObject(1, parameter);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                request = getRequestWithFields(rs);
            }
        } catch (SQLException e){
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
        return Optional.ofNullable(request);
    }

    /**
     * Gets list of Requests from database by some parameter
     * @param parameter - value of some field in database
     * @param querySQL - String representation for SQL query
     * @return List of Request entities
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public List<Request> getList(Object parameter, String querySQL) throws DAOException{
        List<Request> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(querySQL)){
            stmt.setObject(1, parameter);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getRequestWithFields(rs));
            }
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
        return list;
    }

    /**
     * Gets instance of Request from database by id
     * @param id - value of id field in database
     * @return Optional.ofNullable - request is null if there is no request
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public Optional<Request> getById(long id) {
        return get(id, GET_REQUEST_BY_ID);
    }

    /**
     * Gets list of Requests from database by user login
     * @param login - value of login field in database
     * @return List of Request entities
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public List<Request> getByLogin(String login) {
        return getList(login, GET_REQUESTS_BY_LOGIN);
    }

    /**
     * Gets list of Requests from database by activity name
     * @param activityName - value of activity_name field in database
     * @return List of Request entities
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public List<Request> getByActivity(String activityName) {
        return getList(activityName, GET_REQUESTS_BY_ACTIVITY);
    }

    /**
     * Gets list of Requests from database by action to do
     * @param actionName - value of action_description field in database
     * @return List of Request entities
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public List<Request> getByActionToDo(String actionName) {
        return getList(actionName, GET_REQUESTS_BY_ACTION);
    }

    /**
     * Gets list of Requests from database by user login and action to do
     * @param login - value of login field in database
     * @param action - value of action_description field in database
     * @return List of Request entities
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public List<Request> getByLoginAndAction(String login, String action) throws DAOException{
        List<Request> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_REQUESTS_BY_LOGIN_AND_ACTION)){
            stmt.setString(1, login);
            stmt.setString(2, action);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getRequestWithFields(rs));
            }
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
        return list;
    }

    /**
     * Gets list of all requests from database
     * @return requests list
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public List<Request> getAll() throws DAOException {
        List<Request> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
                PreparedStatement stmt = con.prepareStatement(SELECT_ALL_REQUEST_FIELDS)){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getRequestWithFields(rs));
            }
        } catch (SQLException e){
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
        return list;
    }

    /**
     * Inserts new request to database
     * @param request - id will be generated by database. Login, activity name and action to do cannot be null
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void save(Request request) throws DAOException{
        try(Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(INSERT_REQUEST, Statement.RETURN_GENERATED_KEYS)) {
            setRequestFields(request, stmt);
            ResultSet rs = stmt.getGeneratedKeys();
            request.setId(rs.next() ? rs.getLong(1) : 0);
        } catch (SQLException e){
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
    }

    private static void setRequestFields(Request t, PreparedStatement stmt) throws SQLException {
        int k = 0;
        stmt.setString(++k, t.getLogin());
        stmt.setString(++k, t.getActivityName());
        stmt.setString(++k, t.getActionToDo());
        stmt.setString(++k, t.getDescription());
        stmt.executeUpdate();
    }

    /**
     * Updates request
     * @param request contains Request entity to be updated
     * @param params should contain login, activity name and action to do to be updated
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void update(Request request, String[] params) {
        try(Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(UPDATE_REQUEST)){
            int k = 0;
            stmt.setString(++k, params[0]);
            stmt.setString(++k, params[1]);
            stmt.setString(++k, params[2]);
            stmt.setString(++k, params[3]);
            stmt.setLong(++k, request.getId());
            stmt.executeUpdate();
        } catch (DAOException e){
            logger.warn(e.getMessage());
            throw e;
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
    }

    /**
     * Deletes request record in database
     * @param request - Request entity
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void delete(Request request) throws DAOException{
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_REQUEST)){
            stmt.setLong(1, request.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
    }

    /**
     * Checks if database contains equal request
     * @param request - Request entity to check
     * @return true if database doesn't contain equal request, false otherwise
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public boolean ifRequestUnique(Request request) throws DAOException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_EQUAL_REQUEST)) {
            stmt.setString(1, request.getLogin());
            stmt.setString(2, request.getActivityName());
            stmt.setString(3, request.getActionToDo());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return false;
            }
        } catch (SQLException e){
            logger.fatal(e);
            throw new DAOException(UNKNOWN_ERROR);
        }
        return true;
    }

    /**
     * Checks if some user has certain activity
     * @param user - user
     * @param activity - activity
     * @return true if database doesn't contain equal request, false otherwise
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public boolean ifUserHasActivity(User user, Activity activity){
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(IF_USER_HAS_ACTIVITY)) {
            stmt.setLong(1, user.getAccount());
            stmt.setLong(2, activity.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
        return false;
    }

    /**
     * This method reads request's fields from result set
     */
    private static Request getRequestWithFields(ResultSet rs) throws SQLException {
        return new Request.Builder()
                .withId(rs.getLong(1))
                .withLogin(rs.getString(2))
                .withActivityName(rs.getString(3))
                .withActionToDo(rs.getString(4))
                .withDescription(rs.getString(5))
                .build();
    }
}

