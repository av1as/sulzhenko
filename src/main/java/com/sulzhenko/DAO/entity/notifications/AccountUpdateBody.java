package com.sulzhenko.DAO.entity.notifications;

import com.sulzhenko.DAO.entity.User;

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
