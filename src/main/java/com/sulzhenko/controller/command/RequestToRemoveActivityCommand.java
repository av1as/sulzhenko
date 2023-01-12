package com.sulzhenko.controller.command;

import com.sulzhenko.model.entity.*;
import com.sulzhenko.model.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

/**
 * Register controller action
 *
 *
 */
public class RequestToRemoveActivityCommand implements Command {
    RequestService requestService = getApplicationContext().getRequestService();
    private static final Logger logger = LogManager.getLogger(RequestToRemoveActivityCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        Request req = new Request.Builder()
                .withLogin(((User) request.getSession().getAttribute("user")).getLogin())
                .withActivityName(request.getParameter("activity_name"))
                .withActionToDo("remove")
                .withDescription("")
                .build();
        try{
            requestService.addRequest(req);
        } catch (ServiceException e){
            logger.warn(e.getMessage());
        }
        return "controller?action=user_activities";
    }
}