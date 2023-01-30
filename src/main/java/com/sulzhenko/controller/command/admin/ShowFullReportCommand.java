package com.sulzhenko.controller.command.admin;

import com.sulzhenko.DTO.ReportDTO;
import com.sulzhenko.DTO.UserActivityDTO;
import com.sulzhenko.Util.PdfMakerUtil;
import com.sulzhenko.controller.command.Command;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.model.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

import static com.sulzhenko.controller.ApplicationContext.getApplicationContext;
import static com.sulzhenko.Util.PaginationUtil.paginate;

public class ShowFullReportCommand implements Command, Constants, Path {
    UserActivityService userActivityService = getApplicationContext().getUserActivityService();
    ReportService reportService = getApplicationContext().getReportService();
    private static final Logger logger = LogManager.getLogger(ShowFullReportCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        setPage(request);
        List<UserActivityDTO> userActivities = userActivityService.listAllUserActivitiesSorted(request.getParameter(PAGE));
        List<UserActivityDTO> fullUserActivities = userActivityService.listFullPdf();
        List<ReportDTO> report = reportService.viewReportPage(userActivities);
        List<ReportDTO> fullReport = reportService.viewReportPage(fullUserActivities);
        int noOfRecords;
        try {
            createPdf(request, report, fullReport);
            noOfRecords = reportService.getNumberOfRecords();
        } catch (ServiceException e){
            logger.warn(e);
            session.setAttribute(ERROR, e.getMessage());
            return PAGE_ERROR;
        }
        request.setAttribute(REPORT, report);
        int recordsPerPage = 5;
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        request.setAttribute(NO_OF_PAGES, noOfPages);
        paginate(noOfRecords, request);
        request.setAttribute(QUERY, SHOW_FULL_REPORT);
        return PAGE_FULL_REPORT;
    }

    private static void createPdf(HttpServletRequest request, List<ReportDTO> report, List<ReportDTO> fullReport) {
        String locale;
        if(request.getSession().getAttribute(LOCALE) != null){
            locale = (String) request.getSession().getAttribute(LOCALE);
        } else {
            locale = EN;
        }
        PdfMakerUtil pdfMakerUtil = getApplicationContext().getPdfUtil();
        pdfMakerUtil.getReportPDF(locale, fullReport, REPORT, null, null);
        pdfMakerUtil.getReportPDF(locale, report, PAGE, PAGE, request.getParameter(PAGE));
    }
    private static void setPage(HttpServletRequest request) {
        int page = 1;
        if(request.getParameter(PAGE) != null)
            page = Integer.parseInt(request.getParameter(PAGE));
        request.setAttribute(CURRENT_PAGE, page);
    }
}


