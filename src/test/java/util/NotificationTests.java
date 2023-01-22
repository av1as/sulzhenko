package util;

import com.sulzhenko.Util.notifications.*;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.services.ServiceException;
import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import static com.sulzhenko.Util.PaginationUtil.paginate;
import static com.sulzhenko.model.Constants.*;
import static model.DAOTestUtils.getTestRequestToAdd;
import static model.DAOTestUtils.getTestUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NotificationTests {
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
}
