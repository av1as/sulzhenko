package com.sulzhenko.controller.command.admin;

import com.sulzhenko.controller.command.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.model.services.RequestService;
import com.sulzhenko.model.services.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.sulzhenko.controller.context.ApplicationContext.getApplicationContext;

public class DeclineRequestCommand implements Command, Constants, Path {
    RequestService requestService = getApplicationContext().getRequestService();
    private static final Logger logger = LogManager.getLogger(DeclineRequestCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        String forward = PAGE_ERROR_FULL;
        int id = Integer.parseInt(request.getParameter(ID));
        try{
            requestService.deleteRequest(requestService.getRequest(id));
            forward = PAGE_SHOW_REQUESTS;
        } catch(ServiceException e){
            logger.warn(e.getMessage());
        }
        return forward;
    }
}
