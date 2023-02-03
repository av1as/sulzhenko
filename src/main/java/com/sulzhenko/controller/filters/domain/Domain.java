package com.sulzhenko.controller.filters.domain;

import java.util.Set;

/**
 * Checks if user can access page or use action
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class Domain {
    /** contains page name */
    private final String servletPath;
    /** contains action name */
    private final String action;
    /** contains user allowed pages */
    private Set<String> domainPages;
    /** contains user allowed actions */
    private Set<String> domainActions;

    private Domain(String servletPath, String action, String role) {
        this.servletPath = servletPath;
        this.action = action;
        setDomains(role);
    }
    /**
     * Gets Domain for logged user
     * @param servletPath - contains page to access
     * @param action - contains action to call
     * @param role - user's role
     * @return Domain
     */
    public static Domain getDomain(String servletPath, String action, String role) {
        return new Domain(servletPath, action, role);
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
    /**
     * Checks if user is allowed to access page or call action
     * @return false if not allowed
     */
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