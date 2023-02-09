package com.sulzhenko.DTO;

import java.io.Serializable;

/**
 * ActivityDTO class. Fields adapted to view.
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */

public class ActivityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String category;
    private int numberOfUsers;

    public ActivityDTO(String name, String category, int numberOfUsers) {
        this.name = name;
        this.category = category;
        this.numberOfUsers = numberOfUsers;
    }

    public ActivityDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getNumberOfUsers() {
        return numberOfUsers;
    }
    public void setNumberOfUsers(int numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }
}
