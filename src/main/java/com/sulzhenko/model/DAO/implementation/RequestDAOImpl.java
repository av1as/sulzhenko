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

import static com.sulzhenko.model.DAO.SQLQueries.RequestQueries.GET_REQUESTS_BY_LOGIN_AND_ACTION;

/**
 * This class describes CRUD operations with Request class entities
 */
public class RequestDAOImpl implements RequestDAO, Constants {
    private final DataSource dataSource;
    UserDAO userDAO;
    ActivityDAO activityDAO;
    public RequestDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.userDAO = new UserDAOImpl(dataSource);
        this.activityDAO = new ActivityDAOImpl(dataSource);
    }
    private static final Logger logger = LogManager.getLogger(RequestDAOImpl.class);

    @Override
    public Optional<Request> get(Object parameter, String querySQL) throws DAOException{
        Request t = null;
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(querySQL)
        ) {
            stmt.setObject(1, parameter);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                t = getRequestWithFields(rs);
            }
        } catch (SQLException e){
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
        return Optional.ofNullable(t);
    }
    @Override
    public List<Request> getList(Object parameter, String querySQL) throws DAOException{
        List<Request> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(querySQL)
        ) {
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
    public Request getById(long id) {
        return get(id, SQLQueries.RequestQueries.GET_REQUEST_BY_ID).orElse(null);
    }
    public List<Request> getByLogin(String login) {
        return getList(login, SQLQueries.RequestQueries.GET_REQUESTS_BY_LOGIN);
    }
    public List<Request> getByActivity(String activityName) {
        return getList(activityName, SQLQueries.RequestQueries.GET_REQUESTS_BY_ACTIVITY);
    }
    public List<Request> getByActionToDo(String actionName) {
        return getList(actionName, SQLQueries.RequestQueries.GET_REQUESTS_BY_ACTION);
    }
    @Override
    public List<Request> getByLoginAndAction(String login, String action) throws DAOException{
        List<Request> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_REQUESTS_BY_LOGIN_AND_ACTION)
        ) {
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

    @Override
    public List<Request> getAll() throws DAOException {
        List<Request> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
                PreparedStatement stmt = con.prepareStatement(SQLQueries.RequestQueries.SELECT_ALL_REQUEST_FIELDS)
        ) {
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

    @Override
    public void save(Request t) throws DAOException{
        try(Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(SQLQueries.RequestQueries.INSERT_REQUEST,
                Statement.RETURN_GENERATED_KEYS)) {
            setRequestFields(t, stmt);
            ResultSet rs = stmt.getGeneratedKeys();
            t.setId(rs.next() ? rs.getLong(1) : 0);
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

    @Override
    public void update(Request t, String[] params) {
        try(Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(SQLQueries.RequestQueries.UPDATE_REQUEST)) {
            int k = 0;
            stmt.setString(++k, params[0]);
            stmt.setString(++k, params[1]);
            stmt.setString(++k, params[2]);
            stmt.setString(++k, params[3]);
            stmt.setLong(++k, t.getId());
            stmt.executeUpdate();
        } catch (DAOException e){
            logger.warn(e.getMessage());
            throw e;
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
    }
    @Override
    public void delete(Request t) throws DAOException{
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQLQueries.RequestQueries.DELETE_REQUEST)){

            stmt.setLong(1, t.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
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

