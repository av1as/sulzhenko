package com.sulzhenko.DAO.entity.notifications;

import com.sulzhenko.DAO.entity.User;

public interface NotificationFactory {
    String createTheme();
    String createBody();
}
