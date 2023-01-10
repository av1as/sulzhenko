package model;

import com.sulzhenko.model.entity.*;

public final class DAOTestUtils {

    public static User getTestUser() {
        return new User.Builder()
                .withLogin("testuser")
                .withAccount(1L)
                .withEmail("me@me.me")
                .withPassword("asdf")
                .withFirstName("asfd")
                .withLastName("asdf")
                .withRole("system user")
                .withStatus("active")
                .withNotification("on")
                .build();
    }

    public static Category getTestCategory() {
        return new Category("test category", 1L);
    }

    public static Activity getTestActivity() {
        return new Activity(1L, "test activity", getTestCategory());
    }
    public static Request getTestRequestToAdd(){
        return new Request(1L, "testUser", "test activity", "add", "");
    }
    public static Request getTestRequestToRemove() {
        return new Request(2L, "testUser", "test activity", "remove", "");
    }
}
