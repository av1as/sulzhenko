package com.sulzhenko.DAO.entity.notifications;
/**
 * This class describes the topic of notification about user request update
 */
public class RequestUpdateSubject implements Subject {
    @Override
    public String asSubject() {
        return "Timekeeping: your request update";
    }
}
