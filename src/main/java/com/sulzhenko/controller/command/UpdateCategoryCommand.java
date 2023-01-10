package com.sulzhenko.controller.command;

import com.sulzhenko.model.DAO.CategoryDAO;
import com.sulzhenko.model.entity.Category;
import com.sulzhenko.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

/**
 * Register controller action
 *
 */
public class UpdateCategoryCommand implements Command {
    CategoryDAO categoryDAO = getApplicationContext().getCategoryDAO();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        long categoryId = Long.parseLong(request.getParameter("category_id"));
        String newCategoryName = request.getParameter("newname");
        Category category = categoryDAO.getById(categoryId).orElse(null);
        String[] param = {newCategoryName};
        categoryDAO.update(category, param);
        return "controller?action=show_categories";
    }
}