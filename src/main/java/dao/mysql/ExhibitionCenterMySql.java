package dao.mysql;

import dao.interfaces.ExhibitionCenterDao;
import entities.ExhibitionCenter;
import exceptions.DBException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class ExhibitionCenterMySql implements ExhibitionCenterDao {

    private final Connection connection;
    private final ResourceBundle QUERIES;
    private static final String FIELD_ID = "id";
    private static final String FIELD_TITLE = "title";
    private static final String FIELD_ADDRESS = "address";
    private static final String FIELD_MAIL = "email";
    private static final String FIELD_WEB_PAGE = "web_page";
    private static final Logger LOGGER = Logger.getLogger(ExhibitionCenterMySql.class);

    ExhibitionCenterMySql(Connection connection) {
        this.QUERIES = ResourceBundle.getBundle("QueriesMySql");
        this.connection = connection;
    }

    ExhibitionCenterMySql(Connection connection, ResourceBundle resourceBundle) {
        this.QUERIES = resourceBundle;
        this.connection = connection;
    }

    @Override
    public void setLockExhibitionCenterTable() throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement("LOCK TABLES exhibition_center WRITE, " +
                        "exhibition_center_phone WRITE")) {
            statement.execute();
        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    @Override
    public void unlockTable() throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement("unlock table")) {
            statement.execute();

        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Get ExhibitionCenter entity from exhibition_center table
     *
     * @param id of ExhibitionCenter
     * @return ExhibitionCenter entity
     * will return ExhibitionCenter().emptyCenter() if have not matches
     * with looking exhibition_center id
     * @throws DBException
     */
    @Override
    public ExhibitionCenter getExhibitionCenterById(Integer id) throws DBException {
        List<ExhibitionCenter> exhibitionCenters;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES
                .getString("exhibitionCenter.getById"))) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            exhibitionCenters = parseExhibitionCenterSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getExhibitionCenterById with idExhibitionCenter:" + id);
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (exhibitionCenters == null || exhibitionCenters.isEmpty()) {
            return new ExhibitionCenter().emptyCenter();
        } else {
            return exhibitionCenters.get(0);
        }
    }

    /**
     * Get ExhibitionCenter entity from exhibition_center table
     *
     * @param title of ExhibitionCenter
     * @return ExhibitionCenter entity
     * will return ExhibitionCenter().emptyCenter() if have not matches
     * with looking exhibition_center title
     * @throws DBException
     */
    @Override
    public ExhibitionCenter getExhibitionCenterByTitle(String title) throws DBException {
        List<ExhibitionCenter> exhibitionCenters;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES
                .getString("exhibitionCenter.getByTitle"))) {
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            exhibitionCenters = parseExhibitionCenterSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getExhibitionCenterByTitle with ExCenter title:" + title);
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (exhibitionCenters == null || exhibitionCenters.isEmpty()) {
            return new ExhibitionCenter().emptyCenter();
        } else {
            return exhibitionCenters.get(0);
        }
    }

    /**
     * Get List of all ExhibitionCenter entities from exhibition_center
     * with using 'select from table
     *
     * @param search line will be looking matches with title, address, email, web page or id
     * @return List of ExhibitionCenter entities or Collections.emptyList()
     * @throws DBException
     */
    @Override
    public List<ExhibitionCenter> getExhibitionCentersBySearch(String search) throws DBException {
        List<ExhibitionCenter> exhibitionCenters ;
        search = "%" + search + "%";
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("exhibitionCenter.search"))) {
            prepareStatementToSearch(search, statement);
            ResultSet resultSet = statement.executeQuery();
            exhibitionCenters = parseExhibitionCenterSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getExhibitionCentersBySearch with search:" + search);
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (exhibitionCenters == null) {
            return Collections.emptyList();
        }

        return exhibitionCenters;
    }

    private void prepareStatementToSearch(String search, PreparedStatement statement)
            throws SQLException {
        statement.setString(1, search);
        statement.setString(2, search);
        statement.setString(3, search);
        statement.setString(4, search);
        statement.setString(5, search);
    }

    /**
     * Get ExhibitionCenter entity from exhibition_center table
     *
     * @param eMail of ExhibitionCenter
     * @return ExhibitionCenter entity
     * will return ExhibitionCenter().emptyCenter() if have not matches
     * with looking exhibition_center eMail
     * @throws DBException
     */
    @Override
    public ExhibitionCenter getExhibitionCenterByMail(String eMail) throws DBException {
        List<ExhibitionCenter> exhibitionCenters;
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("exhibitionCenter.getByEMail"))) {
            statement.setString(1, eMail);
            ResultSet resultSet = statement.executeQuery();
            exhibitionCenters = parseExhibitionCenterSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getExhibitionCenterByMail(" + eMail + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (exhibitionCenters == null) {
            return new ExhibitionCenter().emptyCenter();
        } else {
            return exhibitionCenters.get(0);
        }
    }

    /**
     * Get List of all ExhibitionCenter entities from exhibition_center
     *
     * @return List of ExhibitionCenter entities or Collections.emptyList()
     * @throws DBException
     */
    @Override
    public List<ExhibitionCenter> getAllExhibitionCenter() throws DBException {
        List<ExhibitionCenter> exhibitionCenters;
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("exhibitionCenter.getAll"))) {
            ResultSet resultSet = statement.executeQuery();
            exhibitionCenters = parseExhibitionCenterSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getAllExhibitionCenter();");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (exhibitionCenters == null) {
            return Collections.emptyList();
        } else {
            return exhibitionCenters;
        }
    }

    /**
     * Delete chosen exhibition center from exhibition_center table
     * with dependent phone from exhibition_center_phone
     *
     * @param exhibitionCenter entity
     * @throws DBException
     */
    @Override
    public void deleteExhibitionCenter(ExhibitionCenter exhibitionCenter) throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("exhibitionCenter.delete"))) {
            statement.setInt(1, exhibitionCenter.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When deleteExhibitionCenter(" + exhibitionCenter + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Insert in to exhibition_center table new exhibitionCenter entity
     *
     * @param exhibitionCenter entity
     * @throws DBException
     */
    @Override
    public void insertExhibitionCenter(ExhibitionCenter exhibitionCenter) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement
                (QUERIES.getString("exhibitionCenter.insert"),
                        Statement.RETURN_GENERATED_KEYS)) {
            prepareStatementToInsert(statement, exhibitionCenter);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            // get userId after insert
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    exhibitionCenter.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When insertExhibitionCenter(" + exhibitionCenter + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Update data in exhibition_center table
     *
     * @param exhibitionCenter entity
     * @throws DBException
     */
    @Override
    public void updateExhibitionCenter(ExhibitionCenter exhibitionCenter) throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("exhibitionCenter.update"))) {
            prepareStatementToUpdate(statement, exhibitionCenter);
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When updateExhibitionCenter(" + exhibitionCenter + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Check if title in exhibition_center table
     *
     * @param title line will comparing for matches with title from exhibition_center table
     * @return true if title already exist in table
     * @throws DBException
     */
    @Override
    public boolean isTitleInTable(String title) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("exhibitionCenter.getByTitle"))) {
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When isTitleInTable(" + title + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Delete chosen exhibition center from exhibition_center table
     * with dependent phone from exhibition_center_phone
     *
     * @param id exhibitionCenter
     * @throws DBException
     */
    @Override
    public void deleteExhibitionCenterById(Integer id) throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("exhibitionCenter.delete"))) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When deleteExhibitionCenterById(" + id + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    private List<ExhibitionCenter> parseExhibitionCenterSet(ResultSet resultSet)
            throws DBException {
        List<ExhibitionCenter> exhibitionCenters = new ArrayList<>();

        try {
            while (resultSet.next()) {

                ExhibitionCenter exCenter = new ExhibitionCenter.Builder()
                        .setId(resultSet.getInt(FIELD_ID))
                        .setTitle(resultSet.getString(FIELD_TITLE))
                        .setAddress(resultSet.getString(FIELD_ADDRESS))
                        .seteMail(resultSet.getString(FIELD_MAIL))
                        .setWebPage(resultSet.getString(FIELD_WEB_PAGE))
                        .build();
                exhibitionCenters.add(exCenter);
            }
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When parseExhibitionCenterSet(" + resultSet + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        return exhibitionCenters;
    }

    private void prepareStatementToInsert(PreparedStatement statement,
                                          ExhibitionCenter exhibitionCenter) throws DBException {
        try {
            statement.setString(1, exhibitionCenter.getTitle());
            statement.setString(2, exhibitionCenter.getAddress());
            statement.setString(3, exhibitionCenter.geteMail());
            statement.setString(4, exhibitionCenter.getWebPage());
        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    private void prepareStatementToUpdate(PreparedStatement statement,
                                          ExhibitionCenter exhibitionCenter) throws DBException {
        try {
            statement.setString(1, exhibitionCenter.getTitle());
            statement.setString(2, exhibitionCenter.getAddress());
            statement.setString(3, exhibitionCenter.geteMail());
            statement.setString(4, exhibitionCenter.getWebPage());
            statement.setInt(5, exhibitionCenter.getId());
        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

}
