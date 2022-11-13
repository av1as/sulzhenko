//package com.sulzhenko.DAO;
//
//import com.sulzhenko.DAO.entity.User;
//
//import java.util.Optional;
//
//public class UserApplication {
//
//    private static DAO<User> userDAO;
//
//    public static void main(String[] args) {
//        userDAO = new UserDAO();
//
//        User user1 = getUser(2);
//        System.out.println(user1);
//        //userDAO.update(user1, new String[]{"Jake", "jake@domain.com"});
//
////        User user2 = getUser(1);
////        userDAO.delete(user2);
////        userDAO.save(new User("Julie", "julie@domain.com"));
////
////        userDAO.getAll().forEach(user -> System.out.println(user.getName()));
//    }
//
//    private static User getUser(int id) {
//        Optional<User> user = userDAO.getById(id);
//
//        return user.orElseGet(
//          () -> new User());
//    }
//}
//https://www.baeldung.com/java-dao-pattern