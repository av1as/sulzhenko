package com.sulzhenko.listeners;

import com.sulzhenko.ApplicationContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ContextListener  class.
 *
 */
public class ContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ContextListener.class);

    /** Name of properties file to configure DataSource, EmailSender and Captcha */
    private static final String PROPERTIES_FILE = "datasourse.properties";

    /**
     * creates AppContext and passes ServletContext and properties to initialize all required classes
     * @param sce passed by application
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ApplicationContext.createAppContext(sce.getServletContext(), PROPERTIES_FILE);
        logger.info("ApplicationContext is set");
    }
}
