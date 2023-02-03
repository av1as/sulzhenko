package com.sulzhenko.model.services;

import com.sulzhenko.model.Constants;
import com.sulzhenko.DTO.ActivityDTO;
import com.sulzhenko.model.entity.Activity;
import java.util.List;

public interface ActivityService extends Constants {
    Activity getActivity(String activityName) throws ServiceException;
    void addActivity(String name, String categoryName) throws ServiceException;
    void updateActivity(String oldName, String newName, String newCategoryName) throws ServiceException;
    void deleteActivity(String name) throws ServiceException;
    int getNumberOfRecords(String filter) throws ServiceException;
    List<ActivityDTO> listActivitiesSorted(String filter, String order, String parameter, String page)
            throws ServiceException;
    boolean isNameAvailable(String name) throws ServiceException;
}
