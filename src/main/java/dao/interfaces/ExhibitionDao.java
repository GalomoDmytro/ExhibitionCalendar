package dao.interfaces;

import entities.Exhibition;
import exceptions.DBException;

import java.util.List;

public interface ExhibitionDao {

    Exhibition getExhibitionById(Integer id) throws DBException;

    Exhibition getExhibitionByTitle(String title) throws DBException;

    void insertExhibition(Exhibition exhibitionCenter) throws DBException;

    void updateExhibition(Exhibition exhibitionCenter) throws DBException;

    List<Exhibition> getAllExhibition() throws DBException;

    void deleteExhibition(Exhibition exhibitionCenter) throws DBException;
}
