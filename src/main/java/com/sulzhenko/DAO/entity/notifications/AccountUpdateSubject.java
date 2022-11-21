package com.sulzhenko.DAO.entity.notifications;
/**
 * This class describes the topic of notification about account's update
 */
public class AccountUpdateSubject implements Subject {
    @Override
    public String asSubject() {
        return "Timekeeping: your account update";
    }
}
