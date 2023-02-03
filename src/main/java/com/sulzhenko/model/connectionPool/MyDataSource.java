package com.sulzhenko.model.connectionPool;

import static com.sulzhenko.model.connectionPool.ConnectionConstants.*;
import com.zaxxer.hikari.*;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Class to configure and get HikariDataSource. Use it to connect to database
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */

public class MyDataSource {
    private static DataSource dataSource;
    /**
     * Configures and gets HikariDataSource.
     * @param properties - all required info to configure datasource
     * @return singleton instance of HikariDataSource
     */
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