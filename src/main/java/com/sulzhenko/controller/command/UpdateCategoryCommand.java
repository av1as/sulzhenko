package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.model.services.CategoryService;
import com.sulzhenko.model.services.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.sql.SQLException;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

/**
 * Update category controller action
 *
 */
public class UpdateCategoryCommand implements Command, Constants, Path {
    CategoryService categoryService = getApplicationContext().getCategoryService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String oldName = request.getParameter(CATEGORY_NAME);
        String newName = request.getParameter(NEW_NAME);
        HttpSession session = request.getSession();
        String forward;
        try{
            categoryService.updateCategory(oldName, newName);
            forward = PAGE_SHOW_CATEGORIES;
        } catch(ServiceException e){
            session.setAttribute(ERROR, e.getMessage());
            forward = PAGE_ERROR_FULL;
        }
        return forward;
    }
}