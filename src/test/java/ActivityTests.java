import com.sulzhenko.model.DAO.ActivityDAO;
import com.sulzhenko.model.DAO.CategoryDAO;
import com.sulzhenko.model.DAO.implementation.ActivityDAOImpl;
import com.sulzhenko.model.DAO.DAOException;
import com.sulzhenko.model.DAO.implementation.CategoryDAOImpl;
import com.sulzhenko.model.connectionPool.MyDataSource;
import com.sulzhenko.model.entity.Activity;

import com.sulzhenko.model.entity.Category;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

//public class ActivityTests {
//    private static Connection con;
//    private static final ActivityDAO activityDAO = ActivityDAOImpl.getInstance();
//    private static final CategoryDAO categoryDAO = CategoryDAOImpl.getInstance();
//
//    @BeforeAll
//    static void globalSetUp() throws SQLException {
//        con = DataSource.getConnection();
////        con.createStatement().executeUpdate(DROP_USERS_ACTIVITIES_TABLE);
////        con.createStatement().executeUpdate(DROP_REQUESTS_TABLE);
////        con.createStatement().executeUpdate(DROP_USERS_TABLE);
////        con.createStatement().executeUpdate(DROP_USER_STATUS_TABLE);
////        con.createStatement().executeUpdate(DROP_ROLES_TABLE);
////        con.createStatement().executeUpdate(DROP_ACTIVITIES_TABLE);
////        con.createStatement().executeUpdate(DROP_CATEGORIES_OF_ACTIVITY_TABLE);
////        con.createStatement().executeUpdate(DROP_ACTIONS_WITH_REQUESTS_TABLE);
////        con.createStatement().executeUpdate(CREATE_ACTIONS_WITH_REQUESTS_TABLE);
////        con.createStatement().executeUpdate(CREATE_CATEGORIES_OF_ACTIVITY_TABLE);
////        con.createStatement().executeUpdate(CREATE_ACTIVITIES_TABLE);
////        con.createStatement().executeUpdate(CREATE_ROLES_TABLE);
////        con.createStatement().executeUpdate(CREATE_USER_STATUS_TABLE);
////        con.createStatement().executeUpdate(CREATE_USERS_TABLE);
////        con.createStatement().executeUpdate(CREATE_REQUESTS_TABLE);
////        con.createStatement().executeUpdate(CREATE_USERS_ACTIVITIES_TABLE);
////        con.createStatement().executeUpdate(INITIAL_ROLE_ADMIN);
////        con.createStatement().executeUpdate(INITIAL_ROLE_USER);
//        con.createStatement().executeUpdate("DELETE FROM activity;");
//        con.createStatement().executeUpdate("DELETE FROM category_of_activity;");
//        categoryDAO.save(new Category("categoryA"));
//        categoryDAO.save(new Category("categoryB"));
//        categoryDAO.save(new Category("categoryC"));
////        activityDAO.addCategory("categoryB", con);
////        activityDAO.addCategory("categoryC", con);
//    }
//
//    @AfterAll
//    static void globalTearDown() throws SQLException {
//        con.createStatement().executeUpdate("DELETE FROM activity;");
//        con.createStatement().executeUpdate("DELETE FROM category_of_activity;");
//        con.close();
//    }
////    @BeforeEach
////    void setUp() throws SQLException {
////        //dbm = DBManager.getInstance();
////
////    }
//
//    @AfterEach
//    void tearDown() throws SQLException {
//        con.createStatement().executeUpdate("DELETE FROM activity;");
//    }
//    @Test
//    void testEquality() {
//        Activity activity1 = new Activity.Builder().withName("testActivity").build();
//        Activity activity2 = new Activity.Builder().withName("testActivity").build();
//        Activity activity3 = new Activity.Builder().withName("tesActivity").build();
//        Activity activity4 = new Activity.Builder()
//                .withName("testActivity")
//                .withCategory(categoryDAO.getByName("categoryC").orElse(null))
//                .build();
//        Activity activity5 = new Activity.Builder()
//                .withName("tesActivity")
//                .withCategory(categoryDAO.getByName("categoryC").orElse(null))
//                .build();
//        assertEquals("testActivity",  activity1.getName());
//        assertEquals(activity1, activity2, "Two activities must be equaled if their names are equaled");
//        assertEquals(activity1, activity4, "Two activities must be equaled if their names are equaled");
//        assertEquals(activity3, activity5, "Two activities must be equaled if their names are equaled");
//        assertNotEquals(activity1, activity3, "Two activities must not be equaled if their names aren't equaled");
//        assertNotEquals(activity4, activity5, "Two activities must not be equaled if their names aren't equaled");
//    }
//    @Test
//    void testInsert() {
//        List<Activity> activities = createAndInsertActivities(1, 5);
////        for (Activity activity: activities){
////            activityDAO.save(activity);
////        }
//        List<Activity> activitiesFromDB = sort(activityDAO.getAll(), Activity::getName);
//        assertEquals(activities, activitiesFromDB);
//    }
//    @Test
//    void testActivityInsertException(){
//        createAndInsertActivities(1, 3);
//        Activity activity1 = new Activity.Builder()
//                .withName("activity1")
//                .withCategory(categoryDAO.getByName("categoryB").orElse(null))
//                .build();
//        Activity activity2 = new Activity.Builder()
//                .withName("activity3")
//                .withCategory(new Category("categoryD"))
//                .build();
//        DAOException thrown = assertThrows(DAOException.class,
//                () -> activityDAO.isDataCorrect(activity1));
//        assertEquals("wrong.name", thrown.getMessage());
//        thrown = assertThrows(DAOException.class,
//                () -> activityDAO.isDataCorrect(activity2));
//        assertEquals("wrong.category", thrown.getMessage());
//
//        thrown = assertThrows(DAOException.class,
//                () -> activityDAO.save(activity1));
//        assertEquals("wrong.name", thrown.getMessage());
//        thrown = assertThrows(DAOException.class,
//                () -> activityDAO.save(activity2));
//        assertEquals("wrong.category", thrown.getMessage());
//        assertEquals(2, activityDAO.getAll().size());
//    }
//
//    @Test
//    void testUpdate() {
//        List<Activity> activities = createAndInsertActivities(1, 5);
//        Activity newActivity = new Activity.Builder()
//                .withName("updated activity2")
//                .withCategory(categoryDAO.getByName("categoryC").orElse(null))
//                .build();
//        String[] param = {"updated activity2", "categoryC"};
//        activityDAO.update(activityDAO.getByName("activity2"), param);
//        List<Activity> activitiesFromDB = sort(activityDAO.getAll(), Activity::getName);
//        activities.set(1, newActivity);
//        sort(activities, Activity::getName);
//        assertEquals(activities, activitiesFromDB);
//    }
//    @Test
//    void testDelete() {
//        List<Activity> activities = createAndInsertActivities(1, 5);
//        activityDAO.delete(activityDAO.getByName("activity2"));
//        List<Activity> activitiesFromDB = sort(activityDAO.getAll(), Activity::getName);
//        activities.remove(1);
//        sort(activities, Activity::getName);
//        assertEquals(activities, activitiesFromDB);
//    }
//    @Test
//    void testDeletedCategory() {
//        createAndInsertActivities(1, 5);
//        categoryDAO.delete(categoryDAO.getByName("categoryB").orElse(null));
//        List<Activity> activitiesFromDB = sort(activityDAO.getAll(), Activity::getName);
//        assertEquals(activitiesFromDB, new ArrayList<Activity>());
//        categoryDAO.save(new Category("categoryB"));
//    }
//    @Test
//    void testGetActivitiesByCategory(){
//        createAndInsertActivities(1, 5);
//        Activity activity5 = new Activity.Builder()
//                .withName("activity5")
//                .withCategory(categoryDAO.getByName("categoryA").orElse(null))
//                .build();
//        activityDAO.save(activity5);
//        assertEquals(4, activityDAO.getByCategory("categoryB").size());
//    }
//
//    private static <T, U extends Comparable<? super U>> List<T>
//    sort(List<T> items, Function<T, U> extractor) {
//        items.sort(Comparator.comparing(extractor));
//        return items;
//    }
//    private List<Activity> createAndInsertActivities(int from, int to) {
//        List<Activity> activities = IntStream.range(from, to)
//                .mapToObj(ActivityTests::createActivity)
//                .collect(Collectors.toList());
//        for (Activity activity : activities) {
//            activityDAO.save(activity);
//        }
//        return activities;
//    }
//    private static Activity createActivity(int number){
//        return new Activity.Builder()
//                .withName("activity" + number)
//                .withCategory(categoryDAO.getByName("categoryB").orElse(null))
//                .build();
//    }
//}
