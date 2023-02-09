package com.sulzhenko.controller.command.admin;

import com.sulzhenko.controller.command.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.DTO.ActivityDTO;
import com.sulzhenko.model.services.ActivityService;
import com.sulzhenko.model.services.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import static com.sulzhenko.controller.context.ApplicationContext.getApplicationContext;

/**
 * NewActivity controller action
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class NewActivityCommand implements Command, Constants, Path {
    ActivityService activityService = getApplicationContext().getActivityService();
    private static final Logger logger = LogManager.getLogger(NewActivityCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        HttpSession session = request.getSession();
        String forward = PAGE_SHOW_ACTIVITIES;
        String addedName = request.getParameter(ADDED_NAME);
        String addedCategory = request.getParameter(ADDED_CATEGORY);
        String filter = request.getParameter(FILTER);
        String order = request.getParameter(ORDER);
        String parameter = request.getParameter(PARAMETER);
        String page = request.getParameter(PAGE);
        try {
            activityService.addActivity(addedName, addedCategory);
            List<ActivityDTO> activities = activityService.listActivitiesSorted(filter, order, parameter, page);
            request.setAttribute(ACTIVITIES, activities);
        } catch(ServiceException e){
            logger.warn(e.getMessage());
            session.setAttribute(ERROR, WRONG_ACTIVITY);
            forward = PAGE_ERROR_FULL;
        }
        return forward;
    }
}
