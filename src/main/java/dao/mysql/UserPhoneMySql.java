package dao.mysql;

import dao.interfaces.UserPhonesDao;
import exceptions.DBException;

import javax.management.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserPhoneMySql implements UserPhonesDao {

    private static final String FIELD_MAIL = "email";

    private Connection connection;
    private static final ResourceBundle QUERIES = ResourceBundle.getBundle("QueriesMySql");

    UserPhoneMySql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<String> getPhones(String eMail) throws DBException{
        List<String> phones = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("userPhone.getPhones"))) {
            statement.setString(1, eMail);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                phones.add(resultSet.getString(FIELD_MAIL));
            }
        } catch (SQLException exeption) {
            throw new DBException(exeption);
        }
        return phones;
    }

    @Override
    public void insertPhone(String mail, String phone) throws DBException {
        try(PreparedStatement statement = connection.prepareStatement(QUERIES.getString("userPhone.insertPhone"))) {
            statement.setString(1, phone);
            statement.setString(2, mail);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    @Override
    public void deletePhone(String mail, String phone) throws DBException {
        try(PreparedStatement statement = connection.prepareStatement(QUERIES.getString("userPhone.delete"))) {
            statement.setString(1, mail);
            statement.setString(2, phone);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }
}
