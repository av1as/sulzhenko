package com.sulzhenko.model.DAO;

import com.sulzhenko.model.entity.Activity;

import java.util.List;

public interface ActivityDAO extends DAO<Activity>{
    Activity getById(long id);
    Activity getByName(String name);
    List<Activity> getByCategory(String categoryName);
}
