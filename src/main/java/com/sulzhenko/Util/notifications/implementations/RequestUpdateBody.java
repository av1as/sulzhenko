package com.sulzhenko.Util.notifications.implementations;

import com.sulzhenko.Util.notifications.Body;
import com.sulzhenko.model.entity.Request;
import com.sulzhenko.model.entity.User;
/**
 * This class describes the main part of notification about user request update
 */
public class RequestUpdateBody implements Body {
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
        return String.format("Hello, %s,\nthe request from your account %s to %s activity %s %s.",
                user.getFullName(), user.getLogin(), request.getActionToDo(),
                request.getActivityName(), updateDescription);
    }
}
