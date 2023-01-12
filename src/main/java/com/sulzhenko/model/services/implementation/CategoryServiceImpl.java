package com.sulzhenko.model.services.implementation;

import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.DAO.implementation.CategoryDAOImpl;
import com.sulzhenko.model.entity.Category;
import com.sulzhenko.model.services.CategoryService;
import com.sulzhenko.model.services.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        } else throw new ServiceException("wrong.category");
    }
    @Override
    public List<Category> getAllCategories(){
        return categoryDAO.getAll();
    }
    @Override
    public List<Category> viewAllCategories(int startPosition, int size){
        List<Category> list = new ArrayList<>();
        for(int i = startPosition; (i < (startPosition + size)) && (i < new CategoryDAOImpl(dataSource).getAll().size()); i++){
            list.add(new CategoryDAOImpl(dataSource).getAll().get(i));
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
