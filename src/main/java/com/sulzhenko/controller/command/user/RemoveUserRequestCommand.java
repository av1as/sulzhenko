package com.sulzhenko.controller.command.user;

import com.sulzhenko.controller.command.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.model.services.RequestService;
import com.sulzhenko.model.services.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static com.sulzhenko.controller.context.ApplicationContext.getApplicationContext;

/**
 * Remove user request controller action
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class RemoveUserRequestCommand implements Command, Constants, Path {
    RequestService requestService = getApplicationContext().getRequestService();
    private static final Logger logger = LogManager.getLogger(RemoveUserRequestCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        HttpSession session = request.getSession();
        String forward = PAGE_ERROR_FULL;
        try{
            requestService.removeRequest(Long.valueOf(request.getParameter(REQUEST_ID)));
            forward = PAGE_CONTROLLER_USER_REQUESTS;
        } catch (ServiceException e){
            logger.warn(e.getMessage());
            session.setAttribute(ERROR, e.getMessage());
        }
        return forward;
    }
}
