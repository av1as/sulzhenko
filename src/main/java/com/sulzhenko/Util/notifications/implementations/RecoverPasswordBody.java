package com.sulzhenko.Util.notifications.implementations;

import com.sulzhenko.Util.notifications.Body;
import com.sulzhenko.model.entity.User;

/**
 * RecoverPasswordBody class for creating message text about user password recovering
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class RecoverPasswordBody implements Body {
    private final User user;
    private final String temporaryPassword;
    public RecoverPasswordBody(User user, String temporaryPassword) {
        this.user = user;
        this.temporaryPassword = temporaryPassword;
    }
    /**
     * Forms body of message about password recovering
     * @return text of email
     */
    @Override
    public String asText() {
        return String.format("Hello, %s,\nsomeone reset password for your account %s.\n" +
                "Here is your new password: %s.\n" +
                "Note: for security reason, you must change your password after logging in.",
                user.getFullName(), user.getLogin(), temporaryPassword);
    }
}
