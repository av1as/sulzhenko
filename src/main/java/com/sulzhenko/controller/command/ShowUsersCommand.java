package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.model.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import static com.sulzhenko.controller.ApplicationContext.getApplicationContext;

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
        String status = request.getParameter(STATUS);
        request.setAttribute(USERS, userService.getUserList(status, page, recordsPerPage));
        int noOfRecords = userService.getNumberOfRecords(status);
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        request.setAttribute(NO_OF_PAGES, noOfPages);
        request.setAttribute(CURRENT_PAGE, page);
        return PAGE_USERS;
    }
}
