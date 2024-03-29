package com.sulzhenko.controller.command.common;

import com.sulzhenko.controller.command.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.DTO.UserDTO;
import com.sulzhenko.model.services.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * ProfileInfo controller action
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class ProfileInfoCommand implements Command, Constants, Path {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute(USER);
        request.setAttribute(LOGIN, userDTO.getLogin());
        request.setAttribute(PASSWORD, userDTO.getPassword());
        request.setAttribute(EMAIL, userDTO.getEmail());
        request.setAttribute(FIRST_NAME, userDTO.getFirstName());
        request.setAttribute(LAST_NAME, userDTO.getLastName());
        return PAGE_PROFILE;
    }
}
