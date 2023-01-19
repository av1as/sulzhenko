package com.sulzhenko.controller;

/**
 * Class represents all jsp-pages and commands ina application
 *
 */
public interface Path {
  String PAGE_ACTIVITIES = "/jsp/activities.jsp";
  String PAGE_CATEGORIES = "/jsp/categories.jsp";
  String PAGE_CONTROLLER_USER_ACTIVITIES = "controller?action=user_activities";
  String PAGE_ERROR_FULL = "/TimeKeeping/jsp/error_page.jsp";
  String PAGE_ERROR = "/jsp/error_page.jsp";
  String PAGE_FULL_REPORT = "/jsp/full_report.jsp";
  String PAGE_HELLO_USER = "/TimeKeeping/jsp/hellouser.jsp";
  String PAGE_INDEX = "index.jsp";

  String PAGE_LOGIN = "/TimeKeeping/index.jsp";
  String PAGE_MENU_ADMIN = "/jsp/menu_admin.jsp";
  String PAGE_MENU_ADMIN_FULL = "/TimeKeeping/jsp/menu_admin.jsp";
  String PAGE_MENU_SYSTEM_USER = "/jsp/menu_user.jsp";
  String PAGE_MENU_SYSTEM_USER_FULL = "/TimeKeeping/jsp/menu_user.jsp";
  String PAGE_PROFILE = "/TimeKeeping/jsp/profile.jsp";
  String PAGE_REQUESTS = "/jsp/requests.jsp";
  String PAGE_SHOW_ACTIVITIES = "controller?action=show_activities";
  String PAGE_SHOW_CATEGORIES = "controller?action=show_categories";
  String PAGE_SHOW_REQUESTS = "controller?action=show_requests";
  String PAGE_SHOW_USERS = "/TimeKeeping/controller?action=users";
  String PAGE_USER_ACTIVITIES = "/jsp/user_activities.jsp";
  String PAGE_USERS = "/jsp/users.jsp";
//  String PAGE_REPORT = "/jsp/report.jsp";
//  String PAGE_BRIEF_REPORT = "/jsp/brief_report.jsp";

}