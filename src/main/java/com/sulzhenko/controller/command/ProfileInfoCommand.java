package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.model.DTO.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * ProfileInfo controller action
 */
public class ProfileInfoCommand implements Command, Constants, Path {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute(USER);
        setAttributes(request, userDTO);
        return PAGE_PROFILE;
    }
    private void setAttributes(HttpServletRequest request, UserDTO userDTO) {
        request.setAttribute(LOGIN, userDTO.getLogin());
        request.setAttribute(PASSWORD, userDTO.getPassword());
        request.setAttribute(EMAIL, userDTO.getEmail());
        request.setAttribute(FIRST_NAME, userDTO.getFirstName());
        request.setAttribute(LAST_NAME, userDTO.getLastName());
//        request.setAttribute("notif", Objects.equals(userDTO.getNotification(), "on") ? "checked": "unchecked");
        request.setAttribute(MENU, getMenu(userDTO));
    }
    private String getMenu(UserDTO userDTO){
        String menu = PAGE_LOGIN;
        if(userDTO.getRole() == UserDTO.Role.ADMIN){
            menu = PAGE_MENU_ADMIN;
        } else if (userDTO.getRole() == UserDTO.Role.SYSTEM_USER) {
            menu = PAGE_MENU_SYSTEM_USER;
        }
        return menu;
    }
}
