package com.sulzhenko.DTO;

import java.io.Serializable;
import java.util.Objects;

import static com.sulzhenko.DTO.UserDTO.Role.extractRole;

/**
 * UserDTO class. Fields are similar to User entity
 * Use UserDTO.builder().withFieldName(fieldValue).build() to create an instance
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */

public class UserDTO implements Serializable {
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
    private UserDTO(){}

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

    public UserDTO.Role getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public String getNotification() {
        return notification;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return login.equals(userDTO.login);
    }
    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
    public static class Builder {
        private final UserDTO userDTO;

        public Builder() {
            userDTO = new UserDTO();
        }

        public UserDTO.Builder withAccount(Long account) {
            userDTO.account = account;
            return this;
        }

        public UserDTO.Builder withLogin(String login) {
            userDTO.login = login;
            return this;
        }

        public UserDTO.Builder withEmail(String email) {
            userDTO.email = email;
            return this;
        }

        public UserDTO.Builder withPassword(String password) {
            userDTO.password = password;
            return this;
        }

        public UserDTO.Builder withFirstName(String firstName) {
            userDTO.firstName = firstName;
            return this;
        }

        public UserDTO.Builder withLastName(String lastName) {
            userDTO.lastName = lastName;
            return this;
        }

        public UserDTO.Builder withRole(String role) {
            userDTO.role = extractRole(role);
            return this;
        }

        public UserDTO.Builder withStatus(String status) {
            userDTO.status = status;
            return this;
        }

        public UserDTO.Builder withNotification(String notification) {
            userDTO.notification = notification;
            return this;
        }

        public UserDTO build() {
            if (userDTO.account == null) {
                userDTO.account = 0L;
            }
            if (userDTO.login == null) {
                throw new IllegalArgumentException();
            }
            return userDTO;
        }
    }
    public String isNotificationChecked(){
        return (Objects.equals(this.getNotification(), "on") ? "checked": "unchecked");
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

        static UserDTO.Role extractRole(String value) {
            if (Objects.equals(value, "administrator")) return ADMIN;
            else if (Objects.equals(value, "system user")) return SYSTEM_USER;
            else return UNKNOWN;
        }
    }
}
