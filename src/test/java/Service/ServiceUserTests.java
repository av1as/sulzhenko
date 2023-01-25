package Service;

import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.DAO.UserDAO;
import com.sulzhenko.model.DAO.implementation.UserDAOImpl;
import com.sulzhenko.DTO.UserDTO;
import com.sulzhenko.model.entity.User;
import com.sulzhenko.model.services.ServiceException;
import com.sulzhenko.model.services.UserService;
import com.sulzhenko.model.services.implementation.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static model.DAOTestUtils.getTestUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class ServiceUserTests {
    @Test
    void testGetUser() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            User resultUser = userService.getUser("testuser");
            assertNotNull(resultUser);
            assertEquals(getTestUser(), resultUser);
        }
    }
    @Test
    void testGetUserEmpty() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);
            User resultUser = userService.getUser("testuser");
            assertNull(resultUser);
        }
    }
    @Test
    void testSQLExceptionGetUser() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userService.getUser("testuser"));
    }
    @Test
    void testGetUserDTO() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            prepareResultSet(rs);
            UserDTO actual = userService.getUserDTO("testuser");
            UserDTO expected = new UserDTO.Builder()
                    .withAccount(1L)
                    .withLogin("testuser")
                    .withPassword("asdf")
                    .withEmail("me@me.me")
                    .withFirstName("asdf")
                    .withLastName("asdf")
                    .withRole("system user")
                    .withStatus("active")
                    .withNotification("on")
                    .build();
            assertNotNull(actual);
            assertEquals(expected, actual);
        }
    }
    @Test
    void testGetUserDTOEmpty() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);
            UserDTO actual = userService.getUserDTO("testuser");
            assertNull(actual);
        }
    }
    @Test
    void testSQLExceptionGetUserDTO() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userService.getUserDTO("testuser"));
    }
    @Test
    void testSQLExceptionAddUser() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        UserDTO userDTO = new UserDTO.Builder()
                .withAccount(1L)
                .withLogin("testuser")
                .withPassword("asdf")
                .withEmail("me@me.me")
                .withFirstName("asdf")
                .withLastName("asdf")
                .withRole("system user")
                .withStatus("active")
                .withNotification("on")
                .build();
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(ServiceException.class, () -> userService.addUser(userDTO,"asdf"));
    }
    @Test
    void testSQLExceptionUpdateUser() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        UserDTO userDTO = new UserDTO.Builder()
                .withAccount(1L)
                .withLogin("testuser")
                .withPassword("asdf")
                .withEmail("me@me.me")
                .withFirstName("asdf")
                .withLastName("asdf")
                .withRole("system user")
                .withStatus("active")
                .withNotification("on")
                .build();
        String[] param = {"updated user2", "user2@domen.com", "password2", "updated first",
                "updated last", "administrator", "active", "off"};
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userService.updateUser(userDTO,param));
    }
    @Test
    void testSQLExceptionRecoverPassword() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userService.recoverPassword("login"));
    }
    @Test
    void testSQLExceptionAdminUpdateUser() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        UserDTO userDTO = new UserDTO.Builder()
                .withAccount(1L)
                .withLogin("testuser")
                .withPassword("asdf")
                .withEmail("me@me.me")
                .withFirstName("asdf")
                .withLastName("asdf")
                .withRole("system user")
                .withStatus("active")
                .withNotification("on")
                .build();
        String[] param = {"updated user2", "user2@domen.com", "password2", "updated first",
                "updated last", "administrator", "active", "off"};
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(ServiceException.class, () -> userService.adminUpdateUser(userDTO,param));
    }
    @Test
    void testSQLExceptionDeleteUser() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        User user = getTestUser();
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userService.deleteUser(user));
    }
    @Test
    void testViewAllSystemUsers() throws DAOException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        assertDoesNotThrow(()-> userService.viewAllSystemUsers(0, 0));
    }
    @Test
    void testSQLExceptionViewAllSystemUsers() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userService.viewAllSystemUsers(0, 1));
    }
    @Test
    void testViewAllActiveUsers() throws DAOException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        assertDoesNotThrow(()-> userService.viewAllActiveUsers(0, 0));
    }
    @Test
    void testSQLExceptionViewAllActiveUsers() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userService.viewAllActiveUsers(0, 1));
    }
    @Test
    void testViewAllInactiveUsers() throws DAOException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        assertDoesNotThrow(()-> userService.viewAllInactiveUsers(0, 0));
    }
    @Test
    void testSQLExceptionViewAllInactiveUsers() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userService.viewAllInactiveUsers(0, 1));
    }
    @Test
    void testViewAllDeactivatedUsers() throws DAOException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        assertDoesNotThrow(()-> userService.viewAllDeactivatedUsers(0, 0));
    }
    @Test
    void testSQLExceptionViewAllDeactivatedUsers() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userService.viewAllDeactivatedUsers(0, 1));
    }
    @Test
    void testSQLExceptionIsLoginAvailable() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> userService.isLoginAvailable("test user"));
    }
    @Test
    void testSQLExceptionIsRoleCorrect() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(ServiceException.class, () -> userService.isRoleCorrect("role"));
    }
    @Test
    void testSQLExceptionIsStatusCorrect() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(ServiceException.class, () -> userService.isStatusCorrect("status"));
    }
    @Test
    void testIsUpdateCorrect(){
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        String[] param = {"login", "asfd@aasfd.com", "Password@2", "Asfd", "Asdf"};
        assertDoesNotThrow(()-> userService.isUpdateCorrect(param, "Password@2"));
    }
    @Test
    void testIsUpdateCorrectIncorrect(){
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        String[] param = {"login", "asfd@aasfd.com", "Password", "Asfd", "Asdf"};
        assertThrows(ServiceException.class, ()-> userService.isUpdateCorrect(param, "Password@2"));
    }
    @Test
    void testIsAdminUpdateCorrect(){
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        String[] param = {"login", "asfd@aasfd.com", "Password@2", "Asfd", "Asdf"};
        assertDoesNotThrow(()-> userService.isAdminUpdateCorrect(param));
    }
    @Test
    void testIsAdminUpdateCorrectIncorrect(){
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        String[] param = {"login", "asfdaasfd.com", "Password@2", "Asfd", "Asdf"};
        assertThrows(ServiceException.class, ()-> userService.isAdminUpdateCorrect(param));
    }
    @Test
    void testAreFieldsIncorrect() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        UserDAO userDAO = mock(UserDAOImpl.class);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);
            doReturn(null).when(userDAO).get(anyString(), anyString());
            assertEquals("wrong.login", userService.areFieldsIncorrect("login", "password"));
        }
    }
    @Test
    void testAreFieldsBlankNullLogin() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);
            assertEquals("empty.login", userService.areFieldsBlank(null, "asfd"));
        }
    }

    @Test
    void testAreFieldsBlankNullPassword() throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);
            assertEquals("empty.password", userService.areFieldsBlank("login", null));
        }
    }
    @ParameterizedTest
    @ValueSource(strings = {"active", "inactive", "deactivated", "default"})
    void testGetNumberOfRecords(String status) throws DAOException, SQLException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        try (PreparedStatement stmt = prepareMocks(dataSource)) {
            ResultSet rs = mock(ResultSet.class);
            when(stmt.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);
            assertEquals(0, userService.getNumberOfRecords(status));
        }
    }



    @Test
    void testGetErrorMessageUpdateDifferentPasswords() throws DAOException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        assertEquals("different.passwords", userService.getErrorMessageUpdate("test user",
                "oldPassword", "newPassword1", "newPassword2"));
    }
    @Test
    void testGetErrorMessageUpdateWrongLogin() throws DAOException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        assertEquals("wrong.login", userService.getErrorMessageUpdate(null,
                "oldPassword", "newPassword", "newPassword"));
    }
    @Test
    void testValidateNewUserDifferentPasswords() throws DAOException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        User user = getTestUser();
        assertEquals("different.passwords", userService.validateNewUser(user, "newPassword"));
    }
    @Test
    void testValidateNewUserWrongPassword() throws DAOException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        User user = getTestUser();
        assertEquals("password.requirements", userService.validateNewUser(user, "asdf"));
    }
    @Test
    void testValidateUserUpdateWrongPassword() throws DAOException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        User user = getTestUser();
        assertEquals("password.requirements", userService.validateUserUpdate(user));
    }
    @Test
    void testValidateAdminUserUpdate() throws DAOException {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserServiceImpl(dataSource);
        User user = getTestUser();
        assertNull(userService.validateAdminUserUpdate(user));
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
