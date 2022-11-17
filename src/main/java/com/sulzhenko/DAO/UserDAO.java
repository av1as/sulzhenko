package com.sulzhenko.DAO;

import com.sulzhenko.DAO.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sulzhenko.DAO.SQLQueries.UserQueries.*;
/**
 * This class describes CRUD operations with User class entities
 */
public class UserDAO implements DAO <User>{

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
                t = getFields(rs).build();
            }
        } catch (SQLException e){
            e.printStackTrace();
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
                t = getFields(rs).build();
            }
        } catch (SQLException e){
            e.printStackTrace();
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
                t = getFields(rs).build();
            }
        } catch (SQLException e){
            e.printStackTrace();
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
                list.add(getFields(rs).build());
            }
        } catch (SQLException e){
            e.printStackTrace();
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
                list.add(getFields(rs).build());
            }
        } catch (SQLException e){
            e.printStackTrace();
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
                list.add(getFields(rs).build());
            }
        } catch (SQLException e){
            e.printStackTrace();
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
                list.add(getFields(rs).build());
            }
        } catch (SQLException e){
            e.printStackTrace();
            //throw new DAOException("wrong status: " + status);
        }
        if (list.isEmpty()) indicateNoResult("status", status);
        return list;
    }
    public List<User> getByNotifications(String notifications) {
        List<User> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_USERS_BY_NOTIFICATIONS);
        ) {
            stmt.setString(1, notifications);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getFields(rs).build());
            }
        } catch (SQLException e){
            e.printStackTrace();
            //throw new DAOException("wrong notifications: " + notifications);
        }
        if (list.isEmpty()) indicateNoResult("notifications", notifications);
        return list;
    }
    /**
     * This method reads user fields from result set
     */
    private static User.Builder getFields(ResultSet rs) throws SQLException {
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

    @Override
    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_ALL_USERS);
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
            indicateNoUsers();
        }
        return list;
    }

    @Override
    public void save(User t) {
        int count;
        if(isLoginAvailable(t.getLogin()) && isRoleCorrect(t.getRole()) && isStatusCorrect(t.getStatus())) {
            try (Connection con = DataSource.getConnection();
                 PreparedStatement stmt = con.prepareStatement(INSERT_USER);
            ) {
                int k = 0;
                stmt.setString(++k, t.getLogin());
                stmt.setString(++k, t.getEmail());
                stmt.setString(++k, t.getPassword());
                stmt.setString(++k, t.getFirstName());
                stmt.setString(++k, t.getLastName());
                stmt.setString(++k, t.getRole());
                stmt.setString(++k, t.getStatus());
                stmt.setString(++k, t.getNotifications());
                count = stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DAOException("unknown exception: " + t);
            }
        } else if (!isRoleCorrect(t.getRole())){
            throw new DAOException("wrong role: " + t.getRole());
        } else if (!isStatusCorrect(t.getStatus())){
            throw new DAOException("wrong status: " + t.getStatus());
        } else if (!isLoginAvailable(t.getLogin())){
            throw new DAOException("wrong login: " + t.getLogin());

            } else {
            throw new DAOException("unknown exception: " + t);
        }
//            System.out.println(count > 0 ? "user saved: " + getByLogin(t.getLogin()) : "user wasn't saved");
            }

    @Override
    public void update(User t, String[] params) {
//        if(!isLoginAvailable(t.getLogin())) {
        int count = 0;
        String oldLogin = t.getLogin();
        if(isLoginAvailable(params[0]) && isRoleCorrect(params[5]) && isStatusCorrect(params[6])) {
            try (Connection con = DataSource.getConnection();
                 PreparedStatement stmt = con.prepareStatement(UPDATE_USER);
            ) {
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
                count = stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DAOException("unknown exception: " + Arrays.toString(params));
            }
        } else if (!isRoleCorrect(params[5])){
            throw new DAOException("wrong new role: " + params[5]);
        } else if (!isStatusCorrect(params[6])){
            throw new DAOException("wrong new status: " + params[6]);
        } else if (!isLoginAvailable(params[0])){
            throw new DAOException("wrong new login: " + params[0]);
        } else {
            throw new DAOException("unknown exception: " + t);
        }
//        System.out.println(count > 0 ? "user updated: " + params[0] : "user wasn't updated");
//        } else {
//            System.out.println("this user doesn't exist: " + t.getLogin());
//        }
    }

    @Override
    public void delete(User t) {
        if(!isLoginAvailable(t.getLogin())) {
            int count = 0;
            try (Connection con = DataSource.getConnection();
                 PreparedStatement stmt = con.prepareStatement(DELETE_USER);
            ) {
                stmt.setString(1, t.getLogin());
                count = stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DAOException("wrong user: " + t.getLogin());
            }
//            System.out.println(count > 0 ? "user deleted: " + t.getLogin() : "user wasn't deleted");
        } else {
                throw new DAOException("wrong user: " + t.getLogin());
//                System.out.println("this user doesn't exist: " + t.getLogin());
        }
    }
    public void indicateNoResult(String name, Object value){
        System.out.println("No user with such " + name + ": " + value);
    }
    public void indicateNoUsers(){
        System.out.println("No user available");
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
             PreparedStatement stmt = con.prepareStatement("SELECT * from roles WHERE role_description = ?");
        ) {
            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new DAOException("unknown exception");
        }
        return false;
    }
    public boolean isStatusCorrect(String status) {
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * from user_status WHERE status_name = ?");
        ) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new DAOException("unknown exception");
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
        int count;
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(ADD_STATUS);
        ) {
            stmt.setString(1, statusName);
            count = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("wrong name: " + statusName);
        }
//        System.out.println(count > 0 ? "status added: " + statusName : "status wasn't added");
    }
    public void deleteStatus(String statusName) {
            int count = 0;
            try (Connection con = DataSource.getConnection();
                 PreparedStatement stmt = con.prepareStatement(DELETE_STATUS);
            ) {
                stmt.setString(1, statusName);
                count = stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DAOException("wrong status: " + statusName);
            }
//            System.out.println(count > 0 ? "status deleted: " + statusName : "status wasn't deleted");
        }




    private static UserDAO userDAO = new UserDAO();;

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

