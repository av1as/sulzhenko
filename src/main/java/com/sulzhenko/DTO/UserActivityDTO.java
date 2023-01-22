package com.sulzhenko.DTO;

import java.io.Serializable;

public class UserActivityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String login;
    private String activityName;
    private int activityTime;
    private int numberOfActivities;
    private int totalAmount;
    private String status;
    private String category;

    public UserActivityDTO(String login, String activityName, int activityTime, int numberOfActivities, int totalAmount, String status, String category) {
        this.login = login;
        this.activityName = activityName;
        this.activityTime = activityTime;
        this.numberOfActivities = numberOfActivities;
        this.totalAmount = totalAmount;
        this.status = status;
        this.category = category;
    }

    public UserActivityDTO(String login, int numberOfActivities, int totalAmount) {
        this.login = login;
        this.numberOfActivities = numberOfActivities;
        this.totalAmount = totalAmount;
    }
    public UserActivityDTO(String activityName, int activityTime) {
        this.activityName = activityName;
        this.activityTime = activityTime;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(int activityTime) {
        this.activityTime = activityTime;
    }

    public int getNumberOfActivities() {
        return numberOfActivities;
    }

    public void setNumberOfActivities(int numberOfActivities) {
        this.numberOfActivities = numberOfActivities;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
