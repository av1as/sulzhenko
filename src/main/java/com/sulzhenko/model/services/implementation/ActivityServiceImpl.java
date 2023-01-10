package com.sulzhenko.model.services.implementation;

import com.sulzhenko.model.DAO.ActivityDAO;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.DAO.implementation.ActivityDAOImpl;
import com.sulzhenko.model.DTO.ActivityDTO;
import com.sulzhenko.model.services.ActivityService;
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

    public ActivityServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static final String COMMON_PART = "SELECT activity.activity_name, \n" +
            "COUNT(user_activity.activity_id) as quantity, \n" +
            "category_of_activity.category_name \n" +
            "FROM activity\n" +
            "LEFT JOIN user_activity\n" +
            "ON activity.activity_id = user_activity.activity_id\n" +
            "INNER JOIN category_of_activity\n" +
            "ON category_of_activity.category_id = activity.category_id\n";
    private final String numberOfRecordsQuery = "SELECT COUNT(activity.activity_name)\n" +
            "FROM activity\n" +
            "INNER JOIN category_of_activity\n" +
            "ON activity.category_id = category_of_activity.category_id\n";
    private static final Logger logger = LogManager.getLogger(ActivityServiceImpl.class);
    public String buildQuery(HttpServletRequest request){
        String order = getOrder(request);
        String parameter = getParam(request);
        String filter = request.getParameter("filter");
        int page = 1;
        if(request.getParameter("page") != null)
            page = Integer.parseInt(request.getParameter("page"));
        int records = 5;
        int offset = (page - 1) * records;
        return COMMON_PART +
                applyFilter(filter) +
                applySorting("activity_name") +
                applyOrder(parameter, order, offset, records);
    }

    private static String getParam(HttpServletRequest request) {
        String parameter = request.getParameter("parameter");
        if(Objects.equals(parameter, "number of users")) parameter = "quantity";
        else if(Objects.equals(parameter, "name of activity")) parameter = "activity_name";
        else if(Objects.equals(parameter, "category of activity")) parameter = "category_name";
        return parameter;
    }

    private static String getOrder(HttpServletRequest request) {
        String order;
        if(Objects.equals(request.getParameter("order"), "descending")) order = "DESC";
        else order = "ASC";
        return order;
    }

    private String getTotalRecords(HttpServletRequest request){
        String filter = request.getParameter("filter");
        String query = numberOfRecordsQuery;
        if(!Objects.equals(filter, "all categories")) query += "WHERE category_name = '" + filter + "'";
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
            throw new DAOException(getTotalRecords(request));
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
            throw new DAOException("unknown.error", e);
        }
        return list;
    }
    private String applyFilter(String filter){
        return Objects.equals(filter, "all categories") ? "": "WHERE category_name = '" + filter + "'\n";
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
}
