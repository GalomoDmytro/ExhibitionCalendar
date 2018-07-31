package dao.mysql;

import dao.interfaces.UserPhonesDao;
import exceptions.DBException;

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

        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("user.getPhones"))) {
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
}
