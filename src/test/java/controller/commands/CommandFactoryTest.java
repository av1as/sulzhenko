package controller.commands;

import com.sulzhenko.controller.ApplicationContext;
import com.sulzhenko.controller.Command;
import com.sulzhenko.controller.CommandFactory;
import com.sulzhenko.controller.command.DefaultCommand;
import com.sulzhenko.controller.command.LoginCommand;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.Test;
import static com.sulzhenko.controller.CommandFactory.getCommandFactory;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CommandFactoryTest {
    private static final String PROPERTIES_FILE = "datasourse.properties";
    private final ServletContext servletContext = mock(ServletContext.class);

    @Test
    void testCreateAction() {
        ApplicationContext.createAppContext(servletContext, PROPERTIES_FILE);
        CommandFactory commandFactory = getCommandFactory();
        Command command = commandFactory.createCommand("login");
        assertInstanceOf(LoginCommand.class, command);
    }

    @Test
    void testDefaultAction() {
        ApplicationContext.createAppContext(servletContext, PROPERTIES_FILE);
        CommandFactory commandFactory = getCommandFactory();
        Command command = commandFactory.createCommand("wrong name");
        assertInstanceOf(DefaultCommand.class, command);
    }
}