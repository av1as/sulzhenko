package com.sulzhenko.model.DAO.implementation;

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

public class UserActivityDAOImpl implements UserActivityDAO, Constants {
    private final DataSource dataSource;
    UserDAO userDAO;
    ActivityDAO activityDAO;
    RequestDAO requestDAO;

    public UserActivityDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.userDAO = new UserDAOImpl(dataSource);
        this.activityDAO = new ActivityDAOImpl(dataSource);
        this.requestDAO = new RequestDAOImpl(dataSource);
    }
    private static final Logger logger = LogManager.getLogger(UserActivityDAOImpl.class);


    @Override
    public void addActivityToUser(Request request) throws SQLException {
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement stmt;
            if (Objects.equals(request.getActionToDo(), ADD)) {
                try {
                    User user = userDAO.getByLogin(request.getLogin()).orElse(null);
                    Activity activity = activityDAO.getByName(request.getActivityName());
                    con.setAutoCommit(false);
                    stmt = con.prepareStatement(SQLQueries.RequestQueries.ADD_ACTIVITY_TO_USER);
                    assert user != null;
                    stmt.setLong(1, user.getAccount());
                    stmt.setLong(2, activity.getId());
                    stmt.executeUpdate();
                    requestDAO.delete(request);
                    con.commit();
                } catch (SQLException e) {
                    con.rollback();
                    logger.fatal(e);
                    throw new DAOException(UNKNOWN_ERROR);
                }
            }
        }
    }
    @Override
    public void removeUserActivity(Request request) throws SQLException {
        Connection con = dataSource.getConnection();
        PreparedStatement stmt = null;
        if(Objects.equals(request.getActionToDo(), REMOVE)){
            try{
                User user = userDAO.getByLogin(request.getLogin()).orElse(null);
                Activity activity = activityDAO.getByName(request.getActivityName());
                con.setAutoCommit(false);
                stmt = con.prepareStatement(SQLQueries.RequestQueries.REMOVE_USER_ACTIVITY);
                assert user != null;
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
                assert stmt != null;
                stmt.close();
                con.close();
            }
        }
    }
    @Override
    public int getAmount(User user, Activity activity) {
        int amount = 0;
        try(Connection con = dataSource.getConnection()){
            PreparedStatement stmt = con.prepareStatement(SQLQueries.RequestQueries.GET_AMOUNT);
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
            throw new DAOException(UNKNOWN_ERROR);
        }
        return false;
    }
    public boolean isRequestToAddExists(User user, Activity activity){
        Request requestToCompare = new Request.Builder()
                .withLogin(user.getLogin())
                .withActivityName(activity.getName())
                .withActionToDo(ADD)
                .build();
        for(Request element: requestDAO.getAll()){
            if (element.equals(requestToCompare)) return true;
        }
        return false;
    }
    public boolean isRequestToRemoveExists(User user, Activity activity){
        Request requestToCompare = new Request.Builder()
                .withLogin(user.getLogin())
                .withActivityName(activity.getName())
                .withActionToDo(REMOVE)
                .build();
        for(Request element: requestDAO.getAll()){
            if (element.equals(requestToCompare)) return true;
        }
        return false;
    }
}
