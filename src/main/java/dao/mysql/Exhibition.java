package dao.mysql;

import dao.interfaces.ExhibitionDao;
import exceptions.DBException;

import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

public class Exhibition implements ExhibitionDao {

    private final String FIELD_ID = "id";
    private final String FIELD_TITLE = "title";
    private final String FIELD_IMAGE_SRC = "image_src";

    private Connection connection;
    private static final ResourceBundle QUERIES = ResourceBundle.getBundle("QueriesMySql");

    Exhibition(Connection connection) {
        this.connection = connection;
    }

    @Override
    public entities.Exhibition getExhibitionById(Integer id) throws DBException {
        return null;
    }

    @Override
    public entities.Exhibition getExhibitionByTitle(String title) throws DBException {
        return null;
    }

    @Override
    public void insertExhibition(entities.Exhibition exhibitionCenter) throws DBException {

    }

    @Override
    public void updateExhibition(entities.Exhibition exhibitionCenter) throws DBException {

    }

    @Override
    public List<entities.Exhibition> getAllExhibition() throws DBException {
        return null;
    }

    @Override
    public void deleteExhibition(entities.Exhibition exhibitionCenter) throws DBException {

    }


}
