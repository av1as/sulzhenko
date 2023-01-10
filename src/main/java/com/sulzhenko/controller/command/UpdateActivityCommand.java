package com.sulzhenko.controller.command;

import com.sulzhenko.model.DAO.ActivityDAO;
import com.sulzhenko.model.entity.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

public class UpdateActivityCommand implements Command {
    ActivityDAO activityDAO = getApplicationContext().getActivityDAO();
    private static final Logger logger = LogManager.getLogger(UpdateActivityCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String name = request.getParameter("name");
        String newCategoryName = request.getParameter("newcategory");
        String newName = request.getParameter("newname");
        Activity activity = activityDAO.getByName(name);
        String[] param = {newName, newCategoryName};
        activityDAO.update(activity, param);
        return "controller?action=show_activities";
    }
}
