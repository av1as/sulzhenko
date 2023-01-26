package com.sulzhenko.controller.command.user;

import com.sulzhenko.controller.command.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.DTO.RequestDTO;
import com.sulzhenko.DTO.UserDTO;
import com.sulzhenko.model.services.RequestService;
import com.sulzhenko.model.services.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.controller.ApplicationContext.getApplicationContext;

/**
 * Request to add User activity controller action
 *
 */
public class RequestToAddActivityCommand implements Command, Constants, Path {
    RequestService requestService = getApplicationContext().getRequestService();
    private static final Logger logger = LogManager.getLogger(RequestToAddActivityCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute(USER);
        if(request.getParameter(NEW_ACTIVITY) == null || request.getParameter(NEW_ACTIVITY).isEmpty()){
            logger.warn(WRONG_ACTIVITY);
            session.setAttribute(ERROR, WRONG_ACTIVITY);
            return PAGE_ERROR_FULL;
        }
        RequestDTO requestDTO = new RequestDTO.Builder()
                    .withLogin(userDTO.getLogin())
                    .withActivityName(request.getParameter(NEW_ACTIVITY))
                    .withActionToDo(ADD)
                    .build();
        try{
            requestService.addRequest(requestDTO);
        } catch (ServiceException e){
            logger.warn(e.getMessage());
            session.setAttribute(ERROR, e.getMessage());
        }
        return PAGE_CONTROLLER_USER_ACTIVITIES;
    }
}
