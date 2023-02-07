package com.sulzhenko.controller.command.base;

import com.sulzhenko.controller.command.Command;
import com.sulzhenko.controller.Path;
import com.sulzhenko.model.services.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Default command controller action
 *
 */
public class DefaultCommand implements Command, Path {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        return PAGE_INDEX;
    }
}