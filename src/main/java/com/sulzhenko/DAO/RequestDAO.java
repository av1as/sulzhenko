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

    private static Logger logger = LogManager.getLogger(RequestDAO.class);
    UserDAO userDAO = new UserDAO();
    ActivityDAO activityDAO = new ActivityDAO();
    static RequestDAO requestDAO = new RequestDAO();
    @Override
    public Request getById(int id) {
        Request t = null;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_REQUEST_BY_ID)
        ) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                t = getFields(rs).build();
            }
        } catch (SQLException e){
            logger.fatal("unknown exception with id: {}", id);
//            e.printStackTrace();
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
            logger.fatal("unknown exception with login: {}", login);
//            e.printStackTrace();
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
            logger.fatal("unknown exception with activity: {}", activityName);
//            e.printStackTrace();
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
            logger.fatal("unknown exception with action: {}", actionName);
//            e.printStackTrace();
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
            logger.fatal("unknown exception with list of requests");
//            e.printStackTrace();
            //throw new DAOException("wrong notifications: " + notifications);
        }
        if (list.isEmpty()) {
            indicateNoRequests();
        }
        return list;
    }

    @Override
    public void save(Request t) {
//        int count = 0;
        if (isDataCorrect(t)){
            try (Connection con = DataSource.getConnection();
                 PreparedStatement stmt = con.prepareStatement(INSERT_REQUEST, Statement.RETURN_GENERATED_KEYS);
        ) {
                setRequestFields(t, stmt);
                //            count =
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
            t.setId(rs.next() ? rs.getInt(1) : 0);
        } catch (SQLException e) {
            logger.fatal("unknown exception with request: {}", t);
//                e.printStackTrace();
//            throw new DAOException("unknown exception: " + t);
            }
        } else if(userDAO.isLoginAvailable(t.getLogin())){
            logger.info("wrong login: {}", t.getLogin());
//            throw new DAOException("wrong login: " + t.getLogin());
        } else if(activityDAO.isNameAvailable(t.getActivityName())){
            logger.info("wrong activity: {}", t.getActivityName());
//            throw new DAOException("wrong activity name: " + t.getActivityName());
        } else if(!requestDAO.isActionCorrect(t)) {
            logger.info("wrong action to do: {}", t.getActionToDo());
//            throw new DAOException("wrong action to do: " + t.getActionToDo());
        } else {
            logger.fatal("unknown exception: {}", t);
//            throw new DAOException("unknown exception: " + t);
        }
    }

    private static void setRequestFields(Request t, PreparedStatement stmt) throws SQLException {
        int k = 0;
        stmt.setString(++k, t.getLogin());
        stmt.setString(++k, t.getActivityName());
        stmt.setString(++k, t.getActionToDo());
        stmt.setString(++k, t.getDescription());

    }

    private boolean isDataCorrect(Request t) {
        return !userDAO.isLoginAvailable(t.getLogin()) && !activityDAO.isNameAvailable(t.getActivityName()) && requestDAO.isActionCorrect(t);
    }

    @Override
    public void update(Request t, String[] params) {
        String newLogin = params[0];
        String newActivity = params[1];
        String newAction = params[2];
//        String newDescription = params[3];
        Request newRequest = createUpdatedRequest(params);
//        int count = 0;
        if(isDataCorrect(createUpdatedRequest(params))){
//        if (isDataToUpdateCorrect(newLogin, newActivity, newRequest)){
            try (Connection con = DataSource.getConnection();
                 PreparedStatement stmt = con.prepareStatement(UPDATE_REQUEST);
            ) {
//                int k = 0;
//                stmt.setString(++k, t.getLogin());
//                stmt.setString(++k, t.getActivityName());
//                stmt.setString(++k, t.getActionToDo());
//                stmt.setString(++k, t.getDescription());
                setRequestFields(t, stmt);
                stmt.setInt(5, t.getId());
//                count =
                stmt.executeUpdate();
            } catch (SQLException e) {
                logger.fatal("unknown exception with: {}", t);
//                e.printStackTrace();
//                throw new DAOException("unknown exception: " + t);
            }
        } else if(userDAO.isLoginAvailable(newLogin)){
            logger.info("wrong login: {}", newLogin);
//            throw new DAOException("wrong login: " + t.getLogin());
        } else if(activityDAO.isNameAvailable(newActivity)){
            logger.info("wrong activity name: {}", newActivity);
//            throw new DAOException("wrong activity name: " + t.getActivityName());
        } else if(!requestDAO.isActionCorrect(newRequest)) {
            logger.info("wrong action to do: {}", newAction);
//            throw new DAOException("wrong action to do: " + newRequest.getActionToDo());
        } else {
            logger.fatal("unknown exception: {}, {}",t, params);
//            throw new DAOException("unknown exception: " + t + Arrays.toString(params));
        }
    }

//    private boolean isDataToUpdateCorrect(String newLogin, String newActivity, Request newRequest) {
//        return !userDAO.isLoginAvailable(newLogin) && !activityDAO.isNameAvailable(newActivity) && requestDAO.isActionCorrect(newRequest);
//    }

    private static Request createUpdatedRequest(String[] params) {
        int k = -1;
        return Request.builder()
                .withLogin(params[++k])
                .withActivityName(params[++k])
                .withActionToDo(params[++k])
                .withDescription(params[++k])
                .build();
    }

    @Override
    public void delete(Request t) {
//        int count = 0;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_REQUEST);
        ) {
            stmt.setInt(1, t.getId());
//            count =
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.fatal("unknown exception with request {}", t);
//            e.printStackTrace();
//            throw new DAOException("wrong request: " + t);
        }
//        System.out.println(count > 0 ? "request deleted: " + t: "request wasn't deleted");
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
        logger.info("No request with such {}: {}",name, value);
    }
    public void indicateNoRequests(){
        logger.info("No requests available");
    }
    public boolean isActionCorrect(Request request){
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
        return canRequestBeAdded(request) || canRequestBeRemoved(request);
    }
    public boolean canRequestBeAdded(Request request){
        return (Objects.equals(request.getActionToDo(), "add")) &&
                !ifUserHasActivity(userDAO.getByLogin(request.getLogin()),
                        activityDAO.getByName(request.getActivityName()));
    }
    public boolean canRequestBeRemoved(Request request){
        return (Objects.equals(request.getActionToDo(), "remove") &&
                ifUserHasActivity(userDAO.getByLogin(request.getLogin()),
                        activityDAO.getByName(request.getActivityName())));
    }
    public boolean ifUserHasActivity(User u, Activity a){
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(IF_USER_HAS_ACTIVITY);
        ) {
            stmt.setInt(1, u.getAccount());
            stmt.setInt(2, a.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            logger.fatal("unknown exception with user {} and activity {}", u, a);
//            throw new DAOException("unknown exception");
        }
        return false;
    }
    public void addActivityToUser(Request request){
        User user = userDAO.getByLogin(request.getLogin());
        Activity activity = activityDAO.getByName(request.getActivityName());
        Connection con = null;
        PreparedStatement stmt = null;
        if(Objects.equals(request.getActionToDo(), "add")){
            try{
                con = DataSource.getConnection();
                con.setAutoCommit(false);
                stmt = con.prepareStatement(ADD_ACTIVITY_TO_USER);
                stmt.setInt(1, user.getAccount());
                stmt.setInt(2, activity.getId());
                stmt.executeUpdate();

                delete(request);

            } catch (SQLException e) {
                try {
                    assert con != null;
                    con.rollback();
                } catch (SQLException ex) {
                    logger.fatal(ex);
                }
                logger.fatal(e);
            } finally {
                close(stmt);
                close(con);
            }
        }
    }
    public void removeUserActivity(Request request){
        User user = userDAO.getByLogin(request.getLogin());
        Activity activity = activityDAO.getByName(request.getActivityName());
        Connection con = null;
        PreparedStatement stmt = null;
        if(Objects.equals(request.getActionToDo(), "remove")){
            try{
                con = DataSource.getConnection();
                con.setAutoCommit(false);
                stmt = con.prepareStatement(REMOVE_USER_ACTIVITY);
                stmt.setInt(1, user.getAccount());
                stmt.setInt(2, activity.getId());
                stmt.executeUpdate();

                delete(request);

            } catch (SQLException e) {
                try {
                    assert con != null;
                    con.rollback();
                } catch (SQLException ex) {
                    logger.fatal(ex);
                }
                logger.fatal(e);
            } finally {
                close(stmt);
                close(con);
            }
        }
    }
    public void close(AutoCloseable stmt){
        if(stmt !=null) {
            try {
                stmt.close();
            } catch (Exception e) {
                logger.fatal(e);
            }
        }
    }


//    private static RequestDAO requestDAO = new RequestDAO();
    public static void main(String[] args) {
        Request r = new Request().builder()
                .withLogin("ivan")
                .withActivityName("activity1")
                .withActionToDo("add")
                .withDescription("very fast")
                .build();
        requestDAO.save(r);

        String[] params = {"ivan", "activity2", "add", "as fast as you can"};
        requestDAO.update(requestDAO.getById(1), params);

//        requestDAO.delete(requestDAO.getById(4));
    }
}

//add checking if there is equal request
// or activity toAdd wasn't added / toRemove was added (DONE?)
