package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Objects;

/**
 * ProfileInfo controller action
 */
public class ProfileInfoCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        setAttributes(request, user);
        return Path.PAGE_PROFILE;
    }
    private void setAttributes(HttpServletRequest request, User user) {
        request.setAttribute("login", user.getLogin());
        request.setAttribute("password", user.getPassword());
        request.setAttribute("email", user.getEmail());
        request.setAttribute("firstname", user.getFirstName());
        request.setAttribute("lastname", user.getLastName());
        request.setAttribute("notif", Objects.equals(user.getNotification(), "on") ? "checked": "unchecked");
        request.setAttribute("menu", getMenu(user));
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
