package com.sulzhenko.model.DAO;

/**
 * This class contains constants of SQL queries
 */
public abstract class SQLQueries {

    public static class DroppingTablesQueries{
        public static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS user ;\n";
        public static final String DROP_ACTION_WITH_REQUEST_TABLE =
                "DROP TABLE IF EXISTS action_with_request ;\n";
        public static final String DROP_CATEGORY_OF_ACTIVITY_TABLE =
                "DROP TABLE IF EXISTS category_of_activity` ;\n";
        public static final String DROP_ACTIVITY_TABLE = "DROP TABLE IF EXISTS activity` ;\n";
        public static final String DROP_ROLE_TABLE = "DROP TABLE IF EXISTS role ;\n";
        public static final String DROP_USER_STATUS_TABLE = "DROP TABLE IF EXISTS user_status ;\n";
        public static final String DROP_REQUEST_TABLE = "DROP TABLE IF EXISTS request ;\n";
        public static final String DROP_USER_ACTIVITY_TABLE = "DROP TABLE IF EXISTS user_activity` ;\n";


    }
    public static class CreatingTablesQueries{
        public static final String CREATE_USER_TABLE =
                //DROP_USERS_TABLE +
                "CREATE TABLE IF NOT EXISTS user (\n" +
                "  account INT NOT NULL AUTO_INCREMENT,\n" +
                "  login VARCHAR(45) NOT NULL,\n" +
                "  email VARCHAR(45) NOT NULL,\n" +
                "  password VARCHAR(250) NOT NULL,\n" +
                "  first_name VARCHAR(45) NULL DEFAULT NULL,\n" +
                "  last_name VARCHAR(45) NULL DEFAULT NULL,\n" +
                "  role_id INT NOT NULL,\n" +
                "  status_id INT NOT NULL,\n" +
                "  notifications VARCHAR(45) NOT NULL,\n" +
                "  PRIMARY KEY (login),\n" +
                "  UNIQUE INDEX account_UNIQUE (account ASC) VISIBLE,\n" +
                "  INDEX fk_user_role1 (role_id ASC) VISIBLE,\n" +
                "  INDEX fk_user_user_status2 (status_id ASC) VISIBLE,\n" +
                "  CONSTRAINT fk_user_role1\n" +
                "    FOREIGN KEY (role_id)\n" +
                "    REFERENCES role (role_id)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE,\n" +
                "  CONSTRAINT fk_user_user_status2\n" +
                "    FOREIGN KEY (status_id)\n" +
                "    REFERENCES user_status (status_id)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE)\n" +
                "ENGINE = InnoDB\n" +
                "AUTO_INCREMENT = 1\n" +
                "DEFAULT CHARACTER SET = utf8;";
        public static final String CREATE_ACTION_WITH_REQUEST_TABLE =
                //DROP_ACTIONS_WITH_REQUESTS_TABLE +
                "CREATE TABLE IF NOT EXISTS action_with_request (\n" +
                "  action_id INT NOT NULL AUTO_INCREMENT,\n" +
                "  action_description VARCHAR(45) NOT NULL,\n" +
                "  PRIMARY KEY (action_description),\n" +
                "  UNIQUE INDEX action_id_UNIQUE (action_id ASC) VISIBLE)\n" +
                "ENGINE = InnoDB\n" +
                "AUTO_INCREMENT = 1\n" +
                "DEFAULT CHARACTER SET = utf8;";
        public static final String CREATE_CATEGORY_OF_ACTIVITY_TABLE =
                //DROP_CATEGORIES_OF_ACTIVITY_TABLE +
                "CREATE TABLE IF NOT EXISTS category_of_activity (\n" +
                "  category_id INT NOT NULL AUTO_INCREMENT,\n" +
                "  category_name VARCHAR(100) NOT NULL,\n" +
                "  PRIMARY KEY (category_name),\n" +
                "  UNIQUE INDEX category_id_UNIQUE (category_id ASC) VISIBLE)\n" +
                "ENGINE = InnoDB\n" +
                "AUTO_INCREMENT = 1\n" +
                "DEFAULT CHARACTER SET = utf8;";
        public static final String CREATE_ACTIVITY_TABLE =
                //DROP_ACTIVITIES_TABLE +
                "CREATE TABLE IF NOT EXISTS activity (\n" +
                "  activity_id INT NOT NULL AUTO_INCREMENT,\n" +
                "  activity_name VARCHAR(100) NOT NULL,\n" +
                "  category_id INT NOT NULL,\n" +
                "  PRIMARY KEY (activity_name),\n" +
                "  UNIQUE INDEX activity_id_UNIQUE (activity_id ASC) VISIBLE,\n" +
                "  INDEX fk_activity_category_of_activity1 (category_id ASC) VISIBLE,\n" +
                "  CONSTRAINT fk_activity_category_of_activity1\n" +
                "    FOREIGN KEY (category_id)\n" +
                "    REFERENCES category_of_activity (category_id)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE)\n" +
                "ENGINE = InnoDB\n" +
                "AUTO_INCREMENT = 1\n" +
                "DEFAULT CHARACTER SET = utf8;";
        public static final String CREATE_ROLE_TABLE =
                //DROP_ROLES_TABLE +
                "CREATE TABLE IF NOT EXISTS role (\n" +
                "  role_id INT NOT NULL AUTO_INCREMENT,\n" +
                "  role_description VARCHAR(45) NOT NULL,\n" +
                "  PRIMARY KEY (role_description),\n" +
                "  UNIQUE INDEX role_description_UNIQUE (role_description ASC) VISIBLE,\n" +
                "  UNIQUE INDEX role_id_UNIQUE (role_id ASC) VISIBLE)\n" +
                "ENGINE = InnoDB\n" +
                "AUTO_INCREMENT = 1\n" +
                "DEFAULT CHARACTER SET = utf8;";
        public static final String CREATE_USER_STATUS_TABLE =
                //DROP_USER_STATUS_TABLE +
                "CREATE TABLE IF NOT EXISTS user_status (\n" +
                "  status_id INT NOT NULL AUTO_INCREMENT,\n" +
                "  status_name VARCHAR(45) NOT NULL,\n" +
                "  PRIMARY KEY (status_name),\n" +
                "  UNIQUE INDEX status_name_UNIQUE (status_name ASC) VISIBLE,\n" +
                "  UNIQUE INDEX status_id_UNIQUE (status_id ASC) VISIBLE)\n" +
                "ENGINE = InnoDB\n" +
                "AUTO_INCREMENT = 1\n" +
                "DEFAULT CHARACTER SET = utf8;";
        public static final String CREATE_REQUEST_TABLE =
                //DROP_REQUESTS_TABLE +
                "CREATE TABLE IF NOT EXISTS request (\n" +
                "  request_id INT NOT NULL AUTO_INCREMENT,\n" +
                "  account INT NOT NULL,\n" +
                "  activity_id INT NOT NULL,\n" +
                "  action_id INT NOT NULL,\n" +
                "  request_description VARCHAR(256) NULL DEFAULT NULL,\n" +
                "  PRIMARY KEY (request_id),\n" +
                "  INDEX fk_request_user_idx (account ASC) VISIBLE,\n" +
                "  INDEX fk_request_activity1_idx (activity_id ASC) VISIBLE,\n" +
                "  INDEX fk_request_action_with_request2 (action_id ASC) VISIBLE,\n" +
                "  CONSTRAINT fk_request_action_with_request2\n" +
                "    FOREIGN KEY (action_id)\n" +
                "    REFERENCES action_with_request (action_id)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE,\n" +
                "  CONSTRAINT fk_request_activity1\n" +
                "    FOREIGN KEY (activity_id)\n" +
                "    REFERENCES activity (activity_id)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE,\n" +
                "  CONSTRAINT fk_request_user\n" +
                "    FOREIGN KEY (account)\n" +
                "    REFERENCES user (account)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE)\n" +
                "ENGINE = InnoDB\n" +
                "AUTO_INCREMENT = 1\n" +
                "DEFAULT CHARACTER SET = utf8;";
        public static final String CREATE_USER_ACTIVITY_TABLE =
                //DROP_USERS_ACTIVITIES_TABLE  +
                "CREATE TABLE IF NOT EXISTS user_activity (\n" +
                "  account INT NOT NULL,\n" +
                "  activity_id INT NOT NULL,\n" +
                "  time_amount INT NOT NULL,\n" +
                "  INDEX fk_user_activity_user_idx (account ASC) VISIBLE,\n" +
                "  INDEX fk_user_activity_activity1_idx (activity_id ASC) VISIBLE,\n" +
                "  PRIMARY KEY (activity_id, account),\n" +
                "  CONSTRAINT fk_user_activity_activity1`\n" +
                "    FOREIGN KEY (activity_id)\n" +
                "    REFERENCES activity (activity_id)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE,\n" +
                "  CONSTRAINT fk_user_activity_user\n" +
                "    FOREIGN KEY (account)\n" +
                "    REFERENCES user (account)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE)\n" +
                "ENGINE = InnoDB\n" +
                "DEFAULT CHARACTER SET = utf8;";

    }
    public static class InitialData{
        public static final String INITIAL_ROLE_ADMIN = "INSERT INTO role\n" +
                "(role_id,\n" +
                "role_description)\n" +
                "VALUES\n" +
                "(DEFAULT,\n" +
                "'administrator');\n";
        public static final String INITIAL_ROLE_USER = "INSERT INTO role\n" +
                "(role_id,\n" +
                "role_description)\n" +
                "VALUES\n" +
                "(DEFAULT,\n" +
                "'system user');\n";
        public static final String INITIAL_ACTION_ADD = "INSERT INTO action_with_request\n" +
                "(action_description)\n" +
                "VALUE\n" +
                "('addUser');\n";
        public static final String INITIAL_ACTION_REMOVE = "INSERT INTO action_with_request\n" +
                "(action_description)\n" +
                "VALUE\n" +
                "('remove');\n";

    }
    public static class UserQueries{
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
                "VALUES\n" +
                "(DEFAULT, ?, ?, ?, ?, ?,\n" +
                "(SELECT role_id FROM role WHERE role_description = ?),\n" +
                "(SELECT status_id FROM user_status WHERE status_name = ?),\n" +
                "?);";
        public static final String UPDATE_USER = "UPDATE user\n" +
                "SET\n" +
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
        public static final String USER_HAS_ACTIVITY = "INNER JOIN user_activity\n" +
                "ON user.account = user_activity.account\n" +
                "WHERE user_activity.activity_id = \n" +
                "(SELECT activity_id FROM activity WHERE activity_name = ?) \n";
        public static final String USER_HAS_ACTIVITY_AND_NOTIFICATION = USER_HAS_ACTIVITY +
                "AND user.notification = 'on'";
        public static final String FIND_CONNECTED_USERS = SELECT_ALL_USER_FIELDS + USER_HAS_ACTIVITY;
        public static final String FIND_CONNECTED_USERS_WITH_NOTIFICATION = SELECT_ALL_USER_FIELDS + USER_HAS_ACTIVITY_AND_NOTIFICATION;
    }
    public static class ActivityQueries{
        public static final String SELECT_ALL_ACTIVITY_FIELDS = "SELECT activity.activity_id,\n" +
                "    activity.activity_name,\n" +
                "    category_of_activity.category_name\n" +
                "FROM activity\n" +
                "INNER JOIN category_of_activity\n" +
                "ON activity.category_id = category_of_activity.category_id\n";
        public static final String GET_ALL_ACTIVITIES = SELECT_ALL_ACTIVITY_FIELDS +
                "ORDER BY activity_name;";
        public static final String GET_ACTIVITY_BY_ID = SELECT_ALL_ACTIVITY_FIELDS +
                "WHERE activity_id = ?;";
        public static final String GET_ACTIVITY_BY_NAME = SELECT_ALL_ACTIVITY_FIELDS +
                "WHERE activity_name = ?;";
        public static final String GET_ACTIVITIES_BY_CATEGORY = SELECT_ALL_ACTIVITY_FIELDS +
                "WHERE category_name = ? ORDER BY activity_id;";
        public static final String INSERT_ACTIVITY = "INSERT INTO activity\n" +
                "(activity_id,\n" +
                "activity_name,\n" +
                "category_id)\n" +
                "VALUES\n" +
                "(DEFAULT, ?,\n" +
                "(SELECT category_id FROM category_of_activity WHERE category_name = ?));";
        public static final String UPDATE_ACTIVITY = "UPDATE activity\n" +
                "SET\n" +
                "activity_name = ?,\n" +
                "category_id = (SELECT category_id FROM category_of_activity WHERE category_name = ?)\n" +
                "WHERE activity_name = ?;";
        public static final String DELETE_ACTIVITY = "DELETE FROM activity WHERE activity_name = ?;";
    }
    public static class CategoryQueries{
        public static final String GET_ALL_CATEGORIES = "SELECT * FROM category_of_activity;";

        public static final String GET_CATEGORY_BY_ID = "SELECT * FROM category_of_activity\n" +
                "WHERE category_id = ?";
        public static final String GET_CATEGORY_BY_NAME = "SELECT * FROM category_of_activity\n" +
                "WHERE category_name = ?;";
        public static final String UPDATE_CATEGORY = "UPDATE category_of_activity\n" +
                "SET\n" +
                "category_name = ?\n" +
                "WHERE category_name = ?;";
        public static final String ADD_CATEGORY = "INSERT INTO category_of_activity (category_name) VALUE (?);";
        public static final String DELETE_CATEGORY = "DELETE FROM category_of_activity WHERE category_name = ?;";
        public static final String FIND_CATEGORY_BY_NAME = "SELECT * from category_of_activity WHERE category_name = ?";
    }

    public static class RequestQueries{
        public static final String SELECT_ALL_REQUEST_FIELDS = "SELECT request.request_id,\n" +
                "    user.login,\n" +
                "    activity.activity_name,\n" +
                "    action_with_request.action_description,\n" +
                "    request.request_description\n" +
                "FROM request\n" +
                "Inner JOIN user\n" +
                "ON request.account = user.account\n" +
                "INNER JOIN activity\n" +
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
                "WHERE activity_name = ?;";
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
                "VALUES\n" +
                "((SELECT account FROM user WHERE login = ?),\n" +
                "(SELECT activity_id FROM activity WHERE activity_name = ?),\n" +
                "(SELECT action_id FROM action_with_request WHERE action_description = ?),\n" +
                "?);";
        public static final String UPDATE_REQUEST = "UPDATE request\n" +
                "SET\n" +
                "account = (SELECT account FROM user WHERE login = ?),\n" +
                "activity_id = (SELECT activity_id FROM activity WHERE activity_name = ?),\n" +
                "action_id = (SELECT action_id FROM action_with_request WHERE action_description = ?),\n" +
                "request_description = ?\n" +
                "WHERE request_id = ?;";
        public static final String DELETE_REQUEST = "DELETE FROM request WHERE request_id = ?;";
        public static final String ALL_USER_ACTIVITIES = "SELECT * from user_activity WHERE account = ?";
        public static final String IF_USER_HAS_ACTIVITY = "SELECT * from user_activity WHERE account = ? AND activity_id = ?";
        public static final String ADD_ACTIVITY_TO_USER = "INSERT INTO user_activity\n" +
                "(account,\n" +
                "activity_id)\n" +
                "VALUES\n" +
                "(?,\n" +
                "?);";
        public static final String REMOVE_USER_ACTIVITY = "DELETE FROM user_activity\n" +
                "WHERE account = ? AND activity_id = ?;";
        public static final String SET_AMOUNT = "UPDATE user_activity\n" +
                "SET\n" +
                "time_amount = ?\n" +
                "WHERE account = ?\n" +
                "AND activity_id = ?;";
        public static final String GET_AMOUNT = "SELECT time_amount\n" +
                "FROM user_activity\n" +
                "WHERE account = ? " +
                "AND activity_id = ?;";
    }
}
