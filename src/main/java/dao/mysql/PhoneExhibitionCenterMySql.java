package dao.mysql;

import controller.command.moderatorCommand.ExpoCenterManagement;
import dao.interfaces.PhoneExhibitionCenterDao;
import exceptions.DBException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Dmytro Galomko
 */
public class PhoneExhibitionCenterMySql implements PhoneExhibitionCenterDao {

    private final Connection connection;
    private final ResourceBundle QUERIES;
    private static final String FIELD_EXHIBITION_ID = "exhibition_id";
    private static final String FIELD_PHONE = "phone";
    private static final Logger LOGGER = Logger.getLogger(ExpoCenterManagement.class);

    PhoneExhibitionCenterMySql(Connection connection) {
        this.QUERIES = ResourceBundle.getBundle("QueriesMySql");
        this.connection = connection;
    }

    PhoneExhibitionCenterMySql(Connection connection, ResourceBundle resourceBundle) {
        this.QUERIES = resourceBundle;
        this.connection = connection;
    }

    /**
     * Get List of phones from exhibition_center_phone table
     *
     * @param id of Exhibition Center
     * @return List of Strings or empty List
     * @throws DBException
     */
    @Override
    public List<String> getPhones(Integer id) throws DBException {
        List<String> phones;

        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("centerPhone.getPhones"))) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            phones = new ArrayList<>();
            if (resultSet.getFetchSize() == 0) {
                while (resultSet.next()) {
                    phones.add(resultSet.getString(FIELD_PHONE));
                }
            }
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getPhones(" + id + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (phones == null) {
            return Collections.emptyList();
        }

        return phones;
    }

    /**
     * Insert new phone for specific exhibition_center in
     * exhibition_center_phone table
     *
     * @param id    of  Exhibition Center
     * @param phone
     * @throws DBException
     */
    @Override
    public void insertPhone(Integer id, String phone) throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("centerPhone.insertPhone"))) {
            statement.setString(1, phone);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When insertPhone(" + id + ", " + phone + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Delete all phones for specific Exhibition Center
     * from exhibition_center_phone table
     *
     * @param id of Exhibition Center
     * @throws DBException
     */
    @Override
    public void deletePhone(Integer id) throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("centerPhone.delete"))) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When deletePhone(" + id + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }
}
