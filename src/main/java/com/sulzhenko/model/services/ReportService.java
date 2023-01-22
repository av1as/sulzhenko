package com.sulzhenko.model.services;

import com.sulzhenko.model.Constants;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.DTO.ReportDTO;
import com.sulzhenko.DTO.UserActivityDTO;

import java.util.List;

public interface ReportService extends Constants {
    List<ReportDTO> viewReportPage(List<UserActivityDTO> activities);
    int getNumberOfRecords() throws DAOException;

}
