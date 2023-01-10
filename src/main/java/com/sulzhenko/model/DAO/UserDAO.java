package com.sulzhenko.model.DAO;

import com.sulzhenko.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO extends DAO<User>{
    Optional<User> getById(Long id);
    Optional<User> getByLogin(String login);
    List<User> getByEmail(String email);
    List<User> getByFirstName(String firstName);
    List<User> getByLastName(String lastName);
    List<User> getByRole(String role);
    List<User> getByStatus(String status);
    List<User> getByNotification(String notification);
    void addStatus(String statusName);
    void deleteStatus(String statusName);
}
