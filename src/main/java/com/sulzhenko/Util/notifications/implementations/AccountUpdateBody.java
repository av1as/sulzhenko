package com.sulzhenko.Util.notifications.implementations;

import com.sulzhenko.Util.notifications.Body;
import com.sulzhenko.model.entity.User;

/**
 * AccountUpdateBody class for creating message text about user account update
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class AccountUpdateBody implements Body {
    User user;
    public AccountUpdateBody(User user) {
        this.user = user;
    }

    /**
     * Forms body of message about user account update
     * @return text of email
     */
    @Override
    public String asText() {
        return String.format("Hello, %s,\nyour account %s was updated.", user.getFullName(), user.getLogin());
    }
}
