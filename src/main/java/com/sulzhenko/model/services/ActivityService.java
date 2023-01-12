package com.sulzhenko.model.services;

import com.sulzhenko.model.DTO.ActivityDTO;
import com.sulzhenko.model.entity.Activity;
import com.sulzhenko.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.util.List;

public interface ActivityService {
    Activity getActivity(String activityName);
    void addActivity(String name, String categoryName);
    void updateActivity(String oldName, String newName, String newCategoryName);
//    String buildQuery(HttpServletRequest request);
    void deleteActivity(String name);
    int getNumberOfRecords(HttpServletRequest request) throws SQLException;
    List<ActivityDTO> listActivitiesSorted(HttpServletRequest request);
    boolean isNameAvailable(String name);
    void notifyAboutUpdate(List<User> connectedUsers, String description);
}
