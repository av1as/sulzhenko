package com.sulzhenko.model.DTO;

import java.io.Serializable;
import java.util.List;

public class ReportDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String login;
    private List<UserActivityDTO> activitiesWithTime;
    private int numberOfActivities;
    private int totalTime;
    private String status;

    public ReportDTO(String login, List<UserActivityDTO> activitiesWithTime, int numberOfActivities, int totalTime, String status) {
        this.login = login;
        this.activitiesWithTime = activitiesWithTime;
        this.numberOfActivities = numberOfActivities;
        this.totalTime = totalTime;
        this.status = status;
    }
    public ReportDTO(String login, List<UserActivityDTO> activitiesWithTime, int numberOfActivities, int totalTime) {
        this.login = login;
        this.activitiesWithTime = activitiesWithTime;
        this.numberOfActivities = numberOfActivities;
        this.totalTime = totalTime;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<UserActivityDTO> getActivitiesWithTime() {
        return activitiesWithTime;
    }

    public void setActivitiesWithTime(List<UserActivityDTO> activitiesWithTime) {
        this.activitiesWithTime = activitiesWithTime;
    }

    public int getNumberOfActivities() {
        return numberOfActivities;
    }

    public void setNumberOfActivities(int numberOfActivities) {
        this.numberOfActivities = numberOfActivities;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
