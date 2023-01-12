package com.sulzhenko.model.services;

import com.sulzhenko.model.entity.Category;

import java.util.List;

public interface CategoryService {
    Category getCategory(String name);
    List<Category> getAllCategories();
    void addCategory(String name);
    void deleteCategory(String name);
    void updateCategory(String name, String newCategoryName);
    List<Category> viewAllCategories(int startPosition, int size);
    int getNumberOfCategories();
    boolean isCategoryNameUnique(String name);
}
