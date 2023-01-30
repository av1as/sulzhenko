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
import static com.sulzhenko.model.DAO.SQLQueries.RequestQueries.GET_EQUAL_REQUEST;
import static com.sulzhenko.model.DAO.SQLQueries.RequestQueries.IF_USER_HAS_ACTIVITY;

public class RequestServiceImpl implements RequestService {
    private final DataSource dataSource;
    UserService userService;
    UserDAO userDAO;
    ActivityDAO activityDAO;
    ActivityService activityService;
    UserActivityDAO userActivityDAO;
    RequestDAO requestDAO;

    public RequestServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.userService = new UserServiceImpl(dataSource);
        this.userDAO = new UserDAOImpl(dataSource);
        this.userActivityDAO = new UserActivityDAOImpl(dataSource);
        this.requestDAO = new RequestDAOImpl(dataSource);
        this.activityDAO = new ActivityDAOImpl(dataSource);
        this.activityService = new ActivityServiceImpl(dataSource);
    }
    private static final Logger logger = LogManager.getLogger(RequestServiceImpl.class);
    @Override
    public Request getRequest(long id){
        return requestDAO.getById(id);
    }
    @Override
    public List<Request> getAllRequest(){
        return requestDAO.getAll();
    }
    @Override
    public List<Request> getRequestsToAdd(){
        return requestDAO.getByActionToDo(ADD);
    }
    @Override
    public List<Request> getRequestsToRemove(){
        return requestDAO.getByActionToDo(REMOVE);
    }
    @Override
    public void approveRequest(Request request){
        if(Objects.equals(request.getActionToDo(), ADD)){
            try {
                userActivityDAO.addActivityToUser(request);
                notifyAboutUpdate(request, "has been approved");
            } catch (SQLException e) {
                logger.fatal(e);
                throw new ServiceException(UNKNOWN_ERROR);
            }
        } else if(Objects.equals(request.getActionToDo(), REMOVE)){
            try {
                userActivityDAO.removeUserActivity(request);
                notifyAboutUpdate(request, "has been approved");
            } catch (SQLException e) {
                logger.fatal(e);
                throw new ServiceException(UNKNOWN_ERROR);
            }
        }
    }
    @Override
    public void deleteRequest(Request request){
        try{
            requestDAO.delete(request);
            notifyAboutUpdate(request, "has been declined");
        } catch (DAOException e){
            logger.warn(e.getMessage());
            throw new ServiceException(e);
        }
    }
    @Override
    public void removeRequest(Long requestId){
        Request request = getRequest(requestId);
        try{
            requestDAO.delete(request);
        } catch (DAOException e){
            logger.warn(e.getMessage());
            throw new ServiceException(e);
        }
    }
    @Override
    public void addRequest(RequestDTO requestDTO){
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
    @Override
    public void updateRequest(Request request, String[] params){
        try{
            isDataCorrect(createUpdatedRequest(params));
            requestDAO.update(request, params);
        } catch (DAOException e){
            logger.warn(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }
    @Override
    public void setRequestDescription(Long id, String description){
        Request request = requestDAO.getById(id);
        String[] params = {request.getLogin(), request.getActivityName(), request.getActionToDo(), description};
        updateRequest(request, params);
    }
    @Override
    public List<RequestDTO> viewAllRequests(int startPosition, int size){
        List<RequestDTO> list = new ArrayList<>();
        for(int i = startPosition; (i < (startPosition + size)) && (i < requestDAO.getAll().size()); i++){
            Request request = requestDAO.getAll().get(i);
            RequestDTO requestDTO = new RequestDTO.Builder()
                    .withId(request.getId())
                    .withLogin(request.getLogin())
                    .withActivityName(request.getActivityName())
                    .withActionToDo(request.getActionToDo())
                    .withDescription(request.getDescription())
                    .build();
            list.add(requestDTO);
        }
        return list;
    }
    @Override
    public List<RequestDTO> viewRequestsToAdd(int startPosition, int size){
        List<RequestDTO> list = new ArrayList<>();
        for(int i = startPosition; (i < (startPosition + size))
                && (i < requestDAO.getByActionToDo(ADD).size()); i++){
            Request request = requestDAO.getByActionToDo(ADD).get(i);
            RequestDTO requestDTO = new RequestDTO.Builder()
                    .withLogin(request.getLogin())
                    .withActivityName(request.getActivityName())
                    .withActionToDo(request.getActionToDo())
                    .withDescription(request.getDescription())
                    .build();
            list.add(requestDTO);
        }
        return list;
    }
    @Override
    public List<RequestDTO> viewRequestsToRemove(int startPosition, int size){
        List<RequestDTO> list = new ArrayList<>();
        for(int i = startPosition; (i < (startPosition + size))
                && (i < requestDAO.getByActionToDo(REMOVE).size()); i++){
            Request request = requestDAO.getByActionToDo(REMOVE).get(i);
            RequestDTO requestDTO = new RequestDTO.Builder()
                    .withLogin(request.getLogin())
                    .withActivityName(request.getActivityName())
                    .withActionToDo(request.getActionToDo())
                    .withDescription(request.getDescription())
                    .build();
            list.add(requestDTO);
        }
        return list;
    }
    public List<RequestDTO> getRequestList(int page, String actionToDo, int recordsPerPage){
        if(Objects.equals(actionToDo, ADD)) {
            return viewRequestsToAdd((page-1)*recordsPerPage, recordsPerPage);
        } else if(Objects.equals(actionToDo, REMOVE)) {
            return viewRequestsToRemove((page-1)*recordsPerPage, recordsPerPage);
        } else {
            return viewAllRequests((page-1)*recordsPerPage, recordsPerPage);
        }
    }
    @Override
    public List<RequestDTO> viewAllUserRequests(String login, int startPosition, int size){
        List<RequestDTO> list = new ArrayList<>();
        for(int i = startPosition; (i < (startPosition + size)) && (i < requestDAO.getByLogin(login).size()); i++){
            Request request = requestDAO.getByLogin(login).get(i);
            RequestDTO requestDTO = new RequestDTO.Builder()
                    .withId(request.getId())
                    .withLogin(request.getLogin())
                    .withActivityName(request.getActivityName())
                    .withActionToDo(request.getActionToDo())
                    .withDescription(request.getDescription())
                    .build();
            list.add(requestDTO);
        }
        return list;
    }
    @Override
    public List<RequestDTO> viewUserRequestsAction(String login, String action, int startPosition, int size){
        List<RequestDTO> list = new ArrayList<>();
        for(int i = startPosition; (i < (startPosition + size))
                && (i < requestDAO.getByLoginAndAction(login, action).size()); i++){
            Request request = requestDAO.getByLoginAndAction(login, action).get(i);
            RequestDTO requestDTO = new RequestDTO.Builder()
                    .withId(request.getId())
                    .withLogin(request.getLogin())
                    .withActivityName(request.getActivityName())
                    .withActionToDo(request.getActionToDo())
                    .withDescription(request.getDescription())
                    .build();
            list.add(requestDTO);
        }
        return list;
    }
    public List<RequestDTO> getUserRequestList(String login, int page, String actionToDo, int recordsPerPage){
        if(Objects.equals(actionToDo, ADD)) {
            return viewUserRequestsAction(login, ADD, (page-1)*recordsPerPage, recordsPerPage);
        } else if(Objects.equals(actionToDo, REMOVE)) {
            return viewUserRequestsAction(login, REMOVE, (page-1)*recordsPerPage, recordsPerPage);
        } else {
            return viewAllUserRequests(login, (page-1)*recordsPerPage, recordsPerPage);
        }
    }
    public int getNumberOfRecords(String actionToDo){
        if(Objects.equals(actionToDo, ADD)) {
            return getRequestsToAdd().size();
        } else if(Objects.equals(actionToDo, REMOVE)) {
            return getRequestsToRemove().size();
        } else {
            return getAllRequest().size();
        }
    }
    public int getUserNumberOfRecords(String login, String actionToDo){
        if(Objects.equals(actionToDo, ADD)) {
            return requestDAO.getByLoginAndAction(login, ADD).size();
        } else if(Objects.equals(actionToDo, REMOVE)) {
            return requestDAO.getByLoginAndAction(login, REMOVE).size();
        } else {
            return requestDAO.getByLogin(login).size();
        }
    }
    private boolean ifRequestUnique(Request request) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_EQUAL_REQUEST)) {
            stmt.setString(1, request.getLogin());
            stmt.setString(2, request.getActivityName());
            stmt.setString(3, request.getActionToDo());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return false;
            }
        } catch (SQLException e){
            logger.fatal(e);
            throw new ServiceException(UNKNOWN_ERROR);
        }
        return true;
    }
    private void isDataCorrect(Request request) throws ServiceException{
        if(userService.isLoginAvailable(request.getLogin())) {
            throw new ServiceException(WRONG_LOGIN);
        } else if(activityService.isNameAvailable(request.getActivityName())){
            throw new ServiceException(WRONG_ACTIVITY);
        } else if(!isActionCorrect(request)){
            throw new ServiceException(WRONG_ACTION);
        }
    }
    private boolean isActionCorrect(Request request){
        return canRequestBeAdded(request) || canRequestBeRemoved(request);
    }
    private boolean canRequestBeAdded(Request request){
        return (Objects.equals(request.getActionToDo(), ADD)) &&
                !ifUserHasActivity(Objects.requireNonNull(userDAO.getByLogin(request.getLogin())
                                .orElse(null)),
                        activityDAO.getByName(request.getActivityName()));
    }
    private boolean canRequestBeRemoved(Request request){
        return (Objects.equals(request.getActionToDo(), REMOVE) &&
                ifUserHasActivity(Objects.requireNonNull(userDAO.getByLogin(request.getLogin())
                                .orElse(null)),
                        activityDAO.getByName(request.getActivityName())));
    }
    private boolean ifUserHasActivity(User u, Activity a){
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(IF_USER_HAS_ACTIVITY)) {
            stmt.setLong(1, u.getAccount());
            stmt.setLong(2, a.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
            throw new DAOException(UNKNOWN_ERROR);
        }
        return false;
    }
    private static Request createUpdatedRequest(String[] params) {
        int k = -1;
        return new Request.Builder()
                .withLogin(params[++k])
                .withActivityName(params[++k])
                .withActionToDo(params[++k])
                .withDescription(params[++k])
                .build();
    }
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
