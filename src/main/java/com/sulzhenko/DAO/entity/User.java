package com.sulzhenko.DAO.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class describes user entity
 */

public class User implements Serializable {
    private Integer account;
    private String login;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String role;
    private String status;
    private String notifications;

    public User() {
    }

    public User(Integer account, String login, String email, String password, String firstName, String lastName, String role, String status, String notifications) {
        this.account = account;
        this.login = login;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.status = status;
        this.notifications = notifications;
    }

    public Integer getAccount() {
        return account;
    }

    public String getLogin() {
        return login;
    }


    public String getEmail() {
        return email;
    }


    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public String getNotifications() {
        return notifications;
    }
    public String getFullName(){
        if (firstName != null && lastName != null) return firstName + " " + lastName;
        else if (firstName != null) return firstName;
        else if (lastName != null) return lastName;
        else return login;
    }

    public static Builder builder() {
        return new Builder();
    }
    /**
     * This inner class uses Builder pattern instead of setters
     * with methods like "withXXX" (where XXX - some field of User class)
     */
    public static class Builder {
        private Integer account;
        private String login;
        private String password;
        private String email;
        private String firstName;
        private String lastName;
        private String role;
        private String status;
        private String notifications;

        public Builder withAccount(Integer account) {
            this.account = account;
            return this;
        }
        public Builder withLogin(String login) {
            this.login = login;
            return this;
        }
        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }
        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }
        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        public Builder withRole(String role) {
            this.role = role;
            return this;
        }
        public Builder withStatus(String status) {
            this.status = status;
            return this;
        }
        public Builder withNotifications(String notifications) {
            this.notifications = notifications;
            return this;
        }

        public User build() {
            if(account == null) {
                account = 0;
            }
            if (login == null) {
                throw new IllegalArgumentException();
            }
            return new User(account, login, email, password, firstName, lastName, role, status, notifications);
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return login.equals(user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }

    @Override
    public String toString() {
        return "User{" +
                "account=" + account +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + firstName + '\'' +
                ", surname='" + lastName + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                ", notifications='" + notifications + '\'' +
                '}';
    }


//    public static void main(String[] args) {
//        User user = User.builder().withLogin("asdfas").withEmail("asfda@asdf.com").build();
//        System.out.println(user);
//    }
}
