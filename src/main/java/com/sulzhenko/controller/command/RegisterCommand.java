package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.entity.User;
import com.sulzhenko.model.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Objects;

import static com.sulzhenko.ApplicationContext.getApplicationContext;
import static com.sulzhenko.model.services.validator.InputValidator.validateUser;

/**
 * Register controller action
 *
 *
 */
public class RegisterCommand implements Command {
    UserService userService = getApplicationContext().getUserService();
    private static final Logger logger = LogManager.getLogger(RegisterCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        HttpSession session = request.getSession();
        User user = getUser(request);
        String forward = Path.PAGE_ERROR;
        String errorMessage = getError(request, session, user, forward);
        if (errorMessage != null){
            session.setAttribute("error", errorMessage);
            return forward;
        }
        String validationResult = validateUser(user);
        if(!Objects.equals(validationResult, "")){
            session.setAttribute("error", validationResult);
            return forward;
        }
        try{
            userService.addUser(user);
            forward = Path.HELLO_USER;
        } catch(DAOException e) {
            logger.warn(e.getMessage());
            session.setAttribute("error", e.getMessage());
        }
        return forward;
    }

    private String getError(HttpServletRequest request, HttpSession session, User user, String forward) {
        String errorMessage;
        if (user.getLogin() == null || user.getLogin().isEmpty() ) {
            errorMessage = "empty.login";
            session.setAttribute("error", errorMessage);
            return forward;
        } else if (!userService.isLoginAvailable(user.getLogin())) {
            errorMessage = "duplicate.login";
            session.setAttribute("error", errorMessage);
            return forward;
        } else if (user.getPassword() == null || user.getPassword().isEmpty()) {
            errorMessage = "empty.password";
            session.setAttribute("error", errorMessage);
            return forward;
        } else if (!Objects.equals(user.getPassword(), request.getParameter("password2"))) {
            errorMessage = "different.passwords";
            session.setAttribute("error", errorMessage);
            return forward;
        } else if(user.getEmail()== null || user.getEmail().isEmpty()) {
            errorMessage = "empty.email";
            session.setAttribute("error", errorMessage);
            return forward;
        }
        return null;
    }

    private User getUser(HttpServletRequest request){
        String login = request.getParameter("login");
        String password = request.getParameter("password1");
        String email = request.getParameter("email");
        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        String notifications = request.getParameter("notifications");
        if (notifications == null) notifications = "off";
        return new User.Builder().withLogin(login)
                .withPassword(password)
                .withEmail(email)
                .withFirstName(firstName)
                .withLastName(lastName)
                .withRole("system user")
                .withStatus("inactive")
                .withNotification(notifications)
                .build();
    }
}