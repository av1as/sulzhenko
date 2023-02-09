package DTO;

import com.sulzhenko.DTO.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDtoTests {
    @Test
    void testIsNotificationChecked(){
        UserDTO userDTO = new UserDTO.Builder()
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
        assertEquals("checked", userDTO.isNotificationChecked());
        userDTO = new UserDTO.Builder()
                .withAccount(1L)
                .withLogin("testuser")
                .withPassword("asdf")
                .withEmail("me@me.me")
                .withFirstName("asdf")
                .withLastName("asdf")
                .withRole("system user")
                .withStatus("active")
                .withNotification("off")
                .build();
        assertEquals("unchecked", userDTO.isNotificationChecked());
    }
}
