package com.sulzhenko.DAO;

import com.sulzhenko.DAO.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sulzhenko.DAO.SQLQueries.UserQueries.*;
/**
 * This class describes CRUD operations with User class entities
 */
public class UserDAO implements DAO <User>{
    private static UserDAO userDAO;
    private UserDAO(){
    }
    public static synchronized UserDAO getInstance() {
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
        return userDAO;
    }
    private static final Logger logger = LogManager.getLogger(UserDAO.class);
    @Override
    public User get(Object parameter, String querySQL, Connection con){
        User t = null;
        try (PreparedStatement stmt = con.prepareStatement(querySQL)
        ) {
            stmt.setObject(1, parameter);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                t = getUserWithFields(rs);
            }
        } catch (SQLException e){
            logger.fatal(e.getMessage());
        }
        return t;
    }
    @Override
    public List<User> getList(Object parameter, String querySQL, Connection con){
        List<User> list = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(querySQL)
        ) {
            stmt.setObject(1, parameter);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getUserWithFields(rs));
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        return list;
    }
    public User getById(int id, Connection con) {
        return get(id, GET_USER_BY_ID, con);
    }

    public User getByLogin(String login, Connection con) {
        return get(login, GET_USER_BY_LOGIN, con);
    }

    public List<User> getByEmail(String email, Connection con) {
        return getList(email, GET_USERS_BY_EMAIL, con);
    }
    public List<User> getByFirstName(String firstName, Connection con) {
        return getList(firstName, GET_USERS_BY_FIRST_NAME, con);
    }
    public List<User> getByLastName(String lastName, Connection con) {
        return getList(lastName, GET_USERS_BY_LAST_NAME, con);
    }
    public List<User> getByRole(String role, Connection con) {
        return getList(role, GET_USERS_BY_ROLE, con);
    }
    public List<User> getByStatus(String status, Connection con) {
        return getList(status, GET_USERS_BY_STATUS, con);
    }
    public List<User> getByNotification(String notification, Connection con) {
        return getList(notification, GET_USERS_BY_NOTIFICATION, con);
    }

    @Override
    public List<User> getAll(Connection con) {
        List<User> list = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(GET_ALL_USERS)
        ) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getUserWithFields(rs));
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        return list;
    }
    /**
     * This method reads user fields from result set
     */
    static User getUserWithFields(ResultSet rs) throws SQLException {
        return new User.Builder()
                .withAccount(rs.getInt(1))
                .withLogin(rs.getString(2))
                .withEmail(rs.getString(3))
                .withPassword(rs.getString(4))
                .withFirstName(rs.getString(5))
                .withLastName(rs.getString(6))
                .withRole(rs.getString(7))
                .withStatus(rs.getString(8))
                .withNotification(rs.getString(9))
                .build();
    }
    @Override
    public void save(User t, Connection con) {
            try {
                isDataCorrect(t, con);
                PreparedStatement stmt = con.prepareStatement(INSERT_USER);
                setUserFields(t, stmt);
            } catch (DAOException e){
                logger.info(e.getMessage());
            } catch (SQLException e) {
                logger.fatal(e.getMessage());
            }
    }
    @Override
    public void update(User t, String[] params, Connection con) {
        String oldLogin = t.getLogin();
        try {
            isUpdateCorrect(params, oldLogin, con);
            PreparedStatement stmt = con.prepareStatement(UPDATE_USER);
            updateUserFields(params, oldLogin, stmt);
            notifyAboutUpdate(t, "updated");
        } catch (DAOException e){
            logger.info(e.getMessage());
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
        }
    }
    @Override
    public void delete(User t, Connection con) {
        if(!isLoginAvailable(t.getLogin(), con)) {
            try (
                    PreparedStatement stmt = con.prepareStatement(DELETE_USER)
            ) {
                stmt.setString(1, t.getLogin());
                stmt.executeUpdate();
            } catch (SQLException e) {
                logger.fatal(e.getMessage());
            }
        } else {
            logger.info("wrong user: {}", t.getLogin());
        }
    }

    public void isDataCorrect(User t, Connection con) {
        if (!isRoleCorrect(t.getRole(), con)){
            throw new DAOException("wrong role: " + t.getRole());
        } else if (!isStatusCorrect(t.getStatus(), con)){
            throw new DAOException("wrong status: " + t.getStatus());
        } else if (!isLoginAvailable(t.getLogin(), con)){
            throw new DAOException("wrong login: " + t.getLogin());
        }
    }

    private static void setUserFields(User t, PreparedStatement stmt) throws SQLException {
        int k = 0;
        stmt.setString(++k, t.getLogin());
        stmt.setString(++k, t.getEmail());
        stmt.setString(++k, t.getPassword());
        stmt.setString(++k, t.getFirstName());
        stmt.setString(++k, t.getLastName());
        stmt.setString(++k, t.getRole());
        stmt.setString(++k, t.getStatus());
        stmt.setString(++k, t.getNotification());
        stmt.executeUpdate();
    }

    public void isUpdateCorrect(String[] params, String oldLogin, Connection con) {
        if (!isRoleCorrect(params[5], con)){
            throw new DAOException("wrong role: " + params[5]);
        } else if (!isStatusCorrect(params[6], con)){
            throw new DAOException("wrong status: " + params[6]);
        } else if (!Objects.equals(params[0], oldLogin) && !isLoginAvailable(params[0], con)){
            throw new DAOException("wrong login: " + params[0]);
        } else if (isLoginAvailable(oldLogin, con)){
            throw new DAOException("wrong login: " + oldLogin);
        }
    }

    private static void updateUserFields(String[] params, String oldLogin, PreparedStatement stmt)
            throws SQLException {
        int k = 0;
        stmt.setString(++k, params[k - 1]);
        stmt.setString(++k, params[k - 1]);
        stmt.setString(++k, params[k - 1]);
        stmt.setString(++k, params[k - 1]);
        stmt.setString(++k, params[k - 1]);
        stmt.setString(++k, params[k - 1]);
        stmt.setString(++k, params[k - 1]);
        stmt.setString(++k, params[k - 1]);
        stmt.setString(++k, oldLogin);
        stmt.executeUpdate();
    }


    public void indicateNoResult(String name, Object value){
        logger.info("No user with such {} : {}",  name, value);
    }
    public void indicateNoUsers(){
        logger.info("No user available");
    }
    public boolean isLoginAvailable(String login, Connection con){
        return getByLogin(login, con) == null;
    }
    public boolean isRoleCorrect(String role, Connection con) {
        try (PreparedStatement stmt = con.prepareStatement(FIND_ROLE_BY_DESCRIPTION)
        ) {
            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            logger.fatal("unknown exception when checking role");
            throw new DAOException("unknown exception when checking role");
        }
        return false;
    }
    public boolean isStatusCorrect(String status, Connection con) {
        try (PreparedStatement stmt = con.prepareStatement(FIND_STATUS_BY_NAME)
        ) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            logger.fatal("unknown exception when checking status");
            throw new DAOException("unknown exception when checking status");
        }
        return false;
    }
    public void addStatus(String statusName, Connection con){
        try (PreparedStatement stmt = con.prepareStatement(ADD_STATUS)
        ) {
            stmt.setString(1, statusName);
                    stmt.executeUpdate();
        } catch (SQLException e) {
            logger.info("wrong name: {}", statusName);
        }
    }
    public void deleteStatus(String statusName, Connection con) {
            try (PreparedStatement stmt = con.prepareStatement(DELETE_STATUS)
            ) {
                stmt.setString(1, statusName);
                        stmt.executeUpdate();
            } catch (SQLException e) {
                logger.info("wrong status: {}", statusName);
                throw new DAOException("wrong status: " + statusName);
            }
        }

    public void notifyAboutUpdate(User u, String description){

            // create method to notify connected users



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



    public static void main(String[] args) throws SQLException {
        Connection con = DataSource.getConnection();
        UserDAO ud = getInstance();
//        User t = ud.getById(8);
//        System.out.println(t);
//
//        System.out.println(new UserDAO().getByEmail("mykola.dyak@gmail.com"));
//        System.out.println(new UserDAO().getByLogin("maria"));
//        System.out.println(new UserDAO().getByFirstName("ivan"));
//        System.out.println(new UserDAO().getByLastName("Сум"));
//        System.out.println(new UserDAO().getByRole("system user"));
//        System.out.println(new UserDAO().getByStatus("active"));
//        System.out.println(new UserDAO().getByNotifications("yes"));
//        System.out.println(new UserDAO().getAll());
        User me = new User.Builder()
                .withLogin("avlas2")
                .withEmail("me@me.me")
                .withPassword("asdf")
                .withFirstName("artem")
                .withLastName("sul'zhenko")
                .withRole("AAadministrator")
                .withStatus("unknown status")
                .withNotification("yes")
                .build();
        ud.save(me, con);
//        System.out.println(userDAO.getAll());
//
//
//        String[] avlas27 = {"avlas27", "me27@me.me", "asdf27", "27", "27","administrator", "active", "no"};
//        userDAO.update(userDAO.getByLogin("avlas26"), avlas27);
//        userDAO.delete(userDAO.getByLogin("avlas2"));
//        System.out.println(userDAO.getAll());
//        userDAO.addStatus("deactivated");
//        userDAO.addStatus("some status");
//        userDAO.deleteStatus("some status");
//        System.out.println(userDAO.isRoleCorrect("administrator"));
//        System.out.println(userDAO.isRoleCorrect("incorrect"));
//        System.out.println(userDAO.isStatusCorrect("active"));
//        System.out.println(userDAO.isStatusCorrect("incorrect"));
    }
}

