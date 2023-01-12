package com.sulzhenko.controller.command;

import com.sulzhenko.model.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.ApplicationContext.getApplicationContext;
import static com.sulzhenko.controller.Path.PAGE_ERROR;

/**
 * Add category controller action
 *
 */
public class AddCategoryCommand implements Command {
    CategoryService categoryService = getApplicationContext().getCategoryService();
    private static final Logger logger = LogManager.getLogger(AddCategoryCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String forward;
        try{
            categoryService.addCategory(request.getParameter("addedname"));
            forward = "controller?action=show_categories";
        } catch (ServiceException e){
            logger.warn(e.getMessage());
            forward = PAGE_ERROR;
        }
        return forward;
    }
}
