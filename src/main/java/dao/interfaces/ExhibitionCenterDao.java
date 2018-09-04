package dao.interfaces;

import entities.ExhibitionCenter;
import exceptions.DBException;

import java.util.List;

/**
 * Defines methods for work with exhibition_center table
 */
public interface ExhibitionCenterDao {

    /**
     * Enables client sessions to acquire exhibition_center table locks
     * explicitly for the purpose of cooperating with other sessions
     * for access to tables, or to prevent other
     * sessions from modifying exhibition_center tables during periods when
     * a session requires exclusive access to them
     *
     * @throws DBException
     */
    void setLockExhibitionCenterTable() throws DBException;

    /**
     * Releases any table locks held by the current session
     *
     * @throws DBException
     */
    void unlockTable() throws DBException;

    /**
     * Get ExhibitionCenter entity from exhibition_center table
     *
     * @param id of ExhibitionCenter
     * @return ExhibitionCenter entity
     * will return ExhibitionCenter().emptyCenter() if have not matches
     * with looking exhibition_center id
     * @throws DBException
     */
    ExhibitionCenter getExhibitionCenterById(Integer id) throws DBException;

    /**
     * Get ExhibitionCenter entity from exhibition_center table
     *
     * @param title of ExhibitionCenter
     * @return ExhibitionCenter entity
     * will return ExhibitionCenter().emptyCenter() if have not matches
     * with looking exhibition_center title
     * @throws DBException
     */
    ExhibitionCenter getExhibitionCenterByTitle(String title) throws DBException;

    /**
     * Get ExhibitionCenter entity from exhibition_center table
     *
     * @param eMail of ExhibitionCenter
     * @return ExhibitionCenter entity
     * will return ExhibitionCenter().emptyCenter() if have not matches
     * with looking exhibition_center eMail
     * @throws DBException
     */
    ExhibitionCenter getExhibitionCenterByMail(String eMail) throws DBException;

    /**
     * Get List of all ExhibitionCenter entities from exhibition_center
     *
     * @return List of ExhibitionCenter entities or Collections.emptyList()
     * @throws DBException
     */
    List<ExhibitionCenter> getAllExhibitionCenter() throws DBException;

    /**
     * Get List of all ExhibitionCenter entities from exhibition_center
     * with using 'select from table
     *
     * @param search line will be looking matches with title, address, email, web page or id
     * @return List of ExhibitionCenter entities or Collections.emptyList()
     * @throws DBException
     */
    List<ExhibitionCenter> getExhibitionCentersBySearch(String search) throws DBException;

    /**
     * Check if title in exhibition_center table
     *
     * @param title line will comparing for matches with title from exhibition_center table
     * @return true if title already exist in table
     * @throws DBException
     */
    boolean isTitleInTable(String title) throws DBException;

    /**
     * Delete chosen exhibition center from exhibition_center table
     * with dependent phone from exhibition_center_phone
     *
     * @param exhibitionCenter entity
     * @throws DBException
     */
    void deleteExhibitionCenter(ExhibitionCenter exhibitionCenter) throws DBException;

    /**
     * Delete chosen exhibition center from exhibition_center table
     * with dependent phone from exhibition_center_phone
     *
     * @param id exhibitionCenter
     * @throws DBException
     */
    void deleteExhibitionCenterById(Integer id) throws DBException;

    /**
     * Insert in to exhibition_center table new exhibitionCenter entity
     *
     * @param exhibitionCenter entity
     * @throws DBException
     */
    void insertExhibitionCenter(ExhibitionCenter exhibitionCenter) throws DBException;

    /**
     * Update data in exhibition_center table
     *
     * @param exhibitionCenter entity
     * @throws DBException
     */
    void updateExhibitionCenter(ExhibitionCenter exhibitionCenter) throws DBException;

}
