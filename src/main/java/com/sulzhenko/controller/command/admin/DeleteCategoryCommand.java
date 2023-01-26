package com.sulzhenko.controller.command.admin;

import com.sulzhenko.controller.command.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.model.services.CategoryService;
import com.sulzhenko.model.services.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.controller.ApplicationContext.getApplicationContext;

/**
 * Delete category controller action
 *
 */
public class DeleteCategoryCommand implements Command, Constants, Path {
    CategoryService categoryService = getApplicationContext().getCategoryService();
    private static final Logger logger = LogManager.getLogger(DeleteCategoryCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String categoryName = request.getParameter(CATEGORY_NAME);
        try{
            categoryService.deleteCategory(categoryName);
        } catch (ServiceException e){
            logger.warn(e.getMessage());
        }
        return PAGE_SHOW_CATEGORIES;
    }
}
