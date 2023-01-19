package com.sulzhenko.model.DTO;

import java.io.Serializable;

public class CategoryDTO implements Serializable {
    String name;
    long id;

    public CategoryDTO(String name, long id) {
        this.name = name;
    }

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
