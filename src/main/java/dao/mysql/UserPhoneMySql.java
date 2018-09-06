package dao.mysql;

import controller.command.common.RegistrationCommand;
import dao.interfaces.UserPhoneDao;
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

public class UserPhoneMySql implements UserPhoneDao {

    private static final String FIELD_MAIL = "email";
    private static final String FIELD_PHONE = "phone";
    private static final Logger LOGGER = Logger.getLogger(RegistrationCommand.class);

    private Connection connection;
    private final ResourceBundle QUERIES;

    UserPhoneMySql(Connection connection) {
        this.QUERIES = ResourceBundle.getBundle("QueriesMySql");
        this.connection = connection;
    }

    UserPhoneMySql(Connection connection, ResourceBundle resourceBundle) {
        this.QUERIES = resourceBundle;
        this.connection = connection;
    }

    /**
     * Get List of all Phones for chosen user from user_phone table
     *
     * @param eMail of User
     * @return List of Strings phones or empty List
     * @throws DBException
     */
    @Override
    public List<String> getPhones(String eMail) throws DBException {
        List<String> phones;

        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("userPhone.getPhones"))) {
            statement.setString(1, eMail);
            ResultSet resultSet = statement.executeQuery();
            phones = new ArrayList<>();
            while (resultSet.next()) {
                phones.add(resultSet.getString(FIELD_PHONE));
            }
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getPhones(" + eMail + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
        if(phones == null) {
            return Collections.emptyList();
        }

        return phones;
    }

    /**
     * Insert new phone to User in user_phone table
     *
     * @param mail  of User for identification User
     * @param phone to insert
     * @throws DBException
     */
    @Override
    public void insertPhone(String mail, String phone) throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("userPhone.insertPhone"))) {
            statement.setString(1, phone);
            statement.setString(2, mail);
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When insertPhone(" + mail + ","
                    + phone + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Delete User phone from user_phone table
     *
     * @param mail  of User
     * @param phone to delete
     * @throws DBException
     */
    @Override
    public void deletePhone(String mail, String phone) throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("userPhone.delete"))) {
            statement.setString(1, mail);
            statement.setString(2, phone);
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When deletePhone(" + mail + ","
                    + phone + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }
}
