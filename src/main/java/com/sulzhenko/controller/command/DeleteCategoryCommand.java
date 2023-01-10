package com.sulzhenko.controller.command;

import com.sulzhenko.model.DAO.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

/**
 * Register controller action
 *
 *
 */
public class DeleteCategoryCommand implements Command {
    CategoryDAO categoryDAO = getApplicationContext().getCategoryDAO();
    private static final Logger logger = LogManager.getLogger(DeleteCategoryCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String categoryName = request.getParameter("category_name");
        try{
            categoryDAO.delete(categoryDAO.getByName(categoryName).orElse(null));
        } catch (DAOException e){
            logger.warn(e.getMessage());
        }
        return "controller?action=show_categories";
    }
}
