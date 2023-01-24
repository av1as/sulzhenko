package Service;

import com.sulzhenko.DTO.ActivityDTO;
import com.sulzhenko.DTO.UserDTO;
import com.sulzhenko.model.services.ServiceException;
import com.sulzhenko.model.services.UserActivityService;
import com.sulzhenko.model.services.implementation.UserActivityServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
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
        UserDTO userDTO = new UserDTO.Builder()
                .withLogin("login")
                .withPassword("password")
                .withEmail("email")
                .withFirstName("fn")
                .withLastName("ln")
                .withRole("system user")
                .withStatus("active")
                .withNotification("on")
                .build();
        ActivityDTO activityDTO = new ActivityDTO("activity");
        assertThrows(ServiceException.class, ()-> userActivityService.setAmount(userDTO, activityDTO, -5));
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
        HttpServletRequest request = mock(HttpServletRequest.class);
        UserDTO userDTO = mock(UserDTO.class);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            when(userDTO.getLogin()).thenReturn("test user");
            ResultSet rs = mock(ResultSet.class);
            when(ignored.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);
            assertDoesNotThrow(() -> userActivityService.listUserActivitiesSorted(request, userDTO));
            assertEquals(0, userActivityService.listUserActivitiesSorted(request, userDTO).size());
        }
    }
    @Test
    void testSQLExceptionListUserActivitiesSorted() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityService userActivityService = new UserActivityServiceImpl(dataSource);
        HttpServletRequest request = mock(HttpServletRequest.class);
        UserDTO userDTO = mock(UserDTO.class);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            when(dataSource.getConnection()).thenThrow(new SQLException());
            assertThrows(ServiceException.class, () -> userActivityService.listUserActivitiesSorted(request, userDTO));
        }
    }
    @Test
    void testListUserActivitiesBriefSorted() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityService userActivityService = new UserActivityServiceImpl(dataSource);
        HttpServletRequest request = mock(HttpServletRequest.class);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(ignored.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);
            assertDoesNotThrow(() -> userActivityService.listUserActivitiesBriefSorted(request));
            assertEquals(0, userActivityService.listUserActivitiesBriefSorted(request).size());
        }
    }
    @Test
    void testSQLExceptionListUserActivitiesBriefSorted() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityService userActivityService = new UserActivityServiceImpl(dataSource);
        HttpServletRequest request = mock(HttpServletRequest.class);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            when(dataSource.getConnection()).thenThrow(new SQLException());
            assertThrows(ServiceException.class, () -> userActivityService.listUserActivitiesBriefSorted(request));
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
}
