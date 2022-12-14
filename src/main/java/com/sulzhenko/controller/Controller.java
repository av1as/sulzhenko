package com.sulzhenko.controller;

import com.sulzhenko.controller.command.Command;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static com.sulzhenko.controller.Path.PAGE_ERROR;


/**
 * Main Controller Servlet
 *
 */
@WebServlet("/controller")
public class Controller extends HttpServlet {
  private static final Logger logger = LogManager.getLogger(Controller.class);
  private static final ActionFactory ACTION_FACTORY = ActionFactory.getActionFactory();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.getRequestDispatcher(executeRequest(req, resp)).forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    resp.sendRedirect(executeRequest(req, resp));
  }

  private String executeRequest(HttpServletRequest req, HttpServletResponse resp){

    Command action = ACTION_FACTORY.createCommand(req.getParameter("action"));
    String path = PAGE_ERROR;
    try {
      path = action.execute(req, resp);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    return path;
  }
}