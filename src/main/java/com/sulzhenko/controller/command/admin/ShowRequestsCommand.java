package com.sulzhenko.controller.command.admin;

import com.sulzhenko.controller.command.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;

import com.sulzhenko.model.services.RequestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;

import static com.sulzhenko.controller.ApplicationContext.getApplicationContext;
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
        String actionToDo=request.getParameter(ACTION_TO_DO);
        int noOfRecords = requestService.getNumberOfRecords(actionToDo);
        request.setAttribute(REQUESTS,
                requestService.getRequestList(page, actionToDo, recordsPerPage));
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        request.setAttribute(NO_OF_PAGES, noOfPages);
        request.setAttribute(CURRENT_PAGE, page);
        request.setAttribute(QUERY, SHOW_REQUESTS);
        return PAGE_REQUESTS;
    }
}