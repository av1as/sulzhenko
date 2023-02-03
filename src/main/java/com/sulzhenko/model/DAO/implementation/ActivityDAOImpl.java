package com.sulzhenko.model.DAO.implementation;

import com.sulzhenko.model.Constants;
import com.sulzhenko.model.DAO.ActivityDAO;
import com.sulzhenko.model.DAO.CategoryDAO;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.entity.Activity;
import com.sulzhenko.model.entity.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import static com.sulzhenko.model.DAO.SQLQueries.ActivityQueries.*;

/**
 * ActivityDAO class for CRUD operations with database. Matches tables 'activity' in database.
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class ActivityDAOImpl implements ActivityDAO, Constants {
    /** An instance of datasource to provide connection to database */
    private final DataSource dataSource;
    CategoryDAO categoryDAO;

    public ActivityDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.categoryDAO = new CategoryDAOImpl(dataSource);
    }

    private static final Logger logger = LogManager.getLogger(ActivityDAOImpl.class);

    /**
     * Gets instance of Activity from database by some parameter
     * @param parameter - value of some field in database
     * @param querySQL - String representation for SQL query
     * @return Optional.ofNullable - activity is null if there is no user
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public Optional<Activity> get(Object parameter, String querySQL) throws DAOException {
        Activity activity = null;
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(querySQL)) {
            stmt.setObject(1, parameter);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                activity = getActivityWithFields(rs);
            }
        } catch (SQLException e){
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
        return Optional.ofNullable(activity);
    }

    /**
     * Gets list of Activities from database by some parameter
     * @param parameter - value of some field in database
     * @param querySQL - String representation for SQL query
     * @return List of Activity entities
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public List<Activity> getList(Object parameter, String querySQL) throws DAOException{
        List<Activity> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(querySQL)) {
            stmt.setObject(1, parameter);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getActivityWithFields(rs));
            }
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
        return list;
    }

    /**
     * Gets sorted and ordered list of records in database
     * @param querySQL - SQL query for database
     * @return LinkedHashMap of Activity as key and number of users as value
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public Map<Activity, Integer> getSortedList(String querySQL) throws DAOException{
        Map<Activity, Integer> map = new LinkedHashMap<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(querySQL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Activity activity = new Activity.Builder()
                        .withName(rs.getString(1))
                        .withCategory(new Category(rs.getString(3)))
                        .build();
                map.put(activity, rs.getInt(2));
            }
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
        return map;
    }

    /**
     * Gets list of all activities from database
     * @return activities list
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public List<Activity> getAll() throws DAOException{
        List<Activity> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_ALL_ACTIVITIES)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getActivityWithFields(rs));
            }
        } catch (SQLException e){
            logger.fatal(e);
            throw new DAOException(UNKNOWN_ERROR);
        }
        return list;
    }

    /**
     * Inserts new activity to database
     * @param activity - id will be generated by database. Name and category cannot be null
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void save(Activity activity) throws DAOException{
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(INSERT_ACTIVITY)) {
            int k = 0;
            stmt.setString(++k, activity.getName());
            stmt.setString(++k, activity.getCategory().getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
    }

    /**
     * Updates activity
     * @param activity contains Activity entity to be updated
     * @param params should contain all activity fields except id to be updated
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void update(Activity activity, String[] params) throws DAOException{
        String oldName = activity.getName();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(UPDATE_ACTIVITY)) {
            int k = 0;
            stmt.setString(++k, params[k-1]);
            stmt.setString(++k, params[k-1]);
            stmt.setString(++k, oldName);
            stmt.executeUpdate();
        } catch (SQLException e){
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
    }

    /**
     * Deletes activity record in database
     * @param activity - Activity entity
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public void delete(Activity activity) throws DAOException{
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_ACTIVITY)) {
            stmt.setString(1, activity.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
    }

    /**
     * Gets instance of Activity from database by id
     * @param id - value of account field in database
     * @return Optional.ofNullable - activity is null if there is no activity
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public Optional<Activity> getById(long id) throws DAOException {
        return get(id, GET_ACTIVITY_BY_ID);
    }

    /**
     * Gets instance of Activity from database by name
     * @param name - value of activity_name field in database
     * @return Optional.ofNullable - activity is null if there is no activity
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public Optional<Activity> getByName(String name) throws DAOException {
        return get(name, GET_ACTIVITY_BY_NAME);
    }

    /**
     * Gets list of Activities from database by category
     * @param categoryName - value of category_name field in database
     * @return List of Activity entities
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public List<Activity> getByCategory(String categoryName) throws DAOException {
        return getList(categoryName, GET_ACTIVITIES_BY_CATEGORY);
    }
    /**
     * Gets number of records in database according to filter
     * @param filter - filter for accounting records
     * @return List of Activity entities
     * @throws DAOException is wrapper for SQLException
     */
    @Override
    public int getNumberOfRecords(String filter) throws DAOException{
        int number = 0;
        try(Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(getTotalRecords(filter))){
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                number = rs.getInt(1);
            }
        } catch (SQLException e){
            throw new DAOException(UNKNOWN_ERROR);
        }
        return number;
    }

    /**
     * This method reads activity fields from result set
     * @param rs - Result set after executing SQL query
     * @return new Activity entity
     */
    private Activity getActivityWithFields(ResultSet rs) throws SQLException {
        return new Activity.Builder()
                .withId(rs.getLong(1))
                .withName(rs.getString(2))
                .withCategory(categoryDAO.getByName(rs.getString(3)).orElse(null))
                .build();
    }
    /**
     * Construes SQL query to get total number of records in database according to chosen filter
     * @param filter - filter to choose some records
     * @return SQL query
     */
    private String getTotalRecords(String filter){
        String query = "SELECT COUNT(activity.activity_name)\n" +
                "FROM activity\n" +
                "INNER JOIN category_of_activity\n" +
                "ON activity.category_id = category_of_activity.category_id\n";
        if(!Objects.equals(filter, ALL_CATEGORIES) && filter != null) query += String.format("WHERE category_name = '%s'", filter);
        return query;
    }
}
