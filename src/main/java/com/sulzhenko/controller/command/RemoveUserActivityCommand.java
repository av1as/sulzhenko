package com.sulzhenko.controller.command;

import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.DAO.RequestDAO;
import com.sulzhenko.model.entity.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

/**
 * Register controller action
 *
 *
 */
public class RemoveUserActivityCommand implements Command {
    RequestDAO requestDAO = getApplicationContext().getRequestDAO();
    private static final Logger logger = LogManager.getLogger(RemoveUserActivityCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String activityName = request.getParameter("activity_name");
        Request req = new Request.Builder()
                .withLogin(user.getLogin())
                .withActivityName(activityName)
                .withActionToDo("remove")
                .withDescription("")
                .build();
        try{
            requestDAO.save(req);
        } catch (DAOException e){
            logger.warn(e.getMessage());
        }
        return "controller?action=user_activities";
    }
}