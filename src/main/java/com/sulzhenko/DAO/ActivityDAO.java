package com.sulzhenko.DAO;

import com.sulzhenko.DAO.entity.Activity;
import com.sulzhenko.DAO.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sulzhenko.DAO.SQLQueries.ActivityQueries.*;
import static com.sulzhenko.DAO.SQLQueries.UserQueries.FIND_CONNECTED_USERS;

/**
 * This class describes CRUD operations with Activity class entities
 */
public class ActivityDAO implements DAO<Activity>{
    private static ActivityDAO activityDAO;
    private ActivityDAO(){
    }
    public static synchronized ActivityDAO getInstance() {
        if (activityDAO == null) {
            activityDAO = new ActivityDAO();
        }
        return activityDAO;
    }
    private static final Logger logger = LogManager.getLogger(ActivityDAO.class);
    @Override
    public Activity get(Object parameter, String querySQL, Connection con){
        Activity a = null;
        try (PreparedStatement stmt = con.prepareStatement(querySQL)
        ) {
            stmt.setObject(1, parameter);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                a = getActivityWithFields(rs);
            }
        } catch (SQLException e){
            logger.fatal(e.getMessage());
        }
        return a;
    }
    @Override
    public List<Activity> getList(Object parameter, String querySQL, Connection con){
        List<Activity> list = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(querySQL)
        ) {
            stmt.setObject(1, parameter);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getActivityWithFields(rs));
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        return list;
    }
    @Override
    public List<Activity> getAll(Connection con) {
        List<Activity> list = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(GET_ALL_ACTIVITIES);
        ) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getActivityWithFields(rs));
            }
        } catch (SQLException e){
            logger.fatal(e);
        }
        return list;
    }
    @Override
    public void save(Activity t, Connection con) {
        try {
            isDataCorrect(t, con);
            PreparedStatement stmt = con.prepareStatement(INSERT_ACTIVITY);
            int k = 0;
            stmt.setString(++k, t.getName());
            stmt.setString(++k, t.getCategory());
            stmt.executeUpdate();
        } catch (DAOException e) {
            logger.info(e.getMessage());
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
        }
    }
    @Override
    public void update(Activity t, String[] params, Connection con) {
        String oldName = t.getName();
        List<User> connectedUsers = getConnectedUsers(t, con);
        String description = "activity " + "\"" + oldName + "\" "
                + "now has name " + "\"" + params[0] + "\" "
                + "and category " + "\"" + params[1] + "\"";
        try {
            isUpdateCorrect(params, oldName, con);
            PreparedStatement stmt = con.prepareStatement(UPDATE_ACTIVITY);
            int k = 0;
            stmt.setString(++k, params[k-1]);
            stmt.setString(++k, params[k-1]);
            stmt.setString(++k, oldName);
            stmt.executeUpdate();
            notifyAboutUpdate(connectedUsers, description);
        } catch (DAOException e){
            logger.info(e.getMessage());
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
        }
    }
    @Override
    public void delete(Activity t, Connection con) {
        if(!isNameAvailable(t.getName(), con)) {
            try (
                    PreparedStatement stmt = con.prepareStatement(DELETE_ACTIVITY);
            ) {
                stmt.setString(1, t.getName());
                stmt.executeUpdate();
            } catch (SQLException e) {
                logger.info(e.getMessage());
            }
        } else {
            logger.info("this activity doesn't exist: {}", t.getName());
        }
    }
    public Activity getById(int id, Connection con) {
        return get(id, GET_ACTIVITY_BY_ID, con);
    }
    public Activity getByName(String name, Connection con) {
        return get(name, GET_ACTIVITY_BY_NAME, con);
    }
    public List<Activity> getByCategory(String categoryName, Connection con) {
        return getList(categoryName, GET_ACTIVITIES_BY_CATEGORY, con);
    }
    public void isDataCorrect(Activity t, Connection con) {
        if (!isCategoryCorrect(t.getCategory(), con)){
            throw new DAOException("wrong category: " + t.getCategory());
        } else if (!isNameAvailable(t.getName(), con)){
            throw new DAOException("wrong name: " + t.getName());
        }
    }
    /**
     * This method reads activity fields from result set
     */
    private static Activity getActivityWithFields(ResultSet rs) throws SQLException {
        return new Activity.Builder()
                .withId(rs.getInt(1))
                .withName(rs.getString(2))
                .withCategory(rs.getString(3))
                .build();
    }
    public void indicateNoResult(String name, Object value){
        logger.info("No activity with such {}: {}", name, value);
    }
    public void indicateNoActivities(){
        logger.info("No activity available");
    }
    public boolean isNameAvailable(String name, Connection con){
        return getByName(name, con) == null;
    }
    public void addCategory(String categoryName, Connection con){
        try (
             PreparedStatement stmt = con.prepareStatement(ADD_CATEGORY);
        ) {
            stmt.setString(1, categoryName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
    }
    public void deleteCategory(String categoryName, Connection con) {
        try (
             PreparedStatement stmt = con.prepareStatement(DELETE_CATEGORY);
        ) {
            stmt.setString(1, categoryName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
    }
    public boolean isCategoryCorrect(String category, Connection con) {
        try (PreparedStatement stmt = con.prepareStatement(FIND_CATEGORY_BY_NAME);
        ) {
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            logger.fatal("unknown exception when checking category");
            throw new DAOException("unknown exception when checking category");
        }
        return false;
    }
    public void isUpdateCorrect(String[] params, String oldName, Connection con){
        if (!isCategoryCorrect(params[1], con)){
            throw new DAOException("wrong category: " + params[1]);
        } else if (!Objects.equals(params[0], oldName) && !isNameAvailable(params[0], con)){
            throw new DAOException("wrong name: " + params[0]);
        }
    }
    public List<User> getConnectedUsers(Activity activity, Connection con){
        UserDAO userDAO = UserDAO.getInstance();
        return userDAO.getList(activity.getName(), FIND_CONNECTED_USERS, con);
    }
    public void notifyAboutUpdate(List<User> connectedUsers, String description){
        for(User user: connectedUsers){
            // create method to notify connected users


        }
    }

//    private static ActivityDAO activityDAO = new ActivityDAO();;
    public static void main(String[] args) {
//        System.out.println(new ActivityDAO().getById(1));
//        System.out.println(new ActivityDAO().getByName("new activity"));
//        System.out.println(new ActivityDAO().getByCategory("category1"));
//        Activity a = Activity.builder()
//                .withName("newest category")
//                .withCategory("category2")
//                .build();
//        activityDAO.save(a);

//        String[] newest = {"activity3", "category3"};
//        activityDAO.update(activityDAO.getByName("activity3"), newest);
//        activityDAO.delete(activityDAO.getByName("new activity"));
//        activityDAO.addCategory("category3");
//        activityDAO.deleteCategory("category3");
    }
}
