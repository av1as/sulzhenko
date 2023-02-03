package controller;

import com.sulzhenko.DTO.UserDTO;

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
}
