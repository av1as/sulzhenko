package com.sulzhenko.controller.command.common;

import com.sulzhenko.controller.command.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.DTO.UserDTO;
import com.sulzhenko.model.services.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * log out controller action
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class LogoutCommand implements Command, Path, Constants {
    private static final Logger logger = LogManager.getLogger(LogoutCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute(USER);
        session.invalidate();
        if (userDTO != null) {
            String login = userDTO.getLogin();
            logger.info(USER_LOG_OUT, login);
        }
        return PAGE_LOGIN;
    }
}
