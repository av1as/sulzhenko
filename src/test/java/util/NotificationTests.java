package util;

import com.sulzhenko.Util.notifications.*;
import com.sulzhenko.Util.notifications.implementations.*;
import org.junit.jupiter.api.Test;
import static model.DAOTestUtils.getTestRequestToAdd;
import static model.DAOTestUtils.getTestUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NotificationTests {
    @Test
    void testAccountUpdateBody() {
        AccountUpdateBody accountUpdateBody = new AccountUpdateBody(getTestUser());
        String expected = "Hello, asfd asdf,\nyour account testuser was updated.";
        assertEquals(expected, accountUpdateBody.asText());
    }
    @Test
    void testAccountUpdateSubject() {
        AccountUpdateSubject accountUpdateSubject = new AccountUpdateSubject();
        String expected = "Timekeeping: your account update";
        assertEquals(expected, accountUpdateSubject.asSubject());
    }
    @Test
    void testRequestUpdateBody() {
        RequestUpdateBody requestUpdateBody = new RequestUpdateBody(getTestUser(), getTestRequestToAdd(), "has been approved");
        String expected = "Hello, asfd asdf,\nthe request from your account testuser to add activity test activity has been approved.";
        assertEquals(expected, requestUpdateBody.asText());
    }
    @Test
    void testRequestUpdateSubject() {
        RequestUpdateSubject requestUpdateSubject = new RequestUpdateSubject();
        String expected = "Timekeeping: your request update";
        assertEquals(expected, requestUpdateSubject.asSubject());
    }
    @Test
    void testSystemUpdateBody() {
        SystemUpdateBody systemUpdateBody = new SystemUpdateBody(getTestUser(), "some changes were made");
        String expected = "Hello, asfd asdf,\nsystem update connected with your account testuser was made: some changes were made.";
        assertEquals(expected, systemUpdateBody.asText());
    }
    @Test
    void testSystemUpdateSubject() {
        SystemUpdateSubject systemUpdateSubject = new SystemUpdateSubject();
        String expected = "Timekeeping: system update";
        assertEquals(expected, systemUpdateSubject.asSubject());
    }
    @Test
    void testRecoverPasswordBody() {
        RecoverPasswordBody recoverPasswordBody = new RecoverPasswordBody(getTestUser(), "PASSWORD");
        String expected = "Hello, asfd asdf,\nsomeone reset password for your account testuser.\n" +
                "Here is your new password: PASSWORD.\n" +
                "Note: for security reason, you must change your password after logging in.";
        assertEquals(expected, recoverPasswordBody.asText());
    }
    @Test
    void testRecoverPasswordSubject() {
        RecoverPasswordSubject recoverPasswordSubject = new RecoverPasswordSubject();
        String expected = "Timekeeping: reset password";
        assertEquals(expected, recoverPasswordSubject.asSubject());
    }
    @Test
    void testAccountUpdateFactory() {
        NotificationFactory accountUpdateFactory = new NotificationFactories().accountUpdateFactory(getTestUser());
        String expectedBody = "Hello, asfd asdf,\nyour account testuser was updated.";
        String expectedSubject = "Timekeeping: your account update";
        assertEquals(expectedBody, accountUpdateFactory.createBody());
        assertEquals(expectedSubject, accountUpdateFactory.createSubject());
    }
    @Test
    void testRequestUpdateFactory() {
        NotificationFactory requestUpdateFactory = new NotificationFactories().requestUpdateFactory(getTestUser(), getTestRequestToAdd(), "has been approved");
        String expectedBody = "Hello, asfd asdf,\nthe request from your account testuser to add activity test activity has been approved.";
        String expectedSubject = "Timekeeping: your request update";
        assertEquals(expectedBody, requestUpdateFactory.createBody());
        assertEquals(expectedSubject, requestUpdateFactory.createSubject());
    }
    @Test
    void testSystemUpdateFactory() {
        NotificationFactory systemUpdateFactory = new NotificationFactories().systemUpdateFactory(getTestUser(), "some changes were made");
        String expectedBody = "Hello, asfd asdf,\nsystem update connected with your account testuser was made: some changes were made.";
        String expectedSubject = "Timekeeping: system update";
        assertEquals(expectedBody, systemUpdateFactory.createBody());
        assertEquals(expectedSubject, systemUpdateFactory.createSubject());
    }
    @Test
    void testRecoverPasswordFactory() {
        NotificationFactory recoverPasswordFactory = new NotificationFactories().recoverPasswordFactory(getTestUser(), "PASSWORD");
        String expectedBody = "Hello, asfd asdf,\nsomeone reset password for your account testuser.\n" +
                "Here is your new password: PASSWORD.\n" +
                "Note: for security reason, you must change your password after logging in.";
        String expectedSubject = "Timekeeping: reset password";
        assertEquals(expectedBody, recoverPasswordFactory.createBody());
        assertEquals(expectedSubject, recoverPasswordFactory.createSubject());
    }
}
