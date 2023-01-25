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
        return "Hello, " + user.getFullName() + ",\n" +
                "someone reset password for your account " + user.getLogin() + ".\n" +
                "Here is your new password: " + temporaryPassword + ".\n" +
                "Note: for security reason, you must change your password after logging in.";
    }
}
