package com.sulzhenko.DAO.entity.notifications;
/**
 * This class describes the topic of notification about user request update
 */
public class RequestUpdateTheme implements Theme{
    @Override
    public String asTheme() {
        return "Timekeeping: your request update";
    }
}
