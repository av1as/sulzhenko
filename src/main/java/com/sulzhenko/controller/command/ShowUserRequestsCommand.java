package com.sulzhenko.controller.command;

import com.sulzhenko.DTO.UserDTO;
import com.sulzhenko.controller.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.model.services.RequestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.sql.SQLException;

import static com.sulzhenko.controller.ApplicationContext.getApplicationContext;

public class ShowUserRequestsCommand implements Command, Constants, Path {
    RequestService requestService = getApplicationContext().getRequestService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute(USER);
        int page = (request.getParameter(PAGE) != null)?
                Integer.parseInt(request.getParameter(PAGE)): 1;
        int recordsPerPage = 5;
        String actionToDo=request.getParameter(ACTION_TO_DO);
        int noOfRecords = requestService.getUserNumberOfRecords(userDTO.getLogin(), actionToDo);
        request.setAttribute(REQUESTS,
                requestService.getUserRequestList(userDTO.getLogin(), page, actionToDo, recordsPerPage));
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        request.setAttribute(NO_OF_PAGES, noOfPages);
        request.setAttribute(CURRENT_PAGE, page);
        return PAGE_USER_REQUESTS;
    }
}
