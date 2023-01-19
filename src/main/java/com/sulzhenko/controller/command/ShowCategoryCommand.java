package com.sulzhenko.controller.command;

import com.sulzhenko.controller.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;

import com.sulzhenko.model.DTO.CategoryDTO;
import com.sulzhenko.model.DTO.UserDTO;
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
public class ShowCategoryCommand implements Command, Constants, Path {
    CategoryService categoryService = getApplicationContext().getCategoryService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        int page = 1;
        int recordsPerPage = 5;
        if(request.getParameter(PAGE) != null)
            page = Integer.parseInt(request.getParameter(PAGE));
        int noOfRecords;
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute(USER);
        List<CategoryDTO> categories = categoryService.viewAllCategories((page-1)*recordsPerPage, recordsPerPage);
        request.setAttribute(CATEGORIES, categories);
        noOfRecords = categoryService.getNumberOfCategories();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        request.setAttribute(NO_OF_PAGES, noOfPages);
        request.setAttribute(CURRENT_PAGE, page);
        request.setAttribute(MENU, getMenu(user));
        return PAGE_CATEGORIES;
    }

    private String getMenu(UserDTO user){
        String menu = PAGE_LOGIN;
        if(user.getRole() == UserDTO.Role.ADMIN){
            menu = PAGE_MENU_ADMIN_FULL;
        } else if (user.getRole() == UserDTO.Role.SYSTEM_USER) {
            menu = PAGE_MENU_SYSTEM_USER_FULL;
        }
        return menu;
    }
}
