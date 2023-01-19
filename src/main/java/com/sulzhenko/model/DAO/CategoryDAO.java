package com.sulzhenko.model.DAO;

import com.sulzhenko.model.entity.Category;

import java.util.Optional;

public interface CategoryDAO extends DAO<Category>{
    Optional<Category> getById(long id);
    Optional<Category> getByName(String name);
}
