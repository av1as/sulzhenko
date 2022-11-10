package com.sulzhenko.DAO.entity;

import java.io.Serializable;
import java.util.Objects;
/**
 * This class describes request entity
 */

public class Request implements Serializable {
    private Integer id;
    private Integer account;
    private Integer activityId;
    private Integer actionToDoId;
    private String description;
    private String status;

    public Request() {
    }

    public Request(Integer id, Integer account, Integer activityId, Integer actionToDoId, String description, String status) {
        this.id = id;
        this.account = account;
        this.activityId = activityId;
        this.actionToDoId = actionToDoId;
        this.description = description;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public Integer getAccount() {
        return account;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public Integer getActionToDoId() {
        return actionToDoId;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public static Builder builder() {
        return new Builder();
    }
    /**
     * This inner class uses Builder pattern instead of setters
     * with methods like "withXXX" (where XXX - some field of Request class)
     */
    public static class Builder {
        private Integer id;
        private Integer account;
        private Integer activityId;
        private Integer actionToDoId;
        private String description;
        private String status;
        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }
        public Builder withAccount(Integer account) {
            this.account = account;
            return this;
        }
        public Builder withActivityId(Integer activityId) {
            this.activityId = activityId;
            return this;
        }
        public Builder withActionToDoId(Integer actionToDoId) {
            this.actionToDoId = actionToDoId;
            return this;
        }
        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }
        public Builder withStatus(String status) {
            this.status = status;
            return this;
        }
        public Request build() {
            if(id == null) {
                id = 0;
            }
            if (account == null || activityId == null || actionToDoId == null || status == null) {
                throw new IllegalArgumentException();
            }
            return new Request(id, account, activityId, actionToDoId, description, status);
        }
    }
        @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return account.equals(request.account) && activityId.equals(request.activityId) && actionToDoId.equals(request.actionToDoId) && status.equals(request.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, activityId, actionToDoId, status);
    }
}
