package com.sulzhenko.controller;

import com.sulzhenko.controller.command.Command;
import com.sulzhenko.controller.command.CommandFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static com.sulzhenko.controller.Constants.ACTION;
import static com.sulzhenko.controller.Path.PAGE_ERROR;


/**
 * Controller  class. Implements Front-controller pattern. Chooses action to execute and redirect or forward result.
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
@WebServlet("/controller")
public class Controller extends HttpServlet {
  private static final Logger logger = LogManager.getLogger(Controller.class);
  private static final CommandFactory ACTION_FACTORY = CommandFactory.getCommandFactory();

  /**
   * Calls and executes action and then forwards requestDispatcher
   * @param request comes from user
   * @param response comes from user
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.getRequestDispatcher(executeRequest(request, response)).forward(request, response);
  }

  /**
   * Calls and executes action and then sendRedirect for PRG pattern implementation
   * @param request comes from user
   * @param response comes from user
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.sendRedirect(executeRequest(request, response));
  }

  /**
   * Gets path to use in doPost/doGet methods. In case of error will return error page
   * @return path
   */
  private String executeRequest(HttpServletRequest request, HttpServletResponse response){
    Command command = ACTION_FACTORY.createCommand(request.getParameter(ACTION));
    String path = PAGE_ERROR;
    try {
      path = command.execute(request, response);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
    return path;
  }
}