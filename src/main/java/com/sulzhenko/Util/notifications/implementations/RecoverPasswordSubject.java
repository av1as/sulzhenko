package com.sulzhenko.Util.notifications.implementations;

import com.sulzhenko.Util.notifications.Subject;

/**
 * RecoverPasswordSubject class for creating message subject about user password recovering
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class RecoverPasswordSubject implements Subject {
    /**
     * Forms subject of message about password recovering
     * @return subject of email
     */
    @Override
    public String asSubject() {
        return "Timekeeping: reset password";
    }
}
