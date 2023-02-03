package com.sulzhenko.Util.notifications.implementations;

import com.sulzhenko.Util.notifications.Subject;

/**
 * RequestUpdateSubject class for creating message subject about user request update
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class RequestUpdateSubject implements Subject {

    /**
     * Forms subject of message about user request update
     * @return subject of email
     */
    @Override
    public String asSubject() {
        return "Timekeeping: your request update";
    }
}
