package com.sulzhenko.model.DAO;

import java.util.List;
import java.util.Optional;

/**
 * This interface sets the list of CRUD operations
 */
public interface DAO<Entity> {
    Optional<Entity> get(Object parameter, String querySQL) throws DAOException;
    List<Entity> getAll() throws DAOException;
    List<Entity> getList(Object parameter, String querySQL) throws DAOException;
    void save(Entity t) throws DAOException;
    void update(Entity t, String[] params) throws DAOException;

    void delete(Entity t) throws DAOException;
}