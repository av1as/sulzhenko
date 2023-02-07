package com.sulzhenko.model.services.implementation;

import com.sulzhenko.Util.notifications.Mailer;
import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.DAO.implementation.*;
import com.sulzhenko.DTO.ActivityDTO;
import com.sulzhenko.model.entity.*;
import com.sulzhenko.Util.notifications.NotificationFactories;
import com.sulzhenko.Util.notifications.NotificationFactory;
import com.sulzhenko.model.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.sql.DataSource;
import java.util.*;
import static com.sulzhenko.model.DAO.SQLQueries.ActivityQueries.COMMON_PART;
import static com.sulzhenko.model.DAO.SQLQueries.ActivityQueries.FIND_CONNECTED_USERS_WITH_NOTIFICATION;

/**
 * ActivityService class for interaction between controller and Activity DAO
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class ActivityServiceImpl implements ActivityService {
    private  final ActivityDAO activityDAO;
    private  final UserDAO userDAO;
    private  final CategoryService categoryService;
    public ActivityServiceImpl(DataSource dataSource) {
        this.activityDAO = new ActivityDAOImpl(dataSource);
        this.categoryService = new CategoryServiceImpl(dataSource);
        this.userDAO = new UserDAOImpl(dataSource);
    }
    private static final Logger logger = LogManager.getLogger(ActivityServiceImpl.class);

    /**
     * Gets instance of Activity by name
     * @param activityName - value of activity name
     * @return instance of Activity class
     */
    @Override
    public Activity getActivity(String activityName) throws ServiceException{
            return activityDAO.getByName(activityName).orElse(null);
    }

    /**
     * Inserts new activity to database
     * @param name - name of activity
     * @param categoryName - name of category
     * @throws ServiceException is wrapper for DAOException
     */
    @Override
    public void addActivity(String name, String categoryName) throws ServiceException{
        if(!categoryService.isCategoryNameAvailable(categoryName) && isNameAvailable(name)) {
            Activity activity = new Activity.Builder()
                    .withName(name)
                    .withCategory(categoryService.getCategory(categoryName))
                    .build();
            try {
                activityDAO.save(activity);
            } catch (DAOException e) {
                logger.warn(e.getMessage());
                throw new ServiceException(e);
            }
        } else if(!isNameAvailable(name)) throw new ServiceException(WRONG_ACTIVITY);
        else if(categoryService.isCategoryNameAvailable(categoryName)) throw new ServiceException(WRONG_CATEGORY);
    }

    /**
     * Updates activity
     * @param oldName - name of activity before update
     * @param newName - name of activity after update
     * @param newCategoryName - name of category after update
     * @throws ServiceException is wrapper for DAOException
     */
    @Override
    public void updateActivity(String oldName, String newName, String newCategoryName) throws ServiceException{
        if(isNameAvailable(oldName) || ((!Objects.equals(newName, oldName)) && !isNameAvailable(newName))){
            throw new ServiceException(WRONG_ACTIVITY);
        } else if(categoryService.isCategoryNameAvailable(newCategoryName)){
            throw new ServiceException(WRONG_CATEGORY);
        } else{
            Activity activity = activityDAO.getByName(oldName).orElse(null);
            String[] param = {newName, newCategoryName};
            assert activity != null;
            List<User> connectedUsers = getConnectedUsersWithNotification(oldName);
            String description = String.format(ACTIVITY_UPDATE, oldName, param[0], param[1]);
            try{
                activityDAO.update(activity, param);
                notifyAboutUpdate(connectedUsers, description);
            } catch (DAOException e){
                logger.warn(e.getMessage());
                throw new ServiceException(e.getMessage());
            }
        }
    }

    /**
     * Deletes activity record
     * @param name - activity name
     * @throws ServiceException is wrapper for DAOException
     */
    @Override
    public void deleteActivity(String name) throws ServiceException{
        if(!isNameAvailable(name)) {
            try{
                activityDAO.delete(activityDAO.getByName(name).orElse(null));
                List<User> connectedUsers = getConnectedUsersWithNotification(name);
                notifyAboutUpdate(connectedUsers, String.format(ACTIVITY_DELETE, name));
            } catch(DAOException e){
                logger.warn(e.getMessage());
                throw new ServiceException(e.getMessage());
            }
        } else {
            logger.info("this activity doesn't exist: {}", name);
            throw new ServiceException(WRONG_ACTIVITY);
        }
    }

    /**
     * Construes SQL query to call DAO class methods
     * @param pageFromRequest - number of page of the whole list of records, represented as a String
     * @param filter - filter to choose some records
     * @param orderFromRequest - ascending or descending order
     * @param parameterFromRequest - parameter to which order is applied
     * @return SQL query
     */
    private static String buildQuery(String pageFromRequest, String filter, String orderFromRequest,
                                     String parameterFromRequest){
        String parameter = getParam(parameterFromRequest, getOrder(orderFromRequest));
        int page = 1;
        if(pageFromRequest != null)
            page = Integer.parseInt(pageFromRequest);
        int records = 5;
        int offset = (page - 1) * records;
        return COMMON_PART + applyFilter(filter) + GROUP_ACTIVITY_NAME +
                String.format(ORDER_BY, parameter, offset, records);
    }

    /**
     * Construes part of SQL query containing parameter of ordering
     * @param parameter - parameter to which ascending or descending order is applied
     * @param order - ascending or descending order for sorting
     * @return part of SQL query
     */
    private static String getParam(String parameter, String order) {
        if(Objects.equals(parameter, NUMBER_OF_USERS)) parameter = String.format(QUANTITY, order);
        else if(Objects.equals(parameter, CATEGORY_OF_ACTIVITY)) parameter = String.format(CATEGORY_NAME, order);
        else parameter = String.format(ACTIVITY_NAME, order);
        return parameter;
    }

    /**
     * Construes part of SQL query containing order of sorting
     * @param order - ascending or descending order
     * @return part of SQL query
     */
    private static String getOrder(String order) {
        return Objects.equals(order, DESCENDING)? DESC : ASC;
    }

    /**
     * Construes filter to form SQL query
     * @param filter - filter for sorting records
     * @return part of SQL query
     */
    private static String applyFilter(String filter){
        if(filter == null || filter.isEmpty() || Objects.equals(filter, ALL_CATEGORIES)) return "";
        else return String.format("WHERE category_name = '%s'\n", filter);
    }

    /**
     * Gets number of records in database according to filter
     * @param filter - filter for accounting records
     * @return number of records
     * @throws ServiceException is wrapper for DAOException
     */
    @Override
    public int getNumberOfRecords(String filter) throws ServiceException{
        int number;
        try {
            number = activityDAO.getNumberOfRecords(filter);
        } catch (DAOException e){
            logger.warn(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return number;
    }

    /**
     * Gets sorted and ordered list of records in database
     * @param filter - filter for accounting records
     * @param order - ascending or descending order
     * @param parameter - parameter of sorting
     * @param page - number of page of the whole list of records, represented as a String
     * @return List of ActivityDTO
     * @throws ServiceException is wrapper for DAOException
     */
    @Override
    public List<ActivityDTO> listActivitiesSorted(String filter, String order, String parameter,
                                                  String page) throws ServiceException{
        List<ActivityDTO> list = new ArrayList<>();
        try {
            Map<Activity, Integer> map = activityDAO.getSortedMap(buildQuery(page, filter, order, parameter));
            map.forEach((activity, number)
                    -> list.add(new ActivityDTO(activity.getName(), activity.getCategory().getName(), number)));
        } catch (DAOException e) {
            throw new ServiceException(UNKNOWN_ERROR);
        }
        return list;
    }
    /**
     * Gets list of users connected with certain activity who turned on notifications
     * @param activityName - name of activity
     * @return List of User entities
     * @throws ServiceException is wrapper for SQLException
     */
    private List<User> getConnectedUsersWithNotification(String activityName){
        return userDAO.getList(activityName, FIND_CONNECTED_USERS_WITH_NOTIFICATION);
    }

    /**
     * Creates and sends emails to certain list of users
     * @param connectedUsers - list of addressees
     * @param description - text fragment to form email body
     */
    private static void notifyAboutUpdate(List<User> connectedUsers, String description){
        for(User user: connectedUsers){
            NotificationFactory factory = new NotificationFactories().systemUpdateFactory(user, description);
            String subject = factory.createSubject();
            String body = factory.createBody();
            Mailer.send(user.getEmail(), subject, body);
        }
    }

    /**
     * Checks if activity name is not already used in database
     * @param name - name of new activity
     * @return true if name is not used, false otherwise
     * @throws ServiceException is wrapper for SQLException
     */
    @Override
    public boolean isNameAvailable(String name) throws ServiceException{
        return getActivity(name) == null;
    }
}
