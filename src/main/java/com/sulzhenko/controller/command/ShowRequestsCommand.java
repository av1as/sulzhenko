package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.entity.*;
import com.sulzhenko.model.services.RequestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static com.sulzhenko.ApplicationContext.getApplicationContext;
/**
 * Show list of requests controller action
 *
 */
public class ShowRequestsCommand implements Command {
    RequestService requestService = getApplicationContext().getRequestService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        int page = (request.getParameter("page") != null)?
                Integer.parseInt(request.getParameter("page")): 1;
        int recordsPerPage = 5;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String actionToDo=request.getParameter("action_to_do");
        int noOfRecords = getNumberOfRecords(actionToDo, requestService);
        request.setAttribute("requests",
                getRequestList(page, actionToDo, requestService, recordsPerPage));
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("menu", getMenu(user));
        return Path.PAGE_REQUESTS;
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
    private List<Request> getRequestList(int page, String actionToDo, RequestService requestService, int recordsPerPage){
        if(Objects.equals(actionToDo, "add")) {
            return requestService.viewRequestsToAdd((page-1)*recordsPerPage, recordsPerPage);
        } else if(Objects.equals(actionToDo, "remove")) {
            return requestService.viewRequestsToRemove((page-1)*recordsPerPage, recordsPerPage);
        } else {
            return requestService.viewAllRequests((page-1)*recordsPerPage, recordsPerPage);
        }
    }
    private int getNumberOfRecords(String actionToDo, RequestService requestService){
        if(Objects.equals(actionToDo, "add")) {
            return requestService.getRequestsToAdd().size();
        } else if(Objects.equals(actionToDo, "remove")) {
            return requestService.getRequestsToRemove().size();
        } else {
            return requestService.getAllRequest().size();
        }
    }
}