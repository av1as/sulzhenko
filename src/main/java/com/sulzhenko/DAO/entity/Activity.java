package com.sulzhenko.DAO.entity;

import java.io.Serializable;
import java.util.Objects;
/**
 * This class describes activity entity
 */

public class Activity implements Serializable {
    private Integer id;
    private String name;
    private String category;

    public Activity() {
    }

    public Activity(Integer id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

//    public static Builder builder() {
//        return new Builder();
//    }

    /**
     * This inner class uses Builder pattern instead of setters
     * with methods like "withXXX" (where XXX - some field of Activity class)
     */
    public static class Builder {
//        private Integer id;
//        private String name;
//        private String category;
        private final Activity activity;
        public Builder() {
            activity = new Activity();
        }
        public Builder withId(Integer id) {
            activity.id = id;
            return this;
        }
        public Builder withName(String name) {
            activity.name = name;
            return this;
        }
        public Builder withCategory(String category) {
            activity.category = category;
            return this;
        }
        public Activity build() {
            if(activity.id == null) {
                activity.id = 0;
            }
            if (activity.name == null) {
                throw new IllegalArgumentException();
            }
            return activity;
        }
    }

        @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return name.equals(activity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
