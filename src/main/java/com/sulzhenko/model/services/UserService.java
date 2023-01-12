package com.sulzhenko.model.services;

import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.entity.User;

import java.util.List;

public interface UserService {
    User getUser(String login);
    void addUser(User t);
    void updateUser(User t, String[] params);
    void deleteUser(User t) throws DAOException;
    boolean isLoginAvailable(String login);
    boolean isRoleCorrect(String role) throws DAOException;
    boolean isStatusCorrect(String status) throws DAOException;
    void isUpdateCorrect(String[] params, String oldLogin);
    int getNumberOfRecords(String status);
    void notifyAboutUpdate(User u, String description);
    List<User> viewAllSystemUsers(int startPosition, int size);
    List<User> viewAllActiveUsers(int startPosition, int size);
    List<User> viewAllInactiveUsers(int startPosition, int size);
    List<User> viewAllDeactivatedUsers(int startPosition, int size);
    void adminUpdateUser(User t, String[] params);
    String areFieldsIncorrect(String login, String password);
}
