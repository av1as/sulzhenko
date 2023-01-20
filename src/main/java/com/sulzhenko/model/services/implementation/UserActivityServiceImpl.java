package com.sulzhenko.model.services.implementation;

import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.DAO.implementation.*;
import com.sulzhenko.model.DTO.UserDTO;
import com.sulzhenko.model.DTO.ActivityDTO;
import com.sulzhenko.model.DTO.UserActivityDTO;
import javax.sql.DataSource;

import com.sulzhenko.model.entity.Activity;
import com.sulzhenko.model.entity.User;
import com.sulzhenko.model.services.ServiceException;
import com.sulzhenko.model.services.UserActivityService;
import com.sulzhenko.model.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserActivityServiceImpl implements UserActivityService {
    private final DataSource dataSource;
    UserDAO userDAO;
    ActivityDAO activityDAO;
    UserActivityDAO userActivityDAO;
    UserService userService;
    private static final Logger logger = LogManager.getLogger(UserActivityServiceImpl.class);

    public UserActivityServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.userDAO = new UserDAOImpl(dataSource);
        this.activityDAO = new ActivityDAOImpl(dataSource);
        this.userActivityDAO = new UserActivityDAOImpl(dataSource);
        this.userService = new UserServiceImpl(dataSource);
    }

    private static final String ALL_USER_QUERY = "SELECT \n" +
            "user_activity.account as current_account,\n" +
            "user.login,\n" +
            "activity.activity_name,\n" +
            "time_amount,\n" +
            "(SELECT COUNT(*) FROM user_activity WHERE user_activity.account=current_account) as number,\n" +
            "(SELECT SUM(time_amount) FROM user_activity WHERE user_activity.account=current_account) as total\n" +
            "FROM user_activity\n" +
            "INNER JOIN user\n" +
            "ON user_activity.account=user.account\n" +
            "INNER JOIN activity\n" +
            "ON user_activity.activity_id = activity.activity_id\n" +
            "ORDER BY current_account ASC LIMIT %d, %d";
    private static final String QUERY_BRIEF = "SELECT \n" +
            "user_activity.account as current_account,\n" +
            "user.login,\n" +
            "\n" +
            "(SELECT COUNT(*) FROM user_activity WHERE user_activity.account=current_account) as number,\n" +
            "(SELECT SUM(time_amount) FROM user_activity WHERE user_activity.account=current_account) as total\n" +
            "FROM user_activity\n" +
            "INNER JOIN user\n" +
            "ON user_activity.account=user.account\n" +
            "INNER JOIN activity\n" +
            "ON user_activity.activity_id = activity.activity_id\n" +
            "GROUP BY login ORDER BY login ASC LIMIT %d, %d";
    private static final String USER_QUERY = "SELECT \n" +
            "user_activity.account as current_account,\n" +
            "user.login,\n" +
            "activity.activity_name,\n" +
            "time_amount,\n" +
            "(SELECT COUNT(*) FROM user_activity WHERE user_activity.account=current_account) as number,\n" +
            "(SELECT SUM(time_amount) FROM user_activity WHERE user_activity.account=current_account) as total\n" +
            "FROM user_activity\n" +
            "INNER JOIN user\n" +
            "ON user_activity.account=user.account\n" +
            "INNER JOIN activity\n" +
            "ON user_activity.activity_id = activity.activity_id\n" +
            "WHERE login = ?\n" +
            "ORDER BY activity_name ASC LIMIT %d, %d";
    public void setAmount(UserDTO userDTO, ActivityDTO activityDTO, int amount) throws DAOException {
        if(amount >= 0) {
            User user = userDAO.getByLogin(userDTO.getLogin()).orElse(null);
            Activity activity = activityDAO.getByName(activityDTO.getName());
            if (userActivityDAO.ifUserHasActivity(user, activity)) {
                try (Connection con = dataSource.getConnection();
                     PreparedStatement stmt = con.prepareStatement(SQLQueries.RequestQueries.SET_AMOUNT)) {
                    stmt.setInt(1, amount);
                    assert user != null;
                    stmt.setLong(2, user.getAccount());
                    stmt.setLong(3, activity.getId());
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    throw new ServiceException(UNKNOWN_ERROR, e);
                }
            } else {
                throw new ServiceException(USER_HAS_NO_ACTIVITY);
            }
        } else {
            throw new ServiceException(AMOUNT_NONNEGATIVE);
        }
    }
    @Override
    public List<ActivityDTO> allAvailableActivities(UserDTO userDTO) {
        List<ActivityDTO> list = new ArrayList<>();
        User user = userService.getUser(userDTO.getLogin());
        for(Activity element: activityDAO.getAll()){
            if(!userActivityDAO.ifUserHasActivity(user, element)
                    && !userActivityDAO.isRequestToAddExists(user, element)){
                list.add(new ActivityDTO(element.getName()));
            }
        }
        return list;
    }


    public int getNumberOfRecords(String login) throws DAOException {
        int number = 0;
        String numberOfRecordsQuery = "SELECT COUNT(*) FROM user_activity\n" +
                "INNER JOIN user\n" +
                "ON user.account = user_activity.account\n" +
                "WHERE user.login = '" + login + "'";
        try(Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(numberOfRecordsQuery)){
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                number = rs.getInt(1);
            }
        } catch (SQLException e){
            throw new ServiceException(UNKNOWN_ERROR);
        }
        return number;
    }

    public List<UserActivityDTO> listAllUserActivitiesSorted(HttpServletRequest request){
        List<UserActivityDTO> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(buildAllUsersQuery(request))) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UserActivityDTO userActivity = getUserActivityDTOWithFields(rs);
                list.add(userActivity);
            }
        } catch (SQLException e) {
            throw new ServiceException(UNKNOWN_ERROR, e);
        }
        return list;
    }
    public List<UserActivityDTO> listUserActivitiesSorted(HttpServletRequest request, UserDTO userDTO){
        List<UserActivityDTO> list = new ArrayList<>();
//        logger.info(buildUserQuery(request));
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(buildUserQuery(request))) {
            stmt.setString(1, userDTO.getLogin());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UserActivityDTO userActivityDTO = getUserActivityDTOWithFields(rs);
                list.add(userActivityDTO);
            }
        } catch (SQLException e) {
            throw new ServiceException(UNKNOWN_ERROR, e);
        }
        return list;
    }
    public List<UserActivityDTO> listUserActivitiesBriefSorted(HttpServletRequest request){
        List<UserActivityDTO> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(buildQueryBrief(request))) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UserActivityDTO userActivity = getUserActivityDTOWithFieldsBrief(rs);
                list.add(userActivity);
            }
        } catch (SQLException e) {
            throw new ServiceException(UNKNOWN_ERROR, e);
        }
        return list;
    }
    private String buildAllUsersQuery(HttpServletRequest request){
        int page = 1;
        if(request.getParameter(PAGE) != null)
            page = Integer.parseInt(request.getParameter(PAGE));
        int records = 5;
        int offset = (page - 1) * records;
        return String.format(ALL_USER_QUERY, offset, records);
    }
    private String buildQueryBrief(HttpServletRequest request){
        int page = 1;
        if(request.getParameter(PAGE) != null)
            page = Integer.parseInt(request.getParameter(PAGE));
        int records = 5;
        int offset = (page - 1) * records;
        return String.format(QUERY_BRIEF, offset, records);
    }
    private String buildUserQuery(HttpServletRequest request){
        int page = 1;
        if(request.getParameter(PAGE) != null)
            page = Integer.parseInt(request.getParameter(PAGE));
        int records = 5;
        int offset = (page - 1) * records;
        return String.format(USER_QUERY, offset, records);
    }
    private UserActivityDTO getUserActivityDTOWithFields(ResultSet rs) throws SQLException {
        User user = userDAO.getByLogin(rs.getString(2)).orElse(null);
        Activity activity = activityDAO.getByName(rs.getString(3));
        String status = this.defineStatus(user, activity);
        String category = this.defineCategory(rs.getString(3));
        return new UserActivityDTO(rs.getString(2),
                rs.getString(3), rs.getInt(4),
                rs.getInt(5), rs.getInt(6), status, category);
    }
    private UserActivityDTO getUserActivityDTOWithFieldsBrief(ResultSet rs) throws SQLException {
        return new UserActivityDTO(rs.getString(2),
                rs.getInt(3), rs.getInt(4));
    }
    private String defineCategory(String activityName){
        Activity activity = activityDAO.getByName(activityName);
        return activity.getCategory().getName();
    }
    private String defineStatus(User user, Activity activity){
        String status;
        if (userActivityDAO.ifUserHasActivity(user, activity)
                && !userActivityDAO.isRequestToRemoveExists(user, activity)){
            status = ACTIVE_STATUS;
        } else if(userActivityDAO.ifUserHasActivity(user, activity)
                && userActivityDAO.isRequestToRemoveExists(user, activity)){
            status = PENDING_REMOVING;
        } else if(!userActivityDAO.ifUserHasActivity(user, activity)
                && userActivityDAO.isRequestToAddExists(user, activity)){
            status = PENDING_ADDING;
        } else {
            status = ERROR;
        }
        return status;
    }
}
