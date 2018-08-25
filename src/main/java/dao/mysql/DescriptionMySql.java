package dao.mysql;

import dao.interfaces.DescriptionTableDao;
import entities.Exhibition;
import exceptions.DBException;

import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class DescriptionMySql implements DescriptionTableDao {

    private static final String FIELD_ID = "id";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_EXHIBITION_ID = "exhibition_id";
    private static final String FIELD_LANGUAGE = "language";

    private Connection connection;
    private static final ResourceBundle QUERIES = ResourceBundle.getBundle("QueriesMySql");

    DescriptionMySql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Map<String, String> getAllDescription(Exhibition exhibition) throws DBException {
        Map<String, String> languageDescription;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("description.getAll"))) {
            statement.setInt(1, exhibition.getId());
            ResultSet resultSet = statement.executeQuery();
            languageDescription = parseResultSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        if (languageDescription == null) {
            return Collections.<String, String>emptyMap();
        } else {
            return languageDescription;
        }
    }

    @Override
    public Map<String, String> getAllDescriptionById(Integer id) throws DBException {
        Map<String, String> languageDescription;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("description.getAll"))) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            languageDescription = parseResultSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        if (languageDescription == null) {
            return Collections.<String, String>emptyMap();
        } else {
            return languageDescription;
        }
    }

    @Override
    public String getDescription(Exhibition exhibition, String keyLanguage) throws DBException {
        String description;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("description.getDescription"))) {
            statement.setInt(1, exhibition.getId());
            statement.setString(2, keyLanguage);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            description = resultSet.getString(FIELD_DESCRIPTION);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        return description;
    }

    @Override
    public void insertDescription(String keyLanguage, String description, Exhibition exhibition) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("description.insert"),
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, keyLanguage);
            statement.setString(2, description);
            statement.setInt(3, exhibition.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    @Override
    public void insertDescriptionById(String keyLanguage, String description, Integer exhibitionId) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("description.insert"),
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, keyLanguage);
            statement.setString(2, description);
            statement.setInt(3, exhibitionId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    @Override
    public void deleteAllDescriptionForExposition(Exhibition exhibition) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("description.delete"))) {
            statement.setInt(1, exhibition.getId());
            statement.execute();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    @Override
    public void deleteDescriptionForLang(Exhibition exhibition, String keyLanguage) throws DBException {
        String description;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("description.deleteLang"))) {
            statement.setInt(1, exhibition.getId());
            statement.setString(2, keyLanguage);
            ResultSet resultSet = statement.executeQuery();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    private Map<String, String> parseResultSet(ResultSet resultSet) throws DBException {
        Map<String, String> langDescription = new HashMap<>();

        try {
            while (resultSet.next()) {
                String key = resultSet.getString(FIELD_LANGUAGE);
                String description = resultSet.getString(FIELD_DESCRIPTION);
                langDescription.put(key, description);
            }
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        return langDescription;
    }
}
