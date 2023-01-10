package com.sulzhenko.filters.domain;

import java.util.HashSet;
import java.util.Set;
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
    }

    static {
        SYSTEM_USER_ACTIONS.addAll(GUEST_ACTIONS);
        SYSTEM_USER_ACTIONS.add("show_profile");
        SYSTEM_USER_ACTIONS.add("update_user");
        SYSTEM_USER_ACTIONS.add("user_activities");
        SYSTEM_USER_ACTIONS.add("set_amount");
        SYSTEM_USER_ACTIONS.add("remove_activity");
        SYSTEM_USER_ACTIONS.add("add_activity");
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
        ADMIN_ACTIONS.add("show_report");
        ADMIN_ACTIONS.add("show_brief_report");
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