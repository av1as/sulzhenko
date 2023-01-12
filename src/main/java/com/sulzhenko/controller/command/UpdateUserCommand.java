package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.DAO.UserDAO;
import com.sulzhenko.model.entity.User;
import com.sulzhenko.model.hashingPasswords.Sha;
import com.sulzhenko.model.services.ServiceException;
import com.sulzhenko.model.services.UserService;
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
 * Update user controller action
 *
 */
public class UpdateUserCommand implements Command {
//    UserDAO userDAO = getApplicationContext().getUserDAO();
    UserService userService = getApplicationContext().getUserService();
    Sha sha = getApplicationContext().getSha();
    private static final Logger logger = LogManager.getLogger(UpdateUserCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String role = user.getRole().value;
        String currentPassword = request.getParameter("currentpassword");
        String errorMessage;
        String forward = Path.PAGE_ERROR;
        User newUser;
        if(Objects.equals(request.getParameter("newpassword"), request.getParameter("newpasswordconfirm"))){
            newUser = getUser(request);
        } else {
            errorMessage = "different.passwords";
            session.setAttribute("error", errorMessage);
            return forward;
        }
        if (user.getLogin() == null ) {
            errorMessage = "wrong.login";
            session.setAttribute("error", errorMessage);
            return forward;
        } else {
            try {
                if (!sha.hashToHex(currentPassword, Optional.ofNullable(user.getLogin())).equals(user.getPassword())) {
                    errorMessage = "wrong.password";
                    session.setAttribute("error", errorMessage);
                    return forward;
                }
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                throw new ServiceException("unknown.error");
            }
        }
        String[] param = {newUser.getLogin(), newUser.getEmail(), newUser.getPassword(), newUser.getFirstName(),
                newUser.getLastName(), role, "active", newUser.getNotification()};
        try{
            userService.updateUser(user, param);
            user = userService.getUser(newUser.getLogin());
            session.setAttribute("user", user);
        } catch (ServiceException e) {
            logger.fatal(e.getMessage());
            session.setAttribute("error", e.getMessage());
            return forward;
        }
        return Path.PAGE_PROFILE;
    }
    private User getUser(HttpServletRequest request) {
        String login = request.getParameter("newlogin");
        String currentPassword = request.getParameter("currentpassword");
        String newPassword = request.getParameter("newpassword");
        if(Objects.equals(newPassword, "")) newPassword = currentPassword;
        String email = request.getParameter("newemail");
        String firstName = request.getParameter("newfirstname");
        String lastName = request.getParameter("newlastname");
        String notifications = request.getParameter("newnotifications");
        if (notifications == null) notifications = "off";
        return new User.Builder().withLogin(login)
                .withPassword(newPassword)
                .withEmail(email)
                .withFirstName(firstName)
                .withLastName(lastName)
                .withRole("system user")
                .withStatus("inactive")
                .withNotification(notifications)
                .build();
    }
}
