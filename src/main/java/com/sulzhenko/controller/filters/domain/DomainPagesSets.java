package com.sulzhenko.controller.filters.domain;

import java.util.HashSet;
import java.util.Set;

public final class DomainPagesSets {
    private DomainPagesSets() {}

    private static final Set<String> GUEST_PAGES = new HashSet<>();
    private static final Set<String> SYSTEM_USER_PAGES = new HashSet<>();
    private static final Set<String> ADMIN_PAGES = new HashSet<>();

    static {
        GUEST_PAGES.add("index.jsp");
        GUEST_PAGES.add("jsp/register.jsp");
        GUEST_PAGES.add("controller");
        GUEST_PAGES.add("jsp/error_page.jsp");
        GUEST_PAGES.add("jsp/hellouser.jsp");
    }

    static {
        SYSTEM_USER_PAGES.add("jsp/error_page.jsp");
        SYSTEM_USER_PAGES.add("controller");
        SYSTEM_USER_PAGES.add("jsp/profile.jsp");
        SYSTEM_USER_PAGES.add("jsp/user_activities.jsp");
        SYSTEM_USER_PAGES.add("jsp/hellouser.jsp");
        SYSTEM_USER_PAGES.add("jsp/menu_user.jsp");
        SYSTEM_USER_PAGES.add("TimeKeeping/jsp/test.jsp");
        SYSTEM_USER_PAGES.add("jsp/test.jsp");
        SYSTEM_USER_PAGES.add("jsp/user_requests.jsp");
    }

    static {
        ADMIN_PAGES.add("controller");
        ADMIN_PAGES.add("jsp/activities.jsp");
        ADMIN_PAGES.add("jsp/profile.jsp");
        ADMIN_PAGES.add("jsp/report.jsp");
        ADMIN_PAGES.add("jsp/update_user");
        ADMIN_PAGES.add("jsp/brief_report.jsp");
        ADMIN_PAGES.add("jsp/full_report.jsp");
        ADMIN_PAGES.add("jsp/categories.jsp");
        ADMIN_PAGES.add("jsp/error_page.jsp");
        ADMIN_PAGES.add("jsp/menu_admin.jsp");
        ADMIN_PAGES.add("jsp/requests.jsp");
        ADMIN_PAGES.add("jsp/users.jsp");
        ADMIN_PAGES.add("report.pdf");
        ADMIN_PAGES.add("page.pdf");
    }

    public static Set<String> getGuestPages() {
        return GUEST_PAGES;
    }


    public static Set<String> getSystemUserPages() {
        return SYSTEM_USER_PAGES;
    }

    public static Set<String>  getAdminPages() {
        return ADMIN_PAGES;
    }
}