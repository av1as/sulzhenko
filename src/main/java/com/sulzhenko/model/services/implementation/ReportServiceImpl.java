package com.sulzhenko.model.services.implementation;

import com.sulzhenko.DTO.ReportDTO;
import com.sulzhenko.DTO.UserActivityDTO;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.services.ReportService;
import com.sulzhenko.model.services.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReportServiceImpl implements ReportService {
    private final DataSource dataSource;
    private static final Logger logger = LogManager.getLogger(ReportServiceImpl.class);

    public ReportServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<ReportDTO> viewReportPage(List<UserActivityDTO> activities) {
        List<ReportDTO> list = new ArrayList<>();
        List<UserActivityDTO> activitiesWithTime = new ArrayList<>();
        for (int i = 0; i < activities.size(); i++) {
            String login = activities.get(i).getLogin();
            String activityName = activities.get(i).getActivityName();
            int activityTime = activities.get(i).getActivityTime();
            if (isFirstRecord(activities, i)) {
                activitiesWithTime = new ArrayList<>();
            }
            activitiesWithTime.add(new UserActivityDTO(activityName, activityTime));
            if (isLastRecord(activities, i)) {
                createAndAddElement(activities, list, activitiesWithTime, i, login);
            }
        }
        return list;
    }
    public int getNumberOfRecords() throws DAOException {
        int number = 0;
        String numberOfRecordsQuery = "SELECT COUNT(*) FROM user_activity";
        try(Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(numberOfRecordsQuery)){
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                number = rs.getInt(1);
            }
        } catch (SQLException e){
            logger.fatal(e);
            throw new ServiceException(UNKNOWN_ERROR);
        }
        return number;
    }

    private static void createAndAddElement(List<UserActivityDTO> activities, List<ReportDTO> list,
                                            List<UserActivityDTO> activitiesWithTime, int i,
                                            String login) {
        int numberOfActivities = activities.get(i).getNumberOfActivities();
        int totalAmount = activities.get(i).getTotalAmount();
        ReportDTO element = new ReportDTO(login, activitiesWithTime, numberOfActivities, totalAmount);
        list.add(element);
    }

    private boolean isFirstRecord(List<UserActivityDTO> activities, int i){
        return isFirstOfList(i) || isFirstOfBlock(i, activities);
    }
    private boolean isLastRecord(List<UserActivityDTO> activities, int i){
        return isLastOfList(i, activities) || isLastOfBlock(i, activities);
    }
    private boolean isFirstOfList(int i){
        return i == 0;
    }
    private boolean isLastOfList(int i, List<UserActivityDTO> list){
        return i == list.size() - 1;
    }
    private boolean isFirstOfBlock(int i, List<UserActivityDTO> list){
        return !Objects.equals(list.get(i).getLogin(), list.get(i - 1).getLogin());
    }
    private boolean isLastOfBlock(int i, List<UserActivityDTO> list){
        return !Objects.equals(list.get(i).getLogin(), list.get(i + 1).getLogin());
    }
}
