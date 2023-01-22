package com.sulzhenko.Util.notifications;

import com.sulzhenko.model.entity.User;
/**
 * This class describes the main part of notification about account's update
 */
public class AccountUpdateBody implements Body{
    User user;
    public AccountUpdateBody(User user) {
        this.user = user;
    }
    @Override
    public String asText() {
        return "Hello, " + user.getFullName() + ",\n" +
                "your account " + user.getLogin() + " was updated.";
    }
}
