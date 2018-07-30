package dao.interfaces;

import entities.User;

import java.util.List;

public interface UserDao extends DBHelperDao {

    User getById(Integer id);

    User getByName(String name);

    User getByMail(String eMail);

    List<User> getAllUsers();

    User updateUser(User user);

    User createUser(User user);

    boolean deleteUser(User user);
}
