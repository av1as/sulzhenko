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
    private String notification;

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
        this.notification = notifications;
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

    public String getNotification() {
        return notification;
    }
    public String getFullName(){
        if (firstName != null && lastName != null) return firstName + " " + lastName;
        else if (firstName != null) return firstName;
        else if (lastName != null) return lastName;
        else return login;
    }

//    public static Builder builder() {
//        return new Builder();
//    }
    /**
     * This inner class uses Builder pattern instead of setters
     * with methods like "withXXX" (where XXX - some field of User class)
     */
    public static class Builder {
//        private Integer account;
//        private String login;
//        private String password;
//        private String email;
//        private String firstName;
//        private String lastName;
//        private String role;
//        private String status;
//        private String notifications;
        private final User user;

        public Builder() {
            user = new User();
        }

        public Builder withAccount(Integer account) {
            user.account = account;
            return this;
        }
        public Builder withLogin(String login) {
            user.login = login;
            return this;
        }
        public Builder withEmail(String email) {
            user.email = email;
            return this;
        }
        public Builder withPassword(String password) {
            user.password = password;
            return this;
        }
        public Builder withFirstName(String firstName) {
            user.firstName = firstName;
            return this;
        }
        public Builder withLastName(String lastName) {
            user.lastName = lastName;
            return this;
        }
        public Builder withRole(String role) {
            user.role = role;
            return this;
        }
        public Builder withStatus(String status) {
            user.status = status;
            return this;
        }
        public Builder withNotification(String notification) {
            user.notification = notification;
            return this;
        }

        public User build() {
            if(user.account == null) {
                user.account = 0;
            }
            if (user.login == null) {
                throw new IllegalArgumentException();
            }
            return user;
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
                ", notifications='" + notification + '\'' +
                '}';
    }


//    public static void main(String[] args) {
//        User user = User.builder().withLogin("asdfas").withEmail("asfda@asdf.com").build();
//        System.out.println(user);
//    }
}
