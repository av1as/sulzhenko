package controller.listener;

import com.sulzhenko.controller.context.ApplicationContext;
import com.sulzhenko.controller.listeners.ContextListener;
import jakarta.servlet.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContextListenerTest {
    private static final ServletContextEvent sce = mock(ServletContextEvent.class);
    private static final ServletContext servletContext = mock(ServletContext.class);

    @Test
    void testContextInitialized() {
        when(sce.getServletContext()).thenReturn(servletContext);
        new ContextListener().contextInitialized(sce);
        assertNotNull(ApplicationContext.getApplicationContext());
    }
}