package com.sulzhenko.model.services;

import com.sulzhenko.model.DTO.ReportDTO;
import com.sulzhenko.model.DTO.UserActivityDTO;

import java.util.List;

public interface ReportService {
    List<ReportDTO> viewReportPage(List<UserActivityDTO> activities);

}
