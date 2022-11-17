import com.sulzhenko.DAO.DataSource;
import com.sulzhenko.DAO.UserDAO;
import com.sulzhenko.DAO.entity.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.sulzhenko.DAO.SQLQueries.CreatingTablesQueries.*;
import static com.sulzhenko.DAO.SQLQueries.DropingTablesQueries.*;
import static com.sulzhenko.DAO.SQLQueries.InitialData.*;
import static org.junit.jupiter.api.Assertions.*;


public class UserTests {

//    protected User user = new User();
//    private Integer account;
//    private String login;
//    private String email;
//    private String password;
//    private String firstName;
//    private String lastName;
//    private String role;
//    private String status;
//    private String notifications;
//
//    public UserTests(User user, Integer account, String login, String email, String password, String firstName, String lastName, String role, String status, String notifications) {
//        this.user = user;
//        this.account = account;
//        this.login = login;
//        this.email = email;
//        this.password = password;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.role = role;
//        this.status = status;
//        this.notifications = notifications;
//    }
    private static Connection con;
    private static UserDAO userDAO = new UserDAO();

    @BeforeAll
    static void globalSetUp() throws SQLException {
        con = DataSource.getConnection();
        con.createStatement().executeUpdate(DROP_USERS_ACTIVITIES_TABLE);
        con.createStatement().executeUpdate(DROP_REQUESTS_TABLE);
        con.createStatement().executeUpdate(DROP_USERS_TABLE);
        con.createStatement().executeUpdate(DROP_USER_STATUS_TABLE);
        con.createStatement().executeUpdate(DROP_ROLES_TABLE);
        con.createStatement().executeUpdate(DROP_ACTIVITIES_TABLE);
        con.createStatement().executeUpdate(DROP_CATEGORIES_OF_ACTIVITY_TABLE);
        con.createStatement().executeUpdate(DROP_ACTIONS_WITH_REQUESTS_TABLE);
        con.createStatement().executeUpdate(CREATE_ACTIONS_WITH_REQUESTS_TABLE);
        con.createStatement().executeUpdate(CREATE_CATEGORIES_OF_ACTIVITY_TABLE);
        con.createStatement().executeUpdate(CREATE_ACTIVITIES_TABLE);
        con.createStatement().executeUpdate(CREATE_ROLES_TABLE);
        con.createStatement().executeUpdate(CREATE_USER_STATUS_TABLE);
        con.createStatement().executeUpdate(CREATE_USERS_TABLE);
        con.createStatement().executeUpdate(CREATE_REQUESTS_TABLE);
        con.createStatement().executeUpdate(CREATE_USERS_ACTIVITIES_TABLE);
        con.createStatement().executeUpdate(INITIAL_ROLE_ADMIN);
        con.createStatement().executeUpdate(INITIAL_ROLE_USER);
        userDAO.addStatus("active");
        userDAO.addStatus("inactive");
        userDAO.addStatus("deactivated");
        //con.createStatement().executeUpdate("DELETE FROM users;");
    }

    @AfterAll
    static void globalTearDown() throws SQLException {
        con.createStatement().executeUpdate("DELETE FROM roles;");
        con.createStatement().executeUpdate("DELETE FROM user_status;");
        con.close();
    }
    @BeforeEach
    void setUp() throws SQLException {
        //dbm = DBManager.getInstance();

    }

    @AfterEach
    void tearDown() throws SQLException {
//        con.createStatement().executeUpdate(DROP_USERS_ACTIVITIES_TABLE);
//        con.createStatement().executeUpdate(DROP_REQUESTS_TABLE);
//        con.createStatement().executeUpdate(DROP_USERS_TABLE);
//        con.createStatement().executeUpdate(DROP_USER_STATUS_TABLE);
//        con.createStatement().executeUpdate(DROP_ROLES_TABLE);
//        con.createStatement().executeUpdate(DROP_ACTIVITIES_TABLE);
//        con.createStatement().executeUpdate(DROP_CATEGORIES_OF_ACTIVITY_TABLE);
//        con.createStatement().executeUpdate(DROP_ACTIONS_WITH_REQUESTS_TABLE);
//        con.createStatement().executeUpdate("TRUNCATE TABLE users;");
        con.createStatement().executeUpdate("DELETE FROM users WHERE login LIKE '%user%';");

    }
    @Test
    void testEquality() {
        User user1 = User.builder().withLogin("testUser").build();
        User user2 = User.builder().withLogin("testUser").build();
        User user3 = User.builder().withLogin("tesUser").build();
        User user4 = User.builder().withLogin("testUser")
                .withAccount(0)
                .withEmail("me@me.me")
                .withPassword("asdf")
                .withFirstName("asfd")
                .withLastName("asdf")
                .withRole("administrator")
                .withStatus("active")
                .withNotifications("yes")
                .build();
        User user5 = User.builder().withLogin("tesUser")
                .withAccount(0)
                .withEmail("me@me.me")
                .withPassword("asdf")
                .withFirstName("asfd")
                .withLastName("asdf")
                .withRole("administrator")
                .withStatus("active")
                .withNotifications("yes")
                .build();
        assertEquals("testUser",  user1.getLogin());
        assertEquals(user1, user2, "Two users must be equaled if their logins are equaled");
        assertEquals(user1, user4, "Two users must be equaled if their logins are equaled");
        assertEquals(user3, user5, "Two users must be equaled if their logins are equaled");
        assertNotEquals(user1, user3, "Two users must not be equaled if their logins aren't equaled");
        assertNotEquals(user4, user5, "Two users must not be equaled if their logins aren't equaled");
    }
    @Test
    void testInsert() {
        //List usersFromDBBefore = sort(userDAO.getAll(), User::getLogin);
        List<User> users = createAndInsertUsers(1, 5);
//        for (User user: users){
//            userDAO.save(user);
//        }
        List<User> usersFromDB = sort(userDAO.getAll(), User::getLogin);
        assertEquals(users, usersFromDB);
    }

    @Test
    void testUpdate() {
        List<User> users = createAndInsertUsers(1, 5);
//        for (User user: users){
//            userDAO.save(user);
//        }
        User newUser = User.builder()
                .withLogin("updated user2")
                .withEmail("user2@domen.com")
                .withPassword("password2")
                .withFirstName("updated first")
                .withLastName("updated last")
                .withRole("administrator")
                .withStatus("active")
                .withNotifications("no").build();
        String[] param = {"updated user2", "user2@domen.com", "password2", "updated first", "updated last", "administrator", "active", "no"};
        userDAO.update(userDAO.getByLogin("user2"), param );
        List<User> usersFromDB = sort(userDAO.getAll(), User::getLogin);
        users.set(1, newUser);
        sort(users, User::getLogin);
        assertEquals(users, usersFromDB);
    }
    @Test
    void testDelete() {
        List<User> users = createAndInsertUsers(1, 5);
//        for (User user: users){
//            userDAO.save(user);
//        }
        userDAO.delete(userDAO.getByLogin("user2"));
        List<User> usersFromDB = sort(userDAO.getAll(), User::getLogin);
        users.remove(1);
        sort(users, User::getLogin);
        assertEquals(users, usersFromDB);
    }
    @Test
    void testDeletedStatus() {
        List<User> users = createAndInsertUsers(1, 5);
//        for (User user: users){
//            userDAO.save(user);
//        }
        userDAO.deleteStatus("active");
        List<User> usersFromDB = sort(userDAO.getAll(), User::getLogin);
        assertEquals(usersFromDB, new ArrayList<User>());
        userDAO.addStatus("active");
    }



    private static <T, U extends Comparable<? super U>> List<T>
    sort(List<T> items, Function<T, U> extractor) {
        items.sort(Comparator.comparing(extractor));
        return items;
    }
    private List<User> createAndInsertUsers(int from, int to) {
        //DBManager dbm = DBManager.getInstance();
        List<User> users = IntStream.range(from, to)
                .mapToObj(UserTests::createUser)
                .collect(Collectors.toList());
        for (User user : users) {
            userDAO.save(user);
        }
        return users;
    }
    private static User createUser(int number){
        User u = User.builder().withLogin("user" + number)
                .withEmail("user" + number + "@domen.com")
                .withPassword("password" + number)
                .withRole("administrator")
                .withStatus("active")
                .withNotifications("no")
                .build();
        return u;
    }
}
