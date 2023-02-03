package com.sulzhenko.DTO;

import java.io.Serializable;

/**
 * RequestDTO class. Fields adapted to view.
 * Use RequestDTO.builder().withFieldName(fieldValue).build() to create an instance
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */

public class RequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String login;
    private String activityName;
    private String actionToDo;
    private String description;

    private RequestDTO() {
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getActionToDo() {
        return actionToDo;
    }

    public String getDescription() {
        return description;
    }
    public static class Builder { private final RequestDTO requestDTO;
        public Builder(){
            requestDTO = new RequestDTO();
        }
        public Builder withId(Long id) {
            requestDTO.id = id;
            return this;
        }
        public Builder withLogin(String login) {
            requestDTO.login = login;
            return this;
        }
        public Builder withActivityName(String activityName) {
            requestDTO.activityName = activityName;
            return this;
        }
        public Builder withActionToDo(String actionToDo) {
            requestDTO.actionToDo = actionToDo;
            return this;
        }
        public Builder withDescription(String description) {
            requestDTO.description = description;
            return this;
        }
        public RequestDTO build() {
            if(requestDTO.id == null) {
                requestDTO.id = 0L;
            }
            if (requestDTO.login == null || requestDTO.activityName == null || requestDTO.actionToDo == null) {
                throw new IllegalArgumentException();
            }
            return requestDTO;
        }
    }
}
