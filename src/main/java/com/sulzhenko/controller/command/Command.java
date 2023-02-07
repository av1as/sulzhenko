package com.sulzhenko.controller.command;

import com.sulzhenko.model.services.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;

/**
 * Main interface for the Command pattern implementation.
 *
 */
public interface Command {
  /**
   * Execution method for command.
   *
   * @return Address to go once the command is executed.
   */
  String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException;
}