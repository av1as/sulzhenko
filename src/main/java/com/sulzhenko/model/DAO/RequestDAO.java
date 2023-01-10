package com.sulzhenko.model.DAO;

import com.sulzhenko.model.entity.Activity;
import com.sulzhenko.model.entity.Request;
import com.sulzhenko.model.entity.User;
import java.util.List;

public interface RequestDAO extends DAO<Request>{
    Request getById(long id);
    List<Request> getByLogin(String login);
    List<Request> getByActivity(String activityName);
    List<Request> getByActionToDo(String actionName);
    void isDataCorrect(Request t);
    boolean isActionCorrect(Request request);
    boolean canRequestBeAdded(Request request);
    boolean canRequestBeRemoved(Request request);
    boolean ifUserHasActivity(User u, Activity a);
    List<Request> viewAllRequests(int startPosition, int size);
    List<Request> viewRequestsToAdd(int startPosition, int size);
    List<Request> viewRequestsToRemove(int startPosition, int size);

}
