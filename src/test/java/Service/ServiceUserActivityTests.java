package Service;

import com.sulzhenko.DTO.UserDTO;
import com.sulzhenko.model.entity.Activity;
import com.sulzhenko.model.entity.Category;
import com.sulzhenko.model.entity.User;
import com.sulzhenko.model.services.ServiceException;
import com.sulzhenko.model.services.UserActivityService;
import com.sulzhenko.model.services.implementation.UserActivityServiceImpl;
import org.junit.jupiter.api.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class ServiceUserActivityTests {
    @Test
    void testSetAmountNegative() {
        DataSource dataSource = mock(DataSource.class);
        UserActivityService userActivityService = new UserActivityServiceImpl(dataSource);
        assertThrows(ServiceException.class,
                ()-> userActivityService.setAmount("login", "activity", -5));
    }
    @Test
    void testSQLExceptionGetNumberOfRecords() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityService userActivityService = new UserActivityServiceImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(ServiceException.class, ()-> userActivityService.getNumberOfRecords("test user"));
    }
    @Test
    void testListAllUserActivitiesSorted() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityService userActivityService = new UserActivityServiceImpl(dataSource);
//        HttpServletRequest request = mock(HttpServletRequest.class);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(ignored.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);
            assertDoesNotThrow(() -> userActivityService.listAllUserActivitiesSorted("1"));
            assertEquals(0, userActivityService.listAllUserActivitiesSorted("1").size());
        }
    }
    @Test
    void testSQLExceptionListAllUserActivitiesSorted() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityService userActivityService = new UserActivityServiceImpl(dataSource);
//        HttpServletRequest request = mock(HttpServletRequest.class);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            when(dataSource.getConnection()).thenThrow(new SQLException());
            assertThrows(ServiceException.class, () -> userActivityService.listAllUserActivitiesSorted("1"));
        }
    }
    @Test
    void testListUserActivitiesSorted() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityService userActivityService = new UserActivityServiceImpl(dataSource);
        UserDTO userDTO = mock(UserDTO.class);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            when(userDTO.getLogin()).thenReturn("test user");
            ResultSet rs = mock(ResultSet.class);
            when(ignored.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);
            assertDoesNotThrow(() -> userActivityService.listUserActivitiesSorted("1", userDTO));
            assertEquals(0, userActivityService.listUserActivitiesSorted("1", userDTO).size());
        }
    }
    @Test
    void testSQLExceptionListUserActivitiesSorted() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityService userActivityService = new UserActivityServiceImpl(dataSource);
        UserDTO userDTO = mock(UserDTO.class);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            when(dataSource.getConnection()).thenThrow(new SQLException());
            assertThrows(ServiceException.class, () -> userActivityService.listUserActivitiesSorted("1", userDTO));
        }
    }

    @Test
    void testIfRequestToAddExists() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityService userActivityService = new UserActivityServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            assertTrue(userActivityService.isRequestToAddExists(getTestUser(), getTestActivity()));
        }
    }
    @Test
    void testIfRequestToRemoveExists() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityService userActivityService = new UserActivityServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareRemoveResultSet(rs);
            assertTrue(userActivityService.isRequestToRemoveExists(getTestUser(), getTestActivity()));
        }
    }
    private PreparedStatement prepareMocks(DataSource dataSource) throws SQLException {
        Connection con = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(dataSource.getConnection()).thenReturn(con);
        when(con.prepareStatement(isA(String.class))).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setObject(isA(int.class), isA(Object.class));
        doNothing().when(preparedStatement).setInt(isA(int.class), isA(int.class));
        doNothing().when(preparedStatement).setLong(isA(int.class), isA(long.class));

        doNothing().when(preparedStatement).setString(isA(int.class), isA(String.class));
        doNothing().when(preparedStatement).close();
        when(preparedStatement.execute()).thenReturn(true);
        return preparedStatement;
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
        return new Activity.Builder()
                .withId(1L)
                .withName("test activity")
                .withCategory(getTestCategory())
                .build();
    }
    private static void prepareResultSet(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong(1)).thenReturn(1L);
        when(resultSet.getString(2)).thenReturn("testuser");
        when(resultSet.getString(3)).thenReturn("test activity");
        when(resultSet.getString(4)).thenReturn("add");
        when(resultSet.getString(5)).thenReturn("asap");

    }
    private static void prepareRemoveResultSet(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong(1)).thenReturn(1L);
        when(resultSet.getString(2)).thenReturn("testuser");
        when(resultSet.getString(3)).thenReturn("test activity");
        when(resultSet.getString(4)).thenReturn("remove");
        when(resultSet.getString(5)).thenReturn("asap");
    }
}
