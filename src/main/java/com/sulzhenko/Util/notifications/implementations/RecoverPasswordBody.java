package com.sulzhenko.Util.notifications.implementations;

import com.sulzhenko.Util.notifications.Body;
import com.sulzhenko.model.entity.User;

public class RecoverPasswordBody implements Body {
    User user;
    String temporaryPassword;
    public RecoverPasswordBody(User user, String temporaryPassword) {
        this.user = user;
        this.temporaryPassword = temporaryPassword;
    }
    @Override
    public String asText() {
        return String.format("Hello, %s,\nsomeone reset password for your account %s.\n" +
                "Here is your new password: %s.\n" +
                "Note: for security reason, you must change your password after logging in.",
                user.getFullName(), user.getLogin(), temporaryPassword);
    }
}
