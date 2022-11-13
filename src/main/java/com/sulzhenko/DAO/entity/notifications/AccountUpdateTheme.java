package com.sulzhenko.DAO.entity.notifications;
/**
 * This class describes the topic of notification about account's update
 */
public class AccountUpdateTheme implements Theme{
    @Override
    public String asTheme() {
        return "Timekeeping: your account update";
    }
}
