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
        return new Activity.Builder()
                .withId(1L)
                .withName("test activity")
                .withCategory(getTestCategory())
                .build();
    }
    public static Request getTestRequestToAdd(){

        return new Request.Builder()
                .withId(1L)
                .withLogin("testuser")
                .withActivityName("test activity")
                .withActionToDo("add")
                .withDescription("asap")
                .build();
    }
    public static Request getTestRequestToRemove() {
        return new Request.Builder()
                .withId(2L)
                .withLogin("testuser")
                .withActivityName("test activity")
                .withActionToDo("remove")
                .withDescription("asap")
                .build();
    }
}
