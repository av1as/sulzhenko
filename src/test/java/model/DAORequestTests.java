package model;

import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.DAO.RequestDAO;
import com.sulzhenko.model.DAO.implementation.RequestDAOImpl;
import com.sulzhenko.model.entity.Request;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.sulzhenko.model.DAO.SQLQueries.RequestQueries.*;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static model.DAOTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class DAORequestTests {
    @Test
    void testEquality() {
        Request request1 = getTestRequestToAdd();
        Request request2 = getTestRequestToAdd();
        assertEquals(request1, request2, "Two requests must be equaled if their accounts," +
                "activities' id and actions to do are equaled");
    }
    @Test
    void testNonEquality() {
        Request request1 = getTestRequestToAdd();
        Request request2 = getTestRequestToRemove();
        assertNotEquals(request1, request2, "Two requests must be equaled if their accounts," +
                "activities' id and actions to do are equaled");
    }
    @Test
    void testSave() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            when(dataSource.getConnection().prepareStatement(INSERT_REQUEST, RETURN_GENERATED_KEYS))
                    .thenReturn(ignored);
            ResultSet rs = mock(ResultSet.class);
            when(ignored.getGeneratedKeys()).thenReturn(rs);
            assertDoesNotThrow(() -> requestDAO.save(getTestRequestToAdd()));
        }
    }
    @Test
    void testSqlExceptionSave() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        Request request = getTestRequestToAdd();
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> requestDAO.save(request));
    }
    @Test
    void testGet() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            Request resultRequest = requestDAO.get(1L, GET_REQUEST_BY_ID).orElse(null);
            assertNotNull(resultRequest);
            assertEquals(getTestRequestToAdd(), resultRequest);
        }
    }
    @Test
    void testGetById() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            Request resultRequest = requestDAO.getById(1L);
            assertNotNull(resultRequest);
            assertEquals(getTestRequestToAdd(), resultRequest);
        }
    }
    @Test
    void testGetByIdEmpty() throws SQLException, DAOException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            Request resultRequest = requestDAO.getById(1L);
            assertNull(resultRequest);
        }
    }
    @Test
    void testSqlExceptionGetById() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> requestDAO.getById(1L));
    }
    @Test
    void testGetAll() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            List<Request> requests = requestDAO.getAll();
            assertEquals(1, requests.size());
            assertEquals(getTestRequestToAdd(), requests.get(0));
        }
    }
    @Test
    void testGetEmptyAll() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            List<Request> requests = requestDAO.getAll();
            assertEquals(0, requests.size());
        }
    }
    @Test
    void testSqlExceptionGetAll() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, requestDAO::getAll);
    }
    @Test
    void testList() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            List<Request> requests = requestDAO.getList("testUser", GET_REQUESTS_BY_LOGIN);
            assertEquals(1, requests.size());
            assertEquals(getTestRequestToAdd(), requests.get(0));
        }
    }
    @Test
    void testGetEmptyList() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            List<Request> requests = requestDAO.getList("testUser", GET_REQUESTS_BY_LOGIN);
            assertEquals(0, requests.size());
        }
    }
    @Test
    void testSqlExceptionGetList() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> requestDAO.getList("testUser", GET_REQUESTS_BY_LOGIN));
    }
    @Test
    void testGetByLogin() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            List<Request> requests = requestDAO.getList("testUser", GET_REQUESTS_BY_LOGIN);
            assertEquals(1, requests.size());
            assertEquals(getTestRequestToAdd(), requests.get(0));
        }
    }
    @Test
    void testGetEmptyGetByLogin() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            List<Request> requests = requestDAO.getList("testUser", GET_REQUESTS_BY_LOGIN);
            assertEquals(0, requests.size());
        }
    }
    @Test
    void testSqlExceptionGetByLogin() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> requestDAO.getList("testUser", GET_REQUESTS_BY_LOGIN));
    }
    @Test
    void testGetByActivity() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            List<Request> requests = requestDAO.getByActivity("test activity");
            assertEquals(1, requests.size());
            assertEquals(getTestRequestToAdd(), requests.get(0));
        }
    }
    @Test
    void testGetEmptyGetByActivity() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            List<Request> requests = requestDAO.getByActivity("test activity");
            assertEquals(0, requests.size());
        }
    }
    @Test
    void testSqlExceptionGetByActivity() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> requestDAO.getByActivity("test activity"));
    }
    @Test
    void testGetByActionToDo() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            List<Request> requests = requestDAO.getByActionToDo("add");
            assertEquals(1, requests.size());
            assertEquals(getTestRequestToAdd(), requests.get(0));
        }
    }
    @Test
    void testGetEmptyGetByActionToDo() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            List<Request> requests = requestDAO.getByActionToDo("add");
            assertEquals(0, requests.size());
        }
    }
    @Test
    void testSqlExceptionGetByActionToDo() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> requestDAO.getByActionToDo("add"));
    }
    @Test
    void testUpdate() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        String[] param = {"testuser", "test activity", "remove", "asap"};
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> requestDAO.update(getTestRequestToAdd(), param));
        }
    }
    @Test
    void testSqlExceptionUpdate() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        Request request = getTestRequestToAdd();
        String[] param = {"testuser", "test activity", "remove", "asap"};
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> requestDAO.update(request, param));
    }
    @Test
    void testDelete() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> requestDAO.delete(getTestRequestToAdd()));
        }
    }
    @Test
    void testSqlExceptionDelete() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        Request request = getTestRequestToAdd();
        RequestDAO requestDAO = new RequestDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> requestDAO.delete(request));
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
        when(resultSet.getString(3)).thenReturn("test activity");
        when(resultSet.getString(4)).thenReturn("add");
        when(resultSet.getString(5)).thenReturn("asap");
    }
}
