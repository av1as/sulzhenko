package model;

import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.DAO.implementation.UserDAOImpl;
import com.sulzhenko.model.entity.User;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static model.DAOTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class DAOUserTests {
    @Test
    void testEquality() {
        User user1 = getTestUser();
        User user2 = getTestUser();
        assertEquals(user1, user2, "Two users must be equaled if their logins are equaled");
    }
    @Test
    void testSave() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> userDAO.save(getTestUser()));
        }
    }
    @Test
    void testSqlExceptionSave() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        User user = getTestUser();
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userDAO.save(user));
    }
    @Test
    void testGet() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            User resultUser = userDAO.get(1L, SQLQueries.UserQueries.GET_USER_BY_ID).orElse(null);
            assertNotNull(resultUser);
            assertEquals(getTestUser(), resultUser);
        }
    }
    @Test
    void testGetById() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            User resultUser = userDAO.getById(1L).orElse(null);
            assertNotNull(resultUser);
            assertEquals(getTestUser(), resultUser);
        }
    }
    @Test
    void testGetByIdEmpty() throws SQLException, DAOException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            User resultUser = userDAO.getById(1L).orElse(null);
            assertNull(resultUser);
        }
    }
    @Test
    void testSqlExceptionGetById() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userDAO.getById(1L));
    }
    @Test
    void testGetByLogin() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            User resultUser = userDAO.getByLogin("testuser").orElse(null);
            assertNotNull(resultUser);
            assertEquals(getTestUser(), resultUser);
        }
    }
    @Test
    void testGetByLoginEmpty() throws SQLException, DAOException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            User resultUser = userDAO.getByLogin("testuser").orElse(null);
            assertNull(resultUser);
        }
    }
    @Test
    void testSqlExceptionGetByLogin() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userDAO.getByLogin("testuser"));
    }
    @Test
    void testGetAll() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            List<User> users = userDAO.getAll();
            assertEquals(1, users.size());
            assertEquals(getTestUser(), users.get(0));
        }
    }
    @Test
    void testGetEmptyAll() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            List<User> users = userDAO.getAll();
            assertEquals(0, users.size());
        }
    }
    @Test
    void testSqlExceptionGetAll() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, userDAO::getAll);
    }
    @Test
    void testList() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            List<User> users = userDAO.getList(1L, SQLQueries.UserQueries.GET_USER_BY_ID);
            assertEquals(1, users.size());
            assertEquals(getTestUser(), users.get(0));
        }
    }
    @Test
    void testGetEmptyList() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            List<User> users = userDAO.getList(1L, SQLQueries.UserQueries.GET_USER_BY_ID);
            assertEquals(0, users.size());
        }
    }
    @Test
    void testSqlExceptionGetList() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userDAO.getList(1L, SQLQueries.UserQueries.GET_USER_BY_ID));
    }
    @Test
    void testGetByEmail() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            List<User> users = userDAO.getByEmail("me@me.me");
            assertEquals(1, users.size());
            assertEquals(getTestUser(), users.get(0));
        }
    }
    @Test
    void testGetEmptyListByEmail() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            List<User> users = userDAO.getByEmail("me@me.me");
            assertEquals(0, users.size());
        }
    }
    @Test
    void testSqlExceptionGetByEmail() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userDAO.getByEmail("me@me.me"));
    }
    @Test
    void testGetByFirstName() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            List<User> users = userDAO.getByFirstName("asdf");
            assertEquals(1, users.size());
            assertEquals(getTestUser(), users.get(0));
        }
    }
    @Test
    void testGetEmptyListByFirstName() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            List<User> users = userDAO.getByFirstName("asdf");
            assertEquals(0, users.size());
        }
    }
    @Test
    void testSqlExceptionGetByFirstName() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userDAO.getByFirstName("asdf"));
    }
    @Test
    void testGetByLastName() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            List<User> users = userDAO.getByLastName("asdf");
            assertEquals(1, users.size());
            assertEquals(getTestUser(), users.get(0));
        }
    }
    @Test
    void testGetEmptyListByLastName() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            List<User> users = userDAO.getByLastName("asdf");
            assertEquals(0, users.size());
        }
    }
    @Test
    void testSqlExceptionGetByLastName() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userDAO.getByLastName("asdf"));
    }
    @Test
    void testGetByRole() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            List<User> users = userDAO.getByRole("system user");
            assertEquals(1, users.size());
            assertEquals(getTestUser(), users.get(0));
        }
    }
    @Test
    void testGetEmptyListByRole() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            List<User> users = userDAO.getByRole("system user");
            assertEquals(0, users.size());
        }
    }
    @Test
    void testSqlExceptionGetByRole() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userDAO.getByRole("system user"));
    }
    @Test
    void testGetByStatus() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            List<User> users = userDAO.getByStatus("active");
            assertEquals(1, users.size());
            assertEquals(getTestUser(), users.get(0));
        }
    }
    @Test
    void testGetEmptyListByStatus() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            List<User> users = userDAO.getByStatus("active");
            assertEquals(0, users.size());
        }
    }
    @Test
    void testSqlExceptionGetByStatus() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userDAO.getByStatus("active"));
    }
    @Test
    void testGetByNotification() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            List<User> users = userDAO.getByNotification("on");
            assertEquals(1, users.size());
            assertEquals(getTestUser(), users.get(0));
        }
    }
    @Test
    void testGetEmptyListByNotification() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            List<User> users = userDAO.getByNotification("on");
            assertEquals(0, users.size());
        }
    }
    @Test
    void testSqlExceptionGetByNotification() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userDAO.getByNotification("on"));
    }
    @Test
    void testUpdate() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        String[] param = {"updated user2", "user2@domen.com", "password2", "updated first",
                "updated last", "administrator", "active", "off"};
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> userDAO.update(getTestUser(), param));
        }
    }
    @Test
    void testSqlExceptionUpdate() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        User user = getTestUser();
        String[] param = {"updated user2", "user2@domen.com", "password2", "updated first",
                "updated last", "administrator", "active", "off"};
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userDAO.update(user, param));
    }
    @Test
    void testDelete() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserDAO userDAO = new UserDAOImpl(dataSource);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> userDAO.delete(getTestUser()));
        }
    }

    @Test
    void testSqlExceptionDelete() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        User user = getTestUser();
        UserDAO userDAO = new UserDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userDAO.delete(user));
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
        when(resultSet.getString(2)).thenReturn("testuser");
        when(resultSet.getString(3)).thenReturn("me@me.me");
        when(resultSet.getString(4)).thenReturn("asdf");
        when(resultSet.getString(5)).thenReturn("asdf");
        when(resultSet.getString(6)).thenReturn("asdf");
        when(resultSet.getString(7)).thenReturn("system user");
        when(resultSet.getString(8)).thenReturn("active");
        when(resultSet.getString(9)).thenReturn("on");
    }
}
