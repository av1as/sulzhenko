package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.entity.User;
import com.sulzhenko.model.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

/**
 * ProfileInfo controller action
 */
public class ShowUsersCommand implements Command {
    UserService userService = getApplicationContext().getUserService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        int page = 1;
        int recordsPerPage = 5;
        if(request.getParameter("page") != null)
            page = Integer.parseInt(request.getParameter("page"));
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String status = request.getParameter("status");
        request.setAttribute("users", getUserList(status, page, recordsPerPage));
        int noOfRecords = userService.getNumberOfRecords(status);
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("menu", getMenu(user));
        return Path.PAGE_USERS;
    }
    private String getMenu(User user){
        String menu = Path.PAGE_LOGIN;
        if(user.getRole() == User.Role.ADMIN){
            menu = "/TimeKeeping" + Path.MENU_ADMIN;
        } else if (user.getRole() == User.Role.SYSTEM_USER) {
            menu = "/TimeKeeping" + Path.MENU_SYSTEM_USER;
        }
        return menu;
    }
    private List<User> getUserList(String status, int page, int recordsPerPage){
        if(Objects.equals(status, "active")) {
            return userService.viewAllActiveUsers((page-1)*recordsPerPage, recordsPerPage);
        } else if(Objects.equals(status, "inactive")) {
            return userService.viewAllInactiveUsers((page-1)*recordsPerPage, recordsPerPage);
        } else if(Objects.equals(status, "deactivated")) {
            return userService.viewAllDeactivatedUsers((page-1)*recordsPerPage, recordsPerPage);
        } else {
            return userService.viewAllSystemUsers((page-1)*recordsPerPage, recordsPerPage);
        }
    }

}
