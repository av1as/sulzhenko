package com.sulzhenko.controller.command.admin;

import com.sulzhenko.controller.command.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.model.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.sulzhenko.controller.context.ApplicationContext.getApplicationContext;

/**
 * Add category controller action
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class AddCategoryCommand implements Command, Path, Constants {
    CategoryService categoryService = getApplicationContext().getCategoryService();
    private static final Logger logger = LogManager.getLogger(AddCategoryCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        String forward;
        try{
            categoryService.addCategory(request.getParameter(ADDED_NAME));
            forward = PAGE_SHOW_CATEGORIES;
        } catch (ServiceException e){
            logger.warn(e.getMessage());
            request.getSession().setAttribute(ERROR, e.getMessage());
            forward = PAGE_ERROR_FULL;
        }
        return forward;
    }
}
