import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.DAO.implementation.*;
import com.sulzhenko.model.connectionPool.MyDataSource;
import com.sulzhenko.model.entity.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.sulzhenko.model.DAO.SQLQueries.InitialData.*;
import static org.junit.jupiter.api.Assertions.*;

//public class UserActivityTests {
//    private static Connection con;
//    private static final RequestDAO REQUEST_DAO = RequestDAOImpl.getInstance();
//    private static final ActivityDAO ACTIVITY_DAO = ActivityDAOImpl.getInstance();
//    private static final CategoryDAO CATEGORY_DAO = CategoryDAOImpl.getInstance();
//    private static final UserDAO USER_DAO = UserDAOImpl.getInstance();
//    private static final UserActivityDAO USER_ACTIVITY_DAO = UserActivityDAOImpl.getInstance();
//
//    @BeforeAll
//    static void globalSetUp() throws SQLException {
//        con = DataSource.getConnection();
//        con.createStatement().executeUpdate("DELETE FROM user_activity;");
//        con.createStatement().executeUpdate("DELETE FROM request;");
//        con.createStatement().executeUpdate("DELETE FROM activity;");
//        con.createStatement().executeUpdate("DELETE FROM category_of_activity;");
//        CATEGORY_DAO.save(new Category("categoryA"));
//        CATEGORY_DAO.save(new Category("categoryB"));
//        CATEGORY_DAO.save(new Category("categoryC"));
//        createAndInsertActivities(1, 5);
//        con.createStatement().executeUpdate("DELETE FROM role;");
//        con.createStatement().executeUpdate("DELETE FROM user_status;");
//        con.createStatement().executeUpdate(INITIAL_ROLE_ADMIN);
//        con.createStatement().executeUpdate(INITIAL_ROLE_USER);
//        USER_DAO.addStatus("active");
//        USER_DAO.addStatus("inactive");
//        USER_DAO.addStatus("deactivated");
//        con.createStatement().executeUpdate("DELETE FROM action_with_request;");
//        con.createStatement().executeUpdate(INITIAL_ACTION_ADD);
//        con.createStatement().executeUpdate(INITIAL_ACTION_REMOVE);
//        createAndInsertUsers(1, 5);
//    }
//
//    @AfterAll
//    static void globalTearDown() throws SQLException {
//        con.close();
//    }
//    @BeforeEach
//    void setUp() throws SQLException {
//        con = DataSource.getConnection();
//        con.createStatement().executeUpdate("DELETE FROM user_activity;");
//        con.createStatement().executeUpdate("DELETE FROM request;");
//    }
//
//    @AfterEach
//    void tearDown() throws SQLException {
//        con.close();
//
//    }
//    @Test
//    void testAdd() throws SQLException {
//        approveAddingRequests();
//        List<Request> empty = new ArrayList<>();
//        assertEquals(empty, REQUEST_DAO.getAll());
//    }
//    @Test
//    void testAllUserActivities() throws SQLException {
//        approveAddingRequests();
//        User user1 = USER_DAO.getByLogin("user1");
//        Map<Activity, UserActivityDAOImpl.Parameters> map = USER_ACTIVITY_DAO.allUserActivities(user1);
//        assertEquals(2, map.size());
//    }
//    @Test
//    void testRemoveUserActivity() throws SQLException {
//        approveRemovingRequests();
////        List<Request> empty = new ArrayList<>();
//        assertEquals(1, REQUEST_DAO.getAll().size());
//    }
//    @Test
//    void testAmount() throws SQLException {
//        approveAddingRequests();
//        USER_ACTIVITY_DAO.setAmount(USER_DAO.getByLogin("user1"), ACTIVITY_DAO.getByName("activity1"), 99);
//        assertEquals(99, USER_ACTIVITY_DAO.getAmount(USER_DAO.getByLogin("user1"), ACTIVITY_DAO.getByName("activity1")));
//    }
//
//    private static void approveAddingRequests() throws SQLException {
//        Request request1 = new Request.Builder()
//                .withLogin("user1")
//                .withActivityName("activity1")
//                .withActionToDo("addUser")
//                .withDescription("asap")
//                .build();
//        Request request4 = new Request.Builder()
//                .withLogin("user2")
//                .withActivityName("activity1")
//                .withActionToDo("addUser")
//                .withDescription("asap")
//                .build();
//        Request request5 = new Request.Builder()
//                .withLogin("user1")
//                .withActivityName("activity2")
//                .withActionToDo("addUser")
//                .withDescription("asap")
//                .build();
//        REQUEST_DAO.save(request1);
//        REQUEST_DAO.save(request4);
//        REQUEST_DAO.save(request5);
//        USER_ACTIVITY_DAO.addActivityToUser(request1);
//        USER_ACTIVITY_DAO.addActivityToUser(request4);
//        USER_ACTIVITY_DAO.addActivityToUser(request5);
//    }
//    private static void approveRemovingRequests() throws SQLException {
//        approveAddingRequests();
//        Request request1 = new Request.Builder()
//                .withLogin("user1")
//                .withActivityName("activity1")
//                .withActionToDo("remove")
//                .withDescription("asap")
//                .build();
//        Request request4 = new Request.Builder()
//                .withLogin("user2")
//                .withActivityName("activity1")
//                .withActionToDo("remove")
//                .withDescription("asap")
//                .build();
//        Request request5 = new Request.Builder()
//                .withLogin("user1")
//                .withActivityName("activity2")
//                .withActionToDo("remove")
//                .withDescription("asap")
//                .build();
//        REQUEST_DAO.save(request1);
//        REQUEST_DAO.save(request4);
//        REQUEST_DAO.save(request5);
//        USER_ACTIVITY_DAO.removeUserActivity(request1);
//        USER_ACTIVITY_DAO.removeUserActivity(request4);
//        USER_ACTIVITY_DAO.removeUserActivity(request5);
//        Request request6 = new Request.Builder()
//                .withLogin("user1")
//                .withActivityName("activity2")
//                .withActionToDo("addUser")
//                .withDescription("asap")
//                .build();
//        REQUEST_DAO.save(request6);
//        USER_ACTIVITY_DAO.removeUserActivity(request6);
//    }
//
//
//    private static void createAndInsertActivities(int from, int to) {
//        List<Activity> activities = IntStream.range(from, to)
//                .mapToObj(UserActivityTests::createActivity)
//                .collect(Collectors.toList());
//        for (Activity activity : activities) {
//            ACTIVITY_DAO.save(activity);
//        }
//    }
//    private static Activity createActivity(int number){
//        return new Activity.Builder().withName("activity" + number)
//                .withCategory(CATEGORY_DAO.getByName("categoryC").orElse(null))
//                .build();
//    }
//    private static void createAndInsertUsers(int from, int to) {
//        List<User> users = IntStream.range(from, to)
//                .mapToObj(UserActivityTests::createUser)
//                .collect(Collectors.toList());
//        for (User user : users) {
//            USER_DAO.save(user);
//        }
//    }
//    private static User createUser(int number){
//        return new User.Builder().withLogin("user" + number)
//                .withEmail("user" + number + "@domen.com")
//                .withPassword("password" + number)
//                .withRole("system user")
//                .withStatus("active")
//                .withNotification("off")
//                .build();
//    }
//}
