package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.entity.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static com.sulzhenko.ApplicationContext.getApplicationContext;

/**
 * Register controller action
 *
 *
 */
public class SetAmountCommand implements Command {
    ActivityDAO activityDAO = getApplicationContext().getActivityDAO();
    UserActivityDAO uaDAO = getApplicationContext().getUserActivityDAO();
    private static final Logger logger = LogManager.getLogger(SetAmountCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)  {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String activityName = request.getParameter("activity_name");
        int amount = Integer.parseInt(request.getParameter("amount"));
        Activity activity = activityDAO.getByName(activityName);
        try{
            uaDAO.setAmount(user, activity, amount);
        } catch (DAOException e){
            session.setAttribute("error", e.getMessage());
            logger.warn(e.getMessage());
            return Path.PAGE_ERROR;
        }
        request.setAttribute("user", user);
        return "controller?action=user_activities";
    }
}
