package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.DTO.UserDTO;
import com.sulzhenko.model.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Objects;

import static com.sulzhenko.controller.ApplicationContext.getApplicationContext;

/**
 * Login controller action
 *
 */
public class LoginCommand implements Command, Constants, Path {
  UserService userService = getApplicationContext().getUserService();
  private static final Logger logger = LogManager.getLogger(LoginCommand.class);

  @Override
  public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
    HttpSession session = request.getSession(true);
    String forward;
    if (userService.areFieldsBlank(request) != null) {
      session.setAttribute(ERROR, userService.areFieldsBlank(request));
      return PAGE_ERROR_FULL;
    } else {
      String login = request.getParameter(LOGIN);
      if (userService.areFieldsIncorrect(login, request.getParameter(PASSWORD)) != null){
        session.setAttribute(ERROR, userService.areFieldsIncorrect(login, request.getParameter(PASSWORD)));
        forward = PAGE_ERROR_FULL;
      } else {
        UserDTO userDTO = userService.getUserDTO(login);
        session.setAttribute(USER, userDTO);
        setMenu(userDTO, session);
        forward = getPageOnRole(userDTO.getRole(), userDTO.getStatus());
        logger.info("user log in: {}", login);
      }
    }
    return forward;
  }

  private void setMenu(UserDTO userDTO, HttpSession session) {
    String menu = PAGE_LOGIN;
    if (userDTO.getRole() == UserDTO.Role.ADMIN) {
      menu = PAGE_MENU_ADMIN;
    } else if (userDTO.getRole() == UserDTO.Role.SYSTEM_USER) {
      menu = PAGE_MENU_SYSTEM_USER;
    }
    session.setAttribute(MENU, menu);
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