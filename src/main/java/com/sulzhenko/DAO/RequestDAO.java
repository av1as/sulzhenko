package com.sulzhenko.DAO;

import static com.sulzhenko.DAO.SQLQueries.RequestQueries.*;

import com.sulzhenko.DAO.entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class describes CRUD operations with Request class entities
 */
public class RequestDAO implements DAO<Request>{
    private static RequestDAO requestDAO;
    private RequestDAO(){
    }
    public static synchronized RequestDAO getInstance() {
        if (requestDAO == null) {
            requestDAO = new RequestDAO();
        }
        return requestDAO;
    }
    private static Logger logger = LogManager.getLogger(RequestDAO.class);
    UserDAO userDAO = UserDAO.getInstance();
    ActivityDAO activityDAO = ActivityDAO.getInstance();

    @Override
    public Request get(Object parameter, String querySQL, Connection con){
        Request t = null;
        try (PreparedStatement stmt = con.prepareStatement(querySQL)
        ) {
            stmt.setObject(1, parameter);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                t = getRequestWithFields(rs);
            }
        } catch (SQLException e){
            logger.fatal(e.getMessage());
        }
        return t;
    }
    @Override
    public List<Request> getList(Object parameter, String querySQL, Connection con){
        List<Request> list = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(querySQL)
        ) {
            stmt.setObject(1, parameter);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getRequestWithFields(rs));
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        return list;
    }
    public Request getById(int id, Connection con) {
        return get(id, GET_REQUEST_BY_ID, con);
    }
    public List<Request> getByLogin(String login, Connection con) {
        return getList(login, GET_REQUESTS_BY_LOGIN, con);
    }
    public List<Request> getByActivity(String activityName, Connection con) {
        return getList(activityName, GET_REQUESTS_BY_ACTIVITY, con);
    }
    public List<Request> getByActionToDo(String actionName, Connection con) {
        return getList(actionName, GET_REQUESTS_BY_ACTION, con);
    }

    @Override
    public List<Request> getAll(Connection con) {
        List<Request> list = new ArrayList<>();
        try (
             PreparedStatement stmt = con.prepareStatement(SELECT_ALL_REQUEST_FIELDS);
        ) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getRequestWithFields(rs));
            }
        } catch (SQLException e){
            logger.fatal(e.getMessage());
            throw new DAOException(e.getMessage());
        }
        if (list.isEmpty()) {
            indicateNoRequests();
        }
        return list;
    }

    @Override
    public void save(Request t, Connection con) {
        try {
            isDataCorrect(t, con);
            PreparedStatement stmt = con.prepareStatement(INSERT_REQUEST,
                    Statement.RETURN_GENERATED_KEYS);
            setRequestFields(t, stmt);
            ResultSet rs = stmt.getGeneratedKeys();
            t.setId(rs.next() ? rs.getInt(1) : 0);
        } catch (DAOException e){
            logger.info(e.getMessage());
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
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

    public void isDataCorrect(Request t, Connection con) {
        if(userDAO.isLoginAvailable(t.getLogin(), con)) {
            throw new DAOException("wrong login: " + t.getLogin());
        } else if(activityDAO.isNameAvailable(t.getActivityName(), con)){
            throw new DAOException("wrong activity: " + t.getActivityName());
        } else if(!requestDAO.isActionCorrect(t, con)){
            throw new DAOException("wrong action: " + t.getActionToDo());
        }
    }

    @Override
    public void update(Request t, String[] params, Connection con) {
        try {
            isDataCorrect(createUpdatedRequest(params), con);
            PreparedStatement stmt = con.prepareStatement(UPDATE_REQUEST);
            setRequestFields(t, stmt);
            stmt.setInt(5, t.getId());
            stmt.executeUpdate();
        } catch (DAOException e){
            logger.info(e.getMessage());
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
        }
    }
    private static Request createUpdatedRequest(String[] params) {
        int k = -1;
        return new Request.Builder()
                .withLogin(params[++k])
                .withActivityName(params[++k])
                .withActionToDo(params[++k])
                .withDescription(params[++k])
                .build();
    }

    @Override
    public void delete(Request t, Connection con) {
        try {
            PreparedStatement stmt = con.prepareStatement(DELETE_REQUEST);
            stmt.setInt(1, t.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
        }
    }

    /**
     * This method reads request's fields from result set
     */
    private static Request getRequestWithFields(ResultSet rs) throws SQLException {
        return new Request.Builder().withId(rs.getInt(1))
                .withLogin(rs.getString(2))
                .withActivityName(rs.getString(3))
                .withActionToDo(rs.getString(4))
                .withDescription(rs.getString(5))
                .build();
    }
    public void indicateNoResult(String name, Object value){
        logger.info("No request with such {}: {}",name, value);
    }
    public void indicateNoRequests(){
        logger.info("No requests available");
    }
    public boolean isActionCorrect(Request request, Connection con){
//        switch (request.getActionToDo()){
//            case "add":
//                if(!ifUserHasActivity(userDAO.getByLogin(request.getLogin()), activityDAO.getByName(request.getActivityName()))){
//                    return true;
//            }
//                break;
//            case "remove":
//                if(ifUserHasActivity(userDAO.getByLogin(request.getLogin()), activityDAO.getByName(request.getActivityName()))) {
//                    return true;
//                }
//                break;
//            default: throw new DAOException("unknown exception: " + request + request.getActionToDo());
//        }
        return canRequestBeAdded(request, con) || canRequestBeRemoved(request, con);
    }
    public boolean canRequestBeAdded(Request request, Connection con){
        return (Objects.equals(request.getActionToDo(), "add")) &&
                !ifUserHasActivity(userDAO.getByLogin(request.getLogin(), con),
                        activityDAO.getByName(request.getActivityName(), con), con);
    }
    public boolean canRequestBeRemoved(Request request, Connection con){
        return (Objects.equals(request.getActionToDo(), "remove") &&
                ifUserHasActivity(userDAO.getByLogin(request.getLogin(), con),
                        activityDAO.getByName(request.getActivityName(), con), con));
    }
    public boolean ifUserHasActivity(User u, Activity a, Connection con){
        try (
             PreparedStatement stmt = con.prepareStatement(IF_USER_HAS_ACTIVITY);
        ) {
            stmt.setInt(1, u.getAccount());
            stmt.setInt(2, a.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(e.getMessage());
        }
        return false;
    }
    public void addActivityToUser(Request request, Connection con) throws SQLException {
        User user = userDAO.getByLogin(request.getLogin(), con);
        Activity activity = activityDAO.getByName(request.getActivityName(), con);
        if(Objects.equals(request.getActionToDo(), "add")){
            try{
                con.setAutoCommit(false);
                PreparedStatement stmt = con.prepareStatement(ADD_ACTIVITY_TO_USER);
                stmt.setInt(1, user.getAccount());
                stmt.setInt(2, activity.getId());
                stmt.executeUpdate();
                delete(request, con);
            } catch (SQLException e) {
                con.rollback();
                logger.fatal(e);
                throw new DAOException(e.getMessage());
            }
        }
    }
    public void removeUserActivity(Request request, Connection con) throws SQLException {
        User user = userDAO.getByLogin(request.getLogin(), con);
        Activity activity = activityDAO.getByName(request.getActivityName(), con);
        if(Objects.equals(request.getActionToDo(), "remove")){
            try{
                con.setAutoCommit(false);
                PreparedStatement stmt = con.prepareStatement(REMOVE_USER_ACTIVITY);
                stmt.setInt(1, user.getAccount());
                stmt.setInt(2, activity.getId());
                stmt.executeUpdate();
                delete(request, con);
            } catch (SQLException e) {
                con.rollback();
                logger.fatal(e);
                throw new DAOException(e.getMessage());
            }
        }
    }
    public void setAmount(User user, Activity activity, int amount, Connection con){
        if(ifUserHasActivity(user, activity, con)){
            try{
                PreparedStatement stmt = con.prepareStatement(SET_AMOUNT);
                stmt.setInt(1, amount);
                stmt.setInt(2, user.getAccount());
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException(e);
            }
        } else {
            throw new DAOException(String.format("user %s doesn't have activity %s", user.getLogin(), activity.getName()));
        }
    }



//    public void close(AutoCloseable stmt){
//        if(stmt !=null) {
//            try {
//                stmt.close();
//            } catch (Exception e) {
//                logger.fatal(e);
//            }
//        }
//    }


//    private static RequestDAO requestDAO = new RequestDAO();
//    public static void main(String[] args) {
//        Request r = new Request.Builder()
//                .withLogin("ivan")
//                .withActivityName("activity1")
//                .withActionToDo("add")
//                .withDescription("very fast")
//                .build();
//        requestDAO.save(r, con);
//
//        String[] params = {"ivan", "activity2", "add", "as fast as you can"};
//        requestDAO.update(requestDAO.getById(1), params);
//
////        requestDAO.delete(requestDAO.getById(4));
//    }
}

//add checking if there is equal request
// or activity toAdd wasn't added / toRemove was added (DONE?)
