package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.entity.*;
import com.sulzhenko.model.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

/**
 * Request to add User activity controller action
 *
 */
public class RequestToAddActivityCommand implements Command {
    RequestService requestService = getApplicationContext().getRequestService();
    private static final Logger logger = LogManager.getLogger(RequestToAddActivityCommand.class);
    private static final String NEW_ACTIVITY = "new_activity";
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(request.getParameter(NEW_ACTIVITY) == null || request.getParameter(NEW_ACTIVITY).isEmpty()){
            logger.warn("wrong activity");
            session.setAttribute("error", "wrong.activity");
            return Path.PAGE_ERROR;
        }
        Request t = new Request.Builder()
                    .withLogin(user.getLogin())
                    .withActivityName(request.getParameter(NEW_ACTIVITY))
                    .withActionToDo("add")
                    .build();
        try{
            requestService.addRequest(t);
        } catch (ServiceException e){
            logger.warn(e.getMessage());
            session.setAttribute("error", e.getMessage());
        }
        return "controller?action=user_activities";
    }
}
