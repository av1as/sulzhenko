package com.sulzhenko.DAO;

import com.sulzhenko.DAO.entity.Activity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.sulzhenko.DAO.SQLQueries.ActivityQueries.*;
/**
 * This class describes CRUD operations with Activity class entities
 */
public class ActivityDAO implements DAO<Activity>{
    @Override
    public Activity getById(int id) {
        Activity t = null;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_ACTIVITY_BY_ID);
        ) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                t = getFields(rs).build();
            }
        } catch (SQLException e){
            e.printStackTrace();
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
                t = getFields(rs).build();
            }
        } catch (SQLException e){
            e.printStackTrace();
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
                list.add(getFields(rs).build());
            }
        } catch (SQLException e){
            e.printStackTrace();
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
                list.add(getFields(rs).build());
            }
        } catch (SQLException e){
            e.printStackTrace();
            //throw new DAOException("wrong notifications: " + notifications);
        }
        if (list.isEmpty()) {
            indicateNoActivities();;
        }
        return list;
    }

    @Override
    public void save(Activity t) {
        if(isNameAvailable(t.getName())) {
            int count = 0;
            try (Connection con = DataSource.getConnection();
                 PreparedStatement stmt = con.prepareStatement(INSERT_ACTIVITY);
            ) {
                int k = 0;
                stmt.setString(++k, t.getName());
                stmt.setString(++k, t.getCategory());
                count = stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DAOException("wrong name: " + t.getName());
            }
//            System.out.println(count > 0 ? "category saved: " + getByName(t.getName()) : "category wasn't saved");
        } else {
            System.out.println("this name is not available: " + t.getName());
        }
    }

    @Override
    public void update(Activity t, String[] params) {
        if(!isNameAvailable(t.getName())) {
            String oldName = t.getName();
            int count = 0;
            try (Connection con = DataSource.getConnection();
                 PreparedStatement stmt = con.prepareStatement(UPDATE_ACTIVITY);
            ) {

                int k = 0;
                stmt.setString(++k, params[k-1]);
                stmt.setString(++k, params[k-1]);
                stmt.setString(++k, oldName);
                count = stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DAOException("wrong activity: " + t.getName());
            }
//            System.out.println(count > 0 ? "activity updated: " + params[0] : "activity wasn't updated");
        } else {
//            System.out.println("this activity doesn't exist: " + t.getName());
        }

    }

    @Override
    public void delete(Activity t) {
        if(!isNameAvailable(t.getName())) {
            int count = 0;
            try (Connection con = DataSource.getConnection();
                 PreparedStatement stmt = con.prepareStatement(DELETE_ACTIVITY);
            ) {
                stmt.setString(1, t.getName());
                count = stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DAOException("wrong activity: " + t.getName());
            }
//            System.out.println(count > 0 ? "activity deleted: " + t.getName() : "activity wasn't deleted");
        } else {
            System.out.println("this activity doesn't exist: " + t.getName());
        }
    }

    /**
     * This method reads activity fields from result set
     */
    private static Activity.Builder getFields(ResultSet rs) throws SQLException {
        return Activity.builder().withId(rs.getInt(1))
                .withName(rs.getString(2))
                .withCategory(rs.getString(3));
    }
    public void indicateNoResult(String name, Object value){
        System.out.println("No activity with such " + name + ": " + value);
    }
    public void indicateNoActivities(){
        System.out.println("No activity available");
    }
    public boolean isNameAvailable(String name){
        return getByName(name) == null;
    }
    public void addCategory(String categoryName){
        int count;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(ADD_CATEGORY);
        ) {
            stmt.setString(1, categoryName);
            count = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("wrong name: " + categoryName);
        }
//        System.out.println(count > 0 ? "category added: " +categoryName : "category wasn't added");
    }
    public void deleteCategory(String categoryName) {
        int count = 0;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_CATEGORY);
        ) {
            stmt.setString(1, categoryName);
            count = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("wrong category: " + categoryName);
        }
//        System.out.println(count > 0 ? "category deleted: " + categoryName : "category wasn't deleted");
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
