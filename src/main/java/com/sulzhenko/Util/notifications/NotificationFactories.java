package com.sulzhenko.Util.notifications;

import com.sulzhenko.Util.notifications.implementations.*;
import com.sulzhenko.model.entity.Request;
import com.sulzhenko.model.entity.User;

/**
 * NotificationFactories class for creating factories which make emails
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class NotificationFactories {
    /**
     * Creates factory which produces notifications about account's update
     * @param user - addressee
     * @return NotificationFactory object which creates emails about user account update
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
     * Creates factory which produces notifications about request's update
     * @param user - addressee
     * @param request - updated request
     * @param updateDescription - some description to form text of email
     * @return NotificationFactory object which creates emails about user request update
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
     * Creates factory which produces notifications about system update
     * @param user - addressee
     * @param updateDescription - some description to form text of email
     * @return NotificationFactory object which creates emails about system update
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

    /**
     * Creates factory which produces notifications about recovering password
     * @param user - addressee
     * @param temporaryPassword - temporary password to access account
     * @return NotificationFactory object which creates emails about recovered password
     */
    public NotificationFactory recoverPasswordFactory (User user, String temporaryPassword){
        return new NotificationFactory() {
            @Override
            public String createSubject() {
                return new RecoverPasswordSubject().asSubject();
            }

            @Override
            public String createBody() {
                return new RecoverPasswordBody(user, temporaryPassword).asText();
            }
        };
    }
}
