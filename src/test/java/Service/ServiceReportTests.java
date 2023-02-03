package Service;

import com.sulzhenko.DTO.UserActivityDTO;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.DAO.UserActivityDAO;
import com.sulzhenko.model.DAO.implementation.UserActivityDAOImpl;
import com.sulzhenko.model.services.ReportService;
import com.sulzhenko.model.services.ServiceException;
import com.sulzhenko.model.services.implementation.ReportServiceImpl;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.sulzhenko.model.DAO.SQLQueries.UserActivityQueries.GET_NUMBER_OF_RECORDS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServiceReportTests {
    @Test
    void testViewReportPage() {
        DataSource dataSource = mock(DataSource.class);
        ReportService reportService = new ReportServiceImpl(dataSource);
        List<UserActivityDTO> activities = new ArrayList<>();
        assertDoesNotThrow(()-> reportService.viewReportPage(activities));
    }
    @Test
    void testGetNumberOfRecords() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportService reportService = new ReportServiceImpl(dataSource);
        UserActivityDAO userActivityDAO = mock(UserActivityDAOImpl.class);
        Connection con = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        when(dataSource.getConnection()).thenReturn(con);
        when(con.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);
        when(userActivityDAO.getNumberOfRecords()).thenReturn(5);
        assertDoesNotThrow(reportService::getNumberOfRecords);
        assertEquals(5, userActivityDAO.getNumberOfRecords());
    }
    @Test
    void testSqlExceptionGetNumberOfRecords() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        ReportService reportService = new ReportServiceImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(ServiceException.class, reportService::getNumberOfRecords);
    }
}
