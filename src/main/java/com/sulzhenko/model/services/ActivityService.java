package com.sulzhenko.model.services;

import com.sulzhenko.model.Constants;
import com.sulzhenko.DTO.ActivityDTO;
import com.sulzhenko.model.entity.Activity;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.util.List;

public interface ActivityService extends Constants {
    Activity getActivity(String activityName);
    void addActivity(String name, String categoryName);
    void updateActivity(String oldName, String newName, String newCategoryName);
    void deleteActivity(String name);
    int getNumberOfRecords(HttpServletRequest request) throws SQLException;
    List<ActivityDTO> listActivitiesSorted(HttpServletRequest request);
    boolean isNameAvailable(String name);
}
