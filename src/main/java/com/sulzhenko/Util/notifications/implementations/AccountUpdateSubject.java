package com.sulzhenko.Util.notifications.implementations;

import com.sulzhenko.Util.notifications.Subject;

/**
 * This class describes the topic of notification about account's update
 */
public class AccountUpdateSubject implements Subject {
    @Override
    public String asSubject() {
        return "Timekeeping: your account update";
    }
}
