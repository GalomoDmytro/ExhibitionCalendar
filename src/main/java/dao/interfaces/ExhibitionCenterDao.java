package dao.interfaces;

import entities.ExhibitionCenter;

import java.util.List;

public interface ExhibitionCenterDao extends DBHelperDao {

    ExhibitionCenter getExhibitionCenterById(Integer id);

    ExhibitionCenter getExhibitionCenterByTitle(String title);

    ExhibitionCenter getExhibitionCenterByMail(String eMail);

    ExhibitionCenter createExhibitionCenter(ExhibitionCenter exhibitionCenter);

    ExhibitionCenter updateExhibitionCenter(ExhibitionCenter exhibitionCenter);

    List<ExhibitionCenter> getAllExhibitionCenter();

    boolean deleteExhibitionCenter(ExhibitionCenter exhibitionCenter);
}
