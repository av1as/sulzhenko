package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.model.services.RequestService;
import com.sulzhenko.model.services.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.sulzhenko.controller.ApplicationContext.getApplicationContext;

public class SetRequestDescriptionCommand implements Command, Constants, Path {
    RequestService requestService = getApplicationContext().getRequestService();
    private static final Logger logger = LogManager.getLogger(SetRequestDescriptionCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)  {
        HttpSession session = request.getSession();
        Long requestId = Long.valueOf(request.getParameter(REQUEST_ID));
        String description = request.getParameter(DESCRIPTION);
        try{
            requestService.setRequestDescription(requestId, description);
        } catch (ServiceException e){
            session.setAttribute(ERROR, e.getMessage());
            logger.warn(e.getMessage());
            return Path.PAGE_ERROR_FULL;
        }
        return PAGE_CONTROLLER_USER_REQUESTS;
    }
}
