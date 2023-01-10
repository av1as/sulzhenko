package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.DAO.ActivityDAO;
import com.sulzhenko.model.DAO.DAOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

public class DeleteActivityCommand implements Command {
    ActivityDAO activityDAO = getApplicationContext().getActivityDAO();
    private static final Logger logger = LogManager.getLogger(DeleteActivityCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String forward = Path.PAGE_ERROR;
        String name = request.getParameter("activity_name");
        try{
            activityDAO.delete(activityDAO.getByName(name));
            forward = "controller?action=show_activities";
        } catch (DAOException e){
            logger.warn(e.getMessage());
        }
        return forward;
    }
}
