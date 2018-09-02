package dao.mysql;

import controller.command.moderatorCommand.EditExposition;
import dao.interfaces.DescriptionTableDao;
import entities.Exhibition;
import exceptions.DBException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class DescriptionMySql implements DescriptionTableDao {

    private final ResourceBundle QUERIES;
    private final Connection connection;
    private static final String FIELD_ID = "id";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_EXHIBITION_ID = "exhibition_id";
    private static final String FIELD_LANGUAGE = "language";

    private static final Logger LOGGER = Logger.getLogger(DescriptionMySql.class);

    DescriptionMySql(Connection connection) {
        this.QUERIES = ResourceBundle.getBundle("QueriesMySql");
        this.connection = connection;
    }

    DescriptionMySql(Connection connection, ResourceBundle queries) {
        this.QUERIES = queries;
        this.connection = connection;
    }

    @Override
    public void setLockDescriptionTable() throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement("LOCK TABLE description WRITE")) {
            statement.execute();
        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    @Override
    public void unlockTable() throws DBException {
        try (PreparedStatement statement = connection.prepareStatement("UNLOCK TABLE")) {
            statement.execute();

        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    @Override
    public Map<String, String> getAllDescription(Exhibition exhibition) throws DBException {
        Map<String, String> languageDescription;
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("description.getAll"))) {
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
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("description.getAll"))) {
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
        String description = null;
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("description.getDescription"))) {
            statement.setInt(1, exhibition.getId());
            statement.setString(2, keyLanguage);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                description = resultSet.getString(FIELD_DESCRIPTION);
            }
        } catch (SQLException exception) {
            System.out.println(exception);
            throw new DBException(exception);
        }

        if (description == null) {
            return "oops, nothing to show";
        }

        return description;
    }

    @Override
    public void insertDescription(String keyLanguage, String description, Exhibition exhibition)
            throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("description.insert"),
                        Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, keyLanguage);
            statement.setString(2, description);
            statement.setInt(3, exhibition.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    @Override
    public void insertDescriptionById(String keyLanguage, String description, Integer exhibitionId)
            throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("description.insert"),
                        Statement.RETURN_GENERATED_KEYS)) {
            LOGGER.info("lang " + keyLanguage);
            LOGGER.info("description " + description);
            statement.setString(2, keyLanguage);
            statement.setString(1, description);
            statement.setInt(3, exhibitionId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    @Override
    public void deleteAllDescriptionForExposition(Exhibition exhibition)
            throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("description.delete"))) {
            statement.setInt(1, exhibition.getId());
            statement.execute();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    @Override
    public void deleteDescriptionForLang(Exhibition exhibition, String keyLanguage)
            throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("description.deleteLang"))) {
            statement.setInt(1, exhibition.getId());
            statement.setString(2, keyLanguage);
            statement.executeUpdate();
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
