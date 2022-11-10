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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getActionToDoId() {
        return actionToDoId;
    }

    public void setActionToDoId(Integer actionToDoId) {
        this.actionToDoId = actionToDoId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
