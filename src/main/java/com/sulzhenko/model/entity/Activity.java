package com.sulzhenko.model.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Activity entity class. Matches table 'activity' in database.
 * Use Activity.builder().withFieldName(fieldValue).build() to create an instance
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */

public class Activity implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private Category category;

    private Activity() {
    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    /**
     * This inner class uses Builder pattern instead of setters
     * with methods like "withXXX" (where XXX - some field of Activity class)
     */
    public static class Builder {
        private final Activity activity;
        public Builder() {
            activity = new Activity();
        }
        public Builder withId(Long id) {
            activity.id = id;
            return this;
        }
        public Builder withName(String name) {
            activity.name = name;
            return this;
        }
        public Builder withCategory(Category category) {
            activity.category = category;
            return this;
        }
        public Activity build() {
            if(activity.id == null) {
                activity.id = 0L;
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
}
