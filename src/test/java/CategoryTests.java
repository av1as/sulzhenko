import com.sulzhenko.model.DAO.CategoryDAO;
import com.sulzhenko.model.DAO.implementation.CategoryDAOImpl;
import com.sulzhenko.model.connectionPool.MyDataSource;
import com.sulzhenko.model.entity.Category;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

//public class CategoryTests {
//    private static Connection con;
//
//    private static final CategoryDAO categoryDAO = CategoryDAOImpl.getInstance();
//
//    @BeforeAll
//    static void globalSetUp() throws SQLException {
//        con = DataSource.getConnection();
//        con.createStatement().executeUpdate("DELETE FROM activity;");
//        con.createStatement().executeUpdate("DELETE FROM category_of_activity;");
//    }
//    @AfterAll
//    static void globalTearDown() throws SQLException {
//        con.createStatement().executeUpdate("DELETE FROM activity;");
//        con.createStatement().executeUpdate("DELETE FROM category_of_activity;");
//        con.close();
//    }
//    @AfterEach
//    void tearDown() throws SQLException {
//        con.createStatement().executeUpdate("DELETE FROM category_of_activity;");
//    }
//    @Test
//    void testEquality() {
//        Category category1 = new Category("testCategory");
//        Category category2 = new Category("testCategory");
//        Category category3 = new Category("testCategory1");
//
//        assertEquals("testCategory",  category1.getName());
//        assertEquals(category1, category2, "Two categories must be equaled if their names are equaled");
//
//        assertNotEquals(category1, category3, "Two categories must not be equaled if their names aren't equaled");
//        assertNotEquals(category2, category3, "Two categories must not be equaled if their names aren't equaled");
//    }
//    @Test
//    void testInsert() {
//        List<Category> categories = createAndInsertCategories(1, 5);
//
//        List<Category> categoriesFromDB = sort(categoryDAO.getAll(), Category::getName);
//        assertEquals(categories, categoriesFromDB);
//    }
//    @Test
//    void testUpdate() {
//        List<Category> categories = createAndInsertCategories(1, 5);
//        Category newCategory = new Category("updated category2");
//        String[] param = {"updated category2"};
//        categoryDAO.update(categoryDAO.getByName("category2").orElse(null), param);
//        List<Category> categoriesFromDB = sort(categoryDAO.getAll(), Category::getName);
//        categories.set(1, newCategory);
//        sort(categories, Category::getName);
//        assertEquals(categories, categoriesFromDB);
//    }
//    @Test
//    void testDelete() {
//        List<Category> categories = createAndInsertCategories(1, 5);
//        categoryDAO.delete(categoryDAO.getByName("category2").orElse(null));
//        List<Category> categoriesFromDB = sort(categoryDAO.getAll(), Category::getName);
//        categories.remove(1);
//        sort(categories, Category::getName);
//        assertEquals(categories, categoriesFromDB);
//    }
//
//
//
//    private static <T, U extends Comparable<? super U>> List<T>
//    sort(List<T> items, Function<T, U> extractor) {
//        items.sort(Comparator.comparing(extractor));
//        return items;
//    }
//    private List<Category> createAndInsertCategories(int from, int to) {
//        List<Category> categories = IntStream.range(from, to)
//                .mapToObj(CategoryTests::createCategory)
//                .collect(Collectors.toList());
//        for (Category category : categories) {
//            categoryDAO.save(category);
//        }
//        return categories;
//    }
//    private static Category createCategory(int number){
//        return new Category("category" + number);
//    }
//
//}
