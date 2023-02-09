package com.sulzhenko.controller.command.user;

import com.sulzhenko.controller.command.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.model.services.ServiceException;
import com.sulzhenko.model.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static com.sulzhenko.controller.context.ApplicationContext.getApplicationContext;

/**
 * Recover user password controller action
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class RecoverPasswordCommand implements Command, Constants, Path {
    UserService userService = getApplicationContext().getUserService();
    private static final Logger logger = LogManager.getLogger(RecoverPasswordCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        HttpSession session = request.getSession();
        String forward;
        try{
            userService.recoverPassword(request.getParameter(LOGIN));
            forward = PAGE_CHECK_EMAIL;
        } catch(ServiceException e) {
            logger.warn(e.getMessage());
            session.setAttribute(ERROR, e.getMessage());
            forward = PAGE_ERROR_FULL;
        }
        return forward;
    }
}
