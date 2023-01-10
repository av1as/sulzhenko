package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.DAO.UserDAO;
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
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        int page = 1;
        int recordsPerPage = 5;
        if(request.getParameter("page") != null)
            page = Integer.parseInt(request.getParameter("page"));
        UserDAO userDAO = getApplicationContext().getUserDAO();
        UserService userService = getApplicationContext().getUserService();

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String status = request.getParameter("status");
        request.setAttribute("users", getUserList(status, userService, page, recordsPerPage));
        int noOfRecords = getNumberOfRecords(status, userDAO);
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
    private List<User> getUserList(String status, UserService userService, int page, int recordsPerPage){
        List<User> allUsers = userService.viewAllSystemUsers((page-1)*recordsPerPage, recordsPerPage);
        List<User> activeUsers = userService.viewAllActiveUsers((page-1)*recordsPerPage, recordsPerPage);
        List<User> inactiveUsers = userService.viewAllInactiveUsers((page-1)*recordsPerPage, recordsPerPage);
        List<User> deactivatedUsers = userService.viewAllDeactivatedUsers((page-1)*recordsPerPage, recordsPerPage);


        if(Objects.equals(status, "active")) {
            return activeUsers;
        } else if(Objects.equals(status, "inactive")) {
            return inactiveUsers;
        } else if(Objects.equals(status, "deactivated")) {
            return deactivatedUsers;
        } else {
            return allUsers;
        }
    }
    private static int getNumberOfRecords(String status, UserDAO userDAO){
        if(Objects.equals(status, "active")) {
            return userDAO.getByStatus("active").size();
        } else if(Objects.equals(status, "inactive")) {
            return userDAO.getByStatus("inactive").size();
        } else if(Objects.equals(status, "deactivated")) {
            return userDAO.getByStatus("deactivated").size();
        } else {
            return userDAO.getByRole("system user").size();
        }
    }
}
