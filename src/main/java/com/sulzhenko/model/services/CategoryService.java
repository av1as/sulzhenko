package com.sulzhenko.model.services;

import com.sulzhenko.model.Constants;
import com.sulzhenko.model.DTO.CategoryDTO;
import com.sulzhenko.model.entity.Category;

import java.util.List;

public interface CategoryService extends Constants {
    Category getCategory(String name);
    List<CategoryDTO> getAllCategories();
    void addCategory(String name);
    void deleteCategory(String name);
    void updateCategory(String name, String newCategoryName);
    List<CategoryDTO> viewAllCategories(int startPosition, int size);
    int getNumberOfCategories();
    boolean isCategoryNameUnique(String name);
}
