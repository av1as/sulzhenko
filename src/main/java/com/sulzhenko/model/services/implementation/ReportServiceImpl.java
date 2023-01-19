package com.sulzhenko.model.services.implementation;

import com.sulzhenko.model.DTO.*;
import com.sulzhenko.model.services.ReportService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReportServiceImpl implements ReportService {

    public List<ReportDTO> viewReportPage(List<UserActivityDTO> activities) {
        List<ReportDTO> list = new ArrayList<>();
        List<UserActivityDTO> activitiesWithTime = new ArrayList<>();
        for (int i = 0; i < activities.size(); i++) {
            String login = activities.get(i).getLogin();
            String activityName = activities.get(i).getActivityName();
            int activityTime = activities.get(i).getActivityTime();
            if (isFirstRecord(activities, i)) {
                activitiesWithTime = new ArrayList<>();
            }
            activitiesWithTime.add(new UserActivityDTO(activityName, activityTime));
            if (isLastRecord(activities, i)) {
                createAndAddElement(activities, list, activitiesWithTime, i, login);
            }
        }
        return list;
    }

    private static void createAndAddElement(List<UserActivityDTO> activities, List<ReportDTO> list, List<UserActivityDTO> activitiesWithTime, int i, String login) {
        int numberOfActivities = activities.get(i).getNumberOfActivities();
        int totalAmount = activities.get(i).getTotalAmount();
        ReportDTO element = new ReportDTO(login, activitiesWithTime, numberOfActivities,
                totalAmount);
        list.add(element);
    }

    private boolean isFirstRecord(List<UserActivityDTO> activities, int i){
        return isFirstOfList(i) || isFirstOfBlock(i, activities);
    }
    private boolean isLastRecord(List<UserActivityDTO> activities, int i){
        return isLastOfList(i, activities) || isLastOfBlock(i, activities);
    }
    private boolean isFirstOfList(int i){
        return i == 0;
    }
    private boolean isLastOfList(int i, List<UserActivityDTO> list){
        return i == list.size() - 1;
    }
    private boolean isFirstOfBlock(int i, List<UserActivityDTO> list){
        return !Objects.equals(list.get(i).getLogin(), list.get(i - 1).getLogin());
    }
    private boolean isLastOfBlock(int i, List<UserActivityDTO> list){
        return !Objects.equals(list.get(i).getLogin(), list.get(i + 1).getLogin());
    }
}
