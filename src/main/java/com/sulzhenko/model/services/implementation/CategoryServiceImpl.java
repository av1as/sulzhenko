package com.sulzhenko.model.services.implementation;

import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.DAO.implementation.CategoryDAOImpl;
import com.sulzhenko.DTO.CategoryDTO;
import com.sulzhenko.model.entity.Category;
import com.sulzhenko.model.services.CategoryService;
import com.sulzhenko.model.services.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * CategoryService class for interaction between controller and Category DAO
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class CategoryServiceImpl implements CategoryService {
    private final CategoryDAO categoryDAO;

    public CategoryServiceImpl(DataSource dataSource) {
        this.categoryDAO = new CategoryDAOImpl(dataSource);
    }

    private static final Logger logger = LogManager.getLogger(CategoryServiceImpl.class);

    /**
     * Gets instance of Category by name
     * @param name - value of category name
     * @return instance of Category class
     */
    @Override
    public Category getCategory(String name){
        return categoryDAO.getByName(name).orElse(null);
    }

    /**
     * Inserts new category
     * @param name - name of category
     * @throws ServiceException is wrapper for DAOException
     */
    @Override
    public void addCategory(String name){
        if(isCategoryNameAvailable(name)){
            try{
                categoryDAO.save(new Category(name));
            } catch (DAOException e){
                logger.warn(e.getMessage());
                throw new ServiceException(e);
            }
        } else{
            throw new ServiceException(DUPLICATE_CATEGORY);
        }
    }

    /**
     * Deletes category record
     * @param name - category name
     * @throws ServiceException is wrapper for DAOException
     */
    @Override
    public void deleteCategory(String name){
        if(!isCategoryNameAvailable(name)){
            try{
                categoryDAO.delete(categoryDAO.getByName(name).orElse(null));
            } catch(DAOException e){
                logger.warn(e.getMessage());
                throw new ServiceException(e);
            }
        } else throw new ServiceException(WRONG_CATEGORY);
    }

    /**
     * Updates category
     * @param name - name of category before update
     * @param newCategoryName - name of category after update
     * @throws ServiceException is wrapper for DAOException
     */
    @Override
    public void updateCategory(String name, String newCategoryName){
        if(!isCategoryNameAvailable(name) && isCategoryNameAvailable(newCategoryName)){
            String[] param = {newCategoryName};
            try{
                categoryDAO.update(categoryDAO.getByName(name).orElse(null), param);
            } catch(DAOException e){
                logger.warn(e.getMessage());
                throw new ServiceException(e.getMessage());
            }
        } else throw new ServiceException(WRONG_CATEGORY);
    }

    /**
     * Gets list of all categories
     * @return CategoryDTO list
     */
    @Override
    public List<CategoryDTO> getAllCategories(){
        List<CategoryDTO> result = new ArrayList<>();
        for(Category element: categoryDAO.getAll()){
            result.add(new CategoryDTO(element.getName()));
        }
        return result;
    }

    /**
     * Gets page of list of records from database
     * @param startPosition - sets offset for SQL query
     * @param size - sets number of records per page
     * @return List of CategoryDTO
     */
    @Override
    public List<CategoryDTO> viewAllCategories(int startPosition, int size){
        List<CategoryDTO> list = new ArrayList<>();
        for(int i = startPosition; (i < (startPosition + size)) && (i < categoryDAO.getAll().size()); i++){
            list.add(new CategoryDTO(categoryDAO.getAll().get(i).getName()));
        }
        return list;
    }

    /**
     * Gets number of records in database
     * @return int value - number of records
     */
    @Override
    public int getNumberOfCategories(){
        return categoryDAO.getAll().size();
    }

    /**
     * Checks if category name is not already used in database
     * @param name - name of new category
     * @return true if name is not used, false otherwise
     * @throws ServiceException is wrapper for SQLException
     */
    @Override
    public boolean isCategoryNameAvailable(String name){
        return getCategory(name) == null;
    }
}
