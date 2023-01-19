package com.sulzhenko.model.services;

import com.sulzhenko.model.Constants;
import com.sulzhenko.model.DTO.ReportDTO;
import com.sulzhenko.model.DTO.UserActivityDTO;

import java.util.List;

public interface ReportService extends Constants {
    List<ReportDTO> viewReportPage(List<UserActivityDTO> activities);

}
