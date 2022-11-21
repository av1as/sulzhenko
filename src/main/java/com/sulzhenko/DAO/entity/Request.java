package com.sulzhenko.DAO.entity;

import java.io.Serializable;
import java.util.Objects;
/**
 * This class describes request entity
 */

public class Request implements Serializable {
    private Integer id;
    private String login;
    private String activityName;
    private String actionToDo;
    private String description;
//    private String status;

    public Request() {
    }

    public Request(Integer id, String login, String activityName, String actionToDo, String description) {
        this.id = id;
        this.login = login;
        this.activityName = activityName;
        this.actionToDo = actionToDo;
        this.description = description;
//        this.status = status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
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

//    public String getStatus() {
//        return status;
//    }
//    public String getActionName(){return actionToDo;}
    public String getActivityName(){return activityName;}

    public static Builder builder() {
        return new Builder();
    }
    /**
     * This inner class uses Builder pattern instead of setters
     * with methods like "withXXX" (where XXX - some field of Request class)
     */
    public static class Builder {
        private Integer id;
        private String login;
        private String activityName;
        private String actionToDo;
        private String description;
//        private String status;
        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }
        public Builder withLogin(String login) {
            this.login = login;
            return this;
        }
        public Builder withActivityName(String activityName) {
            this.activityName = activityName;
            return this;
        }
        public Builder withActionToDo(String actionToDo) {
            this.actionToDo = actionToDo;
            return this;
        }
        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }
//        public Builder withStatus(String status) {
//            this.status = status;
//            return this;
//        }
        public Request build() {
            if(id == null) {
                id = 0;
            }
            if (login == null || activityName == null || actionToDo == null) {
                throw new IllegalArgumentException();
            }
            return new Request(id, login, activityName, actionToDo, description);
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

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", activityName='" + activityName + '\'' +
                ", actionToDo='" + actionToDo + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
