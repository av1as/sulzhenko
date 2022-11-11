package com.sulzhenko.DAO.entity.notifications;

public class RequestUpdateTheme implements Theme{
    @Override
    public String asTheme() {
        return "Timekeeping: your request update";
    }
}
