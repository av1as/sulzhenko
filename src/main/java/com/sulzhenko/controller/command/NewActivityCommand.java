package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.DTO.ActivityDTO;
import com.sulzhenko.model.entity.*;
import com.sulzhenko.model.services.ActivityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

public class NewActivityCommand implements Command {
    private static final Logger logger = LogManager.getLogger(NewActivityCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        HttpSession session = request.getSession();
        String forward = "controller?action=show_activities";
        ActivityDAO activityDAO = getApplicationContext().getActivityDAO();
        CategoryDAO categoryDAO = getApplicationContext().getCategoryDAO();
        String addedName = request.getParameter("addedname");
        String addedCategory = request.getParameter("addedcategory");
        Category category = categoryDAO.getByName(addedCategory).orElse(null);
        Activity activity = new Activity.Builder()
                            .withName(addedName)
                            .withCategory(category)
                            .build();
        try {
            activityDAO.save(activity);
            ActivityService activityService = getApplicationContext().getActivityService();
            List<ActivityDTO> activities = activityService.listActivitiesSorted(request);
            request.setAttribute("activities", activities);
        } catch(DAOException e){
            logger.warn(e.getMessage());
            session.setAttribute("error", "wrong.activity");
            forward = Path.PAGE_ERROR;
        }
        return forward;
    }
}
