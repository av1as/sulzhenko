import com.sulzhenko.DAO.DAOException;
import com.sulzhenko.DAO.DataSource;
import com.sulzhenko.DAO.UserDAO;
import com.sulzhenko.DAO.entity.User;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;

import static com.sulzhenko.DAO.SQLQueries.InitialData.*;

//@RunWith(Parameterized.class)
//public class ExceptionTests {
//    private String login;
//    private String email;
//    private String password;
//    private String firstName;
//    private String lastName;
//    private String role;
//    private String status;
//    private String notifications;
//    private static UserDAO userDAO = new UserDAO();
//    Connection con;
//
//    public ExceptionTests(String login, String email, String password, String firstName, String lastName, String role, String status, String notifications) {
//        this.login = login;
//        this.email = email;
//        this.password = password;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.role = role;
//        this.status = status;
//        this.notifications = notifications;
//    }
//    @Before
//    public void globalSetUp() throws SQLException {
//        con = DataSource.getConnection();
//        con.createStatement().executeUpdate(INITIAL_ROLE_ADMIN);
//        con.createStatement().executeUpdate(INITIAL_ROLE_USER);
//        userDAO.addStatus("active");
//        userDAO.addStatus("inactive");
//        userDAO.addStatus("deactivated");
//
//        userDAO.save(User.builder()
//                .withLogin("wrong user1")
//                .withEmail("a@a.a")
//                .withPassword("asfd")
//                .withRole("administrator")
//                .withStatus("active")
//                .withNotifications("yes")
//                .build());
//    }
//
//    @After
//    public void tearDown() throws SQLException {
//        con.createStatement().executeUpdate("DELETE FROM users WHERE login LIKE '%user%';");
//        con.createStatement().executeUpdate("DELETE FROM roles;");
//        con.createStatement().executeUpdate("DELETE FROM user_status;");
//    }
//
//    @Test(expected = DAOException.class)
//    public void testInsertInvalidUser() {
//        userDAO.save(User.builder()
//                .withLogin(login)
//                .withEmail(email)
//                .withPassword(password)
//                .withFirstName(firstName)
//                .withLastName(lastName)
//                .withRole(role)
//                .withStatus(status)
//                .withNotifications(notifications)
//                .build());
//    }
//
//    @Parameterized.Parameters
//    public static Collection invalidFields() {
//        return Arrays.asList(new String[][] {
//                {"wrong user1", "a@a.a", "asfd", "", "", "system user", "active", "yes"},
//                {"wrong user2", "a@a.a", "asfd", "", "", "unknown role", "active", "yes"},
//                {"wrong user3", "a@a.a", "asfd", "", "", "administrator", "unknown status", "yes"}
//        });
//    }
//}
