package com.sulzhenko.model.DAO;

import com.sulzhenko.model.DAO.implementation.UserActivityDAOImpl;
import com.sulzhenko.model.entity.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public interface UserActivityDAO {
    boolean ifUserHasActivity(User u, Activity a);
    void addActivityToUser(Request request) throws SQLException;
    void removeUserActivity(Request request) throws SQLException;
    int getAmount(User user, Activity activity) throws SQLException;
    int getNumberOfUsers(Activity activity);
    boolean isRequestToRemoveExists(User user, Activity activity);
    boolean isRequestToAddExists(User user, Activity activity);
}
