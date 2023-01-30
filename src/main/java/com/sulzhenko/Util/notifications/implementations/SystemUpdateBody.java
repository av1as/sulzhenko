package com.sulzhenko.Util.notifications.implementations;

import com.sulzhenko.Util.notifications.Body;
import com.sulzhenko.model.entity.User;
/**
 * This class describes the main part of notification about some system update connected with user
 */
public class SystemUpdateBody implements Body {
    User user;
    String updateDescription;

    public SystemUpdateBody(User user, String updateDescription) {
        this.user = user;
        this.updateDescription = updateDescription;
    }

    @Override
    public String asText() {
        return String.format("Hello, %s,\nsystem update connected with your account %s was made: %s.",
                user.getFullName(), user.getLogin(), updateDescription);
    }
}
