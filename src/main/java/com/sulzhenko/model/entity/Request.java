package com.sulzhenko.model.entity;

import java.io.Serializable;
import java.util.Objects;
/**
 * Request entity class. Matches table 'request' in database.
 * Use Request.builder().withFieldName(fieldValue).build() to create an instance
 * @author Artem Sulzhenko
 * @version 1.0
 */

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String login;
    private String activityName;
    private String actionToDo;
    private String description;

    private Request() {
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getActionToDo() {
        return actionToDo;
    }

    public String getDescription() {
        return description;
    }

    public String getActivityName(){return activityName;}

    /**
     * This inner class uses Builder pattern instead of setters
     * with methods like "withXXX" (where XXX - some field of Request class)
     */
    public static class Builder {
        private final Request request;
        public Builder(){
            request = new Request();
        }
        public Builder withId(Long id) {
            request.id = id;
            return this;
        }
        public Builder withLogin(String login) {
            request.login = login;
            return this;
        }
        public Builder withActivityName(String activityName) {
            request.activityName = activityName;
            return this;
        }
        public Builder withActionToDo(String actionToDo) {
            request.actionToDo = actionToDo;
            return this;
        }
        public Builder withDescription(String description) {
            request.description = description;
            return this;
        }
        public Request build() {
            if(request.id == null) {
                request.id = 0L;
            }
            if (request.login == null || request.activityName == null || request.actionToDo == null) {
                throw new IllegalArgumentException();
            }
            return request;
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return login.equals(request.login) && activityName.equals(request.activityName) && actionToDo.equals(request.actionToDo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, activityName, actionToDo);
    }
}
