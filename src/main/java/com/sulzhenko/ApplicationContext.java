package com.sulzhenko;

import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.DAO.implementation.*;
import com.sulzhenko.model.connectionPool.MyDataSource;
import com.sulzhenko.model.hashingPasswords.Sha;
import com.sulzhenko.model.services.*;
import com.sulzhenko.model.services.implementation.*;
import jakarta.servlet.ServletContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationContext {
//    private final UserDAO userDAO;
//    private final ActivityDAO activityDAO;
//    private final RequestDAO requestDAO;
//    private final UserActivityDAO userActivityDAO;
    private final UserService userService;
    CategoryService categoryService;
    private final ActivityService activityService;
    private final ReportService reportService;
    private final UserActivityService userActivityService;
    private final RequestService requestService;
    private final Sha sha;
    private ApplicationContext(ServletContext servletContext, String propertiesFile) {
        Properties properties = getProperties(propertiesFile);
        DataSource dataSource = MyDataSource.getDataSource(properties);
//        userDAO = new UserDAOImpl(dataSource);
//        activityDAO = new ActivityDAOImpl(dataSource);
//        requestDAO = new RequestDAOImpl(dataSource);
//        userActivityDAO = new UserActivityDAOImpl(dataSource);
        userService = new UserServiceImpl(dataSource);
        categoryService = new CategoryServiceImpl(dataSource);
        activityService = new ActivityServiceImpl(dataSource);
        reportService = new ReportServiceImpl(dataSource);
        userActivityService = new UserActivityServiceImpl(dataSource);
        requestService = new RequestServiceImpl(dataSource);
        sha = new Sha();
    }
    private static final Logger logger = LogManager.getLogger(ApplicationContext.class);
    private static ApplicationContext applicationContext;

//    public UserDAO getUserDAO() {
//        return userDAO;
//    }


    public ActivityService getActivityService() {
        return activityService;
    }

    public ReportService getReportService() {
        return reportService;
    }

    public UserActivityService getUserActivityService() {
        return userActivityService;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public UserService getUserService() {
        return userService;
    }

    public static void createAppContext(ServletContext servletContext, String propertiesFile) {
        applicationContext = new ApplicationContext(servletContext, propertiesFile);
    }
    private static Properties getProperties(String propertiesFile) {
        Properties properties = new Properties();
        try (InputStream resource = ApplicationContext.class.getClassLoader().getResourceAsStream(propertiesFile)){
            properties.load(resource);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return properties;
    }
    public Sha getSha() {
        return sha;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public RequestService getRequestService() {
        return requestService;
    }
}
