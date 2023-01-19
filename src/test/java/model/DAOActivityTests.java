package model;

import com.sulzhenko.model.DAO.ActivityDAO;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.DAO.implementation.ActivityDAOImpl;
import com.sulzhenko.model.entity.Activity;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.sulzhenko.model.DAO.SQLQueries.ActivityQueries.GET_ACTIVITIES_BY_CATEGORY;
import static com.sulzhenko.model.DAO.SQLQueries.ActivityQueries.GET_ACTIVITY_BY_ID;
import static model.DAOTestUtils.getTestActivity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class DAOActivityTests {
    @Test
    void testEquality() {
        Activity activity1 = getTestActivity();
        Activity activity2 = getTestActivity();
        assertEquals(activity1, activity2, "Two activities must be equaled if their names are equaled");
    }
    @Test
    void testSave() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityDAO activityDAO = new ActivityDAOImpl(dataSource);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> activityDAO.save(getTestActivity()));
        }
    }
    @Test
    void testSqlExceptionSave() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityDAO activityDAO = new ActivityDAOImpl(dataSource);
        Activity activity = getTestActivity();
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> activityDAO.save(activity));
    }
    @Test
    void testGet() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityDAO activityDAO = new ActivityDAOImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            Activity resultActivity = activityDAO.get(1L, GET_ACTIVITY_BY_ID).orElse(null);
            assertNotNull(resultActivity);
            assertEquals(getTestActivity(), resultActivity);
        }
    }
    @Test
    void testGetById() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityDAO activityDAO = new ActivityDAOImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            Activity resultActivity = activityDAO.getById(1L);
            assertNotNull(resultActivity);
            assertEquals(getTestActivity(), resultActivity);
        }
    }
    @Test
    void testGetByIdEmpty() throws SQLException, DAOException {
        DataSource dataSource = mock(DataSource.class);
        ActivityDAO activityDAO = new ActivityDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            Activity resultActivity = activityDAO.getById(1L);
            assertNull(resultActivity);
        }
    }
    @Test
    void testSqlExceptionGetById() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityDAO activityDAO = new ActivityDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> activityDAO.getById(1L));
    }
    @Test
    void testGetByName() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityDAO activityDAO = new ActivityDAOImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            Activity resultActivity = activityDAO.getByName("test activity");
            assertNotNull(resultActivity);
            assertEquals(getTestActivity(), resultActivity);
        }
    }
    @Test
    void testGetByNameEmpty() throws SQLException, DAOException {
        DataSource dataSource = mock(DataSource.class);
        ActivityDAO activityDAO = new ActivityDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            Activity resultActivity = activityDAO.getByName("test activity");
            assertNull(resultActivity);
        }
    }
    @Test
    void testSqlExceptionGetByName() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityDAO activityDAO = new ActivityDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> activityDAO.getByName("test activity"));
    }
    @Test
    void testGetAll() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityDAO activityDAO = new ActivityDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            List<Activity> activities = activityDAO.getAll();
            assertEquals(1, activities.size());
            assertEquals(getTestActivity(), activities.get(0));
        }
    }
    @Test
    void testGetEmptyAll() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityDAO activityDAO = new ActivityDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            List<Activity> activities = activityDAO.getAll();
            assertEquals(0, activities.size());
        }
    }
    @Test
    void testSqlExceptionGetAll() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityDAO activityDAO = new ActivityDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, activityDAO::getAll);
    }
    @Test
    void testList() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityDAO activityDAO = new ActivityDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            List<Activity> activities = activityDAO.getList(1L, GET_ACTIVITIES_BY_CATEGORY);
            assertEquals(1, activities.size());
            assertEquals(getTestActivity(), activities.get(0));
        }
    }
    @Test
    void testGetEmptyList() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityDAO activityDAO = new ActivityDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            List<Activity> activities = activityDAO.getList(1L, GET_ACTIVITIES_BY_CATEGORY);
            assertEquals(0, activities.size());
        }
    }
    @Test
    void testSqlExceptionGetList() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityDAO activityDAO = new ActivityDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> activityDAO.getList(1L, GET_ACTIVITIES_BY_CATEGORY));
    }
    @Test
    void testUpdate() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityDAO activityDAO = new ActivityDAOImpl(dataSource);
        String[] param = {"updated activity", "updated category"};
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> activityDAO.update(getTestActivity(), param));
        }
    }
    @Test
    void testSqlExceptionUpdate() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityDAO activityDAO = new ActivityDAOImpl(dataSource);
        Activity activity = getTestActivity();
        String[] param = {"updated activity", "updated category"};
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> activityDAO.update(activity, param));
    }
    @Test
    void testDelete() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityDAO activityDAO = new ActivityDAOImpl(dataSource);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> activityDAO.delete(getTestActivity()));
        }
    }
    @Test
    void testSqlExceptionDelete() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ActivityDAO activityDAO = new ActivityDAOImpl(dataSource);
        Activity activity = getTestActivity();
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> activityDAO.delete(activity));
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
        when(resultSet.getLong(1)).thenReturn(1L);
    }
}
