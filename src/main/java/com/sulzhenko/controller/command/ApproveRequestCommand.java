package com.sulzhenko.controller.command;

import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.entity.Request;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.ApplicationContext.getApplicationContext;
import static com.sulzhenko.controller.Path.PAGE_ERROR;

public class ApproveRequestCommand implements Command {
    private static final Logger logger = LogManager.getLogger(ApproveRequestCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String forward = PAGE_ERROR;
        long id = Long.parseLong(request.getParameter("id"));
        RequestDAO requestDAO = getApplicationContext().getRequestDAO();
        Request element = requestDAO.getById(id);
        UserActivityDAO uaDAO = getApplicationContext().getUserActivityDAO();
        try{
            uaDAO.approveRequest(element);
            forward = "controller?action=show_requests";
        } catch (DAOException e){
            logger.warn(e.getMessage());
        }
        return forward;
    }
}
