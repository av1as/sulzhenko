package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

public class DeleteActivityCommand implements Command {
    ActivityService activityService = getApplicationContext().getActivityService();
    private static final Logger logger = LogManager.getLogger(DeleteActivityCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String forward = Path.PAGE_ERROR;
        try{
            activityService.deleteActivity(request.getParameter("activity_name"));
            forward = "controller?action=show_activities";
        } catch (ServiceException e){
            logger.warn(e.getMessage());
        }
        return forward;
    }
}
