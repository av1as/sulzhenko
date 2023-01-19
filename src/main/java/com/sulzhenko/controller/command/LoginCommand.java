package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.model.DTO.UserDTO;
import com.sulzhenko.model.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.Objects;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

/**
 * Login controller action
 *
 */
public class LoginCommand implements Command, Constants, Path {

  UserService userService = getApplicationContext().getUserService();

  @Override
  public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
    HttpSession session = request.getSession(true);
    String forward;
    if (userService.areFieldsBlank(request) != null) {
      session.setAttribute(ERROR, userService.areFieldsBlank(request));
      return PAGE_ERROR_FULL;
    } else if (userService.areFieldsIncorrect(request.getParameter(LOGIN), request.getParameter(PASSWORD)) != null){
      session.setAttribute(ERROR, userService.areFieldsIncorrect(request.getParameter(LOGIN), request.getParameter(PASSWORD)));
      forward = PAGE_ERROR_FULL;
    } else {
      UserDTO userDTO = userService.getUserDTO(request.getParameter(LOGIN));
      session.setAttribute(USER, userDTO);
      session.setAttribute(MENU, getMenu(userDTO));
      forward = getPageOnRole(userDTO.getRole(), userDTO.getStatus());
    }
    return forward;
  }

  private String getMenu(UserDTO userDTO) {
    String menu = PAGE_LOGIN;
    if (userDTO.getRole() == UserDTO.Role.ADMIN) {
      menu = PAGE_MENU_ADMIN_FULL;
    } else if (userDTO.getRole() == UserDTO.Role.SYSTEM_USER) {
      menu = PAGE_MENU_SYSTEM_USER_FULL;
    }
    return menu;
  }
  private static String getPageOnRole(UserDTO.Role role, String status){
    String forward = PAGE_LOGIN;
    if (Objects.equals(status, INACTIVE)) {
      forward = PAGE_HELLO_USER;
    } else if (role == UserDTO.Role.ADMIN) {
      forward = PAGE_MENU_ADMIN_FULL;
    } else if (role == UserDTO.Role.SYSTEM_USER && ACTIVE.equals(status)) {
      forward = PAGE_MENU_SYSTEM_USER_FULL;
    }
    return forward;
  }
}