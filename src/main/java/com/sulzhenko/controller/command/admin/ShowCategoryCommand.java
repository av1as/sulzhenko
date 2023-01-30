package com.sulzhenko.controller.command.admin;

import com.sulzhenko.controller.command.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;

import com.sulzhenko.DTO.CategoryDTO;
import com.sulzhenko.model.services.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

import static com.sulzhenko.controller.ApplicationContext.getApplicationContext;

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
        List<CategoryDTO> categories = categoryService.viewAllCategories((page-1)*recordsPerPage, recordsPerPage);
        request.setAttribute(CATEGORIES, categories);
        noOfRecords = categoryService.getNumberOfCategories();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        request.setAttribute(NO_OF_PAGES, noOfPages);
        request.setAttribute(CURRENT_PAGE, page);
        request.setAttribute(QUERY, SHOW_CATEGORIES);
        return PAGE_CATEGORIES;
    }
}
