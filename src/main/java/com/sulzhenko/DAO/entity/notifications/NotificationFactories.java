package com.sulzhenko.DAO.entity.notifications;

import com.sulzhenko.DAO.DAOException;
import com.sulzhenko.DAO.DataSource;
import com.sulzhenko.DAO.entity.Request;
import com.sulzhenko.DAO.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationFactories {
    public NotificationFactory accountUpdateFactory (User user, String updateDescription){

        return new NotificationFactory() {
            @Override
            public String createTheme() {
                return new AccountUpdateTheme().asTheme();
            }

            @Override
            public String createBody() {
                return new AccountUpdateBody(user, updateDescription).asText();
            }
        };
    }
    public NotificationFactory requestUpdateFactory (User user, Request request, String updateDescription){

        return new NotificationFactory() {
            @Override
            public String createTheme() {
                return new RequestUpdateTheme().asTheme();
            }

            @Override
            public String createBody() {
                return new RequestUpdateBody(user, request, updateDescription).asText();
            }
        };
    }
    public NotificationFactory systemUpdateFactory (User user, String updateDescription){

        return new NotificationFactory() {
            @Override
            public String createTheme() {
                return new SystemUpdateTheme().asTheme();
            }

            @Override
            public String createBody() {
                return new SystemUpdateBody(user, updateDescription).asText();
            }
        };
    }

    public static void main(String[] args) {
        List<User> list = findAllUsers();
        for (User user: list) {
            NotificationFactory factory = new NotificationFactories().accountUpdateFactory(user, "activated");
//            System.out.println(new NotificationFactories().accountUpdateFactory(user, "activated").createTheme());
//            System.out.println(new NotificationFactories().accountUpdateFactory(user, "activated").createBody());
            System.out.println(factory.createTheme());
            System.out.println(factory.createBody());

        }

    }
    public static List<User> findAllUsers() throws DAOException {
        List<User> allUsersList = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM users");
             ResultSet rs = stmt.executeQuery()) {
            while(rs.next()){
                //User t = User.builder().withLogin(rs.getString(2)).build();


                //User t = new User();
                User.Builder b = User.builder();
                b.withLogin(rs.getString(2))
                        .withEmail(rs.getString(3))
                        .withFirstName(rs.getString(5))
                        .withLastName(rs.getString(6));

                User t = b.build();



                //System.out.println(t);
                allUsersList.add(t);
            }
        } catch (SQLException e){
            e.printStackTrace();
            //throw new DAOException();
        }
        return allUsersList;
    }

}
