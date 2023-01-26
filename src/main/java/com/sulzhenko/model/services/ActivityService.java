package com.sulzhenko.model.services;

import com.sulzhenko.model.Constants;
import com.sulzhenko.DTO.ActivityDTO;
import com.sulzhenko.model.entity.Activity;

import java.sql.SQLException;
import java.util.List;

public interface ActivityService extends Constants {
    Activity getActivity(String activityName);
    void addActivity(String name, String categoryName);
    void updateActivity(String oldName, String newName, String newCategoryName);
    void deleteActivity(String name);
    int getNumberOfRecords(String filter) throws SQLException;
    List<ActivityDTO> listActivitiesSorted(String filter, String order, String parameter, String page);
    boolean isNameAvailable(String name);
}
