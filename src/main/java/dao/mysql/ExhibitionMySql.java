package dao.mysql;

import dao.interfaces.ExhibitionDao;
import entities.Exhibition;
import exceptions.DBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ExhibitionMySql implements ExhibitionDao {

    private final String FIELD_ID = "id";
    private final String FIELD_TITLE = "title";
    private final String FIELD_IMAGE_SRC = "image_src";

    private Connection connection;
    private static final ResourceBundle QUERIES = ResourceBundle.getBundle("QueriesMySql");

    ExhibitionMySql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Exhibition getExhibitionById(Integer id) throws DBException {
        List<Exhibition> exhibitions;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("exhibition.getById"))) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            exhibitions = parseExhibitionSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        if (exhibitions != null) {
            return exhibitions.get(0);
        }
        return null;
    }

    @Override
    public Exhibition getExhibitionByTitle(String title) throws DBException {
        List<Exhibition> exhibitions;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("exhibition.getByTitle"))) {
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            exhibitions = parseExhibitionSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        if (exhibitions != null) {
            return exhibitions.get(0);
        }
        return null;
    }

    @Override
    public void insertExhibition(Exhibition exhibition) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("exhibition.insert"))) {
            statement.setString(1, exhibition.getTitle());
            statement.setString(2, exhibition.getImgSrc());
            statement.executeQuery();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    @Override
    public void updateExhibition(Exhibition exhibition) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("exhibition.update"))) {
            statement.setString(1, exhibition.getTitle());
            statement.setString(2, exhibition.getImgSrc());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    @Override
    public List<Exhibition> getAllExhibition() throws DBException {
        List<Exhibition> exhibitions;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("exhibition.getAll"))) {
            ResultSet resultSet = statement.executeQuery();
            exhibitions = parseExhibitionSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        if (exhibitions != null) {
            return exhibitions;
        }
        return null;
    }

    @Override
    public void deleteExhibition(Exhibition exhibition) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("exhibition.delete"))) {
            statement.setInt(1, exhibition.getId());
            statement.executeQuery();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    private List<Exhibition> parseExhibitionSet(ResultSet resultSet) throws DBException {
        List<Exhibition> exhibitions = new ArrayList<>();

        try {
            while (resultSet.next()) {
                Exhibition exhibition = new Exhibition.Builder()
                        .setId(resultSet.getInt(FIELD_ID))
                        .setTitle(resultSet.getString(FIELD_TITLE))
                        .setImgSrc(FIELD_IMAGE_SRC)
                        .build();
                exhibitions.add(exhibition);
            }
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        return exhibitions;
    }

}
