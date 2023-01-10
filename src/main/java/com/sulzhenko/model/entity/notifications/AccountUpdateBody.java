package com.sulzhenko.model.entity.notifications;

import com.sulzhenko.model.entity.User;
/**
 * This class describes the main part of notification about account's update
 */
public class AccountUpdateBody implements Body{
    User user;
    String updateDescription;

    public AccountUpdateBody(User user, String updateDescription) {
        this.user = user;
        this.updateDescription = updateDescription;
    }

    @Override
    public String asText() {
        return "Hello, " + user.getFullName() + ",\n" +
                "your account " + user.getLogin() + " was " + updateDescription + ".";
    }
}
