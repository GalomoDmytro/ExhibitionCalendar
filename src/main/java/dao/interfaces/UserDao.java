package dao.interfaces;

import entities.User;
import exceptions.DBException;

import java.util.List;

/**
 * Defines methods for work with user table
 */
public interface UserDao {

    /**
     * Get User from user table
     *
     * @param id of looking User, search parameter
     * @return User or User().empty();
     * @throws DBException
     */
    User getById(Integer id) throws DBException;

    /**
     * Get User from user table
     *
     * @param name of looking User, search parameter
     * @return User or User().empty()
     * @throws DBException
     */
    User getByName(String name) throws DBException;

    /**
     * Get User from user table
     *
     * @param eMail of looking User, search parameter
     * @return User or User().empty()
     * @throws DBException
     */
    User getByMail(String eMail) throws DBException;

    /**
     * Get all Users from user table
     *
     * @return List of Usert or empty List
     * @throws DBException
     */
    List<User> getAllUsers() throws DBException;

    /**
     * Check if chosen name already in user table
     *
     * @param name to search in table
     * @return true if name exist in table, else return false
     * @throws DBException
     */
    boolean isNameInTable(String name) throws DBException;

    /**
     * Check if chosen mail already exist in user table
     *
     * @param eMail to search in user table
     * @return true if mail exist in table, else return false
     * @throws DBException
     */
    boolean isMailInTable(String eMail) throws DBException;

    /**
     * Check if name or mail already exist in user table
     *
     * @param nameOrMail contain string with mail or name for looking
     *                   matches on user table
     * @return true if name or mail already in user table
     * @throws DBException
     */
    boolean isNameOrMailInTable(String nameOrMail) throws DBException;

    /**
     * Update user table
     *
     * @param user
     * @throws DBException
     */
    void updateUser(User user) throws DBException;

    /**
     * Insert User in user table
     *
     * @param user
     * @throws DBException
     */
    void insertUser(User user) throws DBException;

    /**
     * Delete User from user table
     *
     * @param mail of User
     * @throws DBException
     */
    void deleteUser(String mail) throws DBException;
}
