package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.DTO.ActivityDTO;
import com.sulzhenko.model.entity.*;
import com.sulzhenko.model.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

import static com.sulzhenko.ApplicationContext.getApplicationContext;
import static com.sulzhenko.model.Util.PaginationUtil.paginate;

/**
 * ProfileInfo controller action
 */
public class ShowActivityCommand implements Command {
    private static final Logger logger = LogManager.getLogger(ShowActivityCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        setPage(request);
        setMenu(request);
        ActivityService activityService = getApplicationContext().getActivityService();
        List<ActivityDTO> activities = activityService.listActivitiesSorted(request);
        request.setAttribute("activities", activities);
        CategoryDAO categoryDAO = getApplicationContext().getCategoryDAO();
        List<Category> categories = categoryDAO.getAll();
        request.setAttribute("categories", categories);
        int noOfRecords;
        try {
            noOfRecords = activityService.getNumberOfRecords(request);
        } catch (DAOException |SQLException e) {
            String errorMessage = e.getMessage();
            logger.warn(e);
            request.setAttribute(errorMessage, "error");
            return "/jsp/error_page.jsp";
        }
        int recordsPerPage = 5;
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        request.setAttribute("noOfPages", noOfPages);
        paginate(noOfRecords, request);
        return Path.PAGE_ACTIVITIES;
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

