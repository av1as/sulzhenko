package com.sulzhenko.DAO;

import java.sql.Connection;
import java.util.List;

/**
 * This interface sets the list of CRUD operations
 */
public interface DAO<Entity> {
    Entity get(Object parameter, String querySQL, Connection con);
    List<Entity> getAll(Connection con);
    List<Entity> getList(Object parameter, String querySQL, Connection con);

    void save(Entity t, Connection con);

    void update(Entity t, String[] params, Connection con);

    void delete(Entity t, Connection con);
}