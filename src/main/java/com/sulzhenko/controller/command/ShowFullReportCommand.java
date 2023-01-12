package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.DTO.*;
import com.sulzhenko.model.entity.User;
import com.sulzhenko.model.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.sulzhenko.ApplicationContext.getApplicationContext;
import static com.sulzhenko.model.Util.PaginationUtil.paginate;

public class ShowFullReportCommand implements Command {
    UserActivityService userActivityService = getApplicationContext().getUserActivityService();
    ReportService reportService = getApplicationContext().getReportService();
    private static final Logger logger = LogManager.getLogger(ShowFullReportCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        setPage(request);
        setMenu(request);
        List<UserActivityDTO> userActivities = userActivityService.listAllUserActivitiesSorted(request);
        List<ReportDTO> report = reportService.viewReportPage(userActivities);
        request.setAttribute("report", report);
        int noOfRecords;
        try {
            noOfRecords = userActivityService.getNumberOfRecords();
        } catch (ServiceException e) {
            String errorMessage = e.getMessage();
            logger.warn(e);
            request.setAttribute(errorMessage, "message");
            return Path.PAGE_ERROR;
        }
        int recordsPerPage = 5;
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        request.setAttribute("noOfPages", noOfPages);
        paginate(noOfRecords, request);
        return Path.PAGE_FULL_REPORT;
    }

    private static void setPage(HttpServletRequest request) {
        int page = 1;
        if(request.getParameter("page") != null)
            page = Integer.parseInt(request.getParameter("page"));
        request.setAttribute("currentPage", page);
    }

    private void setMenu(HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String menu = "/TimeKeeping" + Path.PAGE_LOGIN;
        if(user.getRole() == User.Role.ADMIN){
            menu = "/TimeKeeping" + Path.MENU_ADMIN;
        } else if (user.getRole() == User.Role.SYSTEM_USER) {
            menu = "/TimeKeeping" + Path.MENU_SYSTEM_USER;
        }
        request.setAttribute("menu", menu);
    }
}


