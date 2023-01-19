package com.sulzhenko.model.DAO;

import com.sulzhenko.model.entity.*;
import java.sql.SQLException;


public interface UserActivityDAO {
    boolean ifUserHasActivity(User u, Activity a);
    void addActivityToUser(Request request) throws SQLException;
    void removeUserActivity(Request request) throws SQLException;
    int getAmount(User user, Activity activity) throws SQLException;
    boolean isRequestToRemoveExists(User user, Activity activity);
    boolean isRequestToAddExists(User user, Activity activity);
}
