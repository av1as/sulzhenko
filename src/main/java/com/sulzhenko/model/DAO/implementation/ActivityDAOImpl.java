package com.sulzhenko.model.DAO.implementation;

import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class describes CRUD operations with Activity class entities
 */
public class ActivityDAOImpl implements ActivityDAO {
    private final DataSource dataSource;
    CategoryDAO categoryDAO;

    public ActivityDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.categoryDAO = new CategoryDAOImpl(dataSource);
    }

    private static final Logger logger = LogManager.getLogger(ActivityDAOImpl.class);
    @Override
    public Optional<Activity> get(Object parameter, String querySQL) throws DAOException{
        Activity a = null;
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(querySQL)
        ) {
            stmt.setObject(1, parameter);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                a = getActivityWithFields(rs);
            }
        } catch (SQLException e){
            logger.fatal(e.getMessage());
            throw new DAOException("unknown.error", e);
        }
        return Optional.ofNullable(a);
    }
    @Override
    public List<Activity> getList(Object parameter, String querySQL) throws DAOException{
        List<Activity> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(querySQL)
        ) {
            stmt.setObject(1, parameter);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getActivityWithFields(rs));
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
            throw new DAOException("unknown.error", e);
        }
        return list;
    }
    @Override
    public List<Activity> getAll() throws DAOException{
        List<Activity> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQLQueries.ActivityQueries.GET_ALL_ACTIVITIES)
        ) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(getActivityWithFields(rs));
            }
        } catch (SQLException e){
            logger.fatal(e);
            throw new DAOException("unknown.error", e);
        }
        return list;
    }
    @Override
    public void save(Activity t) throws DAOException{
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQLQueries.ActivityQueries.INSERT_ACTIVITY)) {
            isDataCorrect(t);
            int k = 0;
            stmt.setString(++k, t.getName());
            stmt.setString(++k, t.getCategory().getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.info(e.getMessage());
            throw new DAOException("unknown.error", e);
        }
    }
    @Override
    public void update(Activity t, String[] params) throws DAOException{
        String oldName = t.getName();
        List<User> connectedUsers = getConnectedUsersWithNotification(t);
        String description = "activity " + "\"" + oldName + "\" "
                + "now has name " + "\"" + params[0] + "\" "
                + "and category " + "\"" + params[1] + "\"";
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQLQueries.ActivityQueries.UPDATE_ACTIVITY)) {
            isUpdateCorrect(params, oldName);
            int k = 0;
            stmt.setString(++k, params[k-1]);
            stmt.setString(++k, params[k-1]);
            stmt.setString(++k, oldName);
            stmt.executeUpdate();
            notifyAboutUpdate(connectedUsers, description);
        } catch (SQLException e){
            logger.info(e.getMessage());
            throw new DAOException("unknown.error", e);
        }
    }
    @Override
    public void delete(Activity t) throws DAOException{
        if(!isNameAvailable(t.getName())) {
            try (Connection con = dataSource.getConnection();
                    PreparedStatement stmt = con.prepareStatement(SQLQueries.ActivityQueries.DELETE_ACTIVITY)
            ) {
                stmt.setString(1, t.getName());
                stmt.executeUpdate();
            } catch (SQLException e) {
                logger.info(e.getMessage());
                throw new DAOException("unknown.error", e);
            }
        } else {
            logger.info("this activity doesn't exist: {}", t.getName());
            throw new DAOException("unknown.activity");
        }
    }
    @Override
    public Activity getById(long id) {
        return get(id, SQLQueries.ActivityQueries.GET_ACTIVITY_BY_ID).orElse(null);
    }
    @Override
    public Activity getByName(String name) {
        return get(name, SQLQueries.ActivityQueries.GET_ACTIVITY_BY_NAME).orElse(null);
    }
    @Override
    public List<Activity> getByCategory(String categoryName) {
        return getList(categoryName, SQLQueries.ActivityQueries.GET_ACTIVITIES_BY_CATEGORY);
    }

    @Override
    public void isDataCorrect(Activity t) throws DAOException {
        if (!isCategoryCorrect(t.getCategory().getName())){
            throw new DAOException("wrong.category");
        } else if (!isNameAvailable(t.getName())){
            throw new DAOException("wrong.name");
        }
    }
    /**
     * This method reads activity fields from result set
     */
    private Activity getActivityWithFields(ResultSet rs) throws SQLException {
        return new Activity.Builder()
                .withId(rs.getLong(1))
                .withName(rs.getString(2))
                .withCategory(categoryDAO.getByName(rs.getString(3)).orElse(null))
                .build();
    }
    @Override
    public boolean isNameAvailable(String name){
        return getByName(name) == null;
    }

    @Override
    public boolean isCategoryCorrect(String category) throws DAOException{
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQLQueries.CategoryQueries.FIND_CATEGORY_BY_NAME)
        ) {
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            logger.fatal("unknown exception when checking category");
            throw new DAOException("unknown.error");
        }
        return false;
    }
    @Override
    public void isUpdateCorrect(String[] params, String oldName) throws DAOException{
        if (!isCategoryCorrect(params[1])){
            throw new DAOException("wrong.category");
        } else if (!Objects.equals(params[0], oldName) && !isNameAvailable(params[0])){
            throw new DAOException("wrong.name");
        }
    }
    @Override
    public List<User> getConnectedUsers(Activity activity){
        UserDAO userDAOImpl = new UserDAOImpl(dataSource);
        return userDAOImpl.getList(activity.getName(), SQLQueries.UserQueries.FIND_CONNECTED_USERS);
    }
    @Override
    public Map<Activity, Integer> listActivitiesSorted(int startPosition, int size, String filter, String parameter, String order) throws DAOException{
        Map<Activity, Integer> map = new LinkedHashMap<>();
        List<Activity> list = new ArrayList<>();
        String category = (Objects.equals(filter, "all") ? "": "WHERE category_name = '" + filter + "'");

        String query = SQLQueries.ActivityQueries.SELECT_ALL_ACTIVITY_FIELDS +
                category +
                "ORDER BY " + parameter + " " + order;
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Activity activity = getActivityWithFields(rs);
                list.add(activity);
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
            throw new DAOException("unknown.error", e);
        }
        for(int i = startPosition; (i < (startPosition + size)) && (i < list.size()); i++){
            Activity activity = list.get(i);
            map.put(activity, getConnectedUsers(activity).size());
        }
        return map;
    }
    @Override
    public Map<Activity, Integer> listActivitiesSortedByUsers(int startPosition, int size, String filter, String order){
        Map<Activity, Integer> map = new LinkedHashMap<>();
        List<Activity> list = (Objects.equals(filter, "all") ? new ActivityDAOImpl(dataSource).getAll(): new ActivityDAOImpl(dataSource).getByCategory(filter));
        for(int i = startPosition; (i < (startPosition + size)) && (i < list.size()); i++){
            Activity activity = list.get(i);
            map.put(activity, getConnectedUsers(activity).size());
        }
        Map<Activity, Integer> sortedMap = new LinkedHashMap<>();
        if(Objects.equals(order, "ASC")) {
            sortedMap = rangeByUsersNumber(map, Map.Entry.comparingByValue());
        } else if (Objects.equals(order, "DESC")){
            sortedMap = rangeByUsersNumber(map, Collections.reverseOrder(Map.Entry.comparingByValue()));
        }
        return sortedMap;
    }

    private static Map<Activity, Integer> rangeByUsersNumber(Map<Activity, Integer> map, Comparator<Map.Entry<Activity, Integer>> comparator) {
        return map.entrySet()
                .stream()
            .sorted(comparator)
                .collect(Collectors
                        .toMap(Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new));
    }

    public List<User> getConnectedUsersWithNotification(Activity activity){
        UserDAO userDAOImpl = new UserDAOImpl(dataSource);
        return userDAOImpl.getList(activity.getName(), SQLQueries.UserQueries.FIND_CONNECTED_USERS_WITH_NOTIFICATION);
    }
    @Override
    public void notifyAboutUpdate(List<User> connectedUsers, String description){
        for(User user: connectedUsers){
            // create method to notify connected users


        }
    }
}
