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

import static com.sulzhenko.DAO.SQLQueries.ActivityQueries.*;
import static com.sulzhenko.DAO.SQLQueries.UserQueries.FIND_CONNECTED_USERS;
import static com.sulzhenko.DAO.UserDAO.getUserFields;

/**
 * This class describes CRUD operations with Activity class entities
 */
public class ActivityDAO implements DAO<Activity>{

    private static Logger logger = LogManager.getLogger(ActivityDAO.class);
    @Override
    public Activity getById(int id) {
        Activity t = null;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_ACTIVITY_BY_ID);
        ) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                t = getActivityFields(rs).build();
            }
        } catch (SQLException e){
            logger.fatal(e);
//            e.printStackTrace();
        }
        if (t == null) indicateNoResult("id", id);
        return t;
    }
    public Activity getByName(String name) {
        Activity t = null;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_ACTIVITY_BY_NAME);
        ) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                t = getActivityFields(rs).build();
            }
        } catch (SQLException e){
            logger.fatal(e);
//            e.printStackTrace();
        }
        if (t == null) indicateNoResult("name", name);
        return t;
    }
    public List<Activity> getByCategory(String categoryName) {
        List<Activity> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_ACTIVITIES_BY_CATEGORY);
        ) {
            stmt.setString(1, categoryName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getActivityFields(rs).build());
            }
        } catch (SQLException e){
            logger.fatal(e);
//            e.printStackTrace();
            //throw new DAOException("wrong status: " + status);
        }
        if (list.isEmpty()) indicateNoResult("category", categoryName);
        return list;
    }


    @Override
    public List<Activity> getAll() {
        List<Activity> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_ALL_ACTIVITIES);
        ) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getActivityFields(rs).build());
            }
        } catch (SQLException e){
            logger.fatal(e);
//            e.printStackTrace();
            //throw new DAOException("wrong notifications: " + notifications);
        }
        if (list.isEmpty()) {
            indicateNoActivities();;
        }
        return list;
    }

    @Override
    public void save(Activity t) {
        if(isNameAvailable(t.getName()) && isCategoryCorrect(t.getCategory())) {
//            int count = 0;
            try (Connection con = DataSource.getConnection();
                 PreparedStatement stmt = con.prepareStatement(INSERT_ACTIVITY);
            ) {
                int k = 0;
                stmt.setString(++k, t.getName());
                stmt.setString(++k, t.getCategory());
//                count =
                stmt.executeUpdate();
            }
            catch (SQLException e) {
                logger.fatal("unknown exception: {}", t);
//                e.printStackTrace();
//                throw new DAOException("unknown exception: " + t);
            }
//            System.out.println(count > 0 ? "category saved: " + getByName(t.getName()) : "category wasn't saved");
        } else if (!isNameAvailable(t.getName())){
            logger.info("wrong activity name: {}", t.getName());
//            throw new DAOException("wrong activity name: " + t.getName());
        } else if (!isCategoryCorrect(t.getCategory())){
            logger.info("wrong category: {}", t.getCategory());
//            throw new DAOException("wrong category: " + t.getCategory());
        } else {
            logger.fatal("unknown exception: {}", t);
//            throw new DAOException("unknown exception: " + t);
        }
    }

    @Override
    public void update(Activity t, String[] params) {
        if(isDataCorrect(t, params)) {
            String oldName = t.getName();
            List<User> connectedUsers = getConnectedUsers(t);
            String description = "activity " + "\"" + oldName + "\" "
                    + "now has name " + "\"" + params[0] + "\" "
                    + "and category " + "\"" + params[1] + "\"";

//            int count = 0;
            try (Connection con = DataSource.getConnection();
                 PreparedStatement stmt = con.prepareStatement(UPDATE_ACTIVITY);
            ) {

                int k = 0;
                stmt.setString(++k, params[k-1]);
                stmt.setString(++k, params[k-1]);
                stmt.setString(++k, oldName);
//                count =
                stmt.executeUpdate();
                notifyAboutUpdate(connectedUsers, description);
            } catch (SQLException e) {
                logger.fatal("unknown exception: {}", t);
//                e.printStackTrace();
//                throw new DAOException("unknown exception: " + t);
            }
//            System.out.println(count > 0 ? "activity updated: " + params[0] : "activity wasn't updated");
        } else if(isNameAvailable(t.getName())){
            logger.info("wrong activity name: {}", t.getName());
//            throw new DAOException("wrong activity name: " + t.getName());
        } else if(!isNameAvailable(params[0])){
            logger.info("wrong activity new name: {}", params[0]);
//            throw new DAOException("wrong activity new name: " + params[0]);
        } else if(!isCategoryCorrect(params[1])){
            logger.info("wrong new category: {}", params[1]);
//            throw new DAOException("wrong category: " + params[1]);
        } else {
            logger.fatal("unknown exception: {}", t);
//            throw new DAOException("unknown exception: " + t);
        }
//        {
////            System.out.println("this activity doesn't exist: " + t.getName());
//        }

    }
    public void notifyAboutUpdate(List<User> connectedUsers, String description){
        for(User user: connectedUsers){
            // create method to notify connected users


        }
    }

    private boolean isDataCorrect(Activity t, String[] params) {
        return !isNameAvailable(t.getName()) && isNameAvailable(params[0]) && isCategoryCorrect(params[1]);
    }

    @Override
    public void delete(Activity t) {
        if(!isNameAvailable(t.getName())) {
//            int count = 0;
            try (Connection con = DataSource.getConnection();
                 PreparedStatement stmt = con.prepareStatement(DELETE_ACTIVITY);
            ) {
                stmt.setString(1, t.getName());
//                count =
                stmt.executeUpdate();
            } catch (SQLException e) {
                logger.info("wrong activity: {}", t.getName());
//                e.printStackTrace();
//                throw new DAOException("wrong activity: " + t.getName());
            }
//            System.out.println(count > 0 ? "activity deleted: " + t.getName() : "activity wasn't deleted");
        } else {
            logger.info("this activity doesn't exist: {}", t.getName());
//            throw new DAOException("this activity doesn't exist: " + t.getName());
        }
    }

    /**
     * This method reads activity fields from result set
     */
    private static Activity.Builder getActivityFields(ResultSet rs) throws SQLException {
        return Activity.builder().withId(rs.getInt(1))
                .withName(rs.getString(2))
                .withCategory(rs.getString(3));
    }
    public void indicateNoResult(String name, Object value){
        logger.info("No activity with such {}: {}", name, value);
    }
    public void indicateNoActivities(){
        logger.info("No activity available");
    }
    public boolean isNameAvailable(String name){
        return getByName(name) == null;
    }
    public void addCategory(String categoryName){
//        int count;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(ADD_CATEGORY);
        ) {
            stmt.setString(1, categoryName);
//            count =
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.info("wrong category name: {}", categoryName);
//            e.printStackTrace();
//            throw new DAOException("wrong name: " + categoryName);
        }
//        System.out.println(count > 0 ? "category added: " +categoryName : "category wasn't added");
    }
    public void deleteCategory(String categoryName) {
//        int count = 0;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_CATEGORY);
        ) {
            stmt.setString(1, categoryName);
//            count =
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.info("wrong category: {}", categoryName);
//            e.printStackTrace();
//            throw new DAOException("wrong category: " + categoryName);
        }
//        System.out.println(count > 0 ? "category deleted: " + categoryName : "category wasn't deleted");
    }
    public boolean isCategoryCorrect(String category) {
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(FIND_CATEGORY_BY_NAME);
        ) {
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            logger.fatal("unknown exception");
//            throw new DAOException("unknown exception");
        }
        return false;
    }
    public List<User> getConnectedUsers(Activity activity){
        List<User> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(FIND_CONNECTED_USERS);
        ) {
            stmt.setString(1, activity.getName());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getUserFields(rs).build());
            }
        } catch (SQLException e){
            logger.info("unknown exception with connected users: {}", activity);
//            e.printStackTrace();
            //throw new DAOException("wrong notifications: " + notifications);
        }
        return list;
    }

    private static ActivityDAO activityDAO = new ActivityDAO();;
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
