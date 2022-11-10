package com.sulzhenko.DAO;
/**
 * This interface sets the list of CRUD operations
 */

public interface DAO<Entity, Key> {
    boolean create(Entity model);
    Entity read(Key key);
    boolean update(Entity model);
    boolean delete(Entity model);
}