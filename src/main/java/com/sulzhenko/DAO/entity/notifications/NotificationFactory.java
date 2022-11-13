package com.sulzhenko.DAO.entity.notifications;

import com.sulzhenko.DAO.entity.User;
/**
 * This interface describes a factory that produce some type of notifications
 */
public interface NotificationFactory {
    String createTheme();
    String createBody();
}
