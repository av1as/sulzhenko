package com.sulzhenko.controller.command.admin;

import com.sulzhenko.controller.command.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.DTO.UserDTO;
import com.sulzhenko.model.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.sulzhenko.controller.context.ApplicationContext.getApplicationContext;

/**
 * Update user by admin controller action
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class AdminUpdateUserCommand implements Command, Constants, Path {
    UserService userService = getApplicationContext().getUserService();
    private static final Logger logger = LogManager.getLogger(AdminUpdateUserCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        HttpSession session = request.getSession();
        String forward = PAGE_ERROR_FULL;
        try{
            UserDTO userDTO = userService.getUserDTO(request.getParameter(OLD_LOGIN));
            String[] param = getUpdateParameters(request, userDTO);
            userService.adminUpdateUser(userDTO, param);
            forward = PAGE_SHOW_USERS;
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            session.setAttribute(ERROR, e.getMessage());
        }
        return forward;
    }

    private static String[] getUpdateParameters(HttpServletRequest request, UserDTO userDTO) {
        String notifications = request.getParameter(NEW_NOTIFICATIONS);
        if (notifications == null) notifications = OFF;
        assert userDTO != null;
        return new String[]{request.getParameter(OLD_LOGIN), request.getParameter(NEW_EMAIL),
                null, request.getParameter(NEW_FIRST_NAME),
                request.getParameter(NEW_LAST_NAME), SYSTEM_USER,
                request.getParameter(NEW_STATUS), notifications};
    }
}