package com.sulzhenko.Util.notifications.implementations;

import com.sulzhenko.Util.notifications.Body;
import com.sulzhenko.model.entity.User;
/**
 * This class describes the main part of notification about account's update
 */
public class AccountUpdateBody implements Body {
    User user;
    public AccountUpdateBody(User user) {
        this.user = user;
    }
    @Override
    public String asText() {
        return String.format("Hello, %s,\nyour account %s was updated.", user.getFullName(), user.getLogin());
    }
}
