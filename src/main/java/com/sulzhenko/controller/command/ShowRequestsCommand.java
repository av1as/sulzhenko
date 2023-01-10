package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.DAO.RequestDAO;
import com.sulzhenko.model.entity.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

public class ShowRequestsCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        int page = 1;
        int recordsPerPage = 5;
        if(request.getParameter("page") != null) page = Integer.parseInt(request.getParameter("page"));
        RequestDAO requestDAO = getApplicationContext().getRequestDAO();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String actionToDo=request.getParameter("action_to_do");
        int noOfRecords = getNumberOfRecords(actionToDo, requestDAO);
        request.setAttribute("requests", getRequestList(page, actionToDo, requestDAO, recordsPerPage));
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
    private List<Request> getRequestList(int page, String actionToDo, RequestDAO requestDAO, int recordsPerPage){
        List<Request> allRequests = requestDAO.viewAllRequests((page-1)*recordsPerPage, recordsPerPage);
        List<Request> requestsToAdd = requestDAO.viewRequestsToAdd((page-1)*recordsPerPage, recordsPerPage);
        List<Request> requestsToRemove = requestDAO.viewRequestsToRemove((page-1)*recordsPerPage, recordsPerPage);

        if(Objects.equals(actionToDo, "add")) {
            return requestsToAdd;
        } else if(Objects.equals(actionToDo, "remove")) {
            return requestsToRemove;
        } else {
            return allRequests;
        }
    }
    private int getNumberOfRecords(String actionToDo, RequestDAO requestDAO){
        if(Objects.equals(actionToDo, "add")) {
            return requestDAO.getByActionToDo("add").size();
        } else if(Objects.equals(actionToDo, "remove")) {
            return requestDAO.getByActionToDo("remove").size();
        } else {
            return requestDAO.getAll().size();
        }
    }
}