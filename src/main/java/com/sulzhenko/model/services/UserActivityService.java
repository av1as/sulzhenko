package com.sulzhenko.model.services;

import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.DTO.UserActivityDTO;
import com.sulzhenko.model.entity.Activity;
import com.sulzhenko.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserActivityService {
    void setAmount(User user, Activity activity, int amount) throws DAOException;
    List<Activity> allAvailableActivities(User u);
    int getNumberOfRecords();
    List<UserActivityDTO> listAllUserActivitiesSorted(HttpServletRequest request);
    List<UserActivityDTO> listUserActivitiesBriefSorted(HttpServletRequest request);
    List<UserActivityDTO> listUserActivitiesSorted(HttpServletRequest request, User user);
}
