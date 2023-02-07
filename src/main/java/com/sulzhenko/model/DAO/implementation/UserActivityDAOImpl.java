package com.sulzhenko.model.DAO.implementation;

import com.sulzhenko.DTO.UserActivityDTO;
import com.sulzhenko.model.Constants;
import com.sulzhenko.model.DAO.*;
import javax.sql.DataSource;
import com.sulzhenko.model.entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import static com.sulzhenko.model.DAO.SQLQueries.UserActivityQueries.*;

/**
 * UserActivity DAO class for CRUD operations with database. Matches table 'user_activity'
 * in database.
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */

public class UserActivityDAOImpl implements UserActivityDAO, Constants {
    /** An instance of datasource to provide connection to database */
    private final DataSource dataSource;
    private final UserDAO userDAO;
    private final ActivityDAO activityDAO;
    private final RequestDAO requestDAO;
    private static final Logger logger = LogManager.getLogger(UserActivityDAOImpl.class);
    public UserActivityDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.userDAO = new UserDAOImpl(dataSource);
        this.activityDAO = new ActivityDAOImpl(dataSource);
        this.requestDAO = new RequestDAOImpl(dataSource);
    }
    /**
     * Inserts a record to table 'user_activity'
     * @param request - Request entity
     * @throws DAOException is wrapper for SQLException, SQLException
     */
    @Override
    public void addActivityToUser(Request request) throws DAOException, SQLException {
        Connection con = dataSource.getConnection();
        if (Objects.equals(request.getActionToDo(), ADD)){
            try (PreparedStatement stmt = con.prepareStatement(ADD_ACTIVITY_TO_USER)){
                User user = userDAO.getByLogin(request.getLogin()).orElse(null);
                Activity activity = activityDAO.getByName(request.getActivityName()).orElse(null);
                con.setAutoCommit(false);
                assert user != null;
                assert activity != null;
                stmt.setLong(1, user.getAccount());
                stmt.setLong(2, activity.getId());
                stmt.executeUpdate();
                requestDAO.delete(request);
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                logger.fatal(e);
                throw new DAOException(UNKNOWN_ERROR);
            } finally{
                con.close();
            }
        }
    }
    /**
     * Deletes a record from table 'user_activity'
     * @param request - Request entity
     * @throws DAOException is wrapper for SQLException, SQLException
     */
    @Override
    public void removeUserActivity(Request request) throws DAOException, SQLException {
        Connection con = dataSource.getConnection();
        if(Objects.equals(request.getActionToDo(), REMOVE)){
            try (PreparedStatement stmt = con.prepareStatement(REMOVE_USER_ACTIVITY)){
                User user = userDAO.getByLogin(request.getLogin()).orElse(null);
                Activity activity = activityDAO.getByName(request.getActivityName()).orElse(null);
                con.setAutoCommit(false);
                assert user != null;
                assert activity != null;
                stmt.setLong(1, user.getAccount());
                stmt.setLong(2, activity.getId());
                stmt.executeUpdate();
                requestDAO.delete(request);
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                logger.fatal(e);
                throw new DAOException(UNKNOWN_ERROR);
            } finally {
                con.close();
            }
        }
    }
    /**
     * Sets amount of time to table 'user_activity'
     * @param user - User entity
     * @param activity - Activity entity
     * @param amount - amount of time
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void setAmount(User user, Activity activity, int amount) throws DAOException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SET_AMOUNT)) {
            stmt.setInt(1, amount);
            stmt.setLong(2, user.getAccount());
            stmt.setLong(3, activity.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.fatal(e);
            throw new DAOException(UNKNOWN_ERROR);
        }
    }

    /**
     * Gets amount of time from database table 'user_activity'
     * @param user - User entity
     * @param activity - Activity entity
     * @return amount of time
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public int getAmount(User user, Activity activity) throws DAOException {
        int amount = 0;
        try(Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(GET_AMOUNT)){
            stmt.setLong(1, user.getAccount());
            stmt.setLong(2, activity.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                amount = rs.getInt(1);
            }            
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
        return amount;
    }

    /**
     * Gets number of records from table 'user_activity'
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public int getNumberOfRecords() throws DAOException {
        int number = 0;
        try(Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(GET_NUMBER_OF_RECORDS)){
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                number = rs.getInt(1);
            }
        } catch (SQLException e){
            logger.fatal(e);
            throw new DAOException(UNKNOWN_ERROR);
        }
        return number;
    }

    /**
     * Gets number of records from table 'user_activity' for certain user
     * @param login - user login
     * @return number of records
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public int getNumberOfRecordsByUser(String login) throws DAOException{
        int number = 0;
        try(Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(GET_NUMBER_OF_RECORDS_BY_USER)){
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                number = rs.getInt(1);
            }
        } catch (SQLException e){
            logger.fatal(e);
            throw new DAOException(UNKNOWN_ERROR);
        }
        return number;
    }

    /**
     * Gets sorted list of all records from table 'user_activity'
     * @param query - SQL query
     * @return number of records
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public List<UserActivityDTO> listAllUserActivitiesSorted(String query) throws DAOException{
        List<UserActivityDTO> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UserActivityDTO userActivity = getUserActivityDTOWithFields(rs);
                list.add(userActivity);
            }
        } catch (SQLException e) {
            logger.fatal(e);
            throw new DAOException(UNKNOWN_ERROR);
        }
        return list;
    }

    /**
     * Gets sorted list of certain user records from table 'user_activity'
     * @param query - SQL query
     * @param login - user login
     * @return number of records
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public List<UserActivityDTO> listUserActivitiesSorted(String query, String login) throws DAOException{
        List<UserActivityDTO> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UserActivityDTO userActivityDTO = getUserActivityDTOWithFields(rs);
                list.add(userActivityDTO);
            }
        } catch (SQLException e) {
            logger.fatal(e);
            throw new DAOException(UNKNOWN_ERROR);
        }
        return list;
    }

    /**
     * Checks if user has certain activity
     * @param user - user
     * @param activity - activity
     * @return true if user has activity, false otherwise
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public boolean ifUserHasActivity(User user, Activity activity){
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(IF_USER_HAS_ACTIVITY)){
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
    private static UserActivityDTO getUserActivityDTOWithFields(ResultSet rs) throws SQLException {
        return new UserActivityDTO(rs.getString(2),
                rs.getString(3), rs.getInt(4),
                rs.getInt(5), rs.getInt(6));
    }
}
