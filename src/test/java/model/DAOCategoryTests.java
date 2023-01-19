package model;

import com.sulzhenko.model.DAO.CategoryDAO;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.DAO.implementation.CategoryDAOImpl;
import com.sulzhenko.model.entity.Category;

import static com.sulzhenko.model.DAO.SQLQueries.CategoryQueries.*;
import static model.DAOTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

class DAOCategoryTests {
    @Test
    void testEquality() {
        Category category1 = getTestCategory();
        Category category2 = getTestCategory();
        assertEquals(category1, category2, "Two categories must be equaled if their names are equaled");
    }
    @Test
    void testSave() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryDAO categoryDAO = new CategoryDAOImpl(dataSource);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> categoryDAO.save(getTestCategory()));
        }
    }
    @Test
    void testSqlExceptionSave() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryDAO categoryDAO = new CategoryDAOImpl(dataSource);
        Category category = getTestCategory();
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> categoryDAO.save(category));
    }
    @Test
    void testGet() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryDAO categoryDAO = new CategoryDAOImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            Category resultCategory = categoryDAO.get(1L, GET_CATEGORY_BY_ID).orElse(null);
            assertNotNull(resultCategory);
            assertEquals(getTestCategory(), resultCategory);
        }
    }
    @Test
    void testGetById() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryDAO categoryDAO = new CategoryDAOImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            Category resultCategory = categoryDAO.getById(1L).orElse(null);
            assertNotNull(resultCategory);
            assertEquals(getTestCategory(), resultCategory);
        }
    }
    @Test
    void testGetByIdEmpty() throws SQLException, DAOException {
        DataSource dataSource = mock(DataSource.class);
        CategoryDAO categoryDAO = new CategoryDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            Category resultCategory = categoryDAO.getById(1L).orElse(null);
            assertNull(resultCategory);
        }
    }
    @Test
    void testSqlExceptionGetById() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryDAO categoryDAO = new CategoryDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> categoryDAO.getById(1L));
    }
    @Test
    void testGetByName() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryDAO categoryDAO = new CategoryDAOImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            Category resultCategory = categoryDAO.getByName("test category").orElse(null);
            assertNotNull(resultCategory);
            assertEquals(getTestCategory(), resultCategory);
        }
    }
    @Test
    void testGetByNameEmpty() throws SQLException, DAOException {
        DataSource dataSource = mock(DataSource.class);
        CategoryDAO categoryDAO = new CategoryDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            Category resultCategory = categoryDAO.getByName("test category").orElse(null);
            assertNull(resultCategory);
        }
    }
    @Test
    void testSqlExceptionGetByName() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryDAO categoryDAO = new CategoryDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> categoryDAO.getByName("test category"));
    }
    @Test
    void testGetAll() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryDAO categoryDAO = new CategoryDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            List<Category> categories = categoryDAO.getAll();
            assertEquals(1, categories.size());
            assertEquals(getTestCategory(), categories.get(0));
        }
    }
    @Test
    void testGetEmptyAll() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryDAO categoryDAO = new CategoryDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            List<Category> categories = categoryDAO.getAll();
            assertEquals(0, categories.size());
        }
    }
    @Test
    void testSqlExceptionGetAll() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryDAO categoryDAO = new CategoryDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, categoryDAO::getAll);
    }
    @Test
    void testList() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryDAO categoryDAO = new CategoryDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            prepareResultSet(resultSet);
            List<Category> categories = categoryDAO.getList(1L, GET_CATEGORY_BY_ID);
            assertEquals(1, categories.size());
            assertEquals(getTestCategory(), categories.get(0));
        }
    }
    @Test
    void testGetEmptyList() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryDAO categoryDAO = new CategoryDAOImpl(dataSource);
        try (PreparedStatement preparedStatement = prepareMocks(dataSource)) {
            ResultSet resultSet = mock(ResultSet.class);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);
            List<Category> categories = categoryDAO.getList(1L, GET_CATEGORY_BY_ID);
            assertEquals(0, categories.size());
        }
    }
    @Test
    void testSqlExceptionGetList() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryDAO categoryDAO = new CategoryDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> categoryDAO.getList(1L, GET_CATEGORY_BY_ID));
    }
    @Test
    void testUpdate() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryDAO categoryDAO = new CategoryDAOImpl(dataSource);
        String[] param = {"updated category"};
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> categoryDAO.update(getTestCategory(), param));
        }
    }
    @Test
    void testSqlExceptionUpdate() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryDAO categoryDAO = new CategoryDAOImpl(dataSource);
        Category category = getTestCategory();
        String[] param = {"updated category"};
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> categoryDAO.update(category, param));
    }
    @Test
    void testDelete() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        CategoryDAO categoryDAO = new CategoryDAOImpl(dataSource);
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> categoryDAO.delete(getTestCategory()));
        }
    }
    @Test
    void testSqlExceptionDelete() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        Category category = getTestCategory();
        CategoryDAO categoryDAO = new CategoryDAOImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> categoryDAO.delete(category));
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
