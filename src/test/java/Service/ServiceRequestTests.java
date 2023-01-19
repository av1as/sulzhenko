package Service;

import com.sulzhenko.model.DTO.RequestDTO;
import com.sulzhenko.model.entity.Request;
import com.sulzhenko.model.services.RequestService;
import com.sulzhenko.model.services.ServiceException;
import com.sulzhenko.model.services.implementation.RequestServiceImpl;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import static model.DAOTestUtils.getTestRequestToAdd;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class ServiceRequestTests {
    @Test
    void testGetRequest() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestService requestService = new RequestServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            Request resultRequest = requestService.getRequest(1L);
            assertNotNull(resultRequest);
            assertEquals(getTestRequestToAdd(), resultRequest);
        }
    }
    @Test
    void testGetAllRequest() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestService requestService = new RequestServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            List<Request> requests = requestService.getAllRequest();
            assertEquals(1, requests.size());
            assertEquals(getTestRequestToAdd(), requests.get(0));
        }
    }
    @Test
    void testGetRequestsToAdd() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestService requestService = new RequestServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            List<Request> requests = requestService.getRequestsToAdd();
            assertEquals(1, requests.size());
            assertEquals(getTestRequestToAdd(), requests.get(0));
        }
    }
    @Test
    void testGetRequestsToRemove() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestService requestService = new RequestServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);
            List<Request> requests = requestService.getRequestsToRemove();
            assertEquals(0, requests.size());
        }
    }
    @Test
    void testSqlExceptionApproveRequest() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestService requestService = new RequestServiceImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        Request request = getTestRequestToAdd();
        assertThrows(ServiceException.class, ()-> requestService.approveRequest(request));
    }
    @Test
    void testDeleteRequest() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestService requestService = new RequestServiceImpl(dataSource);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> requestService.deleteRequest(getTestRequestToAdd()));
        }
    }
    @Test
    void testSqlExceptionAddRequest() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestService requestService = new RequestServiceImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        RequestDTO requestDTO = new RequestDTO.Builder()
                .withId(1L)
                .withLogin("login")
                .withActivityName("activity")
                .withActionToDo("add")
                .build();
        assertThrows(ServiceException.class, ()-> requestService.addRequest(requestDTO));
    }
    @Test
    void testSqlExceptionUpdateRequest() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestService requestService = new RequestServiceImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        Request request = getTestRequestToAdd();
        String[] param = {"test user", "test activity", "remove", "asap"};
        assertThrows(ServiceException.class, ()-> requestService.updateRequest(request, param));
    }
    @Test
    void testViewAllRequests() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestService requestService = new RequestServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);
            List<RequestDTO> requests = requestService.viewAllRequests(0, 0);
            assertEquals(0, requests.size());
        }
    }
    @Test
    void testViewRequestsToAdd() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestService requestService = new RequestServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);
            List<RequestDTO> requests = requestService.viewRequestsToAdd(0, 0);
            assertEquals(0, requests.size());
        }
    }
    @Test
    void testViewRequestsToRemove() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        RequestService requestService = new RequestServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);
            List<RequestDTO> requests = requestService.viewRequestsToRemove(0, 0);
            assertEquals(0, requests.size());
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
        when(resultSet.getString(2)).thenReturn("testuser");
        when(resultSet.getString(3)).thenReturn("test activity");
        when(resultSet.getString(4)).thenReturn("add");
        when(resultSet.getString(5)).thenReturn("asap");
    }
}
