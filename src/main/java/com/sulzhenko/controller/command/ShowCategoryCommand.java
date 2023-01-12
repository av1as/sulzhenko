package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Path;
import com.sulzhenko.model.entity.*;
import com.sulzhenko.model.services.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.sql.SQLException;
import java.util.List;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

/**
 * Show categories controller action
 */
public class ShowCategoryCommand implements Command {
    CategoryService categoryService = getApplicationContext().getCategoryService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        int page = 1;
        int recordsPerPage = 5;
        if(request.getParameter("page") != null)
            page = Integer.parseInt(request.getParameter("page"));
        int noOfRecords;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<Category> categories = categoryService.viewAllCategories((page-1)*recordsPerPage, recordsPerPage);
        request.setAttribute("categories", categories);
        noOfRecords = categoryService.getNumberOfCategories();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("menu", getMenu(user));
        return Path.PAGE_CATEGORIES;
    }

    private String getMenu(User user){
        String menu = Path.PAGE_LOGIN;
        if(user.getRole() == User.Role.ADMIN){
            menu = "/TimeKeeping" + Path.MENU_ADMIN;
        } else if (user.getRole() == User.Role.SYSTEM_USER) {
            menu = "/TimeKeeping" + Path.MENU_SYSTEM_USER;
        }
        return menu;
    }
}
