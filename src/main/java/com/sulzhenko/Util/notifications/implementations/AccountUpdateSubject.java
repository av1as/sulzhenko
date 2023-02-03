package com.sulzhenko.Util.notifications.implementations;

import com.sulzhenko.Util.notifications.Subject;

/**
 * AccountUpdateSubject class for creating message subject about user account update
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class AccountUpdateSubject implements Subject {

    /**
     * Forms subject of message about user account update
     * @return subject of email
     */
    @Override
    public String asSubject() {
        return "Timekeeping: your account update";
    }
}
