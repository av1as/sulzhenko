package com.sulzhenko.controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
public class DefaultCommand implements Command {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return "index.jsp";
    }
}