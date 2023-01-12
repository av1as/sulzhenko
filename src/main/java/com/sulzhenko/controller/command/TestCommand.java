package com.sulzhenko.controller.command;

import com.sulzhenko.model.DAO.UserDAO;
import com.sulzhenko.model.entity.User;
import com.sulzhenko.model.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.util.List;

import static com.sulzhenko.ApplicationContext.getApplicationContext;

//public class TestCommand implements Command {
//    @Override
//    public String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException {
//        int page = 1;
//        int recordsPerPage = 5;
//        if(request.getParameter("page") != null)
//            page = Integer.parseInt(request.getParameter("page"));
//        UserDAO userDAO = getApplicationContext().getUserDAO();
//        UserService userService = getApplicationContext().getUserService();
//
//
//        int noOfRecords = userDAO.getAll().size();
//        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
//
//        request.setAttribute("noOfPages", noOfPages);
//        request.setAttribute("currentPage", page);
//
//
//        List<User> list = userService.viewAllSystemUsers((page-1)*recordsPerPage, recordsPerPage);
//        request.setAttribute("users", list);
//
//        return"jsp/test.jsp";
//    }
//}
