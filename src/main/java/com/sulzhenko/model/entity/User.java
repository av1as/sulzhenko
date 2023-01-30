package com.sulzhenko.model.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class describes user entity
 */

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long account;
    private String login;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;
    private String status;
    private String notification;

    private User() {
    }
    public Long getAccount() {
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

    public Role getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public String getNotification() {
        return notification;
    }

    public String getFullName() {
        if (!firstName.isEmpty() && !lastName.isEmpty()) return firstName + " " + lastName;
        else if (!firstName.isEmpty()) return firstName;
        else if (!lastName.isEmpty()) return lastName;
        else return login;
    }

    /**
     * This inner class uses Builder pattern instead of setters
     * with methods like "withXXX" (where XXX - some field of User class)
     */
    public static class Builder {
        private final User user;

        public Builder() {
            user = new User();
        }

        public Builder withAccount(Long account) {
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
            user.role = Role.extractRole(role);
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
            if (user.account == null) {
                user.account = 0L;
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
    public enum Role {
        ADMIN("administrator"), SYSTEM_USER("system user"), UNKNOWN("unknown role");
        public final String value;

        Role(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Role extractRole(String value) {
            if (Objects.equals(value, "administrator")) return ADMIN;
            else if (Objects.equals(value, "system user")) return SYSTEM_USER;
            else return UNKNOWN;
        }
    }
}
