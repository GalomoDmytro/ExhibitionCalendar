package dao.mysql;

import dao.interfaces.ExhibitionDao;
import entities.Exhibition;
import exceptions.DBException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Dmytro Galomko
 */
public class ExhibitionMySql implements ExhibitionDao {

    private final Connection connection;
    private final ResourceBundle QUERIES;
    private static final String FIELD_ID = "id";
    private static final String FIELD_TITLE = "title";
    private static final String FIELD_IMAGE_SRC = "image_src";
    private static final Logger LOGGER = Logger.getLogger(ExhibitionMySql.class);

    ExhibitionMySql(Connection connection, ResourceBundle resourceBundle) {
        this.QUERIES = resourceBundle;
        this.connection = connection;
    }

    ExhibitionMySql(Connection connection) {
        this.QUERIES = ResourceBundle.getBundle("QueriesMySql");
        this.connection = connection;
    }

    /**
     * Enables client sessions to acquire exhibition_center table locks
     * explicitly for the purpose of cooperating with other sessions
     * for access to tables, or to prevent other
     * sessions from modifying exhibition_center tables during periods when
     * a session requires exclusive access to them
     *
     * @throws DBException
     */
    @Override
    public void setLockExhibitionTable() throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement("LOCK TABLES exhibition WRITE, description WRITE ")) {
            statement.execute();
        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Releases any table locks held by the current session
     *
     * @throws DBException
     */
    @Override
    public void unlockTable() throws DBException {
        try (PreparedStatement statement = connection.prepareStatement("UNLOCK TABLES")) {
            statement.execute();

        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Get Exhibition entity from exhibition table
     *
     * @param id of Exhibition
     * @return Exhibition entity or
     * will return Exhibition().emptyExhibition() if have not matches
     * @throws DBException
     */
    @Override
    public Exhibition getExhibitionById(Integer id) throws DBException {
        List<Exhibition> exhibitions;
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("exhibition.getById"))) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            exhibitions = parseExhibitionSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getExhibitionById(" + id + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (exhibitions != null && !exhibitions.isEmpty()) {
            return exhibitions.get(0);
        }

        return new Exhibition().emptyExhibition();
    }

    /**
     * Get List of Exhibition entities from exhibition table after search
     * for matches title or id
     *
     * @param line will look for matches title or id column from exhibition table
     * @return List of Exhibition entities or
     * empty List
     * @throws DBException
     */
    @Override
    public List<Exhibition> getExhibitionBySearch(String line) throws DBException {
        List<Exhibition> exhibitions;
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("exhibition.search"))) {
            line = "%" + line + "%";
            statement.setString(1, line);
            statement.setString(2, line);
            ResultSet resultSet = statement.executeQuery();
            exhibitions = parseExhibitionSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getExhibitionBySearch(" + line + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (exhibitions != null && !exhibitions.isEmpty()) {
            return exhibitions;
        }
        return Collections.emptyList();
    }

    /**
     * Get Exhibition entity from exhibition table
     *
     * @param title of Exhibition
     * @return Exhibition entity or
     * will return Exhibition().emptyExhibition() if have not matches
     * @throws DBException
     */
    @Override
    public Exhibition getExhibitionByTitle(String title) throws DBException {
        List<Exhibition> exhibitions;
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("exhibition.getByTitle"))) {
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            exhibitions = parseExhibitionSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getExhibitionByTitle(" + title + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (exhibitions != null && !exhibitions.isEmpty()) {
            return exhibitions.get(0);
        }
        return new Exhibition().emptyExhibition();
    }

    /**
     * Insert Exhibition entity to exhibition table
     *
     * @param exhibition entity
     * @throws DBException
     */
    @Override
    public void insertExhibition(Exhibition exhibition) throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("exhibition.insert"),
                        Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, exhibition.getTitle());
            statement.setString(2, exhibition.getImgSrc());
            int affectedRows = statement.executeUpdate();

            // set ID to exhibition
            if (affectedRows == 0) {
                // Creating user failed, no rows affected.
                throw new SQLException();
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    exhibition.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When insertExhibition(" + exhibition + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Update exhibition table with new data
     *
     * @param exhibition entity
     * @throws DBException
     */
    @Override
    public void updateExhibition(Exhibition exhibition) throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("exhibition.update"))) {
            statement.setString(1, exhibition.getTitle());
            statement.setString(2, exhibition.getImgSrc());
            statement.setInt(3, exhibition.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When updateExhibition(" + exhibition + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Get List of all Exhibitions entities from exhibition table
     *
     * @return List of Exhibition or empty List
     * @throws DBException
     */
    @Override
    public List<Exhibition> getAllExhibition() throws DBException {
        List<Exhibition> exhibitions;
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("exhibition.getAll"))) {
            ResultSet resultSet = statement.executeQuery();
            exhibitions = parseExhibitionSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (exhibitions != null && !exhibitions.isEmpty()) {
            return exhibitions;
        }
        return Collections.emptyList();
    }

    /**
     * Delete exhibition from exhibition table
     *
     * @param exhibition entity
     * @throws DBException
     */
    @Override
    public void deleteExhibition(Exhibition exhibition) throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("exhibition.delete"))) {
            statement.setInt(1, exhibition.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When deleteExhibition(" + exhibition + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Delete exhibition from exhibition table
     *
     * @param id of exhibition
     * @throws DBException
     */
    @Override
    public void deleteById(Integer id) throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("exhibition.delete"))) {
            statement.setInt(1, id);
            statement.executeUpdate();
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
                        .setImgSrc(resultSet.getString(FIELD_IMAGE_SRC))
                        .build();
                exhibitions.add(exhibition);
            }
        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        return exhibitions;
    }



}
