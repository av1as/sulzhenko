package com.sulzhenko.controller.command.base;

import com.sulzhenko.controller.command.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.DTO.UserDTO;
import com.sulzhenko.model.services.ServiceException;
import com.sulzhenko.model.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.controller.ApplicationContext.getApplicationContext;

/**
 * Register controller action
 *
 */
public class RegisterCommand implements Command, Constants, Path {
    UserService userService = getApplicationContext().getUserService();
    private static final Logger logger = LogManager.getLogger(RegisterCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        HttpSession session = request.getSession();

        String forward = PAGE_ERROR_FULL;
        try{
            userService.addUser(getUserDTO(request), request.getParameter(PASSWORD2));
            session.setAttribute(USER, getUserDTO(request));
            forward = PAGE_HELLO_USER;
        } catch(ServiceException e) {
            logger.warn(e.getMessage());
            session.setAttribute(ERROR, e.getMessage());
        }
        return forward;
    }
    private UserDTO getUserDTO(HttpServletRequest request){
        return new UserDTO.Builder().withLogin(request.getParameter(LOGIN))
                .withPassword(request.getParameter(PASSWORD1))
                .withEmail(request.getParameter(EMAIL))
                .withFirstName(request.getParameter(FIRST_NAME))
                .withLastName(request.getParameter(LAST_NAME))
                .withRole(SYSTEM_USER)
                .withStatus(INACTIVE)
                .withNotification(request.getParameter(NOTIFICATIONS) == null?
                        OFF: request.getParameter(NOTIFICATIONS))
                .build();
    }
}