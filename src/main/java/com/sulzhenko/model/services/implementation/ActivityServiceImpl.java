package com.sulzhenko.model.services.implementation;

import com.sulzhenko.Util.notifications.Mailer;
import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.DAO.implementation.*;
import com.sulzhenko.DTO.ActivityDTO;
import com.sulzhenko.model.entity.*;
import com.sulzhenko.Util.notifications.NotificationFactories;
import com.sulzhenko.Util.notifications.NotificationFactory;
import com.sulzhenko.model.services.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ActivityServiceImpl implements ActivityService {
    private final DataSource dataSource;
    ActivityDAO activityDAO;
    CategoryService categoryService;

    public ActivityServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.activityDAO = new ActivityDAOImpl(dataSource);
        this.categoryService = new CategoryServiceImpl(dataSource);
    }

    private static final String COMMON_PART = "SELECT activity.activity_name, \n" +
            "COUNT(user_activity.activity_id) as quantity, \n" +
            "category_of_activity.category_name \n" +
            "FROM activity\n" +
            "LEFT JOIN user_activity\n" +
            "ON activity.activity_id = user_activity.activity_id\n" +
            "INNER JOIN category_of_activity\n" +
            "ON category_of_activity.category_id = activity.category_id\n";
    private static final Logger logger = LogManager.getLogger(ActivityServiceImpl.class);
    public Activity getActivity(String activityName){
            return activityDAO.getByName(activityName);
    }
    public void addActivity(String name, String categoryName){
        if(!categoryService.isCategoryNameUnique(categoryName) && isNameAvailable(name)) {
            Category category = categoryService.getCategory(categoryName);
            Activity activity = new Activity.Builder()
                    .withName(name)
                    .withCategory(category)
                    .build();
            try {
                activityDAO.save(activity);
            } catch (DAOException e) {
                logger.warn(e.getMessage());
                throw new ServiceException(e);
            }
        } else if(!isNameAvailable(name)) throw new ServiceException(WRONG_ACTIVITY);
        else if(categoryService.isCategoryNameUnique(categoryName)) throw new ServiceException(WRONG_CATEGORY);
    }
    public void updateActivity(String oldName, String newName, String newCategoryName){
        if(isNameAvailable(oldName) || ((!Objects.equals(newName, oldName)) && !isNameAvailable(newName))){
            throw new ServiceException(WRONG_ACTIVITY);
        } else if(categoryService.isCategoryNameUnique(newCategoryName)){
            throw new ServiceException(WRONG_CATEGORY);
        } else{
            Activity activity = activityDAO.getByName(oldName);
            String[] param = {newName, newCategoryName};
            List<User> connectedUsers = getConnectedUsersWithNotification(activity);
            String description = "activity " + "\"" + oldName + "\" "
                    + "now has name " + "\"" + param[0] + "\" "
                    + "and category " + "\"" + param[1] + "\"";
            try{
                activityDAO.update(activity, param);
                notifyAboutUpdate(connectedUsers, description);
            } catch (DAOException e){
                logger.warn(e.getMessage());
                throw new ServiceException(e.getMessage());
            }
        }
    }
    public void deleteActivity(String name){
        if(!isNameAvailable(name)) {
            List<User> connectedUsers = getConnectedUsersWithNotification(getActivity(name));
            String description = "activity " + "\"" + name + "\" has been deleted";
            try{
                activityDAO.delete(activityDAO.getByName(name));
                notifyAboutUpdate(connectedUsers, description);
            } catch(DAOException e){
                logger.warn(e.getMessage());
                throw new ServiceException(e.getMessage());
            }
        } else {
            logger.info("this activity doesn't exist: {}", name);
            throw new ServiceException(WRONG_ACTIVITY);
        }
    }
    private String buildQuery(HttpServletRequest request){
        String order = getOrder(request);
        String parameter = getParam(request);
        String filter = request.getParameter(FILTER);
        int page = 1;
        if(request.getParameter(PAGE) != null)
            page = Integer.parseInt(request.getParameter(PAGE));
        int records = 5;
        int offset = (page - 1) * records;
        return COMMON_PART +
                applyFilter(filter) +
                applySorting(ACTIVITY_NAME) +
                applyOrder(parameter, order, offset, records);
    }

    private static String getParam(HttpServletRequest request) {
        String parameter = request.getParameter(PARAMETER);
        if(Objects.equals(parameter, NUMBER_OF_USERS)) parameter = QUANTITY;
        else if(Objects.equals(parameter, NAME_OF_ACTIVITY)) parameter = ACTIVITY_NAME;
        else if(Objects.equals(parameter, CATEGORY_OF_ACTIVITY)) parameter = CATEGORY_NAME;
        return parameter;
    }

    private static String getOrder(HttpServletRequest request) {
        String order;
        if(Objects.equals(request.getParameter(ORDER), DESCENDING)) order = DESC;
        else order = ASC;
        return order;
    }

    private String getTotalRecords(HttpServletRequest request){
        String filter = request.getParameter(FILTER);
        String query = "SELECT COUNT(activity.activity_name)\n" +
                    "FROM activity\n" +
                    "INNER JOIN category_of_activity\n" +
                    "ON activity.category_id = category_of_activity.category_id\n";
        if(!Objects.equals(filter, ALL_CATEGORIES)) query += "WHERE category_name = '" + filter + "'";
        return query;
    }
    public int getNumberOfRecords(HttpServletRequest request) throws DAOException{
        int number = 0;
        try(Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(getTotalRecords(request))){
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                number = rs.getInt(1);
            }
        } catch (SQLException e){
            throw new ServiceException(UNKNOWN_ERROR);
        }
        return number;
    }
    public List<ActivityDTO> listActivitiesSorted(HttpServletRequest request){
        List<ActivityDTO> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(buildQuery(request))) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ActivityDTO activity = getActivityDTOWithFields(rs);
                list.add(activity);
            }
        } catch (SQLException e) {
            throw new ServiceException(UNKNOWN_ERROR);
        }
        return list;
    }
    private List<User> getConnectedUsersWithNotification(Activity activity){
        UserDAO userDAOImpl = new UserDAOImpl(dataSource);
        return userDAOImpl.getList(activity.getName(), SQLQueries.UserQueries.FIND_CONNECTED_USERS_WITH_NOTIFICATION);
    }
    private void notifyAboutUpdate(List<User> connectedUsers, String description){
        for(User user: connectedUsers){
            NotificationFactory factory = new NotificationFactories().systemUpdateFactory(user, description);
            String subject = factory.createSubject();
            String body = factory.createBody();
            Mailer.send(user.getEmail(),subject,body);
        }
    }
    private String applyFilter(String filter){
        return Objects.equals(filter, ALL_CATEGORIES) ? "": "WHERE category_name = '" + filter + "'\n";
    }
    private String applySorting(String sortParameter){
        return "GROUP BY " + sortParameter + " \n";
    }
    private String applyOrder(String parameter, String order, int offset, int number){
        return String.format("ORDER BY %s %s LIMIT %d, %d", parameter, order, offset, number);
    }
    private static ActivityDTO getActivityDTOWithFields(ResultSet rs) throws SQLException {
        return new ActivityDTO(rs.getString(1),
                rs.getString(3), rs.getInt(2));
    }
    @Override
    public boolean isNameAvailable(String name){
        return activityDAO.getByName(name) == null;
    }
}
