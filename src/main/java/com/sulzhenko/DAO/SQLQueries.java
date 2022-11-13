package com.sulzhenko.DAO;
/**
 * This class contains constants of SQL queries
 */
public class SQLQueries {
    private SQLQueries() {
    }

    public static final String SELECT_ALL_USER_FIELDS = "SELECT users.account,\n" +
            "    users.login,\n" +
            "    users.email,\n" +
            "    users.password,\n" +
            "    users.first_name,\n" +
            "    users.last_name,    \n" +
            "    roles.role_description,    \n" +
            "    user_status.status_name,\n" +
            "    users.notifications\n" +
            "FROM timekeeping.users\n" +
            "INNER JOIN timekeeping.roles\n" +
            "ON users.role_id = roles.role_id\n" +
            "INNER JOIN timekeeping.user_status\n" +
            "ON users.status_id = user_status.status_id\n";
    public static final String GET_ALL_USERS = SELECT_ALL_USER_FIELDS +
            "ORDER BY account;";

    public static final String GET_USER_BY_ID = SELECT_ALL_USER_FIELDS +
            "WHERE account = ?;";
    public static final String GET_USER_BY_LOGIN = SELECT_ALL_USER_FIELDS +
            "WHERE login = ?;";
    public static final String GET_USERS_BY_EMAIL = SELECT_ALL_USER_FIELDS +
            "WHERE email = ?;";
//    public static final String GET_USER_BY_PASSWORD = SELECT_ALL_FIELDS +
//            "WHERE password = ? ORDER BY account;";
    public static final String GET_USERS_BY_FIRST_NAME = SELECT_ALL_USER_FIELDS +
            "WHERE first_name = ? ORDER BY account;";
    public static final String GET_USERS_BY_LAST_NAME = SELECT_ALL_USER_FIELDS +
            "WHERE last_name = ? ORDER BY account;";
    public static final String GET_USERS_BY_NOTIFICATIONS = SELECT_ALL_USER_FIELDS +
            "WHERE notifications = ? ORDER BY account;";
    public static final String GET_USERS_BY_ROLE = SELECT_ALL_USER_FIELDS +
            "WHERE role_description = ? ORDER BY account;";
    public static final String GET_USERS_BY_STATUS = SELECT_ALL_USER_FIELDS +
            "WHERE status_name = ? ORDER BY account;";
    public static final String INSERT_USER = "INSERT INTO users\n" +
            "(account, login, email, password, first_name, last_name, role_id,\n" +
            "status_id, notifications)\n" +
            "VALUES\n" +
            "(DEFAULT, ?, ?, ?, ?, ?,\n" +
            "(SELECT role_id FROM roles WHERE role_description = ?),\n" +
            "(SELECT status_id FROM user_status WHERE status_name = ?),\n" +
            "?);";
    public static final String UPDATE_USER = "UPDATE users\n" +
            "SET\n" +
            "login = ?,\n" +
            "email = ?,\n" +
            "password = ?,\n" +
            "first_name = ?,\n" +
            "last_name = ?,\n" +
            "role_id = (SELECT role_id FROM roles WHERE role_description = ?),\n" +
            "status_id = (SELECT status_id FROM user_status WHERE status_name = ?),\n" +
            "notifications = ?\n" +
            "WHERE `login` = ?;";
}
