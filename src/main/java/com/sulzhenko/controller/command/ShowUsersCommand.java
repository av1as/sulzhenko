package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;

import com.sulzhenko.model.DTO.UserDTO;
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
public class ShowUsersCommand implements Command, Constants, Path {
    UserService userService = getApplicationContext().getUserService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        int page = 1;
        int recordsPerPage = 5;
        if(request.getParameter(PAGE) != null)
            page = Integer.parseInt(request.getParameter(PAGE));
        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute(USER);
        String status = request.getParameter(STATUS);
        request.setAttribute(USERS, getUserList(status, page, recordsPerPage));
        int noOfRecords = userService.getNumberOfRecords(status);
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        request.setAttribute(NO_OF_PAGES, noOfPages);
        request.setAttribute(CURRENT_PAGE, page);
        request.setAttribute(MENU, getMenu(userDTO));
        return Path.PAGE_USERS;
    }
    private String getMenu(UserDTO user){
        String menu = PAGE_LOGIN;
        if(user.getRole() == UserDTO.Role.ADMIN){
            menu = PAGE_MENU_ADMIN_FULL;
        } else if (user.getRole() == UserDTO.Role.SYSTEM_USER) {
            menu = PAGE_MENU_SYSTEM_USER_FULL;
        }
        return menu;
    }
    private List<UserDTO> getUserList(String status, int page, int recordsPerPage){
        if(Objects.equals(status, ACTIVE)) {
            return userService.viewAllActiveUsers((page-1)*recordsPerPage, recordsPerPage);
        } else if(Objects.equals(status, INACTIVE)) {
            return userService.viewAllInactiveUsers((page-1)*recordsPerPage, recordsPerPage);
        } else if(Objects.equals(status, DEACTIVATED)) {
            return userService.viewAllDeactivatedUsers((page-1)*recordsPerPage, recordsPerPage);
        } else {
            return userService.viewAllSystemUsers((page-1)*recordsPerPage, recordsPerPage);
        }
    }
}
