package com.sulzhenko.model.DAO;

import com.sulzhenko.model.entity.Activity;
import com.sulzhenko.model.entity.Request;
import com.sulzhenko.model.entity.User;
import java.util.List;
import java.util.Optional;

public interface RequestDAO extends DAO<Request>{
    Optional<Request> getById(long id);
    List<Request> getByLogin(String login);
    List<Request> getByActivity(String activityName);
    List<Request> getByActionToDo(String actionName);
    List<Request> getByLoginAndAction(String login, String action) throws DAOException;
    boolean ifRequestUnique(Request request) throws DAOException;
    boolean ifUserHasActivity(User user, Activity activity);
}
