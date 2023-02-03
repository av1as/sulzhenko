package com.sulzhenko.DTO;

import java.io.Serializable;
import java.util.List;

/**
 * ReportDTO class. Fields adapted to view.
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */

public class ReportDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String login;
    private final List<UserActivityDTO> activitiesWithTime;
    private final int numberOfActivities;
    private final int totalTime;

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

    public int getNumberOfActivities() {
        return numberOfActivities;
    }

    public int getTotalTime() {
        return totalTime;
    }
}
