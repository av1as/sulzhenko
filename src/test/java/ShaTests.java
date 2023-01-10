import com.sulzhenko.model.DAO.UserDAO;
import com.sulzhenko.model.connectionPool.MyDataSource;
import com.sulzhenko.model.hashingPasswords.Sha;
import com.sulzhenko.model.DAO.implementation.UserDAOImpl;
import com.sulzhenko.model.entity.User;
import org.junit.jupiter.api.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static com.sulzhenko.model.DAO.SQLQueries.InitialData.*;
import static org.junit.jupiter.api.Assertions.*;
//public class ShaTests {
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
//    void hashToHex() throws UnsupportedEncodingException, NoSuchAlgorithmException {
//        String expected = "21B6C4849DB395945491002D81CE13B7C1AF0CF7D9490C77D17029D73C9D6BA30A986694495A98511C5D50D9578292E6685275E30F3EBBCEA5B6E3D60C9B175B";
//        assertEquals(expected, new Sha().hashToHex("password1", Optional.of("user1")));
//    }
//    @Test
//    void hashToBase64() throws UnsupportedEncodingException, NoSuchAlgorithmException {
//        String expected = "IBBEHJ2ZLZRUKQATGC4TT8GVDPFZSQX30XAP1ZYDA6MKMGAUSVQYURXDUNLXGPLMAFJ14W8+U86LTUPWDJSXWW==";
//        assertEquals(expected, new Sha().hashToBase64("password1", Optional.of("user1")));
//    }
//    @Test
//    void hashToHexFromDB() {
//        createAndInsertUsers(1, 2);
//        String expected = "21B6C4849DB395945491002D81CE13B7C1AF0CF7D9490C77D17029D73C9D6BA30A986694495A98511C5D50D9578292E6685275E30F3EBBCEA5B6E3D60C9B175B";
//        User t = USER_DAO_IMPL.getByLogin("user1");
//        assertEquals(expected, t.getPassword());
//    }
//    private List<User> createAndInsertUsers(int from, int to) {
//        List<User> users = IntStream.range(from, to)
//                .mapToObj(number -> {
//                    try {
//                        return createUser(number);
//                    } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
//                        throw new RuntimeException(e);
//                    }
//                })
//                .collect(Collectors.toList());
//        for (User user : users) {
//            USER_DAO_IMPL.save(user);
//        }
//        return users;
//    }
//    private static User createUser(int number) throws UnsupportedEncodingException, NoSuchAlgorithmException {
//        return new User.Builder().withLogin("user" + number)
//                .withEmail("user" + number + "@domain.com")
//                .withPassword(new Sha().hashToHex("password" + number, Optional.of("user" + number)))
//                .withRole("system user")
//                .withStatus("active")
//                .withNotification("off")
//                .build();
//    }
//}
