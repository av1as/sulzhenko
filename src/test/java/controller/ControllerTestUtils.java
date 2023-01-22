package controller;

import com.sulzhenko.DTO.UserDTO;
import com.sulzhenko.model.entity.Activity;
import com.sulzhenko.model.entity.Category;
import com.sulzhenko.model.entity.Request;
import com.sulzhenko.model.entity.User;

public final class ControllerTestUtils {
    public static UserDTO getUserDTO() {
        return new UserDTO.Builder()
                .withAccount(1L)
                .withLogin("testuser")
                .withPassword("asdf")
                .withEmail("me@me.me")
                .withFirstName("asdf")
                .withLastName("asdf")
                .withRole("system user")
                .withStatus("active")
                .withNotification("on")
                .build();
    }

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

        return new Request(1L, "testuser", "test activity", "add", "asap");
    }
    public static Request getTestRequestToRemove() {
        return new Request(2L, "testuser", "test activity", "remove", "asap");
    }
}
