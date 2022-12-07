import com.sulzhenko.DAO.*;
import com.sulzhenko.DAO.entity.Activity;
import com.sulzhenko.DAO.entity.Request;
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

public class RequestTests {
    private static Connection con;
    private static RequestDAO requestDAO = RequestDAO.getInstance();
    private static ActivityDAO activityDAO = ActivityDAO.getInstance();
    private static UserDAO userDAO = UserDAO.getInstance();

    @BeforeAll
    static void globalSetUp() throws SQLException {
        con = DataSource.getConnection();
        con.createStatement().executeUpdate("DELETE FROM user_activity;");
        con.createStatement().executeUpdate("DELETE FROM request;");
        con.createStatement().executeUpdate("DELETE FROM activity;");
        con.createStatement().executeUpdate("DELETE FROM category_of_activity;");
        activityDAO.addCategory("categoryA", con);
        activityDAO.addCategory("categoryB", con);
        activityDAO.addCategory("categoryC", con);
        createAndInsertActivities(1, 5);
        con.createStatement().executeUpdate("DELETE FROM role;");
        con.createStatement().executeUpdate("DELETE FROM user_status;");
        con.createStatement().executeUpdate(INITIAL_ROLE_ADMIN);
        con.createStatement().executeUpdate(INITIAL_ROLE_USER);
        userDAO.addStatus("active", con);
        userDAO.addStatus("inactive", con);
        userDAO.addStatus("deactivated", con);
        con.createStatement().executeUpdate("DELETE FROM action_with_request;");
        con.createStatement().executeUpdate(INITIAL_ACTION_ADD);
        con.createStatement().executeUpdate(INITIAL_ACTION_REMOVE);
        createAndInsertUsers(1, 5);
    }

    @AfterAll
    static void globalTearDown() throws SQLException {
        con.createStatement().executeUpdate("DELETE FROM user_activity;");
        con.createStatement().executeUpdate("DELETE FROM activity;");
        con.createStatement().executeUpdate("DELETE FROM category_of_activity;");
        con.createStatement().executeUpdate("DELETE FROM user;");
        con.createStatement().executeUpdate("DELETE FROM role;");
        con.createStatement().executeUpdate("DELETE FROM user_status;");
        con.close();
    }

    @AfterEach
    void tearDown() throws SQLException {
        con.createStatement().executeUpdate("DELETE FROM request;");
    }
    @Test
    void testEquality() {
        Request request1 = new Request.Builder()
                .withLogin("user1")
                .withActivityName("activity1")
                .withActionToDo("add")
                .withDescription("asap")
                .build();
        Request request2 = new Request.Builder()
                .withLogin("user1")
                .withActivityName("activity1")
                .withActionToDo("add")
                .build();
        Request request3 = new Request.Builder()
                .withLogin("user1")
                .withActivityName("activity1")
                .withActionToDo("add")
                .withDescription("urgent")
                .build();
        Request request4 = new Request.Builder()
                .withLogin("user2")
                .withActivityName("activity1")
                .withActionToDo("add")
                .withDescription("asap")
                .build();
        Request request5 = new Request.Builder()
                .withLogin("user1")
                .withActivityName("activity2")
                .withActionToDo("add")
                .withDescription("asap")
                .build();
        Request request6 = new Request.Builder()
                .withLogin("user1")
                .withActivityName("activity1")
                .withActionToDo("remove")
                .withDescription("asap")
                .build();
        assertEquals(request1,  request2);
        assertEquals(request1, request3);
        assertNotEquals(request1, request4);
        assertNotEquals(request1, request5);
        assertNotEquals(request1, request6);
    }
    @Test
    void testInsert() {
        Request request1 = new Request.Builder()
                .withLogin("user1")
                .withActivityName("activity1")
                .withActionToDo("add")
                .withDescription("asap")
                .build();
        Request request4 = new Request.Builder()
                .withLogin("user2")
                .withActivityName("activity1")
                .withActionToDo("add")
                .withDescription("asap")
                .build();
        Request request5 = new Request.Builder()
                .withLogin("user1")
                .withActivityName("activity2")
                .withActionToDo("add")
                .withDescription("asap")
                .build();
        Request request6 = new Request.Builder()
                .withLogin("user1")
                .withActivityName("activity1")
                .withActionToDo("remove")
                .withDescription("asap")
                .build();
        List<Request> requests = new ArrayList<>();
        requests.add(request1);
        requests.add(request4);
        requests.add(request5);
        sort(requests, Request::getLogin);

        requestDAO.save(request1, con);
        requestDAO.save(request4, con);
        requestDAO.save(request5, con);

        List<Request> requestsFromDB = sort(requestDAO.getAll(con), Request::getLogin);
        assertEquals(requests, requestsFromDB);
    }

    @Test
    void exceptionToInsert(){
        Request request1 = new Request.Builder()
                .withLogin("user6")
                .withActivityName("activity1")
                .withActionToDo("add")
                .withDescription("asap")
                .build();
        Request request2 = new Request.Builder()
                .withLogin("user1")
                .withActivityName("activity6")
                .withActionToDo("add")
                .withDescription("asap")
                .build();
        Request request3 = new Request.Builder()
                .withLogin("user1")
                .withActivityName("activity4")
                .withActionToDo("remove")
                .withDescription("asap")
                .build();

        DAOException thrown = assertThrows(DAOException.class, () -> requestDAO.isDataCorrect(request1, con));
        assertEquals("wrong login: user6", thrown.getMessage());
        thrown = assertThrows(DAOException.class, () -> requestDAO.isDataCorrect(request2, con));
        assertEquals("wrong activity: activity6", thrown.getMessage());
        thrown = assertThrows(DAOException.class, () -> requestDAO.isDataCorrect(request3, con));
        assertEquals("wrong action: remove", thrown.getMessage());
        assertEquals(new ArrayList<>(), requestDAO.getAll(con));
    }

    private static <T, U extends Comparable<? super U>> List<T>
    sort(List<T> items, Function<T, U> extractor) {
        items.sort(Comparator.comparing(extractor));
        return items;
    }
    private static void createAndInsertActivities(int from, int to) {
        List<Activity> activities = IntStream.range(from, to)
                .mapToObj(RequestTests::createActivity)
                .collect(Collectors.toList());
        for (Activity activity : activities) {
            activityDAO.save(activity, con);
        }
    }
    private static Activity createActivity(int number){
        return new Activity.Builder().withName("activity" + number)
                .withCategory("categoryB")
                .build();
    }
    private static void createAndInsertUsers(int from, int to) {
        List<User> users = IntStream.range(from, to)
                .mapToObj(RequestTests::createUser)
                .collect(Collectors.toList());
        for (User user : users) {
            userDAO.save(user, con);
        }
    }
    private static User createUser(int number){
        return new User.Builder().withLogin("user" + number)
                .withEmail("user" + number + "@domen.com")
                .withPassword("password" + number)
                .withRole("system user")
                .withStatus("active")
                .withNotification("off")
                .build();
    }
}
