package controller;

import com.sulzhenko.controller.ApplicationContext;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ApplicationContextTest {
    private static final String PROPERTIES_FILE = "datasourse.properties";
    private final ServletContext SERVLET_CONTEXT = mock(ServletContext.class);

    @Test
    void testRightFile() {
        assertDoesNotThrow(() -> ApplicationContext.createAppContext(SERVLET_CONTEXT, PROPERTIES_FILE));
        ApplicationContext appContext = ApplicationContext.getApplicationContext();
        assertNotNull(appContext.getUserService());
        assertNotNull(appContext.getReportService());
        assertNotNull(appContext.getCategoryService());
        assertNotNull(appContext.getRequestService());
        assertNotNull(appContext.getActivityService());
        assertNotNull(appContext.getUserActivityService());
        assertNotNull(appContext.getUserService());
    }

    @Test
    void testWrongFile() {
        assertDoesNotThrow(() -> ApplicationContext.createAppContext(SERVLET_CONTEXT, "wrong"));
    }
}