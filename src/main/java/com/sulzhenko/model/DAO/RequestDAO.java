package com.sulzhenko.model.DAO;

import com.sulzhenko.model.Constants;
import com.sulzhenko.model.entity.Activity;
import com.sulzhenko.model.entity.Request;
import com.sulzhenko.model.entity.User;
import com.sulzhenko.model.services.implementation.RequestServiceImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.sulzhenko.model.DAO.SQLQueries.UserActivityQueries.IF_USER_HAS_ACTIVITY;

public interface RequestDAO extends DAO<Request>{
    Optional<Request> getById(long id);
    List<Request> getByLogin(String login);
    List<Request> getByActivity(String activityName);
    List<Request> getByActionToDo(String actionName);
    List<Request> getByLoginAndAction(String login, String action) throws DAOException;
    boolean ifRequestUnique(Request request) throws DAOException;
    boolean ifUserHasActivity(User user, Activity activity);
}
