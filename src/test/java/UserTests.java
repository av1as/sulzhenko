import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.DAO.UserDAO;
import com.sulzhenko.model.connectionPool.MyDataSource;
import com.sulzhenko.model.DAO.implementation.UserDAOImpl;
import com.sulzhenko.model.entity.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static com.sulzhenko.model.DAO.SQLQueries.InitialData.*;
import static org.junit.jupiter.api.Assertions.*;

//class UserTests {
//    private static Connection con;
//    private static final UserDAO USER_DAO_IMPL = UserDAOImpl.getInstance();
//
//    @BeforeAll
//    static void globalSetUp() throws SQLException {
//        con = DataSource.getConnection();
//        con.createStatement().executeUpdate("DELETE FROM role;");
//        con.createStatement().executeUpdate("DELETE FROM user_status;");
//        con.createStatement().executeUpdate(INITIAL_ROLE_ADMIN);
//        con.createStatement().executeUpdate(INITIAL_ROLE_USER);
//        USER_DAO_IMPL.addStatus("active");
//        USER_DAO_IMPL.addStatus("inactive");
//        USER_DAO_IMPL.addStatus("deactivated");
//    }
//
//    @AfterAll
//    static void globalTearDown() throws SQLException {
//        con.createStatement().executeUpdate("DELETE FROM role;");
//        con.createStatement().executeUpdate("DELETE FROM user_status;");
//        con.close();
//    }
//    @BeforeEach
//    void setUp() {
//    }
//
//    @AfterEach
//    void tearDown() throws SQLException {
//        con.createStatement().executeUpdate("DELETE FROM user WHERE login LIKE '%user%';");
//        con.createStatement().executeUpdate("DELETE FROM user WHERE login LIKE '%admin%';");
//    }
//    @Test
//    void testEquality() {
//        User user1 = new User.Builder().withLogin("testUser").build();
//        User user2 = new User.Builder().withLogin("testUser").build();
//        User user3 = new User.Builder().withLogin("tesUser").build();
//        User user4 = new User.Builder().withLogin("testUser")
//                .withAccount(0L)
//                .withEmail("me@me.me")
//                .withPassword("asdf")
//                .withFirstName("asfd")
//                .withLastName("asdf")
//                .withRole("administrator")
//                .withStatus("active")
//                .withNotification("on")
//                .build();
//        User user5 = new User.Builder().withLogin("tesUser")
//                .withAccount(0L)
//                .withEmail("me@me.me")
//                .withPassword("asdf")
//                .withFirstName("asfd")
//                .withLastName("asdf")
//                .withRole("administrator")
//                .withStatus("active")
//                .withNotification("on")
//                .build();
//        assertEquals("testUser",  user1.getLogin());
//        assertEquals(user1, user2, "Two users must be equaled if their logins are equaled");
//        assertEquals(user1, user4, "Two users must be equaled if their logins are equaled");
//        assertEquals(user3, user5, "Two users must be equaled if their logins are equaled");
//        assertNotEquals(user1, user3, "Two users must not be equaled if their logins aren't equaled");
//        assertNotEquals(user4, user5, "Two users must not be equaled if their logins aren't equaled");
//    }
//    @Test
//    void testInsert() {
//        List<User> users = createAndInsertUsers(1, 5);
//        List<User> usersFromDB = sort(USER_DAO_IMPL.getAll(), User::getLogin);
//        assertEquals(users, usersFromDB);
//    }
//
//    @Test
//    void testUpdate() {
//        List<User> users = createAndInsertUsers(1, 5);
//        User newUser = new User.Builder()
//                .withLogin("updated user2")
//                .withEmail("user2@domen.com")
//                .withPassword("password2")
//                .withFirstName("updated first")
//                .withLastName("updated last")
//                .withRole("administrator")
//                .withStatus("active")
//                .withNotification("off").build();
//        String[] param = {"updated user2", "user2@domen.com", "password2", "updated first",
//                "updated last", "administrator", "active", "off"};
//        USER_DAO_IMPL.update(USER_DAO_IMPL.getByLogin("user2"), param);
//        List<User> usersFromDB = sort(USER_DAO_IMPL.getAll(), User::getLogin);
//        users.set(1, newUser);
//        sort(users, User::getLogin);
//        assertEquals(users, usersFromDB);
//
//        User newUser2 = new User.Builder()
//                .withLogin("updated user2")
//                .withEmail("user2@domen.com")
//                .withPassword("password2")
//                .withFirstName("updated First")
//                .withLastName("updated Last")
//                .withRole("administrator")
//                .withStatus("active")
//                .withNotification("off").build();
//        String[] param2 = {"updated user2", "user2@domen.com", "password2", "updated First",
//                "updated Last", "administrator", "active", "off"};
//        USER_DAO_IMPL.update(USER_DAO_IMPL.getByLogin("updated user2"), param2);
//        usersFromDB = sort(USER_DAO_IMPL.getAll(), User::getLogin);
//        users.set(0, newUser2);
//        sort(users, User::getLogin);
//        assertEquals(users, usersFromDB);
//    }
//
//    @Test
//    void testDelete() {
//        List<User> users = createAndInsertUsers(1, 5);
//        USER_DAO_IMPL.delete(USER_DAO_IMPL.getByLogin("user2"));
//        List<User> usersFromDB = sort(USER_DAO_IMPL.getAll(), User::getLogin);
//        users.remove(1);
//        sort(users, User::getLogin);
//        assertEquals(users, usersFromDB);
//    }
//    @Test
//    void testDeletedStatus() {
//        createAndInsertUsers(1, 5);
//        USER_DAO_IMPL.deleteStatus("active");
//        List<User> usersFromDB = sort(USER_DAO_IMPL.getAll(), User::getLogin);
//        assertEquals(usersFromDB, new ArrayList<User>());
//        USER_DAO_IMPL.addStatus("active");
//    }
//
//    //WORKS
//    @Test
//    void testUserInsertException(){
//        createAndInsertUsers(1, 2);
//        User user1 = new User.Builder()
//                .withLogin("user1")
//                .withEmail("a@a.a")
//                .withPassword("asfd")
//                .withRole("administrator")
//                .withStatus("active")
//                .withNotification("on")
//                .build();
//        User user2 = new User.Builder()
//                .withLogin("user2")
//                .withEmail("a@a.a")
//                .withPassword("asfd")
//                .withRole("unknown role")
//                .withStatus("active")
//                .withNotification("on")
//                .build();
//        User user3 = new User.Builder()
//                .withLogin("user3")
//                .withEmail("a@a.a")
//                .withPassword("asfd")
//                .withRole("administrator")
//                .withStatus("unknown status")
//                .withNotification("on")
//                .build();
//        DAOException thrown = assertThrows(DAOException.class,
//                () -> USER_DAO_IMPL.isDataCorrect(user1));
//        assertEquals("wrong.login", thrown.getMessage());
//        thrown = assertThrows(DAOException.class,
//                () -> USER_DAO_IMPL.isDataCorrect(user2));
//        assertEquals("wrong.role", thrown.getMessage());
//        thrown = assertThrows(DAOException.class,
//                () -> USER_DAO_IMPL.isDataCorrect(user3));
//        assertEquals("wrong.status", thrown.getMessage());
//        thrown = assertThrows(DAOException.class,
//                () -> USER_DAO_IMPL.save(user1));
//        assertEquals("wrong.login", thrown.getMessage());
//        thrown = assertThrows(DAOException.class,
//                () -> USER_DAO_IMPL.save(user2));
//        assertEquals("wrong.role", thrown.getMessage());
//        thrown = assertThrows(DAOException.class,
//                () -> USER_DAO_IMPL.save(user3));
//        assertEquals("wrong.status", thrown.getMessage());
//
//        assertEquals(USER_DAO_IMPL.getByEmail("a@a.a"), new ArrayList<User>());
//    }
//    @Test
//    void testUpdateException() {
//        createAndInsertUsers(1, 5);
//        String[] param = {"user1", "a@a.a", "password2", "updated first",
//                "updated last", "administrator", "active", "off"};
//        DAOException thrown = assertThrows(DAOException.class,
//                () -> USER_DAO_IMPL.isUpdateCorrect(param, "user2"));
//        assertEquals("wrong.login", thrown.getMessage());
//        thrown = assertThrows(DAOException.class,
//                () -> USER_DAO_IMPL.update(USER_DAO_IMPL.getByLogin("user2"), param));
//        assertEquals("wrong.login", thrown.getMessage());
//
//        assertEquals(USER_DAO_IMPL.getByEmail("a@a.a"), new ArrayList<User>());
//
//        String[] param2 = {"user2", "a@a.a", "password2", "updated first",
//                "updated last", "unknown role", "active", "off"};
//        thrown = assertThrows(DAOException.class,
//                () -> USER_DAO_IMPL.isUpdateCorrect(param2, "user2"));
//        assertEquals("wrong.role", thrown.getMessage());
//
//
////        USER_DAO_IMPL.update(USER_DAO_IMPL.getByLogin("user2"), param);
//        assertEquals(USER_DAO_IMPL.getByEmail("a@a.a"), new ArrayList<User>());
//
//        String[] param3 = {"user2", "a@a.a", "password2", "updated first",
//                "updated last", "administrator", "unknown status", "off"};
//        thrown = assertThrows(DAOException.class,
//                () -> USER_DAO_IMPL.isUpdateCorrect(param3, "user2"));
//        assertEquals("wrong.status", thrown.getMessage());
//        thrown = assertThrows(DAOException.class,
//                () -> USER_DAO_IMPL.update(USER_DAO_IMPL.getByLogin("user2"), param3));
//        assertEquals("wrong.status", thrown.getMessage());
//
//        assertEquals(USER_DAO_IMPL.getByEmail("a@a.a"), new ArrayList<User>());
//
//    }
//    @Test
//    void testGetUserByFirstName(){
//        User user1 = new User.Builder()
//                .withLogin("user1")
//                .withEmail("a@a.a")
//                .withPassword("asfd")
//                .withFirstName("first Name")
//                .withRole("administrator")
//                .withStatus("active")
//                .withNotification("on")
//                .build();
//        USER_DAO_IMPL.save(user1);
//        User user2 = new User.Builder()
//                .withLogin("user2")
//                .withEmail("a@a.a")
//                .withPassword("asfd")
//                .withFirstName("first name2")
//                .withRole("administrator")
//                .withStatus("active")
//                .withNotification("on")
//                .build();
//        USER_DAO_IMPL.save(user2);
//        assertEquals(1, USER_DAO_IMPL.getByFirstName("first name").size());
//    }
//    @Test
//    void testGetUserByLastName(){
//        User user1 = new User.Builder()
//                .withLogin("user1")
//                .withEmail("a@a.a")
//                .withPassword("asfd")
//                .withLastName("last Name")
//                .withRole("administrator")
//                .withStatus("active")
//                .withNotification("on")
//                .build();
//        USER_DAO_IMPL.save(user1);
//        User user2 = new User.Builder()
//                .withLogin("user2")
//                .withEmail("a@a.a")
//                .withPassword("asfd")
//                .withLastName("last name2")
//                .withRole("administrator")
//                .withStatus("active")
//                .withNotification("on")
//                .build();
//        USER_DAO_IMPL.save(user2);
//        assertEquals(1, USER_DAO_IMPL.getByLastName("last name").size());
//    }
//    @Test
//    void testGetUserByRole(){
//        User admin = new User.Builder()
//                .withLogin("admin")
//                .withEmail("a@a.a")
//                .withPassword("asfd")
//                .withLastName("last Name")
//                .withRole("administrator")
//                .withStatus("active")
//                .withNotification("on")
//                .build();
//        USER_DAO_IMPL.save(admin);
//        createAndInsertUsers(1, 5);
//        assertEquals(4, USER_DAO_IMPL.getByRole("system user").size());
//    }
//    @Test
//    void testGetUserByStatus(){
//        User admin = new User.Builder()
//                .withLogin("admin")
//                .withEmail("a@a.a")
//                .withPassword("asfd")
//                .withLastName("last Name")
//                .withRole("administrator")
//                .withStatus("inactive")
//                .withNotification("on")
//                .build();
//        USER_DAO_IMPL.save(admin);
//        createAndInsertUsers(1, 5);
//        assertEquals(4, USER_DAO_IMPL.getByStatus("active").size());
//    }
//    @Test
//    void testGetUserByNotifications(){
//        User admin = new User.Builder()
//                .withLogin("admin")
//                .withEmail("a@a.a")
//                .withPassword("asfd")
//                .withLastName("last Name")
//                .withRole("administrator")
//                .withStatus("inactive")
//                .withNotification("on")
//                .build();
//        USER_DAO_IMPL.save(admin);
//        createAndInsertUsers(1, 5);
//        assertEquals(1, USER_DAO_IMPL.getByNotification("on").size());
//    }
//    @Test
//    void testGetAll(){
//        List<User> users = createAndInsertUsers(1, 5);
//        assertEquals(users, USER_DAO_IMPL.getAll());
//    }
//
//    private static <T, U extends Comparable<? super U>> List<T>
//    sort(List<T> items, Function<T, U> extractor) {
//        items.sort(Comparator.comparing(extractor));
//        return items;
//    }
//    private List<User> createAndInsertUsers(int from, int to) {
//        List<User> users = IntStream.range(from, to)
//                .mapToObj(UserTests::createUser)
//                .collect(Collectors.toList());
//        for (User user : users) {
//            USER_DAO_IMPL.save(user);
//        }
//        return users;
//    }
//    private static User createUser(int number){
//        return new User.Builder().withLogin("user" + number)
//                .withEmail("user" + number + "@domain.com")
//                .withPassword("password" + number)
//                .withRole("system user")
//                .withStatus("active")
//                .withNotification("off")
//                .build();
//    }
//}
