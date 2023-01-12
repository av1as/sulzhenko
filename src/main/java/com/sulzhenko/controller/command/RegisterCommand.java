package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.entity.User;
import com.sulzhenko.model.services.ServiceException;
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
    public static final String ERROR = "error";
    UserService userService = getApplicationContext().getUserService();
    private static final Logger logger = LogManager.getLogger(RegisterCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        HttpSession session = request.getSession();

        String forward = Path.PAGE_ERROR;
        String errorMessage = getErrorMessage(request);
        if (errorMessage != null){
            session.setAttribute(ERROR, errorMessage);
            return forward;
        }
        String validationResult = validateUser(getUser(request));
        if(!Objects.equals(validationResult, "")){
            session.setAttribute(ERROR, validationResult);
            return forward;
        }
        try{
            userService.addUser(getUser(request));
            forward = Path.HELLO_USER;
        } catch(ServiceException e) {
            logger.warn(e.getMessage());
            session.setAttribute(ERROR, e.getMessage());
        }
        return forward;
    }

    private String getErrorMessage(HttpServletRequest request) {
        User user = getUser(request);
        if (user.getLogin() == null || user.getLogin().isEmpty() ) {
            return "empty.login";
        } else if (!userService.isLoginAvailable(user.getLogin())) {
            return "duplicate.login";
        } else if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return "empty.password";
        } else if (!Objects.equals(user.getPassword(), request.getParameter("password2"))) {
            return "different.passwords";
        } else if(user.getEmail()== null || user.getEmail().isEmpty()) {
            return "empty.email";
        } else return null;
    }

    private User getUser(HttpServletRequest request){
        return new User.Builder().withLogin(request.getParameter("login"))
                .withPassword(request.getParameter("password1"))
                .withEmail(request.getParameter("email"))
                .withFirstName(request.getParameter("firstname"))
                .withLastName(request.getParameter("lastname"))
                .withRole("system user")
                .withStatus("inactive")
                .withNotification(request.getParameter("notifications") == null?
                        "off": request.getParameter("notifications"))
                .build();
    }
}