package com.sulzhenko.model.services;

import com.sulzhenko.model.Constants;
import com.sulzhenko.DTO.ActivityDTO;
import com.sulzhenko.DTO.UserActivityDTO;
import com.sulzhenko.DTO.UserDTO;
import com.sulzhenko.model.entity.Activity;
import com.sulzhenko.model.entity.User;
import java.util.List;

public interface UserActivityService extends Constants {
    void setAmount(String login, String activityName, int amount) throws ServiceException;
    List<ActivityDTO> allAvailableActivities(UserDTO u);
    int getNumberOfRecords(String login);
    List<UserActivityDTO> listAllUserActivitiesSorted(String page) throws ServiceException;
    List<UserActivityDTO> listUserActivitiesSorted(String page, UserDTO userDTO) throws ServiceException;
    List<UserActivityDTO> listFullPdf() throws ServiceException;
    boolean isRequestToRemoveExists(User user, Activity activity);
    boolean isRequestToAddExists(User user, Activity activity);
}
