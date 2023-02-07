package com.sulzhenko.model.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Category entity class. Matches table 'category' in database.
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    long id;

    public Category(String name, long id) {
        this.name = name;
        this.id = id;
    }
    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
