package dao.interfaces;

import entities.User;
import exceptions.DBException;

import java.util.List;

public interface UserDao {

    User getById(Integer id) throws DBException;

    User getByName(String name) throws DBException;

    User getByMail(String eMail) throws DBException;

    List<User> getAllUsers() throws DBException;

    boolean isNameInTable(String name) throws DBException;

    boolean isMailInTable(String eMail) throws DBException;

    void updateUser(User user) throws DBException;

    void insertUser(User user) throws DBException;

    void deleteUser(String mail) throws DBException;
}
