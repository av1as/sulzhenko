package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.model.DTO.ActivityDTO;
import com.sulzhenko.model.DTO.CategoryDTO;
import com.sulzhenko.model.DTO.UserDTO;
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
public class ShowActivityCommand implements Command, Constants, Path {
    ActivityService activityService = getApplicationContext().getActivityService();
    CategoryService categoryService = getApplicationContext().getCategoryService();
    private static final Logger logger = LogManager.getLogger(ShowActivityCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        setPage(request);
        setMenu(request);
        List<ActivityDTO> activities = activityService.listActivitiesSorted(request);
        request.setAttribute(ACTIVITIES, activities);
        List<CategoryDTO> categories = categoryService.getAllCategories();
        request.setAttribute(CATEGORIES, categories);
        int noOfRecords;
        try {
            noOfRecords = activityService.getNumberOfRecords(request);
        } catch (ServiceException | SQLException e) {
            String errorMessage = e.getMessage();
            logger.warn(e);
            request.setAttribute(errorMessage, ERROR);
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
    private void setMenu(HttpServletRequest request){
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute(USER);
        String menu = PAGE_LOGIN;
        if(user.getRole() == UserDTO.Role.ADMIN){
            menu = PAGE_MENU_ADMIN_FULL;
        } else if (user.getRole() == UserDTO.Role.SYSTEM_USER) {
            menu = PAGE_MENU_SYSTEM_USER_FULL;
        }
        request.setAttribute(MENU, menu);
    }
}

