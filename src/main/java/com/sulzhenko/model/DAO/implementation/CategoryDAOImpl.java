package com.sulzhenko.model.DAO.implementation;

import com.sulzhenko.model.Constants;

import com.sulzhenko.model.DAO.CategoryDAO;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.DAO.SQLQueries;
import com.sulzhenko.model.entity.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDAOImpl implements CategoryDAO, Constants {
    private final DataSource dataSource;

    public CategoryDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static final Logger logger = LogManager.getLogger(CategoryDAOImpl.class);

    @Override
    public Optional<Category> get(Object parameter, String querySQL) throws DAOException {
        Category c = null;
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(querySQL)
        ) {
            stmt.setObject(1, parameter);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                c = new Category(rs.getString(2), rs.getLong(1));
            }
        } catch (SQLException e){
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
        return Optional.ofNullable(c);
    }

    @Override
    public List<Category> getAll() throws DAOException {
        List<Category> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQLQueries.CategoryQueries.GET_ALL_CATEGORIES)
        ) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Category(rs.getString(2), rs.getLong(1)));
            }
        } catch (SQLException e){
            logger.fatal(e);
            throw new DAOException(UNKNOWN_ERROR);
        }
        return list;
    }
    @Override
    public Optional<Category> getById(long id) {
        return get(id, SQLQueries.CategoryQueries.GET_CATEGORY_BY_ID);
    }
    public Optional<Category> getByName(String name) {
        return get(name, SQLQueries.CategoryQueries.GET_CATEGORY_BY_NAME);
    }

    @Override
    public List<Category> getList(Object parameter, String querySQL) throws DAOException {
        List<Category> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(querySQL)
        ) {
            stmt.setObject(1, parameter);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Category(rs.getString(2), rs.getInt(1)));
            }
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
        return list;
    }

    @Override
    public void save(Category t) throws DAOException{
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQLQueries.CategoryQueries.ADD_CATEGORY)
        ) {
            stmt.setString(1, t.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
    }

    @Override
    public void update(Category t, String[] params) throws DAOException{
        String oldName = t.getName();
        try(Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(SQLQueries.CategoryQueries.UPDATE_CATEGORY)) {
            int k = 0;
            stmt.setString(++k, params[k-1]);
            stmt.setString(++k, oldName);
            stmt.executeUpdate();
        } catch (SQLException e){
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
    }

    @Override
    public void delete(Category t) throws DAOException{
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQLQueries.CategoryQueries.DELETE_CATEGORY)
        ) {
            stmt.setString(1, t.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
    }
}
