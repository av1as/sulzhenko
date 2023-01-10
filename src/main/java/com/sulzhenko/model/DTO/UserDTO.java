package com.sulzhenko.model.DTO;

import java.io.Serializable;
import java.util.Map;

public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String login;
    private Map<String, Integer> userActivities;
    private int numberOfActivities;
    private int totalAmount;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Map<String, Integer> getUserActivities() {
        return userActivities;
    }

    public void setUserActivities(Map<String, Integer> userActivities) {
        this.userActivities = userActivities;
    }

    public int getNumberOfActivities() {
        return numberOfActivities;
    }

    public void setNumberOfActivities(int numberOfActivities) {
        this.numberOfActivities = numberOfActivities;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int countNumberOfActivities() {
        return userActivities.size();
    }

    public int countTotalAmount() {
        int total = 0;
        for(Integer key: userActivities.values())
            total += key;
        return total;
    }
}
