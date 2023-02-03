package model;

import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.DAO.implementation.UserActivityDAOImpl;
import com.sulzhenko.model.entity.Activity;
import com.sulzhenko.model.entity.Category;
import com.sulzhenko.model.entity.User;
import com.sulzhenko.model.services.ServiceException;
import org.junit.jupiter.api.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static model.DAOTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class DAOUserActivityTests {
    @Test
    void testAddActivityToUserWrongAction() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityDAO userActivityDAO = new UserActivityDAOImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            when(stmt.executeUpdate()).thenReturn(0);
            assertDoesNotThrow(() -> userActivityDAO.addActivityToUser(getTestRequestToRemove()));
        }
    }
    @Test
    void testSqlExceptionAddActivityToUser() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityDAO userActivityDAO = new UserActivityDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(SQLException.class, () -> userActivityDAO.addActivityToUser(getTestRequestToAdd()));
    }
    @Test
    void testRemoveActivityWrongAction() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityDAO userActivityDAO = new UserActivityDAOImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            when(stmt.executeUpdate()).thenReturn(0);
            assertDoesNotThrow(() -> userActivityDAO.removeUserActivity(getTestRequestToAdd()));
        }
    }
    @Test
    void testSqlExceptionRemoveActivity() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityDAO userActivityDAO = new UserActivityDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(SQLException.class, () -> userActivityDAO.removeUserActivity(getTestRequestToRemove()));
    }
    @Test
    void testGetAmount() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityDAO userActivityDAO = new UserActivityDAOImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(true).thenReturn(false);
            when(rs.getInt(1)).thenReturn(7);
            assertEquals(7, userActivityDAO.getAmount(getTestUser(), getTestActivity()));
        }
    }
    @Test
    void testSqlExceptionGetAmount() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityDAO userActivityDAO = new UserActivityDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        User user = getTestUser();
        Activity activity = getTestActivity();
        assertThrows(DAOException.class, () -> userActivityDAO.getAmount(user, activity));
    }
    @Test
    void testGetNumberOfRecords() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityDAO userActivityDAO = new UserActivityDAOImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);
            assertEquals(0, userActivityDAO.getNumberOfRecords());
        }
    }
    @Test
    void testSqlExceptionGetNumberOfRecords() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityDAO userActivityDAO = new UserActivityDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, userActivityDAO::getNumberOfRecords);
    }
    @Test
    void testGetNumberOfRecordsByUser() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityDAO userActivityDAO = new UserActivityDAOImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);
            assertEquals(0, userActivityDAO.getNumberOfRecordsByUser("login"));
        }
    }
    @Test
    void testSqlExceptionGetNumberOfRecordsByUser() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityDAO userActivityDAO = new UserActivityDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userActivityDAO.getNumberOfRecordsByUser("login"));
    }
    @Test
    void testSQLExceptionSetAmount() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityDAO userActivityDAO = new UserActivityDAOImpl(dataSource);
        User user = new User.Builder()
                .withLogin("login")
                .withPassword("password")
                .withEmail("email")
                .withFirstName("fn")
                .withLastName("ln")
                .withRole("system user")
                .withStatus("active")
                .withNotification("on")
                .build();
        Activity activity = new Activity.Builder()
                .withName("activity")
                .withCategory(new Category("category")).build();
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, ()-> userActivityDAO.setAmount(user, activity, -5));
    }
    @Test
    void testSqlExceptionIfUserHasActivity() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityDAO userActivityDAO = new UserActivityDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        User user = getTestUser();
        Activity activity = getTestActivity();
        assertThrows(ServiceException.class, () -> userActivityDAO.ifUserHasActivity(user, activity));
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
