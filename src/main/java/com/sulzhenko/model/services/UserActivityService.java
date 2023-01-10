package com.sulzhenko.model.services;

import com.sulzhenko.model.DTO.UserActivityDTO;
import com.sulzhenko.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserActivityService {
    int getNumberOfRecords();
    List<UserActivityDTO> listAllUserActivitiesSorted(HttpServletRequest request);
    List<UserActivityDTO> listUserActivitiesBriefSorted(HttpServletRequest request);
    List<UserActivityDTO> listUserActivitiesSorted(HttpServletRequest request, User user);
}
