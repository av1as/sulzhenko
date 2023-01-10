package com.sulzhenko.model.services;

import com.sulzhenko.model.DTO.ActivityDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.util.List;

public interface ActivityService {
    String buildQuery(HttpServletRequest request);
    int getNumberOfRecords(HttpServletRequest request) throws SQLException;
    List<ActivityDTO> listActivitiesSorted(HttpServletRequest request);

}
