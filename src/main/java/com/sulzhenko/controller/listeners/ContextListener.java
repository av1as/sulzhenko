package com.sulzhenko.controller.listeners;

import com.sulzhenko.controller.context.ApplicationContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ContextListener  class.
 *
 *  @author Artem Sulzhenko
 *  @version 1.0
 */
public class ContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ContextListener.class);

    /** Name of properties file to configure DataSource and Mailer */
    private static final String PROPERTIES_FILE = "datasource.properties";

    /**
     * creates ApplicationContext and passes ServletContext and properties to initialize all required classes
     * @param sce passed by application
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ApplicationContext.createAppContext(sce.getServletContext(), PROPERTIES_FILE);
        logger.info("ApplicationContext is set");
    }
}
