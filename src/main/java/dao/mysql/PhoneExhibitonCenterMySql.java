package dao.mysql;

import dao.interfaces.PhoneExhibitionCenterDao;
import exceptions.DBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PhoneExhibitonCenterMySql implements PhoneExhibitionCenterDao {

    private static final String FIELD_EXHIBITION_ID = "exhibition_id";
    private static final String FIELD_PHONE = "phone";

    private Connection connection;
    private static final ResourceBundle QUERIES = ResourceBundle.getBundle("QueriesMySql");

    PhoneExhibitonCenterMySql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<String> getPhones(String id) throws DBException {
        List<String> phones = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("centerPhone.getPhones"))) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                phones.add(resultSet.getString(FIELD_PHONE));
            }
        } catch (SQLException exeption) {
            throw new DBException(exeption);
        }
        return phones;
    }

    @Override
    public void insertPhone(String id, String phone) throws DBException {
        try(PreparedStatement statement = connection.prepareStatement(QUERIES.getString("centerPhone.insertPhone"))) {
            statement.setString(1, phone);
            statement.setString(2, id);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    @Override
    public void deletePhone(String id, String phone) throws DBException {
        try(PreparedStatement statement = connection.prepareStatement(QUERIES.getString("centerPhone.delete"))) {
            statement.setString(1, id);
            statement.setString(2, phone);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }
}
