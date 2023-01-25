package com.sulzhenko.model.services;

import com.sulzhenko.model.Constants;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.DTO.ActivityDTO;
import com.sulzhenko.DTO.UserActivityDTO;
import com.sulzhenko.DTO.UserDTO;
import java.util.List;

public interface UserActivityService extends Constants {
    void setAmount(UserDTO userDTO, ActivityDTO activityDTO, int amount) throws DAOException;
    List<ActivityDTO> allAvailableActivities(UserDTO u);
    int getNumberOfRecords(String login);
    List<UserActivityDTO> listAllUserActivitiesSorted(String page);
    List<UserActivityDTO> listUserActivitiesBriefSorted(String page);
    List<UserActivityDTO> listUserActivitiesSorted(String page, UserDTO userDTO);
    List<UserActivityDTO> listFullPdf();
}
