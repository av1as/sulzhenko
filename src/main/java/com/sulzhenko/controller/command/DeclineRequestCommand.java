package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.DAO.RequestDAO;
import com.sulzhenko.model.entity.Request;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

public class DeclineRequestCommand implements Command {
    private static final Logger logger = LogManager.getLogger(DeclineRequestCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String forward = Path.PAGE_ERROR;
        int id = Integer.parseInt(request.getParameter("id"));
        RequestDAO requestDAO = getApplicationContext().getRequestDAO();
        Request element = requestDAO.getById(id);
        try{
            requestDAO.delete(element);
            forward = "controller?action=show_requests";
        } catch(DAOException e){
            logger.warn(e.getMessage());
        }
        return forward;
    }
}
