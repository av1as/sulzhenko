package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.DAO.RequestDAO;
import com.sulzhenko.model.entity.Request;
import com.sulzhenko.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

/**
 * Request to addUser activity controller action
 *
 */
public class RequestToAddActivityCommand implements Command {
    RequestDAO requestDAO = getApplicationContext().getRequestDAO();
    private static final Logger logger = LogManager.getLogger(RequestToAddActivityCommand.class);


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(request.getParameter("new_activity") == null || request.getParameter("new_activity").isEmpty()){
            logger.warn("wrong activity");
            session.setAttribute("error", "wrong.activity");
            return Path.PAGE_ERROR;
        }
        Request t = new Request.Builder()
                    .withLogin(user.getLogin())
                    .withActivityName(request.getParameter("new_activity"))
                    .withActionToDo("add")
                    .build();
        try{
            requestDAO.save(t);
        } catch (DAOException e){
            logger.warn(e.getMessage());
            session.setAttribute("error", e.getMessage());
        }
        return "controller?action=user_activities";
    }
}
