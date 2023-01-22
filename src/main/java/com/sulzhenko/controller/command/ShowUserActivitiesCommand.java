package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.DTO.ActivityDTO;
import com.sulzhenko.DTO.UserActivityDTO;

import com.sulzhenko.DTO.UserDTO;
import com.sulzhenko.model.services.ServiceException;
import com.sulzhenko.model.services.UserActivityService;
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
public class ShowUserActivitiesCommand implements Command, Constants, Path {
    UserActivityService userActivityService = getApplicationContext().getUserActivityService();
    private static final Logger logger = LogManager.getLogger(ShowUserActivitiesCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute(USER);
        List <UserActivityDTO> activities = userActivityService.listUserActivitiesSorted(request, userDTO);
        List<ActivityDTO> available = userActivityService.allAvailableActivities(userDTO);
        setPage(request);
        int noOfRecords;
        try {
            noOfRecords = userActivityService.getNumberOfRecords(userDTO.getLogin());
        } catch (ServiceException e) {
            logger.warn(e);
            session.setAttribute(ERROR, e.getMessage());
            return PAGE_ERROR;
        }
        int recordsPerPage = 5;
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        request.setAttribute(NO_OF_PAGES, noOfPages);
        paginate(noOfRecords, request);
        request.setAttribute(USER, userDTO);
        request.setAttribute(ACTIVITIES, activities);
        request.setAttribute(TO_ADD, available);
        return PAGE_USER_ACTIVITIES;
    }
    private static void setPage(HttpServletRequest request) {
        int page = 1;
        if(request.getParameter(PAGE) != null)
            page = Integer.parseInt(request.getParameter(PAGE));
        request.setAttribute(CURRENT_PAGE, page);
    }
}