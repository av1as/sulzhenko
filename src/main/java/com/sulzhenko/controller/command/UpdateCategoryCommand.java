package com.sulzhenko.controller.command;

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
public class UpdateCategoryCommand implements Command {
    CategoryService categoryService = getApplicationContext().getCategoryService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String oldName = request.getParameter("category_name");
        String newName = request.getParameter("newname");
        HttpSession session = request.getSession();
        String forward;
        try{
            categoryService.updateCategory(oldName, newName);
            forward = "controller?action=show_categories";
        } catch(ServiceException e){
            session.setAttribute("error", e.getMessage());
            forward = Path.PAGE_ERROR;
        }
        return forward;
    }
}