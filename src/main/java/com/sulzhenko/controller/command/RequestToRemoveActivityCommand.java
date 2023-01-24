package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.DTO.RequestDTO;
import com.sulzhenko.DTO.UserDTO;
import com.sulzhenko.model.services.RequestService;
import com.sulzhenko.model.services.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.controller.ApplicationContext.getApplicationContext;

/**
 * Register controller action
 *
 */
public class RequestToRemoveActivityCommand implements Command, Constants, Path {
    RequestService requestService = getApplicationContext().getRequestService();
    private static final Logger logger = LogManager.getLogger(RequestToRemoveActivityCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        RequestDTO req = new RequestDTO.Builder()
                .withLogin(((UserDTO) request.getSession().getAttribute(USER)).getLogin())
                .withActivityName(request.getParameter(ACTIVITY_NAME))
                .withActionToDo(REMOVE)
                .withDescription(EMPTY)
                .build();
        try{
            requestService.addRequest(req);
        } catch (ServiceException e){
            logger.warn(e.getMessage());
        }
        return PAGE_CONTROLLER_USER_ACTIVITIES;
    }
}