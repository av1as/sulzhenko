import com.sulzhenko.DAO.DAOException;
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
import static com.sulzhenko.DAO.SQLQueries.InitialData.*;
import static org.junit.jupiter.api.Assertions.*;

class UserTests {
    private static Connection con;
    private static final UserDAO userDAO = UserDAO.getInstance();

    @BeforeAll
    static void globalSetUp() throws SQLException {
        con = DataSource.getConnection();
        con.createStatement().executeUpdate("DELETE FROM role;");
        con.createStatement().executeUpdate("DELETE FROM user_status;");
        con.createStatement().executeUpdate(INITIAL_ROLE_ADMIN);
        con.createStatement().executeUpdate(INITIAL_ROLE_USER);
        userDAO.addStatus("active", con);
        userDAO.addStatus("inactive", con);
        userDAO.addStatus("deactivated", con);
    }

    @AfterAll
    static void globalTearDown() throws SQLException {
        con.createStatement().executeUpdate("DELETE FROM role;");
        con.createStatement().executeUpdate("DELETE FROM user_status;");
        con.close();
    }
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() throws SQLException {
        con.createStatement().executeUpdate("DELETE FROM user WHERE login LIKE '%user%';");
        con.createStatement().executeUpdate("DELETE FROM user WHERE login LIKE '%admin%';");
    }
    @Test
    void testEquality() {
        User user1 = new User.Builder().withLogin("testUser").build();
        User user2 = new User.Builder().withLogin("testUser").build();
        User user3 = new User.Builder().withLogin("tesUser").build();
        User user4 = new User.Builder().withLogin("testUser")
                .withAccount(0)
                .withEmail("me@me.me")
                .withPassword("asdf")
                .withFirstName("asfd")
                .withLastName("asdf")
                .withRole("administrator")
                .withStatus("active")
                .withNotification("on")
                .build();
        User user5 = new User.Builder().withLogin("tesUser")
                .withAccount(0)
                .withEmail("me@me.me")
                .withPassword("asdf")
                .withFirstName("asfd")
                .withLastName("asdf")
                .withRole("administrator")
                .withStatus("active")
                .withNotification("on")
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
        List<User> users = createAndInsertUsers(1, 5);
        List<User> usersFromDB = sort(userDAO.getAll(con), User::getLogin);
        assertEquals(users, usersFromDB);
    }

    @Test
    void testUpdate() {
        List<User> users = createAndInsertUsers(1, 5);
        User newUser = new User.Builder()
                .withLogin("updated user2")
                .withEmail("user2@domen.com")
                .withPassword("password2")
                .withFirstName("updated first")
                .withLastName("updated last")
                .withRole("administrator")
                .withStatus("active")
                .withNotification("off").build();
        String[] param = {"updated user2", "user2@domen.com", "password2", "updated first",
                "updated last", "administrator", "active", "off"};
        userDAO.update(userDAO.getByLogin("user2", con), param, con);
        List<User> usersFromDB = sort(userDAO.getAll(con), User::getLogin);
        users.set(1, newUser);
        sort(users, User::getLogin);
        assertEquals(users, usersFromDB);

        User newUser2 = new User.Builder()
                .withLogin("updated user2")
                .withEmail("user2@domen.com")
                .withPassword("password2")
                .withFirstName("updated First")
                .withLastName("updated Last")
                .withRole("administrator")
                .withStatus("active")
                .withNotification("off").build();
        String[] param2 = {"updated user2", "user2@domen.com", "password2", "updated First",
                "updated Last", "administrator", "active", "off"};
        userDAO.update(userDAO.getByLogin("updated user2", con), param2, con);
        usersFromDB = sort(userDAO.getAll(con), User::getLogin);
        users.set(0, newUser2);
        sort(users, User::getLogin);
        assertEquals(users, usersFromDB);
    }

    @Test
    void testDelete() {
        List<User> users = createAndInsertUsers(1, 5);
        userDAO.delete(userDAO.getByLogin("user2", con), con);
        List<User> usersFromDB = sort(userDAO.getAll(con), User::getLogin);
        users.remove(1);
        sort(users, User::getLogin);
        assertEquals(users, usersFromDB);
    }
    @Test
    void testDeletedStatus() {
        createAndInsertUsers(1, 5);
        userDAO.deleteStatus("active", con);
        List<User> usersFromDB = sort(userDAO.getAll(con), User::getLogin);
        assertEquals(usersFromDB, new ArrayList<User>());
        userDAO.addStatus("active", con);
    }

    //WORKS
    @Test
    void testUserInsertException(){
        createAndInsertUsers(1, 2);
        User user1 = new User.Builder()
                .withLogin("user1")
                .withEmail("a@a.a")
                .withPassword("asfd")
                .withRole("administrator")
                .withStatus("active")
                .withNotification("on")
                .build();
        User user2 = new User.Builder()
                .withLogin("user2")
                .withEmail("a@a.a")
                .withPassword("asfd")
                .withRole("unknown role")
                .withStatus("active")
                .withNotification("on")
                .build();
        User user3 = new User.Builder()
                .withLogin("user3")
                .withEmail("a@a.a")
                .withPassword("asfd")
                .withRole("administrator")
                .withStatus("unknown status")
                .withNotification("on")
                .build();
        DAOException thrown = assertThrows(DAOException.class,
                () -> userDAO.isDataCorrect(user1, con));
        assertEquals("wrong login: user1", thrown.getMessage());
        thrown = assertThrows(DAOException.class,
                () -> userDAO.isDataCorrect(user2, con));
        assertEquals("wrong role: unknown role", thrown.getMessage());
        thrown = assertThrows(DAOException.class,
                () -> userDAO.isDataCorrect(user3, con));
        assertEquals("wrong status: unknown status", thrown.getMessage());
        userDAO.save(user1, con);
        userDAO.save(user2, con);
        userDAO.save(user3, con);
        assertEquals(userDAO.getByEmail("a@a.a", con), new ArrayList<User>());
    }
    @Test
    void testUpdateException() {
        createAndInsertUsers(1, 5);
        String[] param = {"user1", "a@a.a", "password2", "updated first",
                "updated last", "administrator", "active", "off"};
        DAOException thrown = assertThrows(DAOException.class,
                () -> userDAO.isUpdateCorrect(param, "user2", con));
        assertEquals("wrong login: user1", thrown.getMessage());
        userDAO.update(userDAO.getByLogin("user2", con), param, con);
        assertEquals(userDAO.getByEmail("a@a.a", con), new ArrayList<User>());

        String[] param2 = {"user2", "a@a.a", "password2", "updated first",
                "updated last", "unknown role", "active", "off"};
        thrown = assertThrows(DAOException.class,
                () -> userDAO.isUpdateCorrect(param2, "user2", con));
        assertEquals("wrong role: unknown role", thrown.getMessage());


        userDAO.update(userDAO.getByLogin("user2", con), param, con);
        assertEquals(userDAO.getByEmail("a@a.a", con), new ArrayList<User>());

        String[] param3 = {"user2", "a@a.a", "password2", "updated first",
                "updated last", "administrator", "unknown status", "off"};
        thrown = assertThrows(DAOException.class,
                () -> userDAO.isUpdateCorrect(param3, "user2", con));
        assertEquals("wrong status: unknown status", thrown.getMessage());
        userDAO.update(userDAO.getByLogin("user2", con), param, con);
        assertEquals(userDAO.getByEmail("a@a.a", con), new ArrayList<User>());

    }
    @Test
    void testGetUserByFirstName(){
        User user1 = new User.Builder()
                .withLogin("user1")
                .withEmail("a@a.a")
                .withPassword("asfd")
                .withFirstName("first Name")
                .withRole("administrator")
                .withStatus("active")
                .withNotification("on")
                .build();
        userDAO.save(user1, con);
        User user2 = new User.Builder()
                .withLogin("user2")
                .withEmail("a@a.a")
                .withPassword("asfd")
                .withFirstName("first name2")
                .withRole("administrator")
                .withStatus("active")
                .withNotification("on")
                .build();
        userDAO.save(user2, con);
        assertEquals(1, userDAO.getByFirstName("first name", con).size());
    }
    @Test
    void testGetUserByLastName(){
        User user1 = new User.Builder()
                .withLogin("user1")
                .withEmail("a@a.a")
                .withPassword("asfd")
                .withLastName("last Name")
                .withRole("administrator")
                .withStatus("active")
                .withNotification("on")
                .build();
        userDAO.save(user1, con);
        User user2 = new User.Builder()
                .withLogin("user2")
                .withEmail("a@a.a")
                .withPassword("asfd")
                .withLastName("last name2")
                .withRole("administrator")
                .withStatus("active")
                .withNotification("on")
                .build();
        userDAO.save(user2, con);
        assertEquals(1, userDAO.getByLastName("last name", con).size());
    }
    @Test
    void testGetUserByRole(){
        User admin = new User.Builder()
                .withLogin("admin")
                .withEmail("a@a.a")
                .withPassword("asfd")
                .withLastName("last Name")
                .withRole("administrator")
                .withStatus("active")
                .withNotification("on")
                .build();
        userDAO.save(admin, con);
        createAndInsertUsers(1, 5);
        assertEquals(4, userDAO.getByRole("system user", con).size());
    }
    @Test
    void testGetUserByStatus(){
        User admin = new User.Builder()
                .withLogin("admin")
                .withEmail("a@a.a")
                .withPassword("asfd")
                .withLastName("last Name")
                .withRole("administrator")
                .withStatus("inactive")
                .withNotification("on")
                .build();
        userDAO.save(admin, con);
        createAndInsertUsers(1, 5);
        assertEquals(4, userDAO.getByStatus("active", con).size());
    }
    @Test
    void testGetUserByNotifications(){
        User admin = new User.Builder()
                .withLogin("admin")
                .withEmail("a@a.a")
                .withPassword("asfd")
                .withLastName("last Name")
                .withRole("administrator")
                .withStatus("inactive")
                .withNotification("on")
                .build();
        userDAO.save(admin, con);
        createAndInsertUsers(1, 5);
        assertEquals(1, userDAO.getByNotification("on", con).size());
    }
    @Test
    void testGetAll(){
        List<User> users = createAndInsertUsers(1, 5);
        assertEquals(users, userDAO.getAll(con));
    }

    private static <T, U extends Comparable<? super U>> List<T>
    sort(List<T> items, Function<T, U> extractor) {
        items.sort(Comparator.comparing(extractor));
        return items;
    }
    private List<User> createAndInsertUsers(int from, int to) {
        List<User> users = IntStream.range(from, to)
                .mapToObj(UserTests::createUser)
                .collect(Collectors.toList());
        for (User user : users) {
            userDAO.save(user, con);
        }
        return users;
    }
    private static User createUser(int number){
        return new User.Builder().withLogin("user" + number)
                .withEmail("user" + number + "@domain.com")
                .withPassword("password" + number)
                .withRole("system user")
                .withStatus("active")
                .withNotification("off")
                .build();
    }
}
