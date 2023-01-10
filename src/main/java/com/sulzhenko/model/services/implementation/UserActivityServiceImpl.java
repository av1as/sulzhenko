package com.sulzhenko.model.services.implementation;

import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.DAO.implementation.*;
import com.sulzhenko.model.DTO.UserActivityDTO;
import javax.sql.DataSource;
import com.sulzhenko.model.entity.Activity;
import com.sulzhenko.model.entity.User;
import com.sulzhenko.model.services.UserActivityService;
import jakarta.servlet.http.HttpServletRequest;

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
    UserActivityDAO uaDAO;

    public UserActivityServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.userDAO = new UserDAOImpl(dataSource);
        this.activityDAO = new ActivityDAOImpl(dataSource);
        this.uaDAO = new UserActivityDAOImpl(dataSource);
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
            "ORDER BY login ASC LIMIT %d, %d";
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
            throw new DAOException("unknown.error");
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
            throw new DAOException("unknown.error", e);
        }
        return list;
    }
    public List<UserActivityDTO> listUserActivitiesSorted(HttpServletRequest request, User user){
        List<UserActivityDTO> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(buildUserQuery(request))) {
            stmt.setString(1, user.getLogin());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UserActivityDTO userActivity = getUserActivityDTOWithFields(rs);
                list.add(userActivity);
            }
        } catch (SQLException e) {
            throw new DAOException("unknown.error", e);
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
            throw new DAOException("unknown.error", e);
        }
        return list;
    }
    private String buildAllUsersQuery(HttpServletRequest request){
        int page = 1;
        if(request.getParameter("page") != null)
            page = Integer.parseInt(request.getParameter("page"));
        int records = 5;
        int offset = (page - 1) * records;
        return String.format(ALL_USER_QUERY, offset, records);
    }
    private String buildQueryBrief(HttpServletRequest request){
        int page = 1;
        if(request.getParameter("page") != null)
            page = Integer.parseInt(request.getParameter("page"));
        int records = 5;
        int offset = (page - 1) * records;
        return String.format(QUERY_BRIEF, offset, records);
    }
    private String buildUserQuery(HttpServletRequest request){
        int page = 1;
        if(request.getParameter("page") != null)
            page = Integer.parseInt(request.getParameter("page"));
        int records = 5;
        int offset = (page - 1) * records;
        return String.format(USER_QUERY, offset, records);
    }
    private UserActivityDTO getUserActivityDTOWithFields(ResultSet rs) throws SQLException {
        User user = userDAO.getByLogin(rs.getString(2)).orElse(null);
        Activity activity = activityDAO.getByName(rs.getString(3));
        String status = this.defineStatus(user, activity);
        String category = this.getCategory(rs.getString(3));
        return new UserActivityDTO(rs.getString(2),
                rs.getString(3), rs.getInt(4),
                rs.getInt(5), rs.getInt(6), status, category);
    }
    private UserActivityDTO getUserActivityDTOWithFieldsBrief(ResultSet rs) throws SQLException {
        return new UserActivityDTO(rs.getString(2),
                rs.getInt(3), rs.getInt(4));
    }
    public String getCategory(String activityName){
        Activity activity = activityDAO.getByName(activityName);
        return activity.getCategory().getName();
    }
    public String defineStatus(User user, Activity activity){
        String s;
        if (uaDAO.ifUserHasActivity(user, activity) && !uaDAO.isRequestToRemoveExists(user, activity)){
            s = "active.status";
        } else if(uaDAO.ifUserHasActivity(user, activity) && uaDAO.isRequestToRemoveExists(user, activity)){
            s = "pending.removing";
        } else if(!uaDAO.ifUserHasActivity(user, activity) && uaDAO.isRequestToAddExists(user, activity)){
            s = "pending.adding";
        } else {
            s = "error";
        }
        return s;
    }
}
