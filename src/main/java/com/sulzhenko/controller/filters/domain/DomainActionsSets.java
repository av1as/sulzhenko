package com.sulzhenko.controller.filters.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Contains action sets for guest, system user and administrator. Defines if user has access to the action
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public final class DomainActionsSets {
    private DomainActionsSets() {}

    private static final Set<String> GUEST_ACTIONS = new HashSet<>();
    private static final Set<String> SYSTEM_USER_ACTIONS = new HashSet<>();
    private static final Set<String> ADMIN_ACTIONS = new HashSet<>();

    static {
        GUEST_ACTIONS.add("");
        GUEST_ACTIONS.add("login");
        GUEST_ACTIONS.add("register");
        GUEST_ACTIONS.add("redirect");
        GUEST_ACTIONS.add("controller");
        GUEST_ACTIONS.add("logout");
        GUEST_ACTIONS.add("recover_password");
    }

    static {
        SYSTEM_USER_ACTIONS.addAll(GUEST_ACTIONS);
        SYSTEM_USER_ACTIONS.add("show_profile");
        SYSTEM_USER_ACTIONS.add("update_user");
        SYSTEM_USER_ACTIONS.add("user_activities");
        SYSTEM_USER_ACTIONS.add("set_amount");
        SYSTEM_USER_ACTIONS.add("remove_activity");
        SYSTEM_USER_ACTIONS.add("add_activity");
        SYSTEM_USER_ACTIONS.add("user_requests");
        SYSTEM_USER_ACTIONS.add("set_request_description");
        SYSTEM_USER_ACTIONS.add("remove_request");
    }

    static {
        ADMIN_ACTIONS.addAll(SYSTEM_USER_ACTIONS);
        ADMIN_ACTIONS.add("admin_update");
        ADMIN_ACTIONS.add("users");
        ADMIN_ACTIONS.add("show_categories");
        ADMIN_ACTIONS.add("show_activities");
        ADMIN_ACTIONS.add("update_activity");
        ADMIN_ACTIONS.add("delete_activity");
        ADMIN_ACTIONS.add("new_activity");
        ADMIN_ACTIONS.add("remove_category");
        ADMIN_ACTIONS.add("update_category");
        ADMIN_ACTIONS.add("add_category");
        ADMIN_ACTIONS.add("show_requests");
        ADMIN_ACTIONS.add("approve_request");
        ADMIN_ACTIONS.add("decline_request");
        ADMIN_ACTIONS.add("show_full_report");
    }

    public static Set<String> getGuestActions() {
        return GUEST_ACTIONS;
    }

    public static Set<String> getSystemUserActions() {
        return SYSTEM_USER_ACTIONS;
    }

    public static Set<String>  getAdminActions() {
        return ADMIN_ACTIONS;
    }
}