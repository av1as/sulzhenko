package com.sulzhenko.model.services;

import com.sulzhenko.model.Constants;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.DTO.ActivityDTO;
import com.sulzhenko.DTO.UserActivityDTO;
import com.sulzhenko.DTO.UserDTO;
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
