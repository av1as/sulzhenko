package com.sulzhenko.Util.notifications.implementations;

import com.sulzhenko.Util.notifications.Body;
import com.sulzhenko.model.entity.User;

/**
 * SystemUpdateBody class for creating message text about system update
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class SystemUpdateBody implements Body {
    private final User user;
    private final String updateDescription;

    public SystemUpdateBody(User user, String updateDescription) {
        this.user = user;
        this.updateDescription = updateDescription;
    }

    /**
     * Forms body of message about system update
     * @return text of email
     */
    @Override
    public String asText() {
        return String.format("Hello, %s,\nsystem update connected with your account %s was made: %s.",
                user.getFullName(), user.getLogin(), updateDescription);
    }
}
