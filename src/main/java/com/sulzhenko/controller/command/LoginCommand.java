package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.DAO.UserDAO;
import com.sulzhenko.model.entity.User;
import com.sulzhenko.model.hashingPasswords.Sha;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

/**
 * Login controller action
 *
 */
public class LoginCommand implements Command {
  UserDAO userDAO = getApplicationContext().getUserDAO();

  private static final Logger logger = LogManager.getLogger(LoginCommand.class);

  @Override
  public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
    HttpSession session = request.getSession();
    String forward;
    String login = request.getParameter("login");
    String password = request.getParameter("password");
    User user = userDAO.getByLogin(login).orElse(null);
    assert user != null;
    if (areFieldsBlank(request) != null) {
      session.setAttribute("error", areFieldsBlank(request));
      return Path.PAGE_ERROR;
    } else if (areFieldsIncorrect(user, password) != null){
      session.setAttribute("error", areFieldsIncorrect(user, password));
      forward = Path.PAGE_ERROR;
    } else {
      User.Role role = user.getRole();
      String status = user.getStatus();
      session.setAttribute("user", user);
      session.setAttribute("menu", getMenu(user));
      forward = getPageOnRole(role, status);
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
    String password = request.getParameter("password");
    String errorMessage = null;
    if (login == null || login.isEmpty()) {
      errorMessage = "empty.login";
      request.getSession().setAttribute("error", errorMessage);
    } else if (password == null || password.isEmpty()) {
      errorMessage = "empty.password";
      request.getSession().setAttribute("error", errorMessage);
    }
    logger.warn(errorMessage);
    return errorMessage;
  }

  private static String areFieldsIncorrect(User user, String password) {
    String errorMessage = null;
    if (user.getLogin() == null) {
      errorMessage = "wrong.login";
    } else {
      try {
        if (!new Sha().hashToHex(password, Optional.ofNullable(user.getLogin())).equals(user.getPassword())) {
          errorMessage = "wrong.password";
        }
      } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException(e);
      }
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