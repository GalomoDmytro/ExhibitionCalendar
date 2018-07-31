package dao.interfaces;

import entities.ExhibitionCenter;
import exceptions.DBException;

import java.util.List;

public interface ExhibitionCenterDao {

    ExhibitionCenter getExhibitionCenterById(Integer id) throws DBException;

    ExhibitionCenter getExhibitionCenterByTitle(String title) throws DBException;

    ExhibitionCenter getExhibitionCenterByMail(String eMail) throws DBException;

    ExhibitionCenter createExhibitionCenter(ExhibitionCenter exhibitionCenter) throws DBException;

    ExhibitionCenter updateExhibitionCenter(ExhibitionCenter exhibitionCenter) throws DBException;

    List<ExhibitionCenter> getAllExhibitionCenter() throws DBException;

    boolean deleteExhibitionCenter(ExhibitionCenter exhibitionCenter) throws DBException;
}
