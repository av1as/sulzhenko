package com.sulzhenko.model.services;

import com.sulzhenko.model.Constants;
import com.sulzhenko.DTO.RequestDTO;
import com.sulzhenko.model.entity.Request;

import java.util.List;

public interface RequestService extends Constants {
    void approveRequest(Request request) throws ServiceException;
    Request getRequest(long id);
    List<Request> getAllRequest();
    List<Request> getRequestsToAdd();
    List<Request> getRequestsToRemove();
    void deleteRequest(Request request) throws ServiceException;
    void removeRequest(Long requestId) throws ServiceException;
    void addRequest(RequestDTO requestDTO) throws ServiceException;
    void updateRequest(Request request, String[] params) throws ServiceException;
    void setRequestDescription(Long id, String description);
    List<RequestDTO> viewAllRequests(int startPosition, int size);
    List<RequestDTO> viewRequestsToAdd(int startPosition, int size);
    List<RequestDTO> viewRequestsToRemove(int startPosition, int size);
    List<RequestDTO> getRequestList(int page, String actionToDo, int recordsPerPage);
    List<RequestDTO> viewAllUserRequests(String login, int startPosition, int size);
    List<RequestDTO> viewUserRequestsByAction(String login, String action, int startPosition, int size);
    List<RequestDTO> getUserRequestList(String login, int page, String actionToDo, int recordsPerPage);
    int getNumberOfRecords(String actionToDo);
    int getUserNumberOfRecords(String login, String actionToDo);
    void notifyAboutUpdate(Request request, String description);
    boolean ifRequestUnique(Request request) throws ServiceException;
}
