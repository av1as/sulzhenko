package com.sulzhenko.controller.command;

import com.sulzhenko.model.services.CategoryService;
import com.sulzhenko.model.services.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

/**
 * Delete category controller action
 *
 *
 */
public class DeleteCategoryCommand implements Command {
    CategoryService categoryService = getApplicationContext().getCategoryService();
    private static final Logger logger = LogManager.getLogger(DeleteCategoryCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String categoryName = request.getParameter("category_name");
        try{
            categoryService.deleteCategory(categoryName);
        } catch (ServiceException e){
            logger.warn(e.getMessage());
        }
        return "controller?action=show_categories";
    }
}
