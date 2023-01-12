package com.sulzhenko.model.services;

import com.sulzhenko.model.entity.Request;

import java.util.List;

public interface RequestService {
    void approveRequest(Request request);
    Request getRequest(long id);
    List<Request> getAllRequest();
    List<Request> getRequestsToAdd();
    List<Request> getRequestsToRemove();
    void deleteRequest(Request request);
    void addRequest(Request request);
    void updateRequest(Request request, String[] params);
    List<Request> viewAllRequests(int startPosition, int size);
    List<Request> viewRequestsToAdd(int startPosition, int size);
    List<Request> viewRequestsToRemove(int startPosition, int size);
}
