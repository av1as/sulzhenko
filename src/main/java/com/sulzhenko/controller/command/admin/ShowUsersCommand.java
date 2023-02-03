package com.sulzhenko.controller.command.admin;

import com.sulzhenko.controller.command.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.model.services.ServiceException;
import com.sulzhenko.model.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static com.sulzhenko.controller.context.ApplicationContext.getApplicationContext;

/**
 * ProfileInfo controller action
 */
public class ShowUsersCommand implements Command, Constants, Path {
    UserService userService = getApplicationContext().getUserService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        int page = 1;
        int recordsPerPage = 5;
        if(request.getParameter(PAGE) != null)
            page = Integer.parseInt(request.getParameter(PAGE));
        String status = request.getParameter(STATUS);
        if(status == null) status = ALL;
        request.setAttribute(USERS, userService.getUserList(status, page, recordsPerPage));
        int noOfRecords = userService.getNumberOfRecords(status);
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        request.setAttribute(NO_OF_PAGES, noOfPages);
        request.setAttribute(CURRENT_PAGE, page);
        request.setAttribute(QUERY, String.format(USERS_AND_STATUS, status));
        return PAGE_USERS;
    }
}
