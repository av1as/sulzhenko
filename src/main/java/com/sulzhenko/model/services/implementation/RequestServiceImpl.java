package com.sulzhenko.model.services.implementation;

import com.sulzhenko.Util.notifications.Mailer;
import com.sulzhenko.model.DAO.*;
import com.sulzhenko.model.DAO.implementation.*;
import com.sulzhenko.DTO.RequestDTO;
import com.sulzhenko.model.entity.*;
import com.sulzhenko.Util.notifications.NotificationFactories;
import com.sulzhenko.Util.notifications.NotificationFactory;
import com.sulzhenko.model.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * RequestService class for interaction between controller and Request DAO
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class RequestServiceImpl implements RequestService {
    private  final UserService userService;
    private  final UserDAO userDAO;
    private  final ActivityDAO activityDAO;
    private  final ActivityService activityService;
    private  final UserActivityDAO userActivityDAO;
    private  final RequestDAO requestDAO;

    public RequestServiceImpl(DataSource dataSource) {
        this.userService = new UserServiceImpl(dataSource);
        this.userDAO = new UserDAOImpl(dataSource);
        this.userActivityDAO = new UserActivityDAOImpl(dataSource);
        this.requestDAO = new RequestDAOImpl(dataSource);
        this.activityDAO = new ActivityDAOImpl(dataSource);
        this.activityService = new ActivityServiceImpl(dataSource);
    }
    private static final Logger logger = LogManager.getLogger(RequestServiceImpl.class);

    /**
     * Gets instance of Request by id
     * @param id - request id
     * @return instance of Activity class
     */
    @Override
    public Request getRequest(long id){
        return requestDAO.getById(id).orElse(null);
    }

    /**
     * Gets list of all requests
     * @return list of Requests
     */
    @Override
    public List<Request> getAllRequest(){
        return requestDAO.getAll();
    }

    /**
     * Gets list of all requests to add activity
     * @return list of Requests
     */
    @Override
    public List<Request> getRequestsToAdd(){
        return requestDAO.getByActionToDo(ADD);
    }

    /**
     * Gets list of all requests to remove activity
     * @return list of Requests
     */
    @Override
    public List<Request> getRequestsToRemove(){
        return requestDAO.getByActionToDo(REMOVE);
    }

    /**
     * Approves request
     * @throws ServiceException is wrapper for SQLException, DAOException
     */
    @Override
    public void approveRequest(Request request) throws ServiceException{
        if(Objects.equals(request.getActionToDo(), ADD)){
            try {
                userActivityDAO.addActivityToUser(request);
                notifyAboutUpdate(request, APPROVED_DESCRIPTION);
            } catch (SQLException e) {
                logger.fatal(e);
                throw new ServiceException(UNKNOWN_ERROR);
            }
        } else if(Objects.equals(request.getActionToDo(), REMOVE)){
            try {
                userActivityDAO.removeUserActivity(request);
                notifyAboutUpdate(request, APPROVED_DESCRIPTION);
            } catch (SQLException e) {
                logger.fatal(e);
                throw new ServiceException(UNKNOWN_ERROR);
            }
        }
    }

    /**
     * Deletes request
     * @throws ServiceException is wrapper for DAOException
     */
    @Override
    public void deleteRequest(Request request) throws ServiceException{
        try{
            requestDAO.delete(request);
            notifyAboutUpdate(request, DECLINED_DESCRIPTION);
        } catch (DAOException e){
            logger.warn(e.getMessage());
            throw new ServiceException(e);
        }
    }

    /**
     * Removes request
     * @throws ServiceException is wrapper for DAOException
     */
    @Override
    public void removeRequest(Long requestId) throws ServiceException{
        Request request = getRequest(requestId);
        try{
            requestDAO.delete(request);
        } catch (DAOException e){
            logger.warn(e.getMessage());
            throw new ServiceException(e);
        }
    }

    /**
     * Inserts new request to database
     * @param requestDTO - request to insert as DTO
     * @throws ServiceException is wrapper for DAOException
     */
    @Override
    public void addRequest(RequestDTO requestDTO) throws ServiceException{
        Request request = new Request.Builder()
                .withLogin(requestDTO.getLogin())
                .withActivityName(requestDTO.getActivityName())
                .withActionToDo(requestDTO.getActionToDo())
                .withDescription(requestDTO.getDescription())
                .build();
        if(ifRequestUnique(request)) {
            try {
                isDataCorrect(request);
                requestDAO.save(request);
            } catch (DAOException e){
                logger.warn(e.getMessage());
                throw new ServiceException(e);
            }
        } else throw new ServiceException("request.not.unique");
    }

    /**
     * Updates request
     * @param request - request to update
     * @param params - new fields of request
     * @throws ServiceException is wrapper for DAOException
     */
    @Override
    public void updateRequest(Request request, String[] params) throws ServiceException{
        try{
            isDataCorrect(createUpdatedRequest(params));
            requestDAO.update(request, params);
        } catch (DAOException e){
            logger.warn(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Sets request's description
     * @param id - request id
     * @param description - some request's description
     */
    @Override
    public void setRequestDescription(Long id, String description){
        Request request = requestDAO.getById(id).orElse(null);
        assert request != null;
        String[] params = {request.getLogin(), request.getActivityName(), request.getActionToDo(), description};
        updateRequest(request, params);
    }

    /**
     * Gets sorted and ordered list of requests in database
     * @param startPosition - offset for record's list
     * @param size - number of records per page
     * @return List of RequestDTO
     */
    @Override
    public List<RequestDTO> viewAllRequests(int startPosition, int size){
        List<RequestDTO> list = new ArrayList<>();
        for(int i = startPosition; (i < (startPosition + size)) && (i < requestDAO.getAll().size()); i++){
            list.add(getRequestDtoFromRequest(requestDAO.getAll().get(i)));
        }
        return list;
    }

    /**
     * Auxiliary method to convert request into requestDTO
     * @param request - Request entity
     * @return RequestDTO
     */
    private static RequestDTO getRequestDtoFromRequest(Request request) {
        return new RequestDTO.Builder()
                .withId(request.getId())
                .withLogin(request.getLogin())
                .withActivityName(request.getActivityName())
                .withActionToDo(request.getActionToDo())
                .withDescription(request.getDescription())
                .build();
    }

    /**
     * Gets sorted and ordered list of requests to add activity
     * @param startPosition - offset for record's list
     * @param size - number of records per page
     * @return List of RequestDTO
     */
    @Override
    public List<RequestDTO> viewRequestsToAdd(int startPosition, int size){
        List<RequestDTO> list = new ArrayList<>();
        for(int i = startPosition; (i < (startPosition + size))
                && (i < requestDAO.getByActionToDo(ADD).size()); i++){
            list.add(getRequestDtoFromRequest(requestDAO.getAll().get(i)));
        }
        return list;
    }

    /**
     * Gets sorted and ordered list of requests to remove activity
     * @param startPosition - offset for records' list
     * @param size - number of records per page
     * @return List of RequestDTO
     */
    @Override
    public List<RequestDTO> viewRequestsToRemove(int startPosition, int size){
        List<RequestDTO> list = new ArrayList<>();
        for(int i = startPosition; (i < (startPosition + size))
                && (i < requestDAO.getByActionToDo(REMOVE).size()); i++){
            list.add(getRequestDtoFromRequest(requestDAO.getAll().get(i)));
        }
        return list;
    }

    /**
     * Gets sorted and ordered list of requests
     * @param page - number of page from the whole list to show
     * @param actionToDo - requesting action: to add or to remove activity (or both)
     * @param recordsPerPage - number of records per page
     * @return List of RequestDTO
     */
    public List<RequestDTO> getRequestList(int page, String actionToDo, int recordsPerPage){
        if(Objects.equals(actionToDo, ADD)) {
            return viewRequestsToAdd((page-1)*recordsPerPage, recordsPerPage);
        } else if(Objects.equals(actionToDo, REMOVE)) {
            return viewRequestsToRemove((page-1)*recordsPerPage, recordsPerPage);
        } else {
            return viewAllRequests((page-1)*recordsPerPage, recordsPerPage);
        }
    }

    /**
     * Gets sorted and ordered list of certain user's requests
     * @param login - user login
     * @param startPosition - offset for records' list
     * @param size - number of records per page
     * @return List of RequestDTO
     */
    @Override
    public List<RequestDTO> viewAllUserRequests(String login, int startPosition, int size){
        List<RequestDTO> list = new ArrayList<>();
        for(int i = startPosition; (i < (startPosition + size)) &&
                (i < requestDAO.getByLogin(login).size()); i++){
            list.add(getRequestDtoFromRequest(requestDAO.getByLogin(login).get(i)));
        }
        return list;
    }

    /**
     * Shows list of certain user's requests filtered by action
     * @param login - user login
     * @param action - requested action with activity: to add or to remove
     * @param startPosition - offset for records' list
     * @param size - number of records per page
     * @return List of RequestDTO
     */
    @Override
    public List<RequestDTO> viewUserRequestsByAction(String login, String action, int startPosition, int size){
        List<RequestDTO> list = new ArrayList<>();
        for(int i = startPosition; (i < (startPosition + size))
                && (i < requestDAO.getByLoginAndAction(login, action).size()); i++){
            list.add(getRequestDtoFromRequest(requestDAO.getByLoginAndAction(login, action).get(i)));
        }
        return list;
    }

    /**
     * Shows list of certain user's requests depending on filter
     * @param login - user login
     * @param page - number of page from view
     * @param actionToDo - requested action with activity: to add or to remove
     * @param recordsPerPage - number of records per page
     * @return List of RequestDTO
     */
    public List<RequestDTO> getUserRequestList(String login, int page, String actionToDo, int recordsPerPage){
        if(Objects.equals(actionToDo, ADD)) {
            return viewUserRequestsByAction(login, ADD, (page-1)*recordsPerPage, recordsPerPage);
        } else if(Objects.equals(actionToDo, REMOVE)) {
            return viewUserRequestsByAction(login, REMOVE, (page-1)*recordsPerPage, recordsPerPage);
        } else {
            return viewAllUserRequests(login, (page-1)*recordsPerPage, recordsPerPage);
        }
    }

    /**
     * Gets number of records in database according to requested action
     * @param actionToDo - requested action with activity: to add or to remove
     * @return number of records
     */
    public int getNumberOfRecords(String actionToDo){
        if(Objects.equals(actionToDo, ADD)) {
            return getRequestsToAdd().size();
        } else if(Objects.equals(actionToDo, REMOVE)) {
            return getRequestsToRemove().size();
        } else {
            return getAllRequest().size();
        }
    }

    /**
     * Gets number of records in database according to certain user and requested action
     * @param login - user login
     * @param actionToDo - requested action with activity: to add or to remove
     * @return number of records
     */
    public int getUserNumberOfRecords(String login, String actionToDo){
        if(Objects.equals(actionToDo, ADD)) {
            return requestDAO.getByLoginAndAction(login, ADD).size();
        } else if(Objects.equals(actionToDo, REMOVE)) {
            return requestDAO.getByLoginAndAction(login, REMOVE).size();
        } else {
            return requestDAO.getByLogin(login).size();
        }
    }

    /**
     * Checks if there is equal request in database
     * @param request - request to check
     * @return true if database doesn't contain equal request, false otherwise
     * @throws ServiceException is wrapper for DAOException
     */
    @Override
    public boolean ifRequestUnique(Request request) throws ServiceException {
        boolean result;
        try {
            result = requestDAO.ifRequestUnique(request);
        } catch (DAOException e){
            logger.fatal(e);
            throw new ServiceException(UNKNOWN_ERROR);
        }
        return result;
    }

    /**
     * Auxiliary method to check if input is correct
     * @param request - request to check
     * @throws ServiceException is custom Exception explaining what is wrong with input
     */
    private void isDataCorrect(Request request) throws ServiceException{
        if(userService.isLoginAvailable(request.getLogin())) {
            throw new ServiceException(WRONG_LOGIN);
        } else if(activityService.isNameAvailable(request.getActivityName())){
            throw new ServiceException(WRONG_ACTIVITY);
        } else if(!isActionCorrect(request)){
            throw new ServiceException(WRONG_ACTION);
        }
    }

    /**
     * Auxiliary method to check if input action is correct
     * @param request - request to check
     * @throws ServiceException is custom Exception explaining what is wrong with input
     */
    private boolean isActionCorrect(Request request){
        return canRequestBeAdded(request) || canRequestBeRemoved(request);
    }

    /**
     * Auxiliary method to check if request to add activity can be approved
     * @param request - request to check
     * @return true if request can be approved, otherwise false
     */
    private boolean canRequestBeAdded(Request request){
        return (Objects.equals(request.getActionToDo(), ADD)) &&
                !requestDAO.ifUserHasActivity(Objects.requireNonNull(userDAO.getByLogin(request.getLogin())
                                .orElse(null)),
                        Objects.requireNonNull(activityDAO.getByName(request.getActivityName()).orElse(null)));
    }

    /**
     * Auxiliary method to check if request to remove activity can be approved
     * @param request - request to check
     * @return true if request can be approved, otherwise false
     */
    private boolean canRequestBeRemoved(Request request){
        return (Objects.equals(request.getActionToDo(), REMOVE) &&
                requestDAO.ifUserHasActivity(Objects.requireNonNull(userDAO.getByLogin(request.getLogin())
                                .orElse(null)),
                        Objects.requireNonNull(activityDAO.getByName(request.getActivityName()).orElse(null))));
    }

    /**
     * Auxiliary method to create Request entity and transfer it for checking
     * @param params - request fields to check
     * @return Request entity
     */
    private static Request createUpdatedRequest(String[] params) {
        int k = -1;
        return new Request.Builder()
                .withLogin(params[++k])
                .withActivityName(params[++k])
                .withActionToDo(params[++k])
                .withDescription(params[++k])
                .build();
    }

    /**
     * Creates and sends emails to certain user about their request
     * @param request - Request entity
     * @param description - text fragment to form email body
     */
    @Override
    public void notifyAboutUpdate(Request request, String description){
        User user = userService.getUser(request.getLogin());
        if(Objects.equals(user.getNotification(), ON)) {
            NotificationFactory factory = new NotificationFactories().requestUpdateFactory(user, request, description);
            String subject = factory.createSubject();
            String body = factory.createBody();
            Mailer.send(user.getEmail(), subject, body);
        }
    }
}
