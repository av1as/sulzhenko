package Service;

import com.sulzhenko.model.DAO.ActivityDAO;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.DAO.implementation.ActivityDAOImpl;
import com.sulzhenko.model.entity.Activity;
import com.sulzhenko.model.services.ActivityService;
import com.sulzhenko.model.services.ServiceException;
import com.sulzhenko.model.services.implementation.ActivityServiceImpl;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static model.DAOTestUtils.getTestActivity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class ServiceActivityTests {
    @Test
    void testGetActivity() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityService activityService = new ActivityServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            Activity resultActivity = activityService.getActivity("test activity");
            assertNotNull(resultActivity);
            assertEquals(getTestActivity(), resultActivity);
        }
    }
    @Test
    void testGetActivityEmpty() throws SQLException, DAOException {
        DataSource dataSource = mock(DataSource.class);
        ActivityService activityService = new ActivityServiceImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            Activity resultActivity = activityService.getActivity("test activity");
            assertNull(resultActivity);
        }
    }
    @Test
    void testSqlExceptionGetActivity() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityService activityService = new ActivityServiceImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> activityService.getActivity("test activity"));
    }
    @Test
    void testSqlExceptionAddActivity() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityService activityService = new ActivityServiceImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> activityService.addActivity("test activity", "test category"));
    }
    @Test
    void testSqlExceptionUpdateActivity() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityService activityService = new ActivityServiceImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> activityService.updateActivity("test activity", "new name", "test category"));
    }
    @Test
    void testSqlExceptionDeleteActivity() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityService activityService = new ActivityServiceImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> activityService.deleteActivity("test activity"));
    }

    @Test
    void testGetNumberOfRecords() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityService activityService = new ActivityServiceImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            assertEquals(5, activityService.getNumberOfRecords("asdf"));
        }
    }
    @Test
    void testGetNumberOfRecordsEmpty() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityService activityService = new ActivityServiceImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            assertEquals(0, activityService.getNumberOfRecords("asdf"));
        }
    }
    @Test
    void testSqlGetNumberOfRecords() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityService activityService = new ActivityServiceImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(ServiceException.class, () -> activityService.getNumberOfRecords("asdf"));
    }
    @Test
    void testGetListActivitiesSorted() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityService activityService = new ActivityServiceImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareDTOResultSet(resultSet);
            assertEquals(1, activityService.listActivitiesSorted("asdf", "asdf", "asdf", "1").size());
        }
    }
    @Test
    void testListActivitiesSortedEmpty() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityService activityService = new ActivityServiceImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            assertEquals(0, activityService.listActivitiesSorted("asdf", "asdf", "asdf", "1").size());
        }
    }
    @Test
    void testSqlExceptionListActivitiesSorted() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityService activityService = new ActivityServiceImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(ServiceException.class, () -> activityService.listActivitiesSorted("asdf", "asdf", "asdf", "1"));
    }
    @Test
    void testIsNameAvailable() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityService activityService = new ActivityServiceImpl(dataSource);
        ActivityDAO activityDAO = mock(ActivityDAOImpl.class);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            doReturn(null).when(activityDAO).getByName(anyString());
            assertFalse(activityService.isNameAvailable("test activity"));
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
        when(preparedStatement.execute()).thenReturn(true);
        return preparedStatement;
    }
    private static void prepareResultSet(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong(1)).thenReturn(1L);
        when(resultSet.getString(2)).thenReturn("test activity");
        when(resultSet.getLong(3)).thenReturn(1L);
        when(resultSet.getInt(1)).thenReturn(5);
    }
    private static void prepareDTOResultSet(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString(1)).thenReturn("test activity");
        when(resultSet.getString(3)).thenReturn("test category");
        when(resultSet.getInt(2)).thenReturn(1);
    }
}
