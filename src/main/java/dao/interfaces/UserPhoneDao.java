package dao.interfaces;

import exceptions.DBException;

import java.util.List;

/**
 * Defines methods for work with user_phone table
 *
 * @author Dmytro Galomko
 */
public interface UserPhoneDao {

    /**
     * Get List of all Phones for chosen user from user_phone table
     *
     * @param eMail of User
     * @return List of Strings phones or empty List
     * @throws DBException
     */
    List<String> getPhones(String eMail) throws DBException;

    /**
     * Insert new phone to User in user_phone table
     *
     * @param mail  of User for identification User
     * @param phone to insert
     * @throws DBException
     */
    void insertPhone(String mail, String phone) throws DBException;

    /**
     * Delete User phone from user_phone table
     *
     * @param mail  of User
     * @param phone to delete
     * @throws DBException
     */
    void deletePhone(String mail, String phone) throws DBException;
}
