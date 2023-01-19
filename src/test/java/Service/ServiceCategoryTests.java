package Service;

import com.sulzhenko.model.DAO.CategoryDAO;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.DAO.implementation.CategoryDAOImpl;
import com.sulzhenko.model.entity.Category;
import com.sulzhenko.model.services.CategoryService;
import com.sulzhenko.model.services.implementation.CategoryServiceImpl;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static model.DAOTestUtils.getTestCategory;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class ServiceCategoryTests {
    @Test
    void testGetCategory() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryService categoryService = new CategoryServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            Category resultCategory = categoryService.getCategory("test category");
            assertNotNull(resultCategory);
            assertEquals(getTestCategory(), resultCategory);
        }
    }
    @Test
    void testGetCategoryEmpty() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryService categoryService = new CategoryServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);
            Category resultCategory = categoryService.getCategory("test category");
            assertNull(resultCategory);
        }
    }
    @Test
    void testIsCategoryNameUnique() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryService categoryService = new CategoryServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);
            Category resultCategory = categoryService.getCategory("test category");
            assertNull(resultCategory);
        }
    }
    @Test
    void testAddCategory() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryService categoryService = new CategoryServiceImpl(dataSource);
        CategoryDAO categoryDAO = mock(CategoryDAOImpl.class);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);
            doNothing().when(categoryDAO).save(isA(Category.class));
            assertDoesNotThrow(() -> categoryService.addCategory("test category"));
        }
    }
    @Test
    void testSqlExceptionDeleteCategory() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryService categoryService = new CategoryServiceImpl(dataSource);
        CategoryDAO categoryDAO = mock(CategoryDAOImpl.class);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            doNothing().when(categoryDAO).delete(isA(Category.class));
            when(dataSource.getConnection()).thenThrow(new SQLException());
            assertThrows(DAOException.class, () -> categoryService.deleteCategory("test category"));
        }
    }
    @Test
    void testSqlExceptionUpdateCategory() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryService categoryService = new CategoryServiceImpl(dataSource);
        CategoryDAO categoryDAO = mock(CategoryDAOImpl.class);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            doNothing().when(categoryDAO).update(isA(Category.class), any());
            when(dataSource.getConnection()).thenThrow(new SQLException());
            assertThrows(DAOException.class, () -> categoryService.updateCategory("test category", "test2"));
        }
    }
    @Test
    void testGetAllCategories() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryService categoryService = new CategoryServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            assertDoesNotThrow(categoryService::getAllCategories);
        }
    }
    @Test
    void testViewAllCategories() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryService categoryService = new CategoryServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            assertDoesNotThrow(()-> categoryService.viewAllCategories(0, 0));
        }
    }
    @Test
    void testGetNumberOfCategories() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryService categoryService = new CategoryServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            assertDoesNotThrow(categoryService::getNumberOfCategories);
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
        when(resultSet.getString(2)).thenReturn("test category");
    }
}
