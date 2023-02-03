package com.sulzhenko.model.DAO;

import static com.sulzhenko.model.DAO.SQLQueries.ActivityQueries.WHERE_ACTIVITY_NAME;
import static com.sulzhenko.model.DAO.SQLQueries.RequestQueries.INNER_JOIN_ACTIVITY;

/**
 * This interface contains constants of SQL queries
 */
public interface SQLQueries {
    String VALUES = "VALUES\n";
    String SET = "SET\n";

    class UserQueries{
        private UserQueries() {
        }
        public static final String SELECT_ALL_USER_FIELDS = "SELECT user.account,\n" +
                "    user.login,\n" +
                "    user.email,\n" +
                "    user.password,\n" +
                "    user.first_name,\n" +
                "    user.last_name,    \n" +
                "    role.role_description,    \n" +
                "    user_status.status_name,\n" +
                "    user.notification\n" +
                "FROM user\n" +
                "INNER JOIN role\n" +
                "ON user.role_id = role.role_id\n" +
                "INNER JOIN user_status\n" +
                "ON user.status_id = user_status.status_id\n";
        public static final String GET_ALL_USERS = SELECT_ALL_USER_FIELDS +
                "ORDER BY account;";
        public static final String GET_USER_BY_ID = SELECT_ALL_USER_FIELDS +
                "WHERE account = ?;";
        public static final String GET_USER_BY_LOGIN = SELECT_ALL_USER_FIELDS +
                "WHERE login = ?;";
        public static final String GET_USERS_BY_EMAIL = SELECT_ALL_USER_FIELDS +
                "WHERE email = ?;";
        public static final String GET_USERS_BY_FIRST_NAME = SELECT_ALL_USER_FIELDS +
                "WHERE first_name = ? ORDER BY account;";
        public static final String GET_USERS_BY_LAST_NAME = SELECT_ALL_USER_FIELDS +
                "WHERE last_name = ? ORDER BY account;";
        public static final String GET_USERS_BY_NOTIFICATION = SELECT_ALL_USER_FIELDS +
                "WHERE notification = ? ORDER BY account;";
        public static final String GET_USERS_BY_ROLE = SELECT_ALL_USER_FIELDS +
                "WHERE role_description = ? ORDER BY account;";
        public static final String GET_USERS_BY_STATUS = SELECT_ALL_USER_FIELDS +
                "WHERE status_name = ? \n" +
                "AND role_description = 'system user';";
        public static final String INSERT_USER = "INSERT INTO user\n" +
                "(account, login, email, password, first_name, last_name, role_id,\n" +
                "status_id, notification)\n" +
                VALUES +
                "(DEFAULT, ?, ?, ?, ?, ?,\n" +
                "(SELECT role_id FROM role WHERE role_description = ?),\n" +
                "(SELECT status_id FROM user_status WHERE status_name = ?),\n" +
                "?);";
        public static final String UPDATE_USER = "UPDATE user\n" +
                SET +
                "login = ?,\n" +
                "email = ?,\n" +
                "password = ?,\n" +
                "first_name = ?,\n" +
                "last_name = ?,\n" +
                "role_id = (SELECT role_id FROM role WHERE role_description = ?),\n" +
                "status_id = (SELECT status_id FROM user_status WHERE status_name = ?),\n" +
                "notification = ?\n" +
                "WHERE `login` = ?;";
        public static final String DELETE_USER = "DELETE FROM user WHERE login = ?;";
        public static final String ADD_STATUS = "INSERT INTO user_status (status_name) VALUE (?);";
        public static final String DELETE_STATUS = "DELETE FROM user_status WHERE status_name = ?;";
        public static final String FIND_ROLE_BY_DESCRIPTION = "SELECT * from role WHERE role_description = ?";
        public static final String FIND_STATUS_BY_NAME = "SELECT * from user_status WHERE status_name = ?";
    }
    class ActivityQueries{
        public static final String COMMON_PART = "SELECT activity.activity_name, \n" +
                "COUNT(user_activity.activity_id) as quantity, \n" +
                "category_of_activity.category_name \n" +
                "FROM activity\n" +
                "LEFT JOIN user_activity\n" +
                "ON activity.activity_id = user_activity.activity_id\n" +
                "INNER JOIN category_of_activity\n" +
                "ON category_of_activity.category_id = activity.category_id\n";

        private ActivityQueries() {
        }
        public static final String SELECT_ALL_ACTIVITY_FIELDS = "SELECT activity.activity_id,\n" +
                "    activity.activity_name,\n" +
                "    category_of_activity.category_name\n" +
                "FROM activity\n" +
                "INNER JOIN category_of_activity\n" +
                "ON activity.category_id = category_of_activity.category_id\n";
        public static final String GET_ALL_ACTIVITIES = SELECT_ALL_ACTIVITY_FIELDS +
                "ORDER BY activity_name COLLATE utf8_unicode_ci;";
        public static final String GET_ACTIVITY_BY_ID = SELECT_ALL_ACTIVITY_FIELDS +
                "WHERE activity_id = ?;";
        public static final String WHERE_ACTIVITY_NAME = "WHERE activity_name = ?;";
        public static final String GET_ACTIVITY_BY_NAME = SELECT_ALL_ACTIVITY_FIELDS +
                WHERE_ACTIVITY_NAME;
        public static final String GET_ACTIVITIES_BY_CATEGORY = SELECT_ALL_ACTIVITY_FIELDS +
                "WHERE category_name = ? ORDER BY activity_id COLLATE utf8_unicode_ci;";
        public static final String INSERT_ACTIVITY = "INSERT INTO activity\n" +
                "(activity_id,\n" +
                "activity_name,\n" +
                "category_id)\n" +
                VALUES +
                "(DEFAULT, ?,\n" +
                "(SELECT category_id FROM category_of_activity WHERE category_name = ?));";
        public static final String UPDATE_ACTIVITY = "UPDATE activity\n" +
                SET +
                "activity_name = ?,\n" +
                "category_id = (SELECT category_id FROM category_of_activity WHERE category_name = ?)\n" +
                WHERE_ACTIVITY_NAME;
        public static final String DELETE_ACTIVITY = "DELETE FROM activity WHERE activity_name = ?;";
        public static final String USER_HAS_ACTIVITY = "INNER JOIN user_activity\n" +
                "ON user.account = user_activity.account\n" +
                "WHERE user_activity.activity_id = \n" +
                "(SELECT activity_id FROM activity WHERE activity_name = ?) \n";
        public static final String USER_HAS_ACTIVITY_AND_NOTIFICATION = USER_HAS_ACTIVITY +
                "AND user.notification = 'on'";
        public static final String FIND_CONNECTED_USERS_WITH_NOTIFICATION = UserQueries.SELECT_ALL_USER_FIELDS + USER_HAS_ACTIVITY_AND_NOTIFICATION;
    }
    class CategoryQueries{
        private CategoryQueries() {
        }

        public static final String ALL_CATEGORY_FIELDS = "SELECT * FROM category_of_activity\n";
        public static final String GET_ALL_CATEGORIES = ALL_CATEGORY_FIELDS +
                "ORDER BY category_name COLLATE utf8_unicode_ci ASC;";

        public static final String GET_CATEGORY_BY_ID = ALL_CATEGORY_FIELDS +
                "WHERE category_id = ?";
        public static final String GET_CATEGORY_BY_NAME = ALL_CATEGORY_FIELDS +
                "WHERE category_name = ?;";
        public static final String UPDATE_CATEGORY = "UPDATE category_of_activity\n" +
                SET +
                "category_name = ?\n" +
                "WHERE category_name = ?;";
        public static final String ADD_CATEGORY = "INSERT INTO category_of_activity (category_name) VALUE (?);";
        public static final String DELETE_CATEGORY = "DELETE FROM category_of_activity WHERE category_name = ?;";
    }

    class RequestQueries {
        private RequestQueries() {
        }
        public static final String INNER_JOIN_ACTIVITY = "INNER JOIN activity\n";
        public static final String SELECT_ALL_REQUEST_FIELDS = "SELECT request.request_id,\n" +
                "    user.login,\n" +
                "    activity.activity_name,\n" +
                "    action_with_request.action_description,\n" +
                "    request.request_description\n" +
                "FROM request\n" +
                "Inner JOIN user\n" +
                "ON request.account = user.account\n" +
                INNER_JOIN_ACTIVITY +
                "ON request.activity_id = activity.activity_id\n" +
                "Inner JOIN action_with_request\n" +
                "ON request.action_id = action_with_request.action_id\n";
        public static final String GET_REQUEST_BY_ID = SELECT_ALL_REQUEST_FIELDS +
                "WHERE request_id = ?;";
        public static final String GET_REQUESTS_BY_LOGIN = SELECT_ALL_REQUEST_FIELDS +
                "WHERE login = ?;";
        public static final String GET_REQUESTS_BY_LOGIN_AND_ACTION = SELECT_ALL_REQUEST_FIELDS +
                "WHERE login = ? AND action_description = ?;";
        public static final String GET_REQUESTS_BY_ACTIVITY = SELECT_ALL_REQUEST_FIELDS +
                WHERE_ACTIVITY_NAME;
        public static final String GET_REQUESTS_BY_ACTION = SELECT_ALL_REQUEST_FIELDS +
                "WHERE action_description = ?;";
        public static final String GET_EQUAL_REQUEST = SELECT_ALL_REQUEST_FIELDS +
                "WHERE login = ?" +
                "AND activity_name = ?" +
                "AND action_description = ?;";
        public static final String INSERT_REQUEST = "INSERT INTO request\n" +
                "(account,\n" +
                "activity_id,\n" +
                "action_id,\n" +
                "request_description)\n" +
                VALUES +
                "((SELECT account FROM user WHERE login = ?),\n" +
                "(SELECT activity_id FROM activity WHERE activity_name = ?),\n" +
                "(SELECT action_id FROM action_with_request WHERE action_description = ?),\n" +
                "?);";
        public static final String UPDATE_REQUEST = "UPDATE request\n" +
                SET +
                "account = (SELECT account FROM user WHERE login = ?),\n" +
                "activity_id = (SELECT activity_id FROM activity WHERE activity_name = ?),\n" +
                "action_id = (SELECT action_id FROM action_with_request WHERE action_description = ?),\n" +
                "request_description = ?\n" +
                "WHERE request_id = ?;";
        public static final String DELETE_REQUEST = "DELETE FROM request WHERE request_id = ?;";
    }
    class UserActivityQueries{
        private UserActivityQueries() {
        }
        public static final String IF_USER_HAS_ACTIVITY = "SELECT * from user_activity WHERE account = ? AND activity_id = ?";
        public static final String ADD_ACTIVITY_TO_USER = "INSERT INTO user_activity\n" +
                "(account,\n" +
                "activity_id)\n" +
                VALUES +
                "(?,\n" +
                "?);";
        public static final String ALL_USER_ACTIVITY_FIELDS = "SELECT \n" +
                "user_activity.account as current_account,\n" +
                "user.login,\n" +
                "activity.activity_name,\n" +
                "time_amount,\n" +
                "(SELECT COUNT(*) FROM user_activity WHERE user_activity.account=current_account) as number,\n" +
                "(SELECT SUM(time_amount) FROM user_activity WHERE user_activity.account=current_account) as total\n" +
                "FROM user_activity\n" +
                "INNER JOIN user\n" +
                "ON user_activity.account=user.account\n" +
                INNER_JOIN_ACTIVITY +
                "ON user_activity.activity_id = activity.activity_id\n";
        public static final String ALL_USER_QUERY = ALL_USER_ACTIVITY_FIELDS +
                "ORDER BY current_account ASC LIMIT %d, %d";
        public static final String FULL_REPORT_QUERY = ALL_USER_ACTIVITY_FIELDS +
                "ORDER BY current_account";
        //!!!!!!!!!!!!!!!!!!!!
        public static final String USER_QUERY = ALL_USER_ACTIVITY_FIELDS +
                "WHERE login = ?\n" +
                "ORDER BY activity_name COLLATE utf8_unicode_ci ASC LIMIT %d, %d";
        public static final String REMOVE_USER_ACTIVITY = "DELETE FROM user_activity\n" +
                "WHERE account = ? AND activity_id = ?;";
        public static final String SET_AMOUNT = "UPDATE user_activity\n" +
                SET +
                "time_amount = ?\n" +
                "WHERE account = ?\n" +
                "AND activity_id = ?;";
        public static final String GET_AMOUNT = "SELECT time_amount\n" +
                "FROM user_activity\n" +
                "WHERE account = ? " +
                "AND activity_id = ?;";
        public static final String GET_NUMBER_OF_RECORDS = "SELECT COUNT(*) FROM user_activity";
        public static final String GET_NUMBER_OF_RECORDS_BY_USER = GET_NUMBER_OF_RECORDS +
                "\n" +
                "INNER JOIN user\n" +
                "ON user.account = user_activity.account\n" +
                "WHERE user.login = ?;";
    }
}
