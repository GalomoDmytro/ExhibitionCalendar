package dao.interfaces;

import entities.Exhibition;
import exceptions.DBException;

import java.util.Map;

public interface DescriptionTableDao {

    Map<String, String> getAllDescription(Exhibition exhibition) throws DBException;

    String getDescription(Exhibition exhibition, String keyLanguage) throws DBException;

    void insertDescription(String keyLanguage, String description, Exhibition exhibition) throws DBException;

    void deleteAllDescriptionForExposition(Exhibition exhibition) throws DBException;

    void deleteDescriptionForLang(Exhibition exhibition, String keyLanguage) throws DBException;

}


