package com.sulzhenko.DTO;

import java.io.Serializable;

/**
 * CategoryDTO class. Fields adapted to view.
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */

public class CategoryDTO implements Serializable {
    String name;
    long id;

    public CategoryDTO(String name) {
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
}
