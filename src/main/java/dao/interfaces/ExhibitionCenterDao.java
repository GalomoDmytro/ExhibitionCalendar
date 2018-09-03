package dao.interfaces;

import entities.ExhibitionCenter;
import exceptions.DBException;

import java.util.List;

public interface ExhibitionCenterDao {

    void setLockExhibitionCenterTable() throws DBException;

    void unlockTable() throws DBException;

    ExhibitionCenter getExhibitionCenterById(Integer id) throws DBException;

    ExhibitionCenter getExhibitionCenterByTitle(String title) throws DBException;

    ExhibitionCenter getExhibitionCenterByMail(String eMail) throws DBException;

    List<ExhibitionCenter> getAllExhibitionCenter() throws DBException;

    List<ExhibitionCenter> getExhibitionCentersBySearch(String search) throws DBException;

    boolean isTitleInTable(String title) throws DBException;

    void deleteExhibitionCenter(ExhibitionCenter exhibitionCenter) throws DBException;

    void deleteExhibitionCenterById(Integer id) throws DBException;

    void insertExhibitionCenter(ExhibitionCenter exhibitionCenter) throws DBException;

    void updateExhibitionCenter(ExhibitionCenter exhibitionCenter) throws DBException;

}
