package com.sulzhenko.model.entity.notifications;

/**
 * This interface describes a factory that produce some type of notifications
 */
public interface NotificationFactory {
    String createSubject();
    String createBody();
}
