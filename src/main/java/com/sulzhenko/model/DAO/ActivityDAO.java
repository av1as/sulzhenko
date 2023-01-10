package com.sulzhenko.model.DAO;

import com.sulzhenko.model.entity.*;
import java.util.List;
import java.util.Map;

public interface ActivityDAO extends DAO<Activity>{
    Activity getById(long id);
    Activity getByName(String name);
    List<Activity> getByCategory(String categoryName);
    void isDataCorrect(Activity t);
    boolean isNameAvailable(String name);
    boolean isCategoryCorrect(String category);
    void isUpdateCorrect(String[] params, String oldName);
    List<User> getConnectedUsers(Activity activity);
    Map<Activity, Integer> listActivitiesSorted(int startPosition, int size, String filter, String parameter, String order) throws DAOException;
    Map<Activity, Integer> listActivitiesSortedByUsers(int startPosition, int size, String filter, String order);
    void notifyAboutUpdate(List<User> connectedUsers, String description);
}
