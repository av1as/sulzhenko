package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.entity.User;
import com.sulzhenko.model.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.SQLException;
import java.util.Objects;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

/**
 * Login controller action
 *
 */
public class LoginCommand implements Command {
  public static final String ERROR = "error";
  public static final String PASSWORD = "password";
  UserService userService = getApplicationContext().getUserService();

  private static final Logger logger = LogManager.getLogger(LoginCommand.class);

  @Override
  public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
    HttpSession session = request.getSession();
    String forward;
    User user = userService.getUser(request.getParameter("login"));
    assert user != null;
    if (areFieldsBlank(request) != null) {
      session.setAttribute(ERROR, areFieldsBlank(request));
      return Path.PAGE_ERROR;
    } else if (userService.areFieldsIncorrect(request.getParameter("login"), request.getParameter(PASSWORD)) != null){
      session.setAttribute(ERROR, userService.areFieldsIncorrect(request.getParameter("login"), request.getParameter(PASSWORD)));
      forward = Path.PAGE_ERROR;
    } else {
      session.setAttribute("user", user);
      session.setAttribute("menu", getMenu(user));
      forward = getPageOnRole(user.getRole(), user.getStatus());
    }
    return forward;
  }

  private String getMenu(User user) {
    String menu = Path.PAGE_LOGIN;
    if (user.getRole() == User.Role.ADMIN) {
      menu = "/TimeKeeping" + Path.MENU_ADMIN;
    } else if (user.getRole() == User.Role.SYSTEM_USER) {
      menu = "/TimeKeeping" + Path.MENU_SYSTEM_USER;
    }
    return menu;
  }

  private static String areFieldsBlank(HttpServletRequest request) {
    String login = request.getParameter("login");
    String password = request.getParameter(PASSWORD);
    String errorMessage = null;
    if (login == null || login.isEmpty()) {
      errorMessage = "empty.login";
      request.getSession().setAttribute(ERROR, errorMessage);
    } else if (password == null || password.isEmpty()) {
      errorMessage = "empty.password";
      request.getSession().setAttribute(ERROR, errorMessage);
    }
    logger.warn(errorMessage);
    return errorMessage;
  }


  private static String getPageOnRole(User.Role role, String status){
    String forward = Path.PAGE_LOGIN;
    if (Objects.equals(status, "inactive")) {
      forward = Path.HELLO_USER;
    } else if (role == User.Role.ADMIN) {
      forward = "/TimeKeeping" + Path.MENU_ADMIN;
    } else if (role == User.Role.SYSTEM_USER && "active".equals(status)) {
      forward = "/TimeKeeping" + Path.MENU_SYSTEM_USER;
    }
    return forward;
  }
}