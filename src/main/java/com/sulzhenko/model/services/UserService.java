package com.sulzhenko.model.services;

import com.sulzhenko.model.Constants;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.DTO.UserDTO;
import com.sulzhenko.model.entity.User;
import java.util.List;

public interface UserService extends Constants {
    User getUser(String login);
    UserDTO getUserDTO(String login);
    void addUser(UserDTO userDTO, String passwordConfirm);
    void updateUser(UserDTO t, String[] params);
    void recoverPassword(String login);
    void deleteUser(User t) throws DAOException;
    boolean isLoginAvailable(String login);
    boolean isRoleCorrect(String role) throws DAOException;
    boolean isStatusCorrect(String status) throws DAOException;
    void isUpdateCorrect(String[] params, String oldLogin);
    void isAdminUpdateCorrect(String[] params);
    String getErrorMessageUpdate(String login, String password, String newPassword, String newPasswordConfirm);
    int getNumberOfRecords(String status);
    void notifyAboutUpdate(User u);
    void sendTemporaryPassword(User user, String temporaryPassword);
    List<UserDTO> viewAllSystemUsers(int startPosition, int size);
    List<UserDTO> viewAllActiveUsers(int startPosition, int size);
    List<UserDTO> viewAllInactiveUsers(int startPosition, int size);
    List<UserDTO> viewAllDeactivatedUsers(int startPosition, int size);
    void adminUpdateUser(UserDTO t, String[] params);
    String areFieldsIncorrect(String login, String password);
    String areFieldsBlank(String login, String password);
    String validateNewUser(User user, String passwordConfirm);
    String validateUserUpdate(User user);
    String validateAdminUserUpdate(User user);
    List<UserDTO> getUserList(String status, int page, int recordsPerPage);
}
