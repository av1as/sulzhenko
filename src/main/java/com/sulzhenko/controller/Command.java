package com.sulzhenko.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;

/**
 * Main interface for the Action pattern implementation.
 *
 */
public interface Command {
  /**
   * Execution method for command.
   *
   * @return Address to go once the command is executed.
   */
  String execute(HttpServletRequest request, HttpServletResponse response) throws SQLException;
}