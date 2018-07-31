package dao.interfaces;

import entities.Exhibition;
import exceptions.DBException;

import java.util.List;

public interface ExhibitionDao {

    Exhibition getExhibitionById(Integer id) throws DBException;

    Exhibition getExhibitionByTitle(String title) throws DBException;

    Exhibition getExhibitionByMail(String eMail) throws DBException;

    Exhibition createExhibition(Exhibition exhibitionCenter) throws DBException;

    Exhibition updateExhibition(Exhibition exhibitionCenter) throws DBException;

    List<Exhibition> getAllExhibition() throws DBException;

    boolean deleteExhibition(Exhibition exhibitionCenter) throws DBException;
}
