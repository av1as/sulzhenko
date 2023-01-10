package com.sulzhenko.model.entity.notifications;
/**
 * This class describes the topic of notification about some update in system connected with user
 */
public class SystemUpdateSubject implements Subject {
    @Override
    public String asSubject() {
        return "Timekeeping: system update";
    }
}
