package com.sulzhenko.controller.context;

import com.sulzhenko.Util.PdfMakerUtil;
import com.sulzhenko.model.connectionPool.MyDataSource;
import com.sulzhenko.model.services.*;
import com.sulzhenko.model.services.implementation.*;
import jakarta.servlet.ServletContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;

/**
 * ApplicationContext  class. Contains all objects required for correct application work
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */

public class ApplicationContext {
    private final UserService userService;
    private final CategoryService categoryService;
    private final ActivityService activityService;
    private final ReportService reportService;
    private final UserActivityService userActivityService;
    private final RequestService requestService;
    private final PdfMakerUtil pdfUtil;
    private ApplicationContext(ServletContext servletContext, String propertiesFile) {
        pdfUtil = new PdfMakerUtil(servletContext);
        Properties properties = getProperties(propertiesFile);
        DataSource dataSource = MyDataSource.getDataSource(properties);
        userService = new UserServiceImpl(dataSource);
        categoryService = new CategoryServiceImpl(dataSource);
        activityService = new ActivityServiceImpl(dataSource);
        reportService = new ReportServiceImpl(dataSource);
        userActivityService = new UserActivityServiceImpl(dataSource);
        requestService = new RequestServiceImpl(dataSource);
    }
    private static final Logger logger = LogManager.getLogger(ApplicationContext.class);
    private static ApplicationContext applicationContext;

    public ActivityService getActivityService() {
        return activityService;
    }

    public ReportService getReportService() {
        return reportService;
    }

    public UserActivityService getUserActivityService() {
        return userActivityService;
    }

    /**
     * @return instance of ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public UserService getUserService() {
        return userService;
    }

    /**
     * Creates instance of ApplicationContext to use in Commands. Configure all required classes. Loads properties
     * @param servletContext - to use relative path in classes
     * @param propertiesFile - to configure DataSource and Mailer
     */
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
    public CategoryService getCategoryService() {
        return categoryService;
    }

    public RequestService getRequestService() {
        return requestService;
    }

    public PdfMakerUtil getPdfUtil() {
        return pdfUtil;
    }
}
