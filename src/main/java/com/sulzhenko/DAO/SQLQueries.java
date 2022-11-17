package com.sulzhenko.DAO;

/**
 * This class contains constants of SQL queries
 */
public abstract class SQLQueries {

    public static class DropingTablesQueries{
        public static final String DROP_USERS_TABLE = "DROP TABLE IF EXISTS `timekeeping`.`users` ;\n";
        public static final String DROP_ACTIONS_WITH_REQUESTS_TABLE = "DROP TABLE IF EXISTS `timekeeping`.`actions_with_requests` ;\n";
        public static final String DROP_CATEGORIES_OF_ACTIVITY_TABLE = "DROP TABLE IF EXISTS `timekeeping`.`categories_of_activity` ;\n";
        public static final String DROP_ACTIVITIES_TABLE = "DROP TABLE IF EXISTS `timekeeping`.`activities` ;\n";
        public static final String DROP_ROLES_TABLE = "DROP TABLE IF EXISTS `timekeeping`.`roles` ;\n";
        public static final String DROP_USER_STATUS_TABLE = "DROP TABLE IF EXISTS `timekeeping`.`user_status` ;\n";
        public static final String DROP_REQUESTS_TABLE = "DROP TABLE IF EXISTS `timekeeping`.`requests` ;\n";
        public static final String DROP_USERS_ACTIVITIES_TABLE = "DROP TABLE IF EXISTS `timekeeping`.`users_activities` ;\n";


    }
    public static class CreatingTablesQueries{
        public static final String CREATE_USERS_TABLE =
                //DROP_USERS_TABLE +
                "CREATE TABLE IF NOT EXISTS `timekeeping`.`users` (\n" +
                "  `account` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `login` VARCHAR(45) NOT NULL,\n" +
                "  `email` VARCHAR(45) NOT NULL,\n" +
                "  `password` VARCHAR(250) NOT NULL,\n" +
                "  `first_name` VARCHAR(45) NULL DEFAULT NULL,\n" +
                "  `last_name` VARCHAR(45) NULL DEFAULT NULL,\n" +
                "  `role_id` INT NOT NULL,\n" +
                "  `status_id` INT NOT NULL,\n" +
                "  `notifications` VARCHAR(45) NOT NULL,\n" +
                "  PRIMARY KEY (`login`),\n" +
                "  UNIQUE INDEX `account_UNIQUE` (`account` ASC) VISIBLE,\n" +
                "  INDEX `fk_users_roles1` (`role_id` ASC) VISIBLE,\n" +
                "  INDEX `fk_users_user_status2` (`status_id` ASC) VISIBLE,\n" +
                "  CONSTRAINT `fk_users_roles1`\n" +
                "    FOREIGN KEY (`role_id`)\n" +
                "    REFERENCES `timekeeping`.`roles` (`role_id`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE,\n" +
                "  CONSTRAINT `fk_users_user_status2`\n" +
                "    FOREIGN KEY (`status_id`)\n" +
                "    REFERENCES `timekeeping`.`user_status` (`status_id`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE)\n" +
                "ENGINE = InnoDB\n" +
                "AUTO_INCREMENT = 1\n" +
                "DEFAULT CHARACTER SET = utf8;";
        public static final String CREATE_ACTIONS_WITH_REQUESTS_TABLE =
                //DROP_ACTIONS_WITH_REQUESTS_TABLE +
                "CREATE TABLE IF NOT EXISTS `timekeeping`.`actions_with_requests` (\n" +
                "  `action_id` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `action_description` VARCHAR(45) NOT NULL,\n" +
                "  PRIMARY KEY (`action_description`),\n" +
                "  UNIQUE INDEX `action_id_UNIQUE` (`action_id` ASC) VISIBLE)\n" +
                "ENGINE = InnoDB\n" +
                "AUTO_INCREMENT = 1\n" +
                "DEFAULT CHARACTER SET = utf8;";
        public static final String CREATE_CATEGORIES_OF_ACTIVITY_TABLE =
                //DROP_CATEGORIES_OF_ACTIVITY_TABLE +
                "CREATE TABLE IF NOT EXISTS `timekeeping`.`categories_of_activity` (\n" +
                "  `category_id` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `category_name` VARCHAR(100) NOT NULL,\n" +
                "  PRIMARY KEY (`category_name`),\n" +
                "  UNIQUE INDEX `category_id_UNIQUE` (`category_id` ASC) VISIBLE)\n" +
                "ENGINE = InnoDB\n" +
                "AUTO_INCREMENT = 1\n" +
                "DEFAULT CHARACTER SET = utf8;";
        public static final String CREATE_ACTIVITIES_TABLE =
                //DROP_ACTIVITIES_TABLE +
                "CREATE TABLE IF NOT EXISTS `timekeeping`.`activities` (\n" +
                "  `activity_id` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `activity_name` VARCHAR(100) NOT NULL,\n" +
                "  `category_id` INT NOT NULL,\n" +
                "  PRIMARY KEY (`activity_name`),\n" +
                "  UNIQUE INDEX `activity_id_UNIQUE` (`activity_id` ASC) VISIBLE,\n" +
                "  INDEX `fk_activities_categories_of_activity1` (`category_id` ASC) VISIBLE,\n" +
                "  CONSTRAINT `fk_activities_categories_of_activity1`\n" +
                "    FOREIGN KEY (`category_id`)\n" +
                "    REFERENCES `timekeeping`.`categories_of_activity` (`category_id`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE)\n" +
                "ENGINE = InnoDB\n" +
                "AUTO_INCREMENT = 1\n" +
                "DEFAULT CHARACTER SET = utf8;";
        public static final String CREATE_ROLES_TABLE =
                //DROP_ROLES_TABLE +
                "CREATE TABLE IF NOT EXISTS `timekeeping`.`roles` (\n" +
                "  `role_id` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `role_description` VARCHAR(45) NOT NULL,\n" +
                "  PRIMARY KEY (`role_description`),\n" +
                "  UNIQUE INDEX `role_description_UNIQUE` (`role_description` ASC) VISIBLE,\n" +
                "  UNIQUE INDEX `role_id_UNIQUE` (`role_id` ASC) VISIBLE)\n" +
                "ENGINE = InnoDB\n" +
                "AUTO_INCREMENT = 1\n" +
                "DEFAULT CHARACTER SET = utf8;";
        public static final String CREATE_USER_STATUS_TABLE =
                //DROP_USER_STATUS_TABLE +
                "CREATE TABLE IF NOT EXISTS `timekeeping`.`user_status` (\n" +
                "  `status_id` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `status_name` VARCHAR(45) NOT NULL,\n" +
                "  PRIMARY KEY (`status_name`),\n" +
                "  UNIQUE INDEX `status_name_UNIQUE` (`status_name` ASC) VISIBLE,\n" +
                "  UNIQUE INDEX `status_id_UNIQUE` (`status_id` ASC) VISIBLE)\n" +
                "ENGINE = InnoDB\n" +
                "AUTO_INCREMENT = 1\n" +
                "DEFAULT CHARACTER SET = utf8;";
        public static final String CREATE_REQUESTS_TABLE =
                //DROP_REQUESTS_TABLE +
                "CREATE TABLE IF NOT EXISTS `timekeeping`.`requests` (\n" +
                "  `request_id` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `account` INT NOT NULL,\n" +
                "  `activity_id` INT NOT NULL,\n" +
                "  `action_id` INT NOT NULL,\n" +
                "  `request_description` VARCHAR(256) NULL DEFAULT NULL,\n" +
                "  PRIMARY KEY (`request_id`),\n" +
                "  INDEX `fk_requests_users_idx` (`account` ASC) VISIBLE,\n" +
                "  INDEX `fk_requests_activities1_idx` (`activity_id` ASC) VISIBLE,\n" +
                "  INDEX `fk_requests_actions_with_requests2` (`action_id` ASC) VISIBLE,\n" +
                "  CONSTRAINT `fk_requests_actions_with_requests2`\n" +
                "    FOREIGN KEY (`action_id`)\n" +
                "    REFERENCES `timekeeping`.`actions_with_requests` (`action_id`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE,\n" +
                "  CONSTRAINT `fk_requests_activities1`\n" +
                "    FOREIGN KEY (`activity_id`)\n" +
                "    REFERENCES `timekeeping`.`activities` (`activity_id`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE,\n" +
                "  CONSTRAINT `fk_requests_users`\n" +
                "    FOREIGN KEY (`account`)\n" +
                "    REFERENCES `timekeeping`.`users` (`account`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE)\n" +
                "ENGINE = InnoDB\n" +
                "AUTO_INCREMENT = 1\n" +
                "DEFAULT CHARACTER SET = utf8;";
        public static final String CREATE_USERS_ACTIVITIES_TABLE =
                //DROP_USERS_ACTIVITIES_TABLE  +
                "CREATE TABLE IF NOT EXISTS `timekeeping`.`users_activities` (\n" +
                "  `account` INT NOT NULL,\n" +
                "  `activity_id` INT NOT NULL,\n" +
                "  `time_amount` INT NOT NULL,\n" +
                "  INDEX `fk_users_activities_users_idx` (`account` ASC) VISIBLE,\n" +
                "  INDEX `fk_users_activities_activities1_idx` (`activity_id` ASC) VISIBLE,\n" +
                "  PRIMARY KEY (`activity_id`, `account`),\n" +
                "  CONSTRAINT `fk_users_activities_activities1`\n" +
                "    FOREIGN KEY (`activity_id`)\n" +
                "    REFERENCES `timekeeping`.`activities` (`activity_id`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE,\n" +
                "  CONSTRAINT `fk_users_activities_users`\n" +
                "    FOREIGN KEY (`account`)\n" +
                "    REFERENCES `timekeeping`.`users` (`account`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE)\n" +
                "ENGINE = InnoDB\n" +
                "DEFAULT CHARACTER SET = utf8;";

    }
    public static class InitialData{
        public static final String INITIAL_ROLE_ADMIN = "INSERT INTO `timekeeping`.`roles`\n" +
                "(`role_id`,\n" +
                "`role_description`)\n" +
                "VALUES\n" +
                "(DEFAULT,\n" +
                "'administrator');\n";
        public static final String INITIAL_ROLE_USER = "INSERT INTO `timekeeping`.`roles`\n" +
                "(`role_id`,\n" +
                "`role_description`)\n" +
                "VALUES\n" +
                "(DEFAULT,\n" +
                "'system user');\n";

    }
    public static class UserQueries{
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
        public static final String DELETE_USER = "DELETE FROM users WHERE login = ?;";
        //    public static final String ADD_ROLE = "INSERT INTO roles (role_description) VALUE (?);";
        public static final String ADD_STATUS = "INSERT INTO user_status (status_name) VALUE (?);";
        public static final String DELETE_STATUS = "DELETE FROM user_status WHERE status_name = ?;";
    }
    public static class ActivityQueries{
        public static final String SELECT_ALL_ACTIVITY_FIELDS = "SELECT activities.activity_id,\n" +
                "    activities.activity_name,\n" +
                "    categories_of_activity.category_name\n" +
                "FROM timekeeping.activities\n" +
                "INNER JOIN timekeeping.categories_of_activity\n" +
                "ON activities.category_id = categories_of_activity.category_id\n";
        public static final String GET_ALL_ACTIVITIES = SELECT_ALL_ACTIVITY_FIELDS +
                "ORDER BY activity_id;";
        public static final String GET_ACTIVITY_BY_ID = SELECT_ALL_ACTIVITY_FIELDS +
                "WHERE activity_id = ?;";
        public static final String GET_ACTIVITY_BY_NAME = SELECT_ALL_ACTIVITY_FIELDS +
                "WHERE activity_name = ?;";
        public static final String GET_ACTIVITIES_BY_CATEGORY = SELECT_ALL_ACTIVITY_FIELDS +
                "WHERE category_name = ? ORDER BY activity_id;";
        public static final String INSERT_ACTIVITY = "INSERT INTO activities\n" +
                "(`activity_id`,\n" +
                "`activity_name`,\n" +
                "`category_id`)\n" +
                "VALUES\n" +
                "(DEFAULT, ?,\n" +
                "(SELECT category_id FROM categories_of_activity WHERE category_name = ?));";
        public static final String UPDATE_ACTIVITY = "UPDATE activities\n" +
                "SET\n" +
                "activity_name = ?,\n" +
                "category_id = (SELECT category_id FROM categories_of_activity WHERE category_name = ?)\n" +
                "WHERE activity_name = ?;";
        public static final String DELETE_ACTIVITY = "DELETE FROM activities WHERE activity_name = ?;";
        public static final String ADD_CATEGORY = "INSERT INTO categories_of_activity (category_name) VALUE (?);";
        public static final String DELETE_CATEGORY = "DELETE FROM categories_of_activity WHERE category_name = ?;";
    }

    public static class RequestQueries{
        public static final String SELECT_ALL_REQUEST_FIELDS = "SELECT requests.request_id,\n" +
                "    users.login,\n" +
                "    activities.activity_name,\n" +
                "    actions_with_requests.action_description,\n" +
                "    requests.request_description\n" +
                "FROM requests\n" +
                "Inner JOIN users\n" +
                "ON requests.account = users.account\n" +
                "INNER JOIN activities\n" +
                "ON requests.activity_id = activities.activity_id\n" +
                "Inner JOIN actions_with_requests\n" +
                "ON requests.action_id = actions_with_requests.action_id\n";
        //    public static final String GET_ALL_REQUESTS = SELECT_ALL_REQUEST_FIELDS +
//            "ORDER BY login;";
        public static final String GET_REQUEST_BY_ID = SELECT_ALL_REQUEST_FIELDS +
                "WHERE request_id = ?;";
        public static final String GET_REQUESTS_BY_LOGIN = SELECT_ALL_REQUEST_FIELDS +
                "WHERE login = ?;";
        public static final String GET_REQUESTS_BY_ACTIVITY = SELECT_ALL_REQUEST_FIELDS +
                "WHERE activity_name = ?;";
        public static final String GET_REQUESTS_BY_ACTION = SELECT_ALL_REQUEST_FIELDS +
                "WHERE action_description = ?;";
        public static final String INSERT_REQUEST = "INSERT INTO requests\n" +
                "(account,\n" +
                "activity_id,\n" +
                "action_id,\n" +
                "request_description)\n" +
                "VALUES\n" +
                "((SELECT account FROM users WHERE login = ?),\n" +
                "(SELECT activity_id FROM activities WHERE activity_name = ?),\n" +
                "(SELECT action_id FROM actions_with_requests WHERE action_description = ?),\n" +
                "?);";
        public static final String UPDATE_REQUEST = "UPDATE requests\n" +
                "SET\n" +
                "account = (SELECT account FROM users WHERE login = ?),\n" +
                "activity_id = (SELECT activity_id FROM activities WHERE activity_name = ?),\n" +
                "action_id = (SELECT action_id FROM actions_with_requests WHERE action_description = ?),\n" +
                "request_description = ?\n" +
                "WHERE request_id = ?;";
        public static final String DELETE_REQUEST = "DELETE FROM requests WHERE request_id = ?;";
    }
}
