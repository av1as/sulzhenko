package com.sulzhenko.filters.domain;

import java.util.Set;

public class Domain {
    private final String servletPath;
    private final String action;
    private Set<String> domainPages;
    private Set<String> domainActions;
    private Domain(String servletPath, String action) {
        this.servletPath = servletPath;
        this.action = action;
        setDomains();
    }

    private Domain(String servletPath, String action, String role) {
        this.servletPath = servletPath;
        this.action = action;
        setDomains(role);
    }
    public static Domain getDomain(String servletPath, String action) {
        return new Domain(servletPath, action);
    }

    public static Domain getDomain(String servletPath, String action, String role) {
        return new Domain(servletPath, action, role);
    }

    private void setDomains() {
        domainPages = DomainPagesSets.getGuestPages();
        domainActions = DomainActionsSets.getGuestActions();
    }

    private void setDomains(String role) {
        switch (role) {
            case "SYSTEM_USER": domainPages = DomainPagesSets.getSystemUserPages();
                          domainActions = DomainActionsSets.getSystemUserActions();
                          break;
            case "ADMIN": domainPages = DomainPagesSets.getAdminPages();
                        domainActions = DomainActionsSets.getAdminActions();
                        break;
            default: domainPages = DomainPagesSets.getGuestPages();
                     domainActions = DomainActionsSets.getGuestActions();
        }
    }

    public boolean checkAccess() {
        return !checkPages() || !checkActions();
    }

    private boolean checkPages() {
        if (servletPath != null) {
            return domainPages.contains(servletPath.substring(1));
        }
        return true;
    }

    private boolean checkActions() {
        if (action != null) {
            return domainActions.contains(action);
        }
        return true;
    }
}