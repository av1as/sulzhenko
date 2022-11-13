package com.sulzhenko.DAO.entity.notifications;
/**
 * This class describes the topic of notification about some update in system connected with user
 */
public class SystemUpdateTheme implements Theme{
    @Override
    public String asTheme() {
        return "Timekeeping: system update";
    }
}
