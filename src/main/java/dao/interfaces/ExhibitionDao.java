package dao.interfaces;

import entities.Exhibition;
import exceptions.DBException;

import java.util.List;

public interface ExhibitionDao {

    void setLockExhibitionTable() throws DBException;

    void unlockTable() throws DBException;

    Exhibition getExhibitionById(Integer id) throws DBException;

    Exhibition getExhibitionByTitle(String title) throws DBException;

    List<Exhibition> getExhibitionBySearch(String line) throws  DBException;

    void insertExhibition(Exhibition exhibition) throws DBException;

    void updateExhibition(Exhibition exhibition) throws DBException;

    List<Exhibition> getAllExhibition() throws DBException;

    void deleteExhibition(Exhibition exhibition) throws DBException;

    void deleteById(Integer id) throws DBException;
}
