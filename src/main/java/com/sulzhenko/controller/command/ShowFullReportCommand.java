package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.model.DTO.*;
import com.sulzhenko.model.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.sulzhenko.ApplicationContext.getApplicationContext;
import static com.sulzhenko.model.Util.PaginationUtil.paginate;

public class ShowFullReportCommand implements Command, Constants, Path {
    UserActivityService userActivityService = getApplicationContext().getUserActivityService();
    ReportService reportService = getApplicationContext().getReportService();
    private static final Logger logger = LogManager.getLogger(ShowFullReportCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute(USER);
        setPage(request);
        List<UserActivityDTO> userActivities = userActivityService.listAllUserActivitiesSorted(request);
        List<ReportDTO> report = reportService.viewReportPage(userActivities);
        request.setAttribute(REPORT, report);
        int noOfRecords;
        try {
            noOfRecords = userActivityService.getNumberOfRecords();
        } catch (ServiceException e) {
            logger.warn(e);
            session.setAttribute(ERROR, e.getMessage());
            return Path.PAGE_ERROR_FULL;
        }
        int recordsPerPage = 5;
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        request.setAttribute(NO_OF_PAGES, noOfPages);
        request.setAttribute(MENU, getMenu(user));
        paginate(noOfRecords, request);
        return PAGE_FULL_REPORT;
    }

    private static void setPage(HttpServletRequest request) {
        int page = 1;
        if(request.getParameter(PAGE) != null)
            page = Integer.parseInt(request.getParameter(PAGE));
        request.setAttribute(CURRENT_PAGE, page);
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
}


