package com.sulzhenko.model.services.implementation;

import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.DAO.implementation.CategoryDAOImpl;
import com.sulzhenko.model.DTO.CategoryDTO;
import com.sulzhenko.model.entity.Category;
import com.sulzhenko.model.services.CategoryService;
import com.sulzhenko.model.services.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private final DataSource dataSource;
    private final CategoryDAO categoryDAO;

    public CategoryServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.categoryDAO = new CategoryDAOImpl(dataSource);
    }

    private static final Logger logger = LogManager.getLogger(CategoryServiceImpl.class);

    @Override
    public Category getCategory(String name){
        return categoryDAO.getByName(name).orElse(null);
    }
    @Override
    public void addCategory(String name){
        if(isCategoryNameUnique(name)){
            try{
                categoryDAO.save(new Category(name));
            } catch (DAOException e){
                logger.fatal(e.getMessage());
                throw new ServiceException(e);
            }
        } else{
            throw new ServiceException("duplicate.category");
        }
    }
    @Override
    public void deleteCategory(String name){
        if(!isCategoryNameUnique(name)){
            try{
                categoryDAO.delete(categoryDAO.getByName(name).orElse(null));
            } catch(DAOException e){
                logger.fatal(e.getMessage());
                throw new ServiceException(e);
            }
        }
    }
    @Override
    public void updateCategory(String name, String newCategoryName){
        if(!isCategoryNameUnique(name) && isCategoryNameUnique(newCategoryName)){
            String[] param = {newCategoryName};
            try{
                categoryDAO.update(categoryDAO.getByName(name).orElse(null), param);
            } catch(DAOException e){
                logger.fatal(e.getMessage());
                throw new ServiceException(e.getMessage());
            }
        } else throw new ServiceException(WRONG_CATEGORY);
    }
    @Override
    public List<CategoryDTO> getAllCategories(){
        List<CategoryDTO> result = new ArrayList<>();
        for(Category element: categoryDAO.getAll()){
            result.add(new CategoryDTO(element.getName()));
        }
        return result;
    }
    @Override
    public List<CategoryDTO> viewAllCategories(int startPosition, int size){
        List<CategoryDTO> list = new ArrayList<>();
        for(int i = startPosition; (i < (startPosition + size)) && (i < categoryDAO.getAll().size()); i++){
            list.add(new CategoryDTO(categoryDAO.getAll().get(i).getName()));
        }
        return list;
    }
    @Override
    public int getNumberOfCategories(){
        return categoryDAO.getAll().size();
    }
    public boolean isCategoryNameUnique(String name){
        return getCategory(name) == null;
//        List<Category> list = categoryDAO.getAll();
//        for(Category element: list){
//            if (Objects.equals(element.getName(), name)) return false;
//        }
//        return true;
    }
}
