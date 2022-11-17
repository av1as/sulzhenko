package com.sulzhenko.DAO.entity.notifications;

import com.sulzhenko.DAO.entity.Request;
import com.sulzhenko.DAO.entity.User;
/**
 * This class describes the main part of notification about user request update
 */
public class RequestUpdateBody implements Body{
    User user;
    Request request;
    String updateDescription;

    public RequestUpdateBody(User user, Request request, String updateDescription) {
        this.user = user;
        this.request = request;
        this.updateDescription = updateDescription;
    }

    @Override
    public String asText() {
        return "Hello, " + user.getFullName() + ",\n"
                + "the request from your account " + user.getLogin()
                + " to " + request.getActionToDo()
                + " activity " + request.getActivityName()
                + " was " + updateDescription + ".";
    }
}
