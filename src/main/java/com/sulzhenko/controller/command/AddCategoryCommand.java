package com.sulzhenko.controller.command;

import com.sulzhenko.model.DAO.CategoryDAO;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.entity.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.ApplicationContext.getApplicationContext;
import static com.sulzhenko.controller.Path.PAGE_ERROR;

/**
 * Register controller action
 *
 */
public class AddCategoryCommand implements Command {
    CategoryDAO categoryDAO = getApplicationContext().getCategoryDAO();
    private static final Logger logger = LogManager.getLogger(AddCategoryCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String forward;
        try{
            categoryDAO.save(new Category(request.getParameter("addedname")));
            forward = "controller?action=show_categories";
        } catch (DAOException e){
            logger.warn(e.getMessage());
            forward = PAGE_ERROR;
        }
        return forward;
    }
}
