package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;

import com.sulzhenko.model.DTO.RequestDTO;
import com.sulzhenko.model.DTO.UserDTO;
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
public class ShowRequestsCommand implements Command, Constants, Path {
    RequestService requestService = getApplicationContext().getRequestService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        int page = (request.getParameter(PAGE) != null)?
                Integer.parseInt(request.getParameter(PAGE)): 1;
        int recordsPerPage = 5;
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute(USER);
        String actionToDo=request.getParameter(ACTION_TO_DO);
        int noOfRecords = getNumberOfRecords(actionToDo, requestService);
        request.setAttribute(REQUESTS,
                getRequestList(page, actionToDo, requestService, recordsPerPage));
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        request.setAttribute(NO_OF_PAGES, noOfPages);
        request.setAttribute(CURRENT_PAGE, page);
        request.setAttribute(MENU, getMenu(user));
        return PAGE_REQUESTS;
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
    private List<RequestDTO> getRequestList(int page, String actionToDo, RequestService requestService,
                                            int recordsPerPage){
        if(Objects.equals(actionToDo, ADD)) {
            return requestService.viewRequestsToAdd((page-1)*recordsPerPage, recordsPerPage);
        } else if(Objects.equals(actionToDo, REMOVE)) {
            return requestService.viewRequestsToRemove((page-1)*recordsPerPage, recordsPerPage);
        } else {
            return requestService.viewAllRequests((page-1)*recordsPerPage, recordsPerPage);
        }
    }
    private int getNumberOfRecords(String actionToDo, RequestService requestService){
        if(Objects.equals(actionToDo, ADD)) {
            return requestService.getRequestsToAdd().size();
        } else if(Objects.equals(actionToDo, REMOVE)) {
            return requestService.getRequestsToRemove().size();
        } else {
            return requestService.getAllRequest().size();
        }
    }
}