package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;

import com.sulzhenko.model.DTO.UserDTO;
import com.sulzhenko.model.services.ServiceException;
import com.sulzhenko.model.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

/**
 * Update user controller action
 *
 */
public class UpdateUserCommand implements Command, Constants, Path {
    UserService userService = getApplicationContext().getUserService();
    private static final Logger logger = LogManager.getLogger(UpdateUserCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute(USER);
        String forward = PAGE_ERROR_FULL;
//        String forward = "/TimeKeeping/jsp/test.jsp";
//        session.setAttribute("currentpassword", request.getParameter(CURRENT_PASSWORD));
//        session.setAttribute("newpassword", request.getParameter(NEW_PASSWORD));
//        session.setAttribute("newpasswordconfirm", request.getParameter(NEW_PASSWORD_CONFIRM));



        String errorMessage = userService.getErrorMessageUpdate(userDTO.getLogin(),
                request.getParameter(CURRENT_PASSWORD), request.getParameter(NEW_PASSWORD),
                request.getParameter(NEW_PASSWORD_CONFIRM));
        if(errorMessage == null){
            String[] param = getParam(request, userDTO);
            try{
                userService.updateUser(userDTO, param);
                session.setAttribute(USER, userService.getUserDTO(userDTO.getLogin()));
                forward = PAGE_PROFILE;
            } catch (ServiceException e) {
                logger.fatal(e.getMessage());
                session.setAttribute(ERROR, e.getMessage());
            }
        } else{
            session.setAttribute(ERROR, errorMessage);
        }
        return forward;
    }

    private static String[] getParam(HttpServletRequest request, UserDTO userDTO) {
        String notifications = request.getParameter(NEW_NOTIFICATIONS);
        if (notifications == null) notifications = OFF;
        return new String[]{userDTO.getLogin(), request.getParameter(NEW_EMAIL), request.getParameter(NEW_PASSWORD), request.getParameter(NEW_FIRST_NAME),
                request.getParameter(NEW_LAST_NAME), userDTO.getRole().value, ACTIVE, notifications};
    }
}
