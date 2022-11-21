import com.sulzhenko.DAO.DAOException;
import com.sulzhenko.DAO.DataSource;
import com.sulzhenko.DAO.UserDAO;
import com.sulzhenko.DAO.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final UserDAO userDAO = new UserDAO();

    @BeforeAll
    static void globalSetUp() throws SQLException {
        con = DataSource.getConnection();
//        con.createStatement().executeUpdate(DROP_USERS_ACTIVITIES_TABLE);
//        con.createStatement().executeUpdate(DROP_REQUESTS_TABLE);
//        con.createStatement().executeUpdate(DROP_USERS_TABLE);
//        con.createStatement().executeUpdate(DROP_USER_STATUS_TABLE);
//        con.createStatement().executeUpdate(DROP_ROLES_TABLE);
//        con.createStatement().executeUpdate(DROP_ACTIVITIES_TABLE);
//        con.createStatement().executeUpdate(DROP_CATEGORIES_OF_ACTIVITY_TABLE);
//        con.createStatement().executeUpdate(DROP_ACTIONS_WITH_REQUESTS_TABLE);
//        con.createStatement().executeUpdate(CREATE_ACTIONS_WITH_REQUESTS_TABLE);
//        con.createStatement().executeUpdate(CREATE_CATEGORIES_OF_ACTIVITY_TABLE);
//        con.createStatement().executeUpdate(CREATE_ACTIVITIES_TABLE);
//        con.createStatement().executeUpdate(CREATE_ROLES_TABLE);
//        con.createStatement().executeUpdate(CREATE_USER_STATUS_TABLE);
//        con.createStatement().executeUpdate(CREATE_USERS_TABLE);
//        con.createStatement().executeUpdate(CREATE_REQUESTS_TABLE);
//        con.createStatement().executeUpdate(CREATE_USERS_ACTIVITIES_TABLE);
        con.createStatement().executeUpdate("DELETE FROM role;");
        con.createStatement().executeUpdate("DELETE FROM user_status;");
        con.createStatement().executeUpdate(INITIAL_ROLE_ADMIN);
        con.createStatement().executeUpdate(INITIAL_ROLE_USER);
        userDAO.addStatus("active");
        userDAO.addStatus("inactive");
        userDAO.addStatus("deactivated");
        //con.createStatement().executeUpdate("DELETE FROM user;");
    }

    @AfterAll
    static void globalTearDown() throws SQLException {
        con.createStatement().executeUpdate("DELETE FROM role;");
        con.createStatement().executeUpdate("DELETE FROM user_status;");
        con.close();
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
        con.createStatement().executeUpdate("DELETE FROM user WHERE login LIKE '%user%';");
        con.createStatement().executeUpdate("DELETE FROM user WHERE login LIKE '%admin%';");

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
                .withNotifications("on")
                .build();
        User user5 = User.builder().withLogin("tesUser")
                .withAccount(0)
                .withEmail("me@me.me")
                .withPassword("asdf")
                .withFirstName("asfd")
                .withLastName("asdf")
                .withRole("administrator")
                .withStatus("active")
                .withNotifications("on")
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
                .withNotifications("off").build();
        String[] param = {"updated user2", "user2@domen.com", "password2", "updated first",
                "updated last", "administrator", "active", "off"};
        userDAO.update(userDAO.getByLogin("user2"), param );
        List<User> usersFromDB = sort(userDAO.getAll(), User::getLogin);
        users.set(1, newUser);
        sort(users, User::getLogin);
        assertEquals(users, usersFromDB);

        User newUser2 = User.builder()
                .withLogin("updated user2")
                .withEmail("user2@domen.com")
                .withPassword("password2")
                .withFirstName("updated First")
                .withLastName("updated Last")
                .withRole("administrator")
                .withStatus("active")
                .withNotifications("off").build();
        String[] param2 = {"updated user2", "user2@domen.com", "password2", "updated First",
                "updated Last", "administrator", "active", "off"};
        userDAO.update(userDAO.getByLogin("updated user2"), param2 );
        usersFromDB = sort(userDAO.getAll(), User::getLogin);
        users.set(0, newUser2);
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
        createAndInsertUsers(1, 5);
//        for (User user: users){
//            userDAO.save(user);
//        }
        userDAO.deleteStatus("active");
        List<User> usersFromDB = sort(userDAO.getAll(), User::getLogin);
        assertEquals(usersFromDB, new ArrayList<User>());
        userDAO.addStatus("active");
    }

    //WORKS
    @Test
    void testUserInsertException(){
        createAndInsertUsers(1, 2);
        User user1 = User.builder()
                .withLogin("user1")
                .withEmail("a@a.a")
                .withPassword("asfd")
                .withRole("administrator")
                .withStatus("active")
                .withNotifications("on")
                .build();
        User user2 = User.builder()
                .withLogin("user2")
                .withEmail("a@a.a")
                .withPassword("asfd")
                .withRole("unknown role")
                .withStatus("active")
                .withNotifications("on")
                .build();
        User user3 = User.builder()
                .withLogin("user3")
                .withEmail("a@a.a")
                .withPassword("asfd")
                .withRole("administrator")
                .withStatus("unknown status")
                .withNotifications("on")
                .build();
        assertThrows(DAOException.class, () -> userDAO.isDataCorrect(user1), "wrong login: user1");
        assertThrows(DAOException.class, () -> userDAO.isDataCorrect(user2), "wrong role: unknown role");
        assertThrows(DAOException.class, () -> userDAO.isDataCorrect(user3), "wrong status: unknown status");
//        try {
//            userDAO.isDataCorrect(user1);
//            userDAO.save(user1);
//        } catch (Exception e){
//            assertTrue(e instanceof DAOException);
//            assertEquals("wrong login: user1", e.getMessage());
//        }
//        try {
//            userDAO.isDataCorrect(user2);
//            userDAO.save(user2);
//        } catch (Exception e){
////            assertTrue(e instanceof DAOException);
//            assertEquals("wrong role: unknown role", e.getMessage());
//        }
//        try {
//            userDAO.isDataCorrect(user3);
//            userDAO.save(user3);
//        } catch (Exception e){
////            assertTrue(e instanceof DAOException);
//            assertEquals("wrong status: unknown status", e.getMessage());
//        }
        userDAO.save(user1);
        userDAO.save(user2);
        userDAO.save(user3);
        assertNull(userDAO.getByEmail("a@a.a"));
    }
    @Test
    void testUpdateException() {
//        List<User> users =
        createAndInsertUsers(1, 5);
        String[] param = {"user1", "a@a.a", "password2", "updated first",
                "updated last", "administrator", "active", "off"};
        assertThrows(DAOException.class, () -> userDAO.isUpdateCorrect(param, "user2"), "wrong login: user1");
        userDAO.update(userDAO.getByLogin("user2"), param);
        assertNull(userDAO.getByEmail("a@a.a"));

        String[] param2 = {"user2", "a@a.a", "password2", "updated first",
                "updated last", "unknown role", "active", "off"};
        assertThrows(DAOException.class, () -> userDAO.isUpdateCorrect(param2, "user2"), "wrong role: unknown role");
        userDAO.update(userDAO.getByLogin("user2"), param);
        assertNull(userDAO.getByEmail("a@a.a"));

        String[] param3 = {"user2", "a@a.a", "password2", "updated first",
                "updated last", "administrator", "unknown status", "off"};
        assertThrows(DAOException.class, () -> userDAO.isUpdateCorrect(param3, "user2"), "wrong status: unknown status");
        userDAO.update(userDAO.getByLogin("user2"), param);
        assertNull(userDAO.getByEmail("a@a.a"));

    }
    @Test
    void testGetUserByFirstName(){
        User user1 = User.builder()
                .withLogin("user1")
                .withEmail("a@a.a")
                .withPassword("asfd")
                .withFirstName("first Name")
                .withRole("administrator")
                .withStatus("active")
                .withNotifications("on")
                .build();
        userDAO.save(user1);
        User user2 = User.builder()
                .withLogin("user2")
                .withEmail("a@a.a")
                .withPassword("asfd")
                .withFirstName("first name2")
                .withRole("administrator")
                .withStatus("active")
                .withNotifications("on")
                .build();
        userDAO.save(user2);
        assertEquals(1, userDAO.getByFirstName("first name").size());
    }
    @Test
    void testGetUserByLastName(){
        User user1 = User.builder()
                .withLogin("user1")
                .withEmail("a@a.a")
                .withPassword("asfd")
                .withLastName("last Name")
                .withRole("administrator")
                .withStatus("active")
                .withNotifications("on")
                .build();
        userDAO.save(user1);
        User user2 = User.builder()
                .withLogin("user2")
                .withEmail("a@a.a")
                .withPassword("asfd")
                .withLastName("last name2")
                .withRole("administrator")
                .withStatus("active")
                .withNotifications("on")
                .build();
        userDAO.save(user2);
        assertEquals(1, userDAO.getByLastName("last name").size());
    }
    @Test
    void testGetUserByRole(){
        User admin = User.builder()
                .withLogin("admin")
                .withEmail("a@a.a")
                .withPassword("asfd")
                .withLastName("last Name")
                .withRole("administrator")
                .withStatus("active")
                .withNotifications("on")
                .build();
        userDAO.save(admin);
        createAndInsertUsers(1, 5);
        assertEquals(4, userDAO.getByRole("system user").size());
    }
    @Test
    void testGetUserByStatus(){
        User admin = User.builder()
                .withLogin("admin")
                .withEmail("a@a.a")
                .withPassword("asfd")
                .withLastName("last Name")
                .withRole("administrator")
                .withStatus("inactive")
                .withNotifications("on")
                .build();
        userDAO.save(admin);
        createAndInsertUsers(1, 5);
        assertEquals(4, userDAO.getByStatus("active").size());
    }
    @Test
    void testGetUserByNotifications(){
        User admin = User.builder()
                .withLogin("admin")
                .withEmail("a@a.a")
                .withPassword("asfd")
                .withLastName("last Name")
                .withRole("administrator")
                .withStatus("inactive")
                .withNotifications("on")
                .build();
        userDAO.save(admin);
        createAndInsertUsers(1, 5);
        assertEquals(1, userDAO.getByNotification("on").size());
    }
    @Test
    void testGetAll(){
        List<User> users = createAndInsertUsers(1, 5);
        assertEquals(users, userDAO.getAll());
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
        return User.builder().withLogin("user" + number)
                .withEmail("user" + number + "@domain.com")
                .withPassword("password" + number)
                .withRole("system user")
                .withStatus("active")
                .withNotifications("off")
                .build();
    }

//isDataCorrect throws DAOException (and should be tested for Exception)
//save logs Exception and is tested by getting users from DB
//    @Test
//    public void testExceptionHandling() {
//        Exception e = assertThrows(DAOException.class, () -> onlyThrowsExceptions(5));
//        assertEquals("An exception was thrown!", e.getMessage());
//    }
//
//    public void onlyThrowsExceptions(int number) throws DAOException {
//        if(number == 5) throw new DAOException("An exception was thrown!");
//        else System.out.println(number);
//    }
//    public void doSmth(int number){
//        try{
//            onlyThrowsExceptions(number);
//        } catch (DAOException e){
//            logger.info(e);
//        }
//    }
    private static Logger logger = LogManager.getLogger(UserTests.class);
}
