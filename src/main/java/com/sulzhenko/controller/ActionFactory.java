package com.sulzhenko.controller;

import com.sulzhenko.controller.command.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Main Command factory for Controller
 *
 *
 */
public class ActionFactory {
  private static final ActionFactory ACTION_FACTORY = new ActionFactory();
  private static final Map<String, Command> actions = new HashMap<>();

  /**
     * Singleton
     */


  static {
    // guest commands
    actions.put("default", new DefaultCommand());
    actions.put("login", new LoginCommand());
    actions.put("register", new RegisterCommand());
    actions.put("redirect", null);

    //common commands
    actions.put("logout", new LogoutCommand());
    actions.put("show_profile", new ProfileInfoCommand());
    actions.put("update_user", new UpdateUserCommand());

    //system user commands
    actions.put("user_activities", new ShowUserActivitiesCommand());
    actions.put("set_amount", new SetAmountCommand());
    actions.put("remove_activity", new RequestToRemoveActivityCommand());
    actions.put("add_activity", new RequestToAddActivityCommand());

    //admin commands
    actions.put("admin_update", new AdminUpdateUserCommand());
    actions.put("users", new ShowUsersCommand());
    actions.put("show_categories", new ShowCategoryCommand());
    actions.put("show_activities", new ShowActivityCommand());
    actions.put("update_activity", new UpdateActivityCommand());
    actions.put("delete_activity", new DeleteActivityCommand());
    actions.put("new_activity", new NewActivityCommand());
    actions.put("remove_category", new DeleteCategoryCommand());
    actions.put("update_category", new UpdateCategoryCommand());
    actions.put("add_category", new AddCategoryCommand());
    actions.put("show_requests", new ShowRequestsCommand());
    actions.put("approve_request", new ApproveRequestCommand());
    actions.put("decline_request", new DeclineRequestCommand());
//    actions.put("show_report", new ShowReportCommand());
//    actions.put("show_brief_report", new ShowBriefReportCommand());
    actions.put("show_full_report", new ShowFullReportCommand());
  }

  private ActionFactory() {}

  public static ActionFactory getActionFactory() {
    return ACTION_FACTORY;
  }

  public Command createCommand(String actionName) {
    return actions.getOrDefault(actionName, new DefaultCommand());
  }
}