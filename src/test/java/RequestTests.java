import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.DAO.implementation.*;
import com.sulzhenko.model.connectionPool.MyDataSource;
import com.sulzhenko.model.entity.*;
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

//public class RequestTests {
//    private static Connection con;
//    private static final RequestDAO REQUEST_DAO = RequestDAOImpl.getInstance();
//    private static final CategoryDAO CATEGORY_DAO = CategoryDAOImpl.getInstance();
//    private static final ActivityDAO ACTIVITY_DAO = ActivityDAOImpl.getInstance();
//    private static final UserDAO USER_DAO = UserDAOImpl.getInstance();
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
//        con.createStatement().executeUpdate("DELETE FROM user_activity;");
//        con.createStatement().executeUpdate("DELETE FROM activity;");
//        con.createStatement().executeUpdate("DELETE FROM category_of_activity;");
//        con.createStatement().executeUpdate("DELETE FROM user;");
//        con.createStatement().executeUpdate("DELETE FROM role;");
//        con.createStatement().executeUpdate("DELETE FROM user_status;");
//        con.close();
//    }
//
//    @AfterEach
//    void tearDown() throws SQLException {
//        con.createStatement().executeUpdate("DELETE FROM request;");
//    }
//    @Test
//    void testEquality() {
//        Request request1 = new Request.Builder()
//                .withLogin("user1")
//                .withActivityName("activity1")
//                .withActionToDo("addUser")
//                .withDescription("asap")
//                .build();
//        Request request2 = new Request.Builder()
//                .withLogin("user1")
//                .withActivityName("activity1")
//                .withActionToDo("addUser")
//                .build();
//        Request request3 = new Request.Builder()
//                .withLogin("user1")
//                .withActivityName("activity1")
//                .withActionToDo("addUser")
//                .withDescription("urgent")
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
//        Request request6 = new Request.Builder()
//                .withLogin("user1")
//                .withActivityName("activity1")
//                .withActionToDo("remove")
//                .withDescription("asap")
//                .build();
//        assertEquals(request1,  request2);
//        assertEquals(request1, request3);
//        assertNotEquals(request1, request4);
//        assertNotEquals(request1, request5);
//        assertNotEquals(request1, request6);
//    }
//    @Test
//    void testInsert() {
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
//        Request request6 = new Request.Builder()
//                .withLogin("user1")
//                .withActivityName("activity1")
//                .withActionToDo("remove")
//                .withDescription("asap")
//                .build();
//        List<Request> requests = new ArrayList<>();
//        requests.addUser(request1);
//        requests.addUser(request4);
//        requests.addUser(request5);
//        sort(requests, Request::getLogin);
//
//        REQUEST_DAO.save(request1);
//        REQUEST_DAO.save(request4);
//        REQUEST_DAO.save(request5);
//
//        List<Request> requestsFromDB = sort(REQUEST_DAO.getAll(), Request::getLogin);
//        assertEquals(requests, requestsFromDB);
//    }
//
//    @Test
//    void exceptionToInsert(){
//        Request request1 = new Request.Builder()
//                .withLogin("user6")
//                .withActivityName("activity1")
//                .withActionToDo("addUser")
//                .withDescription("asap")
//                .build();
//        Request request2 = new Request.Builder()
//                .withLogin("user1")
//                .withActivityName("activity6")
//                .withActionToDo("addUser")
//                .withDescription("asap")
//                .build();
//        Request request3 = new Request.Builder()
//                .withLogin("user1")
//                .withActivityName("activity4")
//                .withActionToDo("remove")
//                .withDescription("asap")
//                .build();
//
//        DAOException thrown = assertThrows(DAOException.class, () -> REQUEST_DAO.isDataCorrect(request1));
//        assertEquals("wrong.login", thrown.getMessage());
//        thrown = assertThrows(DAOException.class, () -> REQUEST_DAO.isDataCorrect(request2));
//        assertEquals("wrong.activity", thrown.getMessage());
//        thrown = assertThrows(DAOException.class, () -> REQUEST_DAO.isDataCorrect(request3));
//        assertEquals("wrong.action", thrown.getMessage());
//        assertEquals(new ArrayList<>(), REQUEST_DAO.getAll());
//    }
//
//    private static <T, U extends Comparable<? super U>> List<T>
//    sort(List<T> items, Function<T, U> extractor) {
//        items.sort(Comparator.comparing(extractor));
//        return items;
//    }
//    private static void createAndInsertActivities(int from, int to) {
//        List<Activity> activities = IntStream.range(from, to)
//                .mapToObj(RequestTests::createActivity)
//                .collect(Collectors.toList());
//        for (Activity activity : activities) {
//            ACTIVITY_DAO.save(activity);
//        }
//    }
//    private static Activity createActivity(int number){
//        return new Activity.Builder().withName("activity" + number)
//                .withCategory(CATEGORY_DAO.getByName("categoryB").orElse(null))
//                .build();
//    }
//    private static void createAndInsertUsers(int from, int to) {
//        List<User> users = IntStream.range(from, to)
//                .mapToObj(RequestTests::createUser)
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
