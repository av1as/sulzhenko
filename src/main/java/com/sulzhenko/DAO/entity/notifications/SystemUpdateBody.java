package com.sulzhenko.DAO.entity.notifications;

import com.sulzhenko.DAO.entity.User;

public class SystemUpdateBody implements Body{
    User user;
    String updateDescription;

    public SystemUpdateBody(User user, String updateDescription) {
        this.user = user;
        this.updateDescription = updateDescription;
    }

    @Override
    public String asText() {
        return "Hello, " + user.getFullName() + ",\n"
                + "system update connected with your account " + user.getLogin()
                + " was made: " + updateDescription + ".";
    }
}
