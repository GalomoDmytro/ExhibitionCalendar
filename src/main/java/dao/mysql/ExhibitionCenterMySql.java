package dao.mysql;

import controller.command.RegistrationCommand;
import dao.interfaces.ExhibitionCenterDao;
import entities.ExhibitionCenter;
import exceptions.DBException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ExhibitionCenterMySql implements ExhibitionCenterDao {

    private Connection connection;

    private final String FIELD_ID = "id";
    private final String FIELD_TITLE = "title";
    private final String FIELD_ADDRES = "address";
    private final String FIELD_MAIL = "email";
    private final String FIELD_WEB_PAGE = "web_page";

    private static final ResourceBundle QUERIES = ResourceBundle.getBundle("QueriesMySql");
    private static final Logger log = Logger.getLogger(ExhibitionCenterMySql.class);


    ExhibitionCenterMySql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ExhibitionCenter getExhibitionCenterById(Integer id) throws DBException {
        List<ExhibitionCenter> exhibitionCenters = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("exhibitionCenter.getById"))) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            exhibitionCenters = parseExhibitionCenterSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        if (exhibitionCenters == null) {
            return null;
        } else {
            return exhibitionCenters.get(0);
        }
    }

    @Override
    public ExhibitionCenter getExhibitionCenterByTitle(String title) throws DBException {
        List<ExhibitionCenter> exhibitionCenters;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("exhibitionCenter.getByTitle"))) {
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            exhibitionCenters = parseExhibitionCenterSet(resultSet);
        } catch (SQLException exception) {
            log.error(exception);
            throw new DBException(exception);
        }

        if (exhibitionCenters == null) {
            return null;
        } else {
            return exhibitionCenters.get(0);
        }
    }

    @Override
    public ExhibitionCenter getExhibitionCenterByMail(String eMail) throws DBException {
        List<ExhibitionCenter> exhibitionCenters = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("exhibitionCenter.getByEMail"))) {
            statement.setString(1, eMail);
            ResultSet resultSet = statement.executeQuery();
            exhibitionCenters = parseExhibitionCenterSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        if (exhibitionCenters == null) {
            return null;
        } else {
            return exhibitionCenters.get(0);
        }
    }

    @Override
    public List<ExhibitionCenter> getAllExhibitionCenter() throws DBException {
        List<ExhibitionCenter> exhibitionCenters = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("exhibitionCenter.getAll"))) {
            ResultSet resultSet = statement.executeQuery();
            exhibitionCenters = parseExhibitionCenterSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        if (exhibitionCenters == null) {
            return null;
        } else {
            return exhibitionCenters;
        }
    }

    @Override
    public void deleteExhibitionCenter(ExhibitionCenter exhibitionCenter) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("exhibitionCenter.delete"))) {
            statement.setInt(1, exhibitionCenter.getId());
            statement.executeQuery();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    @Override
    public void insertExhibitionCenter(ExhibitionCenter exhibitionCenter) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement
                (QUERIES.getString("exhibitionCenter.insert"), Statement.RETURN_GENERATED_KEYS)) {
            prepareStatementToInsert(statement, exhibitionCenter);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            // get the insert userId
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    exhibitionCenter.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

        } catch (SQLException exception) {
            log.error(exception);
            throw new DBException(exception);
        }
    }

    @Override
    public void updateExhibitionCenter(ExhibitionCenter exhibitionCenter) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("exhibitionCenter.update"))) {
            prepareStatementToUpdate(statement, exhibitionCenter);
            statement.executeQuery();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

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
            throw new DBException(exception);
        }
    }

    private List<ExhibitionCenter> parseExhibitionCenterSet(ResultSet resultSet) throws DBException {
        List<ExhibitionCenter> exhibitionCenters = new ArrayList<>();

        log.info("bifore parse in parseExhibitionCenterSet");
        try {
            while (resultSet.next()) {

                ExhibitionCenter exCenter = new ExhibitionCenter.Builder()
                        .setId(resultSet.getInt(FIELD_ID))
                        .setTitle(resultSet.getString(FIELD_TITLE))
                        .setAddress(resultSet.getString(FIELD_ADDRES))
                        .seteMail(resultSet.getString(FIELD_MAIL))
                        .setWebPage(resultSet.getString(FIELD_WEB_PAGE))
                        .build();
                exhibitionCenters.add(exCenter);
            }
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        return exhibitionCenters;
    }

    private void prepareStatementToInsert(PreparedStatement statement, ExhibitionCenter exhibitionCenter) throws DBException {
        try {
            statement.setString(1, exhibitionCenter.getTitle());
            statement.setString(2, exhibitionCenter.getAddress());
            statement.setString(3, exhibitionCenter.geteMail());
            statement.setString(4, exhibitionCenter.getWebPage());
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    private void prepareStatementToUpdate(PreparedStatement statement, ExhibitionCenter exhibitionCenter) throws DBException {
        try {
            statement.setString(1, exhibitionCenter.getTitle());
            statement.setString(2, exhibitionCenter.getAddress());
            statement.setString(3, exhibitionCenter.geteMail());
            statement.setString(4, exhibitionCenter.getWebPage());
            statement.setInt(4, exhibitionCenter.getId());
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }
}
