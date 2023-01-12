package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.DTO.UserActivityDTO;
import com.sulzhenko.model.entity.*;
import com.sulzhenko.model.services.UserActivityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

import static com.sulzhenko.ApplicationContext.getApplicationContext;


/**
 * ProfileInfo controller action
 */
public class ShowUserActivitiesCommand implements Command {
    UserActivityService userActivityService = getApplicationContext().getUserActivityService();
    private static final Logger logger = LogManager.getLogger(ShowUserActivitiesCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List <UserActivityDTO> activities = userActivityService.listUserActivitiesSorted(request, user);
        List<Activity> available = userActivityService.allAvailableActivities(user);
        request.setAttribute("user", user);
        request.setAttribute("activities", activities);
        request.setAttribute("menu", getMenu(user));
        request.setAttribute("to_add", available);
        return Path.PAGE_USER_ACTIVITIES;
    }

    private String getMenu(User user){
        String menu = Path.PAGE_LOGIN;
        if(user.getRole() == User.Role.ADMIN){
            menu = Path.MENU_ADMIN;
        } else if (user.getRole() == User.Role.SYSTEM_USER) {
            menu = Path.MENU_SYSTEM_USER;
        }
        return menu;
    }
}