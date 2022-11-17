package com.sulzhenko.DAO;

import static com.sulzhenko.DAO.SQLQueries.RequestQueries.*;

import com.sulzhenko.DAO.entity.Request;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * This class describes CRUD operations with Request class entities
 */
public class RequestDAO implements DAO<Request>{
    @Override
    public Request getById(int id) {
        Request t = null;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_REQUEST_BY_ID);
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
    public List<Request> getByLogin(String login) {
        List<Request> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_REQUESTS_BY_LOGIN);
        ) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getFields(rs).build());
            }
        } catch (SQLException e){
            e.printStackTrace();
            //throw new DAOException("wrong status: " + status);
        }
        if (list.isEmpty()) indicateNoResult("login", login);
        return list;
    }
    public List<Request> getByActivity(String activityName) {
        List<Request> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_REQUESTS_BY_ACTIVITY);
        ) {
            stmt.setString(1, activityName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getFields(rs).build());
            }
        } catch (SQLException e){
            e.printStackTrace();
            //throw new DAOException("wrong status: " + status);
        }
        if (list.isEmpty()) indicateNoResult("activity", activityName);
        return list;
    }
    public List<Request> getByActionToDo(String actionName) {
        List<Request> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_REQUESTS_BY_ACTION);
        ) {
            stmt.setString(1, actionName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getFields(rs).build());
            }
        } catch (SQLException e){
            e.printStackTrace();
            //throw new DAOException("wrong status: " + status);
        }
        if (list.isEmpty()) indicateNoResult("action", actionName);
        return list;
    }

    @Override
    public List<Request> getAll() {
        List<Request> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_ALL_REQUEST_FIELDS);
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
            indicateNoRequests();;
        }
        return list;
    }

    @Override
    public void save(Request t) {
        int count = 0;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(INSERT_REQUEST);
        ) {
            int k = 0;
            stmt.setString(++k, t.getLogin());
            stmt.setString(++k, t.getActivityName());
            stmt.setString(++k, t.getActionToDo());
            stmt.setString(++k, t.getDescription());
            count = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("wrong request");
        }
        System.out.println(count > 0 ? "request saved: " + t : "request wasn't saved");

    }

    @Override
    public void update(Request t, String[] params) {
        int count = 0;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(UPDATE_REQUEST);
        ) {

            int k = 0;
            stmt.setString(++k, params[k-1]);
            stmt.setString(++k, params[k-1]);
            stmt.setString(++k, params[k-1]);
            stmt.setString(++k, params[k-1]);
            stmt.setInt(++k, t.getId());
            count = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("wrong request: " + t);
        }
        System.out.println(count > 0 ? "request updated" : "request wasn't updated");

    }

    @Override
    public void delete(Request t) {
        int count = 0;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_REQUEST);
        ) {
            stmt.setInt(1, t.getId());
            count = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("wrong request: " + t);
        }
        System.out.println(count > 0 ? "request deleted: " + t: "request wasn't deleted");

    }

    /**
     * This method reads request's fields from result set
     */
    private static Request.Builder getFields(ResultSet rs) throws SQLException {
        return Request.builder().withId(rs.getInt(1))
                .withLogin(rs.getString(2))
                .withActivityName(rs.getString(3))
                .withActionToDo(rs.getString(4))
                .withDescription(rs.getString(5));
    }
    public void indicateNoResult(String name, Object value){
        System.out.println("No request with such " + name + ": " + value);
    }
    public void indicateNoRequests(){
        System.out.println("No requests available");
    }


    private static RequestDAO requestDAO = new RequestDAO();;
    public static void main(String[] args) {
//        Request r = new Request().builder()
//                .withLogin("ivan")
//                .withActivityName("activity1")
//                .withActionToDo("add")
//                .withDescription("very fast")
//                .build();
//        requestDAO.save(r);

//        String[] params = {"ivan", "activity2", "add", "as fast as you can"};
//        requestDAO.update(requestDAO.getById(4), params);

//        requestDAO.delete(requestDAO.getById(4));
    }
}

//add checking if there is equal request or activity toAdd wasn't added / toRemove was added
