package Service;

import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.DTO.UserActivityDTO;
import com.sulzhenko.model.services.ReportService;
import com.sulzhenko.model.services.implementation.ReportServiceImpl;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

class ServiceReportTests {
    @Test
    void testViewReportPage() throws DAOException {
        DataSource dataSource = mock(DataSource.class);
        ReportService reportService = new ReportServiceImpl(dataSource);
        List<UserActivityDTO> activities = new ArrayList<>();
        assertDoesNotThrow(()-> reportService.viewReportPage(activities));
    }
}
