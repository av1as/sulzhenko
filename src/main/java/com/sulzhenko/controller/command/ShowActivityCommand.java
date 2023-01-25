package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.DTO.ActivityDTO;
import com.sulzhenko.DTO.CategoryDTO;
import com.sulzhenko.model.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

import static com.sulzhenko.controller.ApplicationContext.getApplicationContext;
import static com.sulzhenko.Util.PaginationUtil.paginate;

/**
 * ProfileInfo controller action
 */
public class ShowActivityCommand implements Command, Constants, Path {
    ActivityService activityService = getApplicationContext().getActivityService();
    CategoryService categoryService = getApplicationContext().getCategoryService();
    private static final Logger logger = LogManager.getLogger(ShowActivityCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        setPage(request);
        String filter = request.getParameter(FILTER);
        String order = request.getParameter(ORDER);
        String parameter = request.getParameter(PARAMETER);
        String page = request.getParameter(PAGE);
        List<ActivityDTO> activities = activityService.listActivitiesSorted(filter, order, parameter, page);
        request.setAttribute(ACTIVITIES, activities);
        List<CategoryDTO> categories = categoryService.getAllCategories();
        request.setAttribute(CATEGORIES, categories);
        int noOfRecords;
        try {
            noOfRecords = activityService.getNumberOfRecords(filter);
        } catch (ServiceException e) {
            logger.warn(e);
            session.setAttribute(ERROR, e.getMessage());
            return PAGE_ERROR;
        } catch (SQLException e) {
            logger.warn(e);
            session.setAttribute(ERROR, UNKNOWN_ERROR);
            return PAGE_ERROR;
        }
        int recordsPerPage = 5;
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        request.setAttribute(NO_OF_PAGES, noOfPages);
        paginate(noOfRecords, request);
        return PAGE_ACTIVITIES;
    }
    private static void setPage(HttpServletRequest request) {
        int page = 1;
        if(request.getParameter(PAGE) != null)
            page = Integer.parseInt(request.getParameter(PAGE));
        request.setAttribute(CURRENT_PAGE, page);
    }
}

