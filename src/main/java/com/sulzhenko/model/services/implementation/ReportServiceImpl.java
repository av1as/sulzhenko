package com.sulzhenko.model.services.implementation;
import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.DAO.implementation.*;
import com.sulzhenko.model.DTO.*;
import com.sulzhenko.model.services.ReportService;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReportServiceImpl implements ReportService {
    private final DataSource dataSource;
    UserDAO userDAO;
    ActivityDAO activityDAO;

    public ReportServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.userDAO = new UserDAOImpl(dataSource);
        this.activityDAO = new ActivityDAOImpl(dataSource);
    }

    public List<ReportDTO> viewReportPage(List<UserActivityDTO> activities){
        List<ReportDTO> list = new ArrayList<>();
        List<UserActivityDTO> activitiesWithTime = new ArrayList<>();
        for(int i = 0; i < activities.size(); i++){
            if(((isFirstOfList(i) || isFirstOfBlock(i, activities)) && isLastOfList(i, activities)) || ((isFirstOfList(i) || isFirstOfBlock(i, activities)) && isLastOfBlock(i, activities))){
                activitiesWithTime = new ArrayList<>();
                String login = activities.get(i).getLogin();
                String activityName = activities.get(i).getActivityName();
                int activityTime = activities.get(i).getActivityTime();
                String status = new UserActivityDAOImpl(dataSource).defineStatus(userDAO.getByLogin(login).orElse(null), activityDAO.getByName(activityName));
                activitiesWithTime.add(new UserActivityDTO(activityName, activityTime, status));
                int numberOfActivities = activities.get(i).getNumberOfActivities();
                int totalAmount = activities.get(i).getTotalAmount();

                ReportDTO element = new ReportDTO(login, activitiesWithTime, numberOfActivities, totalAmount, status);
                list.add(element);
            } else if(isFirstOfList(i) || isFirstOfBlock(i, activities)){
                activitiesWithTime = new ArrayList<>();
                String activityName = activities.get(i).getActivityName();
                int activityTime = activities.get(i).getActivityTime();
                String login = activities.get(i).getLogin();
                String status = new UserActivityDAOImpl(dataSource).defineStatus(userDAO.getByLogin(login).orElse(null), activityDAO.getByName(activityName));
                activitiesWithTime.add(new UserActivityDTO(activityName, activityTime, status));
            } else if(isLastOfList(i, activities) || isLastOfBlock(i, activities)){
                String login = activities.get(i).getLogin();
                String activityName = activities.get(i).getActivityName();
                int activityTime = activities.get(i).getActivityTime();
                String status = new UserActivityDAOImpl(dataSource).defineStatus(userDAO.getByLogin(login).orElse(null), activityDAO.getByName(activityName));
                activitiesWithTime.add(new UserActivityDTO(activityName, activityTime, status));
                int numberOfActivities = activities.get(i).getNumberOfActivities();
                int totalAmount = activities.get(i).getTotalAmount();
                ReportDTO element = new ReportDTO(login, activitiesWithTime, numberOfActivities, totalAmount, status);
                list.add(element);
            } else{
                String activityName = activities.get(i).getActivityName();
                int activityTime = activities.get(i).getActivityTime();
                String login = activities.get(i).getLogin();
                String status = new UserActivityDAOImpl(dataSource).defineStatus(userDAO.getByLogin(login).orElse(null), activityDAO.getByName(activityName));
                activitiesWithTime.add(new UserActivityDTO(activityName, activityTime, status));
            }
        }
        return list;
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
