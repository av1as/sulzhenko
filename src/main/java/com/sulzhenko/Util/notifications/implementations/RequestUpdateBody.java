package com.sulzhenko.Util.notifications.implementations;

import com.sulzhenko.Util.notifications.Body;
import com.sulzhenko.model.entity.Request;
import com.sulzhenko.model.entity.User;

/**
 * RequestUpdateBody class for creating message text about user request update
 *
 * @author Artem Sulzhenko
 * @version 1.0
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

    /**
     * Forms body of message about user request update
     * @return text of email
     */
    @Override
    public String asText() {
        return String.format("Hello, %s,\nthe request from your account %s to %s activity %s %s.",
                user.getFullName(), user.getLogin(), request.getActionToDo(),
                request.getActivityName(), updateDescription);
    }
}
