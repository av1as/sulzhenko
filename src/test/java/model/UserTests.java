package model;

import com.sulzhenko.model.entity.User;
import org.junit.jupiter.api.Test;
import static model.DAOTestUtils.getTestUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTests {
    @Test
    void userTest(){
        User user = getTestUser();
        assertEquals("testuser", user.getLogin());
        assertEquals(1L, user.getAccount());
        assertEquals("me@me.me", user.getEmail());
        assertEquals("asdf", user.getPassword());
        assertEquals("asfd", user.getFirstName());
        assertEquals("asdf", user.getLastName());
        assertEquals("system user", user.getRole().getValue());
        assertEquals("active", user.getStatus());
        assertEquals("on", user.getNotification());
        assertEquals("asfd asdf", user.getFullName());
        assertEquals(User.Role.SYSTEM_USER, user.getRole());
        assertEquals(User.Role.ADMIN, User.Role.extractRole("administrator"));
    }
}
