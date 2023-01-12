package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.DTO.ActivityDTO;
import com.sulzhenko.model.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

public class NewActivityCommand implements Command {
    ActivityService activityService = getApplicationContext().getActivityService();
    private static final Logger logger = LogManager.getLogger(NewActivityCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        HttpSession session = request.getSession();
        String forward = "controller?action=show_activities";
        String addedName = request.getParameter("addedname");
        String addedCategory = request.getParameter("addedcategory");
        try {
            activityService.addActivity(addedName, addedCategory);
            List<ActivityDTO> activities = activityService.listActivitiesSorted(request);
            request.setAttribute("activities", activities);
        } catch(ServiceException e){
            logger.warn(e.getMessage());
            session.setAttribute("error", "wrong.activity");
            forward = Path.PAGE_ERROR;
        }
        return forward;
    }
}
