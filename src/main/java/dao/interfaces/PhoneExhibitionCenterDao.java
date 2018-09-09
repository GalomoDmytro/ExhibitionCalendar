package dao.interfaces;

import exceptions.DBException;

import java.util.List;

/**
 * Defines methods for work with exhibition_center_phone table
 *
 * @author Dmytro Galomko
 */
public interface PhoneExhibitionCenterDao {

    /**
     * Get List of phones from exhibition_center_phone table
     *
     * @param id of Exhibition Center
     * @return List of Strings or empty List
     * @throws DBException
     */
    List<String> getPhones(Integer id) throws DBException;

    /**
     * Insert new phone for specific exhibition_center in
     * exhibition_center_phone table
     *
     * @param id    of  Exhibition Center
     * @param phone
     * @throws DBException
     */
    void insertPhone(Integer id, String phone) throws DBException;

    /**
     * Delete all phones for specific Exhibition Center
     * from exhibition_center_phone table
     *
     * @param id of Exhibition Center
     * @throws DBException
     */
    void deletePhone(Integer id) throws DBException;
}
