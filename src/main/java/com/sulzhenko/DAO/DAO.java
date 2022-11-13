package com.sulzhenko.DAO;

import java.util.List;

/**
 * This interface sets the list of CRUD operations
 */
public interface DAO<E> {

    //Optional<Entity> getById(int id);
    E getById(int id);

    List<E> getAll();

    void save(E t);

    void update(E t, String[] params);

    void delete(E t);
}
//public interface DAO<Entity, Key> {
//    boolean create(Entity model);
//    Entity read(Key key);
//    boolean update(Entity model);
//    boolean delete(Entity model);
//}