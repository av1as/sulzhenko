package com.sulzhenko.Util.notifications.implementations;

import com.sulzhenko.Util.notifications.Subject;

public class RecoverPasswordSubject implements Subject {
    @Override
    public String asSubject() {
        return "Timekeeping: reset password";
    }
}
