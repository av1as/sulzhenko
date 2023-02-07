package com.sulzhenko.model.services.implementation;

import com.sulzhenko.DTO.ReportDTO;
import com.sulzhenko.DTO.UserActivityDTO;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.DAO.UserActivityDAO;
import com.sulzhenko.model.DAO.implementation.UserActivityDAOImpl;
import com.sulzhenko.model.services.ReportService;
import com.sulzhenko.model.services.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ReportService class for interaction between controller and DAO
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class ReportServiceImpl implements ReportService {
    private final UserActivityDAO userActivityDAO;
    private static final Logger logger = LogManager.getLogger(ReportServiceImpl.class);
    public ReportServiceImpl(DataSource dataSource) {
        this.userActivityDAO = new UserActivityDAOImpl(dataSource);
    }

    /**
     * Shows report page according to list of records
     * @param activities - list of records to sow
     * @throws DAOException is wrapper for SQLException
     */
    @Override
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

    /**
     * Gets number of records from database
     * @return  number or records
     * @throws ServiceException is wrapper for SQLException
     */
    @Override
    public int getNumberOfRecords() throws ServiceException {
        int number;
        try {
            number = userActivityDAO.getNumberOfRecords();
        } catch (DAOException e){
            logger.fatal(e);
            throw new ServiceException(UNKNOWN_ERROR);
        }
        return number;
    }

    /**
     * Construes and inserts record into report page
     * @param activities - source list of records
     * @param list - output list
     * @param activitiesWithTime - inner list of activities carried out by some user
     * @param i - sequence number of record
     * @param login - user login
     */
    private static void createAndAddElement(List<UserActivityDTO> activities, List<ReportDTO> list,
                                            List<UserActivityDTO> activitiesWithTime, int i,
                                            String login) {
        int numberOfActivities = activities.get(i).getNumberOfActivities();
        int totalAmount = activities.get(i).getTotalAmount();
        ReportDTO element = new ReportDTO(login, activitiesWithTime, numberOfActivities, totalAmount);
        list.add(element);
    }

    /**
     * Auxiliary method to check if record is first on page or in list of some user's records
     * @param activities - source list of records
     * @param i - sequence number of record
     * @return true if record is first, false otherwise
     */
    private static boolean isFirstRecord(List<UserActivityDTO> activities, int i){
        return isFirstOfList(i) || isFirstOfBlock(i, activities);
    }

    /**
     * Auxiliary method to check if record is last on page or in list of some user's records
     * @param activities - source list of records
     * @param i - sequence number of record
     * @return true if record is last, false otherwise
     */
    private static boolean isLastRecord(List<UserActivityDTO> activities, int i){
        return isLastOfList(i, activities) || isLastOfBlock(i, activities);
    }

    /**
     * Auxiliary method to check if record is first on page
     * @param i - sequence number of record
     * @return true if record is first, false otherwise
     */
    private static boolean isFirstOfList(int i){
        return i == 0;
    }

    /**
     * Auxiliary method to check if record is last on page
     * @param i - sequence number of record
     * @return true if record is last, false otherwise
     */
    private static boolean isLastOfList(int i, List<UserActivityDTO> list){
        return i == list.size() - 1;
    }

    /**
     * Auxiliary method to check if record is first in list of some user's records
     * @param i - sequence number of record
     * @return true if record is first, false otherwise
     */
    private static boolean isFirstOfBlock(int i, List<UserActivityDTO> list){
        return !Objects.equals(list.get(i).getLogin(), list.get(i - 1).getLogin());
    }

    /**
     * Auxiliary method to check if record is last in list of some user's records
     * @param i - sequence number of record
     * @return true if record is last, false otherwise
     */
    private static boolean isLastOfBlock(int i, List<UserActivityDTO> list){
        return !Objects.equals(list.get(i).getLogin(), list.get(i + 1).getLogin());
    }
}
