package com.sulzhenko.Util.notifications;

import com.sulzhenko.model.entity.Request;
import com.sulzhenko.model.entity.User;

/**
 * This class describes factories for producing different types of notifications
 */
public class NotificationFactories {
    /**
     * This method produces notifications about account's update
     */
    public NotificationFactory accountUpdateFactory (User user){
        return new NotificationFactory() {
            @Override
            public String createSubject() {
                return new AccountUpdateSubject().asSubject();
            }

            @Override
            public String createBody() {
                return new AccountUpdateBody(user).asText();
            }
        };
    }
    /**
     * This method produces notifications about user request update
     */
    public NotificationFactory requestUpdateFactory (User user, Request request, String updateDescription){
        return new NotificationFactory() {
            @Override
            public String createSubject() {
                return new RequestUpdateSubject().asSubject();
            }

            @Override
            public String createBody() {
                return new RequestUpdateBody(user, request, updateDescription).asText();
            }
        };
    }
    /**
     * This method produces notifications about some update in system connected with user
     */
    public NotificationFactory systemUpdateFactory (User user, String updateDescription){

        return new NotificationFactory() {
            @Override
            public String createSubject() {
                return new SystemUpdateSubject().asSubject();
            }

            @Override
            public String createBody() {
                return new SystemUpdateBody(user, updateDescription).asText();
            }
        };
    }
}
