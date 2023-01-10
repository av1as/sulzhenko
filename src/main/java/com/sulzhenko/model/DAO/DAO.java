package com.sulzhenko.model.DAO;

import java.util.List;
import java.util.Optional;

/**
 * This interface sets the list of CRUD operations
 */
public interface DAO<Entity> {
    Optional<Entity> get(Object parameter, String querySQL);
    List<Entity> getAll();
    List<Entity> getList(Object parameter, String querySQL);

    void save(Entity t);

    void update(Entity t, String[] params);

    void delete(Entity t);
}