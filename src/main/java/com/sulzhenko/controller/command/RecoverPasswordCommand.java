package com.sulzhenko.controller.command;

import com.sulzhenko.DTO.UserDTO;
import com.sulzhenko.controller.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.model.services.ServiceException;
import com.sulzhenko.model.services.UserActivityService;
import com.sulzhenko.model.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.controller.ApplicationContext.getApplicationContext;

public class RecoverPasswordCommand implements Command, Constants, Path {
    UserService userService = getApplicationContext().getUserService();
    private static final Logger logger = LogManager.getLogger(RecoverPasswordCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        HttpSession session = request.getSession();
        String login = request.getParameter(LOGIN);
        String forward;
        try{
            userService.recoverPassword(login);
            forward = PAGE_CHECK_EMAIL;
        } catch(ServiceException e) {
            logger.warn(e.getMessage());
            session.setAttribute(ERROR, e.getMessage());
            forward = PAGE_ERROR_FULL;
        }
        return forward;
    }
}
