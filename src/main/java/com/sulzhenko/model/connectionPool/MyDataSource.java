package com.sulzhenko.model.connectionPool;

import static com.sulzhenko.model.connectionPool.ConnectionConstants.*;
import com.zaxxer.hikari.*;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * This class provides ready for use Hikari pool connection.
 */

public class MyDataSource {
    private static DataSource dataSource;
    public static synchronized DataSource getDataSource(Properties properties) {
        if (dataSource == null) {
            HikariConfig config = getHikariConfig(properties);
            dataSource = new HikariDataSource(config);
        }
        return dataSource;
    }
    private static HikariConfig getHikariConfig(Properties properties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getProperty(URL));
        config.setUsername(properties.getProperty(USER));
        config.setPassword(properties.getProperty(PASSWORD));
        config.setDriverClassName(properties.getProperty(DRIVER));
        config.setDataSourceProperties(properties);
        return config;
    }

    private MyDataSource() {}
}