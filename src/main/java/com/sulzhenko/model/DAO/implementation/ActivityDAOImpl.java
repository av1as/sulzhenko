package com.sulzhenko.model.DAO.implementation;

import com.sulzhenko.model.Constants;
import com.sulzhenko.model.DAO.ActivityDAO;
import com.sulzhenko.model.DAO.CategoryDAO;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.DAO.SQLQueries;
import com.sulzhenko.model.entity.Activity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * This class describes CRUD operations with Activity class entities
 */
public class ActivityDAOImpl implements ActivityDAO, Constants {
    private final DataSource dataSource;
    CategoryDAO categoryDAO;

    public ActivityDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.categoryDAO = new CategoryDAOImpl(dataSource);
    }

    private static final Logger logger = LogManager.getLogger(ActivityDAOImpl.class);
    @Override
    public Optional<Activity> get(Object parameter, String querySQL) throws DAOException {
        Activity a = null;
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(querySQL)
        ) {
            stmt.setObject(1, parameter);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                a = getActivityWithFields(rs);
            }
        } catch (SQLException e){
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
        return Optional.ofNullable(a);
    }
    @Override
    public List<Activity> getList(Object parameter, String querySQL) throws DAOException{
        List<Activity> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(querySQL)
        ) {
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
    @Override
    public List<Activity> getAll() throws DAOException{
        List<Activity> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQLQueries.ActivityQueries.GET_ALL_ACTIVITIES)
        ) {
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
    @Override
    public void save(Activity t) throws DAOException{
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQLQueries.ActivityQueries.INSERT_ACTIVITY)) {

            int k = 0;
            stmt.setString(++k, t.getName());
            stmt.setString(++k, t.getCategory().getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
    }
    @Override
    public void update(Activity t, String[] params) throws DAOException{
        String oldName = t.getName();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQLQueries.ActivityQueries.UPDATE_ACTIVITY)) {
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
    @Override
    public void delete(Activity t) throws DAOException{
            try (Connection con = dataSource.getConnection();
                    PreparedStatement stmt = con.prepareStatement(SQLQueries.ActivityQueries.DELETE_ACTIVITY)
            ) {
                stmt.setString(1, t.getName());
                stmt.executeUpdate();
            } catch (SQLException e) {
                logger.fatal(e.getMessage());
                throw new DAOException(UNKNOWN_ERROR);
            }
    }
    @Override
    public Activity getById(long id) {
        return get(id, SQLQueries.ActivityQueries.GET_ACTIVITY_BY_ID).orElse(null);
    }
    @Override
    public Activity getByName(String name) {
        return get(name, SQLQueries.ActivityQueries.GET_ACTIVITY_BY_NAME).orElse(null);
    }
    @Override
    public List<Activity> getByCategory(String categoryName) {
        return getList(categoryName, SQLQueries.ActivityQueries.GET_ACTIVITIES_BY_CATEGORY);
    }

    /**
     * This method reads activity fields from result set
     */
    private Activity getActivityWithFields(ResultSet rs) throws SQLException {
        return new Activity.Builder()
                .withId(rs.getLong(1))
                .withName(rs.getString(2))
                .withCategory(categoryDAO.getByName(rs.getString(3)).orElse(null))
                .build();
    }
}
