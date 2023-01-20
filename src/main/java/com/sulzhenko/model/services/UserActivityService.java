package com.sulzhenko.model.services;

import com.sulzhenko.model.Constants;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.DTO.ActivityDTO;
import com.sulzhenko.model.DTO.UserActivityDTO;
import com.sulzhenko.model.DTO.UserDTO;
import com.sulzhenko.model.entity.Activity;
import com.sulzhenko.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserActivityService extends Constants {
    void setAmount(UserDTO userDTO, ActivityDTO activityDTO, int amount) throws DAOException;
    List<ActivityDTO> allAvailableActivities(UserDTO u);
    int getNumberOfRecords(String login);
    List<UserActivityDTO> listAllUserActivitiesSorted(HttpServletRequest request);
    List<UserActivityDTO> listUserActivitiesBriefSorted(HttpServletRequest request);
    List<UserActivityDTO> listUserActivitiesSorted(HttpServletRequest request, UserDTO userDTO);
}
