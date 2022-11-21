package com.sulzhenko.DAO;

import com.sulzhenko.DAO.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.sulzhenko.DAO.SQLQueries.UserQueries.*;
/**
 * This class describes CRUD operations with User class entities
 */
public class UserDAO implements DAO <User>{
    private static Logger logger = LogManager.getLogger(UserDAO.class);
    @Override
//    public Optional<User> getById(int id) {
//        User t = null;
//        try (Connection con = DataSource.getConnection();
//             PreparedStatement stmt = con.prepareStatement(GET_USER_BY_ID);
//             ) {
//            stmt.setInt(1, id);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                User.Builder b = getFields(rs);
//                t = b.build();
//            }
//        } catch (SQLException e){
//            e.printStackTrace();
//            //throw new DAOException("wrong id: " + id);
//            System.out.println("wrong id: " + id);
//        }
//        return Optional.ofNullable(t);
//    }

    public User getById(int id) {
        User t = null;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_USER_BY_ID);
        ) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                t = getUserFields(rs).build();
            }
        } catch (SQLException e){
            logger.fatal("no user with id {}", id);
//            e.printStackTrace();
        }
        if (t == null) indicateNoResult("id", id);
        return t;
    }
    public User getByLogin(String login) {
        User t = null;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_USER_BY_LOGIN);
        ) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                t = getUserFields(rs).build();
            }
        } catch (SQLException e){
            logger.fatal("no user with login {}", login);
//            e.printStackTrace();
            //throw new DAOException("wrong login: " + login);
        }
        if (t == null) indicateNoResult("login", login);
        return t;
    }
    public User getByEmail(String email) {
        User t = null;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_USERS_BY_EMAIL);
        ) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                t = getUserFields(rs).build();
            }
        } catch (SQLException e){
            logger.info("unknown exception with email: {}", email);
//            e.printStackTrace();
            //throw new DAOException("wrong email: " + email);
        }
        if (t == null) indicateNoResult("email", email);
        return t;
    }
//    public List<User> getByPassword(String password) {
//        //User t = null;
//        List<User> list= new ArrayList<>();
//        try (Connection con = DataSource.getConnection();
//             PreparedStatement stmt = con.prepareStatement(GET_USER_BY_PASSWORD);
//        ) {
//            stmt.setString(1, password);
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                User.Builder b = getFields(rs);
//                //t = b.build();
//                list.add(b.build());
//            }
//        } catch (SQLException e){
//            e.printStackTrace();
//            throw new DAOException("wrong password: " + password);
//        }
//        return list;
//    }
    public List<User> getByFirstName(String firstName) {
        List<User> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_USERS_BY_FIRST_NAME);
        ) {
            stmt.setString(1, firstName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getUserFields(rs).build());
            }
        } catch (SQLException e){
            logger.info("unknown exception with first name: {}", firstName);
//            e.printStackTrace();
            //throw new DAOException("wrong first name: " + firstName);
        }
        if (list.isEmpty()) indicateNoResult("first name", firstName);
        return list;
    }
    public List<User> getByLastName(String lastName) {
        List<User> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_USERS_BY_LAST_NAME);
        ) {
            stmt.setString(1, lastName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getUserFields(rs).build());
            }
        } catch (SQLException e){
            logger.info("unknown exception with last name: {}", lastName);
//            e.printStackTrace();
            //throw new DAOException("wrong last name: " + lastNme);
        }
        if (list.isEmpty()) indicateNoResult("last name", lastName);
        return list;
    }
    public List<User> getByRole(String role) {
        List<User> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_USERS_BY_ROLE);
        ) {
            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getUserFields(rs).build());
            }
        } catch (SQLException e){
            logger.info("unknown exception with role: {}", role);
//            e.printStackTrace();
            //throw new DAOException("wrong role: " + role);
        }
        if (list.isEmpty()) indicateNoResult("role", role);
        return list;
    }
    public List<User> getByStatus(String status) {
        List<User> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_USERS_BY_STATUS);
        ) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getUserFields(rs).build());
            }
        } catch (SQLException e){
            logger.info("unknown exception with status: {}", status);
//            e.printStackTrace();
            //throw new DAOException("wrong status: " + status);
        }
        if (list.isEmpty()) indicateNoResult("status", status);
        return list;
    }
    public List<User> getByNotification(String notification) {
        List<User> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_USERS_BY_NOTIFICATION);
        ) {
            stmt.setString(1, notification);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getUserFields(rs).build());
            }
        } catch (SQLException e){
            logger.info("unknown exception with notification: {}", notification);
//            e.printStackTrace();
            //throw new DAOException("wrong notifications: " + notifications);
        }
        if (list.isEmpty()) indicateNoResult("notification", notification);
        return list;
    }


    @Override
    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_ALL_USERS);
        ) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getUserFields(rs).build());
            }
        } catch (SQLException e){
            logger.info("unknown exception with list of all users");
//            e.printStackTrace();
            //throw new DAOException("wrong notifications: " + notifications);
        }
        if (list.isEmpty()) {
            indicateNoUsers();
        }
        return list;
    }
    /**
     * This method reads user fields from result set
     */
    static User.Builder getUserFields(ResultSet rs) throws SQLException {
        return User.builder().withAccount(rs.getInt(1))
                .withLogin(rs.getString(2))
                .withEmail(rs.getString(3))
                .withPassword(rs.getString(4))
                .withFirstName(rs.getString(5))
                .withLastName(rs.getString(6))
                .withRole(rs.getString(7))
                .withStatus(rs.getString(8))
                .withNotifications(rs.getString(9));
    }


    //WORKS
    @Override
    public void save(User t) {
        Connection con = null;
        PreparedStatement stmt = null;
//        int count;
//        if(isDataCorrect(t)) {
            try {
                isDataCorrect(t);
                con = DataSource.getConnection();
                stmt = con.prepareStatement(INSERT_USER);
                setUserFields(t, stmt);
            } catch (DAOException e){
                logger.info(e.getMessage());
            } catch (SQLException e) {
                logger.fatal("unknown exception: {}", t);
//                e.printStackTrace();
//                throw new DAOException("unknown exception: " + t);
            } finally {
                close(stmt);
                close(con);
            }
//        } else if (!isRoleCorrect(t.getRole())){
//            logger.info("wrong role: {}", t.getRole());
//            //throw new DAOException("wrong role: " + t.getRole());
//        } else if (!isStatusCorrect(t.getStatus())){
//            logger.info("wrong status: {}", t.getStatus());
////            throw new DAOException("wrong status: " + t.getStatus());
//        } else if (!isLoginAvailable(t.getLogin())){
//            logger.info("wrong login: {}", t.getLogin());
////            throw new DAOException("wrong login: " + t.getLogin());
//            } else {
//            logger.fatal("unknown exception: {}", t);
////            throw new DAOException("unknown exception: " + t);
//        }
//            System.out.println(count > 0 ? "user saved: " + getByLogin(t.getLogin()) : "user wasn't saved");
    }
    @Override
    public void update(User t, String[] params) {
//        if(!isLoginAvailable(t.getLogin())) {
//        int count = 0;
        String oldLogin = t.getLogin();
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            isUpdateCorrect(params, oldLogin);
            con = DataSource.getConnection();
            stmt = con.prepareStatement(UPDATE_USER);
            updateUserFields(params, oldLogin, stmt);
            notifyAboutUpdate(t, "updated");
        } catch (DAOException e){
            logger.info(e.getMessage());
        } catch (SQLException e) {
            logger.fatal("unknown exception: {}", t);
//                e.printStackTrace();
//                throw new DAOException("unknown exception: " + t);
        } finally {
            close(stmt);
            close(con);
        }

//        System.out.println(count > 0 ? "user updated: " + params[0] : "user wasn't updated");
//        } else {
//            System.out.println("this user doesn't exist: " + t.getLogin());
//        }
    }


    //WORKS
    public void isDataCorrect(User t) {
        if (!isRoleCorrect(t.getRole())){
            throw new DAOException("wrong role: " + t.getRole());
        } else if (!isStatusCorrect(t.getStatus())){
            throw new DAOException("wrong status: " + t.getStatus());
        } else if (!isLoginAvailable(t.getLogin())){
            throw new DAOException("wrong login: " + t.getLogin());
        }
//        return isLoginAvailable(t.getLogin()) && isRoleCorrect(t.getRole()) && isStatusCorrect(t.getStatus());
    }

    private static void setUserFields(User t, PreparedStatement stmt) throws SQLException {
//        int count;
        int k = 0;
        stmt.setString(++k, t.getLogin());
        stmt.setString(++k, t.getEmail());
        stmt.setString(++k, t.getPassword());
        stmt.setString(++k, t.getFirstName());
        stmt.setString(++k, t.getLastName());
        stmt.setString(++k, t.getRole());
        stmt.setString(++k, t.getStatus());
        stmt.setString(++k, t.getNotifications());
//        count =
        stmt.executeUpdate();
    }



    public void isUpdateCorrect(String[] params, String oldLogin) {
        if (!isRoleCorrect(params[5])){
            throw new DAOException("wrong role: " + params[5]);
        } else if (!isStatusCorrect(params[6])){
            throw new DAOException("wrong status: " + params[6]);
        } else if (!Objects.equals(params[0], oldLogin) && !isLoginAvailable(params[0])){
            throw new DAOException("wrong login: " + params[0]);
        } else if (isLoginAvailable(oldLogin)){
            throw new DAOException("wrong login: " + oldLogin);
        }
    }

    private static void updateUserFields(String[] params, String oldLogin, PreparedStatement stmt) throws SQLException {
//        int count;
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
//        count =
        stmt.executeUpdate();
    }

    @Override
    public void delete(User t) {
        if(!isLoginAvailable(t.getLogin())) {
//            int count = 0;
            try (Connection con = DataSource.getConnection();
                 PreparedStatement stmt = con.prepareStatement(DELETE_USER);
            ) {
                stmt.setString(1, t.getLogin());
//                count =
                stmt.executeUpdate();
            } catch (SQLException e) {
                logger.fatal("wrong user: {}", t.getLogin());
//                e.printStackTrace();
//                throw new DAOException("wrong user: " + t.getLogin());
            }
//            System.out.println(count > 0 ? "user deleted: " + t.getLogin() : "user wasn't deleted");
        } else {
            logger.info("wrong user: {}", t.getLogin());
//            throw new DAOException("wrong user: " + t.getLogin());
//                System.out.println("this user doesn't exist: " + t.getLogin());
        }
    }
    public void indicateNoResult(String name, Object value){
        logger.info("No user with such {} : {}",  name, value);
    }
    public void indicateNoUsers(){
        logger.info("No user available");
    }
    public boolean isLoginAvailable(String login){
//        try (Connection con = DataSource.getConnection();
//             PreparedStatement stmt = con.prepareStatement("SELECT * from users WHERE login = ?");
//        ) {
//            stmt.setString(1, login);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                return true;
//            }
//        } catch (SQLException e) {
//            throw new DAOException("unknown exception");
//        }
//        return false;
        return getByLogin(login) == null;
    }
    public boolean isRoleCorrect(String role) {
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(FIND_ROLE_BY_DESCRIPTION);
        ) {
            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            logger.fatal("unknown exception when checking role");
//            throw new DAOException("unknown exception");
        }
        return false;
    }
    public boolean isStatusCorrect(String status) {
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(FIND_STATUS_BY_NAME);
        ) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            logger.fatal("unknown exception when checking status");
//            throw new DAOException("unknown exception");
        }
        return false;
    }
//    public void addRole(String roleName){
//        int count;
//        try (Connection con = DataSource.getConnection();
//             PreparedStatement stmt = con.prepareStatement(ADD_ROLE);
//        ) {
//            stmt.setString(1, roleName);
//            count = stmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DAOException("wrong name: " + roleName);
//        }
//        System.out.println(count > 0 ? "role added: " + roleName : "role wasn't added");
//    }
    public void addStatus(String statusName){
//        int count;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(ADD_STATUS);
        ) {
            stmt.setString(1, statusName);
//            count =
                    stmt.executeUpdate();
        } catch (SQLException e) {
            logger.info("wrong name: {}", statusName);
//            e.printStackTrace();
//            throw new DAOException("wrong name: " + statusName);
        }
//        System.out.println(count > 0 ? "status added: " + statusName : "status wasn't added");
    }
    public void deleteStatus(String statusName) {
//            int count = 0;
            try (Connection con = DataSource.getConnection();
                 PreparedStatement stmt = con.prepareStatement(DELETE_STATUS);
            ) {
                stmt.setString(1, statusName);
//                count =
                        stmt.executeUpdate();
            } catch (SQLException e) {
                logger.info("wrong status: {}", statusName);
//                e.printStackTrace();
//                throw new DAOException("wrong status: " + statusName);
            }
//            System.out.println(count > 0 ? "status deleted: " + statusName : "status wasn't deleted");
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
    public void notifyAboutUpdate(User u, String description){

            // create method to notify connected users



    }




    private static final UserDAO userDAO = new UserDAO();

    public static void main(String[] args) {
//        User t = userDAO.getById(8);
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
        User me = User.builder()
                .withLogin("avlas2")
                .withEmail("me@me.me")
                .withPassword("asdf")
                .withFirstName("artem")
                .withLastName("sul'zhenko")
                .withRole("AAadministrator")
                .withStatus("unknown status")
                .withNotifications("yes")
                .build();
        userDAO.save(me);
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

