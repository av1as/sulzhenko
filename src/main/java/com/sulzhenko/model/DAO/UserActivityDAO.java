package com.sulzhenko.model.DAO;

import com.sulzhenko.DTO.UserActivityDTO;
import com.sulzhenko.model.entity.*;
import java.sql.SQLException;
import java.util.List;

public interface UserActivityDAO {
    void addActivityToUser(Request request) throws DAOException, SQLException;
    void removeUserActivity(Request request) throws DAOException, SQLException;
    void setAmount(User user, Activity activity, int amount) throws DAOException;
    int getAmount(User user, Activity activity) throws DAOException;
    int getNumberOfRecords() throws DAOException;
    int getNumberOfRecordsByUser(String login) throws DAOException;
    boolean ifUserHasActivity(User user, Activity activity);
    List<UserActivityDTO> listAllUserActivitiesSorted(String query) throws DAOException;
    List<UserActivityDTO> listUserActivitiesSorted(String query, String login) throws DAOException;
}
