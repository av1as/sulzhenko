package com.sulzhenko.Util.notifications.implementations;

import com.sulzhenko.Util.notifications.Subject;

/**
 * SystemUpdateSubject class for creating message subject about system update
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class SystemUpdateSubject implements Subject {
    /**
     * Forms subject of message about system update
     * @return subject of email
     */
    @Override
    public String asSubject() {
        return "Timekeeping: system update";
    }
}
