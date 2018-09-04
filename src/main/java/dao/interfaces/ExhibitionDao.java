package dao.interfaces;

import entities.Exhibition;
import exceptions.DBException;

import java.util.List;

/**
 * Defines methods for work with exhibition table
 */
public interface ExhibitionDao {

    /**
     * Enables client sessions to acquire exhibition_center table locks
     * explicitly for the purpose of cooperating with other sessions
     * for access to tables, or to prevent other
     * sessions from modifying exhibition_center tables during periods when
     * a session requires exclusive access to them
     *
     * @throws DBException
     */
    void setLockExhibitionTable() throws DBException;

    /**
     * Releases any table locks held by the current session
     *
     * @throws DBException
     */
    void unlockTable() throws DBException;

    /**
     * Get Exhibition entity from exhibition table
     *
     * @param id of Exhibition
     * @return Exhibition entity or
     * will return Exhibition().emptyExhibition() if have not matches
     * @throws DBException
     */
    Exhibition getExhibitionById(Integer id) throws DBException;

    /**
     * Get Exhibition entity from exhibition table
     *
     * @param title of Exhibition
     * @return Exhibition entity or
     * will return Exhibition().emptyExhibition() if have not matches
     * @throws DBException
     */
    Exhibition getExhibitionByTitle(String title) throws DBException;

    /**
     * Get List of Exhibition entities from exhibition table after search
     * for matches title or id
     *
     * @param line will look for matches title or id column from exhibition table
     * @return List of Exhibition entities or
     * empty List
     * @throws DBException
     */
    List<Exhibition> getExhibitionBySearch(String line) throws DBException;

    /**
     * Insert Exhibition entity to exhibition table
     *
     * @param exhibition entity
     * @throws DBException
     */
    void insertExhibition(Exhibition exhibition) throws DBException;

    /**
     * Update exhibition table with new data
     *
     * @param exhibition entity
     * @throws DBException
     */
    void updateExhibition(Exhibition exhibition) throws DBException;

    /**
     * Get List of all Exhibitions entities from exhibition table
     *
     * @return List of Exhibition or empty List
     * @throws DBException
     */
    List<Exhibition> getAllExhibition() throws DBException;

    /**
     * Delete exhibition from exhibition table
     *
     * @param exhibition entity
     * @throws DBException
     */
    void deleteExhibition(Exhibition exhibition) throws DBException;

    /**
     * Delete exhibition from exhibition table
     *
     * @param id of exhibition
     * @throws DBException
     */
    void deleteById(Integer id) throws DBException;
}
