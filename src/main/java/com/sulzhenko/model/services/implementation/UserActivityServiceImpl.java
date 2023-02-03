package com.sulzhenko.model.services.implementation;

import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.DAO.implementation.*;
import com.sulzhenko.DTO.UserDTO;
import com.sulzhenko.DTO.ActivityDTO;
import com.sulzhenko.DTO.UserActivityDTO;
import javax.sql.DataSource;
import com.sulzhenko.model.entity.Activity;
import com.sulzhenko.model.entity.Request;
import com.sulzhenko.model.entity.User;
import com.sulzhenko.model.services.RequestService;
import com.sulzhenko.model.services.ServiceException;
import com.sulzhenko.model.services.UserActivityService;
import com.sulzhenko.model.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import static com.sulzhenko.model.DAO.SQLQueries.UserActivityQueries.*;

/**
 * UserActivityService class for interaction between controller and UserActivity DAO
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */

public class UserActivityServiceImpl implements UserActivityService {
    UserDAO userDAO;
    ActivityDAO activityDAO;
    UserActivityDAO userActivityDAO;
    RequestDAO requestDAO;
    RequestService requestService;
    UserService userService;
    private static final Logger logger = LogManager.getLogger(UserActivityServiceImpl.class);

    public UserActivityServiceImpl(DataSource dataSource) {
        this.userDAO = new UserDAOImpl(dataSource);
        this.activityDAO = new ActivityDAOImpl(dataSource);
        this.userActivityDAO = new UserActivityDAOImpl(dataSource);
        this.requestDAO = new RequestDAOImpl(dataSource);
        this.requestService = new RequestServiceImpl(dataSource);
        this.userService = new UserServiceImpl(dataSource);
    }

    /**
     * Sets amount of time spent by user on some activity
     * @param login - user login
     * @param activityName - value of activity name
     * @param amount - amount of time
     * @throws ServiceException is custom exception
     */
    @Override
    public void setAmount(String login, String activityName, int amount) throws ServiceException {
        if(amount >= 0) {
            User user = userDAO.getByLogin(login).orElse(null);
            Activity activity = activityDAO.getByName(activityName).orElse(null);
            assert user != null;
            assert activity != null;
            if (userActivityDAO.ifUserHasActivity(user, activity)) {
                userActivityDAO.setAmount(user, activity, amount);
            } else {
                throw new ServiceException(USER_HAS_NO_ACTIVITY);
            }
        } else {
            throw new ServiceException(AMOUNT_NONNEGATIVE);
        }
    }

    /**
     * Gets list of activities available for certain user
     * @param userDTO - user
     * @return List of ActivityDTO
     */
    @Override
    public List<ActivityDTO> allAvailableActivities(UserDTO userDTO) {
        List<ActivityDTO> list = new ArrayList<>();
        User user = userService.getUser(userDTO.getLogin());
        for(Activity element: activityDAO.getAll()){
            if(!userActivityDAO.ifUserHasActivity(user, element)
                    && !isRequestToAddExists(user, element)){
                list.add(new ActivityDTO(element.getName()));
            }
        }
        return list;
    }

    /**
     * Gets number of records in database according to certain user
     * @param login - user login
     * @return number of records
     * @throws ServiceException is wrapper for DAOException
     */
    @Override
    public int getNumberOfRecords(String login) throws ServiceException {
        int number;
        try{
            number = userActivityDAO.getNumberOfRecordsByUser(login);
        } catch (DAOException e){
            logger.warn(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return number;
    }

    /**
     * Gets sorted and ordered list of records in database
     * @param page - number of page of the whole list of records, represented as a String
     * @return List of ActivityDTO
     * @throws ServiceException is wrapper for DAOException
     */
    @Override
    public List<UserActivityDTO> listAllUserActivitiesSorted(String page) throws ServiceException{
        List<UserActivityDTO> list;
        try{
            list = userActivityDAO.listAllUserActivitiesSorted(buildAllUsersQuery(page));
            for(UserActivityDTO element: list){
                User user = userDAO.getByLogin(element.getLogin()).orElse(null);
                Activity activity = activityDAO.getByName(element.getActivityName()).orElse(null);
                assert activity != null;
                element.setCategory(defineCategory(activity));
                element.setStatus(defineStatus(user, activity));
            }
        } catch (DAOException e){
            logger.warn(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return list;
    }

    /**
     * Gets ordered list of records in database
     * @return List of ActivityDTO
     * @throws ServiceException is wrapper for DAOException
     */
    @Override
    public List<UserActivityDTO> listFullPdf() throws ServiceException{
        List<UserActivityDTO> list;
        try{
            list = userActivityDAO.listAllUserActivitiesSorted(FULL_REPORT_QUERY);
            for(UserActivityDTO element: list){
                User user = userDAO.getByLogin(element.getLogin()).orElse(null);
                Activity activity = activityDAO.getByName(element.getActivityName()).orElse(null);
                assert activity != null;
                element.setCategory(defineCategory(activity));
                element.setStatus(defineStatus(user, activity));
            }
        } catch (DAOException e){
            logger.warn(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return list;
    }

    /**
     * Gets sorted and ordered list of records in database relating to certain user
     * @param userDTO - user
     * @param page - number of page of the whole list of records, represented as a String
     * @return List of ActivityDTO
     * @throws ServiceException is wrapper for DAOException
     */
    @Override
    public List<UserActivityDTO> listUserActivitiesSorted(String page, UserDTO userDTO) throws ServiceException{
        List<UserActivityDTO> list;
        try{
            list = userActivityDAO.listUserActivitiesSorted(buildUserQuery(page), userDTO.getLogin());
            for(UserActivityDTO dbElement: list){
                User user = userDAO.getByLogin(dbElement.getLogin()).orElse(null);
                Activity activity = activityDAO.getByName(dbElement.getActivityName()).orElse(null);
                assert activity != null;
                dbElement.setCategory(defineCategory(activity));
                dbElement.setStatus(defineStatus(user, activity));
            }
        } catch (DAOException e){
            logger.warn(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return list;
    }

    /**
     * Checks if request to add activity is already added to database
     * @param user - user who sent the request
     * @param activity - activity related to request
     * @return true if request to add activity is already added to database, false otherwise
     */
    @Override
    public boolean isRequestToAddExists(User user, Activity activity){
        Request requestToCompare = new Request.Builder()
                .withLogin(user.getLogin())
                .withActivityName(activity.getName())
                .withActionToDo(ADD)
                .build();
        return !requestService.ifRequestUnique(requestToCompare);
    }

    /**
     * Checks if request to remove activity is already added to database
     * @param user - user who sent the request
     * @param activity - activity related to request
     * @return true if request to remove activity is already added to database, false otherwise
     */
    @Override
    public boolean isRequestToRemoveExists(User user, Activity activity){
        Request requestToCompare = new Request.Builder()
                .withLogin(user.getLogin())
                .withActivityName(activity.getName())
                .withActionToDo(REMOVE)
                .build();
        return !requestService.ifRequestUnique(requestToCompare);
    }

    /**
     * Construes SQL query to call DAO class methods to get all users' activities
     * @param currentPage - number of page of the whole list of records, represented as a String
     * @return SQL query
     */
    private String buildAllUsersQuery(String currentPage){
        int page = 1;
        if(currentPage != null)
            page = Integer.parseInt(currentPage);
        int records = 5;
        int offset = (page - 1) * records;
        return String.format(ALL_USER_QUERY, offset, records);
    }

    /**
     * Construes SQL query to call DAO class methods to get certain user's activities
     * @param pageFromRequest - number of page of the whole list of records, represented as a String
     * @return SQL query
     */
    private String buildUserQuery(String pageFromRequest){
        int page = 1;
        if(pageFromRequest != null)
            page = Integer.parseInt(pageFromRequest);
        int records = 5;
        int offset = (page - 1) * records;
        return String.format(USER_QUERY, offset, records);
    }

    /**
     * Auxiliary method to define category of some activity
     * @param activity - activity
     * @return Category entity
     */
    private String defineCategory(Activity activity){
        return activity.getCategory().getName();
    }

    /**
     * Auxiliary method to define status of some user's activity
     * @param user - user
     * @param activity - activity
     * @return status
     */
    private String defineStatus(User user, Activity activity){
        String status;
        if (userActivityDAO.ifUserHasActivity(user, activity)
                && !isRequestToRemoveExists(user, activity)){
            status = ACTIVE_STATUS;
        } else if(userActivityDAO.ifUserHasActivity(user, activity)
                && isRequestToRemoveExists(user, activity)){
            status = PENDING_REMOVING;
        } else if(!userActivityDAO.ifUserHasActivity(user, activity)
                && isRequestToAddExists(user, activity)){
            status = PENDING_ADDING;
        } else {
            status = ERROR;
        }
        return status;
    }
}
