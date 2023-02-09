package com.sulzhenko.controller.command.user;

import com.sulzhenko.controller.command.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.DTO.UserDTO;
import com.sulzhenko.model.services.ServiceException;
import com.sulzhenko.model.services.UserActivityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static com.sulzhenko.controller.context.ApplicationContext.getApplicationContext;

/**
 * Set amount of time controller action
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class SetAmountCommand implements Command, Constants, Path {
    UserActivityService userActivityService = getApplicationContext().getUserActivityService();
    private static final Logger logger = LogManager.getLogger(SetAmountCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException  {
        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute(USER);
        String activityName = request.getParameter(ACTIVITY_NAME);
        int amount = Integer.parseInt(request.getParameter(AMOUNT));
        try{
            userActivityService.setAmount(userDTO.getLogin(), activityName, amount);
        } catch (ServiceException e){
            session.setAttribute(ERROR, e.getMessage());
            logger.warn(e.getMessage());
            return PAGE_ERROR_FULL;
        }
        request.setAttribute(USER, userDTO);
        return PAGE_CONTROLLER_USER_ACTIVITIES;
    }
}
