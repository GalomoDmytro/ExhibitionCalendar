package dao.interfaces;

import entities.Exhibition;
import exceptions.DBException;

import java.util.Map;

/**
 * Defines methods for work with Description table
 */
public interface DescriptionTableDao {

    /**
     * Get all descriptions from Description table
     *
     * @param exhibition will be used to find the description
     * @return if there is no description return Collections.emptyMap();
     * else key="Language" value="Description"
     */
    Map<String, String> getAllDescription(Exhibition exhibition) throws DBException;

    /**
     * Get all descriptions from Description table
     *
     * @param id exhibition
     * @return if there is no description return Collections.emptyMap();
     * else key="Language" value="Description"
     */
    Map<String, String> getAllDescriptionById(Integer id) throws DBException;

    /**
     * Get specific descriptions from Description table
     *
     * @param exhibition  entity
     * @param keyLanguage key abbreviation for the language on which the description
     * @return "" String or description for the key language
     */
    String getDescription(Exhibition exhibition, String keyLanguage) throws DBException;

    /**
     * Insert in to Description table new Description for specific Language
     *
     * @param exhibition  entity
     * @param keyLanguage key abbreviation for the language on which the description
     * @param description of the exhibition
     * @param keyLanguage key abbreviation for the language on which the description
     */
    void insertDescription(String keyLanguage, String description, Exhibition exhibition) throws DBException;

    /**
     * Insert in to Description table new Description for specific Language
     *
     * @param id          exhibition
     * @param keyLanguage key abbreviation for the language on which the description
     * @param description of the exhibition
     * @param keyLanguage key abbreviation for the language on which the description
     */
    void insertDescriptionById(String keyLanguage, String description, Integer id) throws DBException;

    /**
     * Delete all descriptions for specific Exhibition from Description table
     *
     * @param exhibition entity
     */
    void deleteAllDescriptionForExposition(Exhibition exhibition) throws DBException;

    /**
     * Delete specific descriptions for specific Exhibition from Description table
     *
     * @param exhibition  entity
     * @param keyLanguage key abbreviation for the language on which the description
     *                    will be deleted
     */
    void deleteDescriptionForLang(Exhibition exhibition, String keyLanguage) throws DBException;

}


