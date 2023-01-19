package com.sulzhenko.model.DAO;

import com.sulzhenko.model.entity.Request;
import java.util.List;

public interface RequestDAO extends DAO<Request>{
    Request getById(long id);
    List<Request> getByLogin(String login);
    List<Request> getByActivity(String activityName);
    List<Request> getByActionToDo(String actionName);
}
