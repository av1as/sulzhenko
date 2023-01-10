package com.sulzhenko.model.DAO.implementation;

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

public class UserActivityDAOImpl implements UserActivityDAO {
    private final DataSource dataSource;
    UserDAO userDAO;
    ActivityDAO activityDAO;

    public UserActivityDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.userDAO = new UserDAOImpl(dataSource);
        this.activityDAO = new ActivityDAOImpl(dataSource);
    }
    private static final Logger logger = LogManager.getLogger(UserActivityDAOImpl.class);
    @Override
    public List<Activity> allAvailableActivities(User u) {
        List<Activity> list = new ArrayList<>();
        for(Activity element: new ActivityDAOImpl(dataSource).getAll()){
            if(!ifUserHasActivity(u, element) && !isRequestToAddExists(u, element)){
                list.add(element);
            }
        }
        return list;
    }


    @Override
    public boolean ifUserHasActivity(User u, Activity a){
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt
                     = con.prepareStatement(SQLQueries.RequestQueries.IF_USER_HAS_ACTIVITY)) {
            stmt.setLong(1, u.getAccount());
            stmt.setLong(2, a.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException("unknown.error", e);
        }
        return false;
    }

    public void approveRequest(Request request){
        if(Objects.equals(request.getActionToDo(), "add")){
            try {
                addActivityToUser(request);
            } catch (SQLException e) {
                throw new DAOException("unknown.error");
            }
        } else if(Objects.equals(request.getActionToDo(), "remove")){
            try {
                removeUserActivity(request);
            } catch (SQLException e) {
                throw new DAOException("unknown.error");
            }
        }
    }

    @Override
    public void addActivityToUser(Request request) throws SQLException {
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement stmt;
            if (Objects.equals(request.getActionToDo(), "add")) {
                try {
                    User user = userDAO.getByLogin(request.getLogin()).orElse(null);
                    Activity activity = activityDAO.getByName(request.getActivityName());
                    con.setAutoCommit(false);
                    stmt = con.prepareStatement(SQLQueries.RequestQueries.ADD_ACTIVITY_TO_USER);
                    assert user != null;
                    stmt.setLong(1, user.getAccount());
                    stmt.setLong(2, activity.getId());
                    stmt.executeUpdate();
                    new RequestDAOImpl(dataSource).delete(request);
                    con.commit();
                } catch (SQLException e) {
                    con.rollback();
                    logger.fatal(e);
                    throw new DAOException("unknown.error", e);
                }
            }
        }
    }
    @Override
    public void removeUserActivity(Request request) throws SQLException {
        Connection con = dataSource.getConnection();
        PreparedStatement stmt = null;
        User user = userDAO.getByLogin(request.getLogin()).orElse(null);
        Activity activity = activityDAO.getByName(request.getActivityName());
        if(Objects.equals(request.getActionToDo(), "remove")){
            try{
                con.setAutoCommit(false);
                stmt = con.prepareStatement(SQLQueries.RequestQueries.REMOVE_USER_ACTIVITY);
                assert user != null;
                stmt.setLong(1, user.getAccount());
                stmt.setLong(2, activity.getId());
                stmt.executeUpdate();
                new RequestDAOImpl(dataSource).delete(request);
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                logger.fatal(e);
                throw new DAOException("unknown.error", e);
            } finally {
                assert stmt != null;
                stmt.close();
                con.close();
            }
        }
    }

    @Override
    public void setAmount(User user, Activity activity, int amount) throws DAOException {
        if(amount < 0){
            throw new DAOException("amount.nonnegative");
        } else if(ifUserHasActivity(user, activity)){
            try(Connection con = dataSource.getConnection();
                PreparedStatement stmt = con.prepareStatement(SQLQueries.RequestQueries.SET_AMOUNT)){

                stmt.setInt(1, amount);
                stmt.setLong(2, user.getAccount());
                stmt.setLong(3, activity.getId());
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException("unknown.error", e);
            }
        } else {
            throw new DAOException("user.has.no.activity");
        }
    }

    @Override
    public int getAmount(User user, Activity activity) throws SQLException {
        PreparedStatement stmt = null;
        int amount = 0;
        try(Connection con = dataSource.getConnection()){
            stmt = con.prepareStatement(SQLQueries.RequestQueries.GET_AMOUNT);
            stmt.setLong(1, user.getAccount());
            stmt.setLong(2, activity.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                amount = rs.getInt(1);
            }            
        } catch (SQLException e) {
            throw new DAOException("unknown.error", e);
        } finally {
            assert stmt != null;
            stmt.close();
        }
        return amount;
    }

    @Override
    public int getNumberOfUsers(Activity activity) {
        return activityDAO.getConnectedUsers(activity).size();
    }

    public boolean isRequestToAddExists(User user, Activity activity){
        Request requestToCompare = new Request.Builder()
                .withLogin(user.getLogin())
                .withActivityName(activity.getName())
                .withActionToDo("add")
                .build();
        for(Request element: new RequestDAOImpl(dataSource).getAll()){
            if (element.equals(requestToCompare)) return true;
        }
        return false;
    }
    public boolean isRequestToRemoveExists(User user, Activity activity){
        Request requestToCompare = new Request.Builder()
                .withLogin(user.getLogin())
                .withActivityName(activity.getName())
                .withActionToDo("remove")
                .build();
        for(Request element: new RequestDAOImpl(dataSource).getAll()){
            if (element.equals(requestToCompare)) return true;
        }
        return false;
    }
    public String defineStatus(User user, Activity activity){
        String status;
        if (ifUserHasActivity(user, activity) && !isRequestToRemoveExists(user, activity)){
            status = "active.status";
        } else if(ifUserHasActivity(user, activity) && isRequestToRemoveExists(user, activity)){
            status = "pending.removing";
        } else if(!ifUserHasActivity(user, activity) && isRequestToAddExists(user, activity)){
            status = "pending.adding";
        } else {
            status = "error";
        }
        return status;
    }
}
