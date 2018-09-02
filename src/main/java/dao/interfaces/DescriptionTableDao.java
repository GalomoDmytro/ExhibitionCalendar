package dao.interfaces;

import entities.Exhibition;
import exceptions.DBException;

import java.util.Map;

public interface DescriptionTableDao {

    void setLockDescriptionTable() throws DBException;

    void unlockTable() throws DBException;

    Map<String, String> getAllDescription(Exhibition exhibition) throws DBException;

    Map<String, String> getAllDescriptionById(Integer id) throws DBException;

    String getDescription(Exhibition exhibition, String keyLanguage) throws DBException;

    void insertDescription(String keyLanguage, String description, Exhibition exhibition) throws DBException;

    void insertDescriptionById(String keyLanguage, String description, Integer id) throws DBException;

    void deleteAllDescriptionForExposition(Exhibition exhibition) throws DBException;

    void deleteDescriptionForLang(Exhibition exhibition, String keyLanguage) throws DBException;

}


