package com.sulzhenko.controller.command;

import com.sulzhenko.model.services.RequestService;
import com.sulzhenko.model.services.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.ApplicationContext.getApplicationContext;
import static com.sulzhenko.controller.Path.PAGE_ERROR;

public class ApproveRequestCommand implements Command {
    RequestService requestService = getApplicationContext().getRequestService();
    private static final Logger logger = LogManager.getLogger(ApproveRequestCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String forward = PAGE_ERROR;
        long id = Long.parseLong(request.getParameter("id"));
        try{
            requestService.approveRequest(requestService.getRequest(id));
            forward = "controller?action=show_requests";
        } catch (ServiceException e){
            logger.warn(e.getMessage());
        }
        return forward;
    }
}
