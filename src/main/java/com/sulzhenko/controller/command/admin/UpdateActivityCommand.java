package com.sulzhenko.controller.command.admin;

import com.sulzhenko.controller.command.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.model.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static com.sulzhenko.controller.context.ApplicationContext.getApplicationContext;

/**
 * Update activity controller action
 *
 */
public class UpdateActivityCommand implements Command, Constants, Path {
    ActivityService activityService = getApplicationContext().getActivityService();
    private static final Logger logger = LogManager.getLogger(UpdateActivityCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        HttpSession session = request.getSession();
        String forward;
        try{
            activityService.updateActivity(request.getParameter(NAME), request.getParameter(NEW_NAME),
                    request.getParameter(NEW_CATEGORY));
            forward = PAGE_SHOW_ACTIVITIES;
        } catch (ServiceException e){
            logger.warn(e.getMessage());
            session.setAttribute(ERROR, e.getMessage());
            forward = PAGE_ERROR_FULL;
        }
        return forward;
    }
}
