package Service;

import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.DTO.UserActivityDTO;
import com.sulzhenko.model.services.ReportService;
import com.sulzhenko.model.services.implementation.ReportServiceImpl;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ServiceReportTests {
    @Test
    void testViewReportPage() throws DAOException {
        ReportService reportService = new ReportServiceImpl();
        List<UserActivityDTO> activities = new ArrayList<>();
        assertDoesNotThrow(()-> reportService.viewReportPage(activities));
    }
}
