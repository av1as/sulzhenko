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
}
