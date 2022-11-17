package com.sulzhenko.DAO;

import java.util.List;

/**
 * This interface sets the list of CRUD operations
 */
public interface DAO<Entity> {

    //Optional<Entity> getById(int id);
    Entity getById(int id);

    List<Entity> getAll();

    void save(Entity t);

    void update(Entity t, String[] params);

    void delete(Entity t);
}
//public interface DAO<Entity, Key> {
//    boolean create(Entity model);
//    Entity read(Key key);
//    boolean update(Entity model);
//    boolean delete(Entity model);
//}