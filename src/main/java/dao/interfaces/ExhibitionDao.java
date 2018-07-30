package dao.interfaces;

import entities.Exhibition;

import java.util.List;

public interface ExhibitionDao extends DBHelperDao {

    Exhibition getExhibitionById(Integer id);

    Exhibition getExhibitionByTitle(String title);

    Exhibition getExhibitionByMail(String eMail);

    Exhibition createExhibition(Exhibition exhibitionCenter);

    Exhibition updateExhibition(Exhibition exhibitionCenter);

    List<Exhibition> getAllExhibition();

    boolean deleteExhibition(Exhibition exhibitionCenter);
}
