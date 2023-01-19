package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.model.DTO.ActivityDTO;
import com.sulzhenko.model.DTO.UserActivityDTO;

import com.sulzhenko.model.DTO.UserDTO;
import com.sulzhenko.model.services.UserActivityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.sql.SQLException;
import java.util.List;

import static com.sulzhenko.ApplicationContext.getApplicationContext;


/**
 * ProfileInfo controller action
 */
public class ShowUserActivitiesCommand implements Command, Constants, Path {
    UserActivityService userActivityService = getApplicationContext().getUserActivityService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute(USER);
        List <UserActivityDTO> activities = userActivityService.listUserActivitiesSorted(request, userDTO);
        List<ActivityDTO> available = userActivityService.allAvailableActivities(userDTO);
        request.setAttribute(USER, userDTO);
        request.setAttribute(ACTIVITIES, activities);
        request.setAttribute(MENU, getMenu(userDTO));
        request.setAttribute(TO_ADD, available);
        return Path.PAGE_USER_ACTIVITIES;
    }

    private String getMenu(UserDTO user){
        String menu = PAGE_LOGIN;
        if(user.getRole() == UserDTO.Role.ADMIN){
            menu = PAGE_MENU_ADMIN;
        } else if (user.getRole() == UserDTO.Role.SYSTEM_USER) {
            menu = PAGE_MENU_SYSTEM_USER;
        }
        return menu;
    }
}