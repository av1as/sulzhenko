import com.sulzhenko.DAO.ActivityDAO;
import com.sulzhenko.DAO.DataSource;
import com.sulzhenko.DAO.RequestDAO;
import com.sulzhenko.DAO.UserDAO;
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
    private static RequestDAO requestDAO = new RequestDAO();
    private static ActivityDAO activityDAO = new ActivityDAO();
    private static UserDAO userDAO = new UserDAO();

    @BeforeAll
    static void globalSetUp() throws SQLException {
        con = DataSource.getConnection();
        con.createStatement().executeUpdate("DELETE FROM user_activity;");
        con.createStatement().executeUpdate("DELETE FROM request;");
        con.createStatement().executeUpdate("DELETE FROM activity;");
        con.createStatement().executeUpdate("DELETE FROM category_of_activity;");
        activityDAO.addCategory("categoryA");
        activityDAO.addCategory("categoryB");
        activityDAO.addCategory("categoryC");
        createAndInsertActivities(1, 5);
        con.createStatement().executeUpdate("DELETE FROM role;");
        con.createStatement().executeUpdate("DELETE FROM user_status;");
        con.createStatement().executeUpdate(INITIAL_ROLE_ADMIN);
        con.createStatement().executeUpdate(INITIAL_ROLE_USER);
        userDAO.addStatus("active");
        userDAO.addStatus("inactive");
        userDAO.addStatus("deactivated");
        con.createStatement().executeUpdate("DELETE FROM action_with_request;");
        con.createStatement().executeUpdate(INITIAL_ACTION_ADD);
        con.createStatement().executeUpdate(INITIAL_ACTION_REMOVE);
        createAndInsertUsers(1, 5);
//        con.createStatement().executeUpdate("INSERT INTO users_activities\n" +
//                "(account,\n" +
//                "activity_id\n" +
//                ")\n" +
//                "VALUES\n" +
//                "(1,\n" +
//                "1);");
//        con.createStatement().executeUpdate("INSERT INTO users_activities\n" +
//                "(account,\n" +
//                "activity_id\n" +
//                ")\n" +
//                "VALUES\n" +
//                "(2,\n" +
//                "2);");
//        con.createStatement().executeUpdate("INSERT INTO users_activities\n" +
//                "(account,\n" +
//                "activity_id\n" +
//                ")\n" +
//                "VALUES\n" +
//                "(3,\n" +
//                "3);");
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
        Request request1 = Request.builder()
                .withLogin("user1")
                .withActivityName("activity1")
                .withActionToDo("add")
                .withDescription("asap")
                .build();
        Request request2 = Request.builder()
                .withLogin("user1")
                .withActivityName("activity1")
                .withActionToDo("add")
                .build();
        Request request3 = Request.builder()
                .withLogin("user1")
                .withActivityName("activity1")
                .withActionToDo("add")
                .withDescription("urgent")
                .build();
        Request request4 = Request.builder()
                .withLogin("user2")
                .withActivityName("activity1")
                .withActionToDo("add")
                .withDescription("asap")
                .build();
        Request request5 = Request.builder()
                .withLogin("user1")
                .withActivityName("activity2")
                .withActionToDo("add")
                .withDescription("asap")
                .build();
        Request request6 = Request.builder()
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
        Request request1 = Request.builder()
                .withLogin("user1")
                .withActivityName("activity1")
                .withActionToDo("add")
                .withDescription("asap")
                .build();
        Request request4 = Request.builder()
                .withLogin("user2")
                .withActivityName("activity1")
                .withActionToDo("add")
                .withDescription("asap")
                .build();
        Request request5 = Request.builder()
                .withLogin("user1")
                .withActivityName("activity2")
                .withActionToDo("add")
                .withDescription("asap")
                .build();
        Request request6 = Request.builder()
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
//        requests.add(request6);
        requestDAO.save(request1);
        requestDAO.save(request4);
        requestDAO.save(request5);
//        requestDAO.save(request6);
        List<Request> requestsFromDB = sort(requestDAO.getAll(), Request::getLogin);
        assertEquals(requests, requestsFromDB);
    }
    @Test
    void exceptionToAdd(){
        Request request1 = Request.builder()
                .withLogin("user6")
                .withActivityName("activity1")
                .withActionToDo("add")
                .withDescription("asap")
                .build();
        Request request2 = Request.builder()
                .withLogin("user1")
                .withActivityName("activity6")
                .withActionToDo("add")
                .withDescription("asap")
                .build();
        Request request3 = Request.builder()
                .withLogin("user1")
                .withActivityName("activity4")
                .withActionToDo("remove")
                .withDescription("asap")
                .build();
        try {
            requestDAO.save(request1);
        } catch (Exception e){
//            assertTrue(e instanceof DAOException);






        assertEquals("wrong login: user6", e.getMessage());
        }
        try {
            requestDAO.save(request2);
        } catch (Exception e){
//            assertTrue(e instanceof DAOException);
            assertEquals("wrong activity: activity6", e.getMessage());
        }
        try {
            requestDAO.save(request3);
        } catch (Exception e){
//            assertTrue(e instanceof DAOException);
            assertEquals("wrong action to do: remove", e.getMessage());
        }

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
            activityDAO.save(activity);
        }
    }
    private static Activity createActivity(int number){
        return Activity.builder().withName("activity" + number)
                .withCategory("categoryB")
                .build();
    }
    private static void createAndInsertUsers(int from, int to) {
        List<User> users = IntStream.range(from, to)
                .mapToObj(RequestTests::createUser)
                .collect(Collectors.toList());
        for (User user : users) {
            userDAO.save(user);
        }
    }
    private static User createUser(int number){
        return User.builder().withLogin("user" + number)
                .withEmail("user" + number + "@domen.com")
                .withPassword("password" + number)
                .withRole("system user")
                .withStatus("active")
                .withNotifications("off")
                .build();
    }
}
