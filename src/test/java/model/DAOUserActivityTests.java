package model;

import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.DAO.implementation.UserActivityDAOImpl;
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
        assertThrows(DAOException.class, () -> userActivityDAO.getAmount(getTestUser(), getTestActivity()));
    }
    @Test
    void testSqlExceptionIfUserHasActivity() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityDAO userActivityDAO = new UserActivityDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userActivityDAO.ifUserHasActivity(getTestUser(), getTestActivity()));
    }
    @Test
    void testIfRequestToAddExists() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityDAO userActivityDAO = new UserActivityDAOImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            assertTrue(userActivityDAO.isRequestToAddExists(getTestUser(), getTestActivity()));
        }
    }
    @Test
    void testIfRequestToRemoveExists() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserActivityDAO userActivityDAO = new UserActivityDAOImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareRemoveResultSet(rs);
            assertTrue(userActivityDAO.isRequestToRemoveExists(getTestUser(), getTestActivity()));
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
