package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.model.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

public class UpdateActivityCommand implements Command, Constants, Path {
    ActivityService activityService = getApplicationContext().getActivityService();
    private static final Logger logger = LogManager.getLogger(UpdateActivityCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        HttpSession session = request.getSession();
        String forward;
        String name = request.getParameter(NAME);
        String newCategoryName = request.getParameter(NEW_CATEGORY);
        String newName = request.getParameter(NEW_NAME);
        try{
            activityService.updateActivity(name, newName, newCategoryName);
            forward = PAGE_SHOW_ACTIVITIES;
        } catch (ServiceException e){
            logger.warn(e.getMessage());
            session.setAttribute(ERROR, e.getMessage());
            forward = Path.PAGE_ERROR_FULL;
        }
        return forward;
    }
}
