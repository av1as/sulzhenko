package com.sulzhenko.model.DAO;

import com.sulzhenko.model.entity.Activity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ActivityDAO extends DAO<Activity>{
    Optional<Activity> getById(long id);
    Optional<Activity> getByName(String name);
    List<Activity> getByCategory(String categoryName);
    Map<Activity, Integer> getSortedMap(String querySQL) throws DAOException;
    int getNumberOfRecords(String filter) throws DAOException;
}
