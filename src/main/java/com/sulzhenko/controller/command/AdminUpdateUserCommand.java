package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.entity.User;
import com.sulzhenko.model.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

/**
 * Update user controller action
 *
 */
public class AdminUpdateUserCommand implements Command {
    UserDAO userDAO = getApplicationContext().getUserDAO();
    UserService userService = getApplicationContext().getUserService();
    private static final Logger logger = LogManager.getLogger(AdminUpdateUserCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        HttpSession session = request.getSession();
        String forward = Path.PAGE_ERROR;
        User user = userDAO.getByLogin(request.getParameter("oldlogin")).orElse(null);
        String[] param = getUpdateParameters(request, user);
        try{
            userService.adminUpdateUser(user, param);
            forward = "/TimeKeeping/controller?action=users";
        } catch (DAOException e) {
            logger.warn(e.getMessage());
            session.setAttribute("error", e.getMessage());
        }
        return forward;
    }

    private static String[] getUpdateParameters(HttpServletRequest request, User user) {
        String login = request.getParameter("newlogin");
        String email = request.getParameter("newemail");
        String firstName = request.getParameter("newfirstname");
        String lastName = request.getParameter("newlastname");
        String status = request.getParameter("newstatus");
        String notifications = request.getParameter("newnotification");
        if (notifications == null) notifications = "off";
        assert user != null;
        return new String[]{login, email, user.getPassword(), firstName, lastName, "system user",
                status, notifications};
    }
}