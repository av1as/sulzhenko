package com.sulzhenko.controller.command.base;

import com.sulzhenko.controller.command.Command;
import com.sulzhenko.controller.Path;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
public class DefaultCommand implements Command, Path {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return PAGE_INDEX;
    }
}