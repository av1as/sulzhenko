package com.sulzhenko.model.services;

import com.sulzhenko.model.Constants;
import com.sulzhenko.model.DTO.RequestDTO;
import com.sulzhenko.model.entity.Request;

import java.util.List;

public interface RequestService extends Constants {
    void approveRequest(Request request);
    Request getRequest(long id);
    List<Request> getAllRequest();
    List<Request> getRequestsToAdd();
    List<Request> getRequestsToRemove();
    void deleteRequest(Request request);
    void addRequest(RequestDTO requestDTO);
    void updateRequest(Request request, String[] params);
    List<RequestDTO> viewAllRequests(int startPosition, int size);
    List<RequestDTO> viewRequestsToAdd(int startPosition, int size);
    List<RequestDTO> viewRequestsToRemove(int startPosition, int size);
}
