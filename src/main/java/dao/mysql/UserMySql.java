package dao.mysql;

import dao.interfaces.UserDao;
import entities.User;
import exceptions.DBException;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class UserMySql implements UserDao {

    private Connection connection;
    private static final ResourceBundle QUERIES = ResourceBundle.getBundle("QueriesMySql");

    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_MAIL = "email";
    private static final String FIELD_FIRST_NAME = "first_name";
    private static final String FIELD_LAST_NAME = "last_name";
    private static final String FIELD_PASSWORD = "password";

    public UserMySql() {
    }

    public UserMySql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User getById(Integer id) throws DBException {
        List<User> user = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("user.getById"))) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            user = parseUsersSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        if (user == null) {
            return null;
        } else {
            return user.get(0);
        }
    }

    @Override
    public User getByName(String name) throws DBException {
        List<User> user = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("user.getByName"))) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            user = parseUsersSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        if (user == null) {
            return null;
        } else {
            return user.get(0);
        }
    }

    @Override
    public User getByMail(String eMail) throws DBException {
        List<User> user = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("user.getByEMail"))) {
            statement.setString(1, eMail);
            ResultSet resultSet = statement.executeQuery();
            user = parseUsersSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        if (user == null) {
            return null;
        } else if (user.size() < 1) {
            return null;
        } else {
            return user.get(0);
        }
    }

    @Override
    public List<User> getAllUsers() throws DBException {
        List<User> user = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("user.getAll"))) {
            ResultSet resultSet = statement.executeQuery();
            user = parseUsersSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        if (user == null) {
            return null;
        } else {
            return user;
        }
    }

    @Override
    public void updateUser(User user) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("user.updateUser"))) {
            prepareStatementToUpdate(statement, user);
            statement.executeQuery();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    @Override
    public void insertUser(User user) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement
                (QUERIES.getString("user.create"), Statement.RETURN_GENERATED_KEYS)) {
            prepareStatementToInsert(statement, user);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            // get the insert userId
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    @Override
    public void deleteUser(String mail) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("user.delete"))) {
            statement.setString(1, mail);
            statement.executeQuery();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    @Override
    public boolean isNameInTable(String name) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("user.nameInTable"))) {
            statement.setString(1, name);
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

    @Override
    public boolean isMailInTable(String eMail) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("user.mailInTable"))) {
            statement.setString(1, eMail);
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

    private List<User> parseUsersSet(ResultSet resultSet) throws DBException {
        List<User> users = new ArrayList<>();

        try {
            while (resultSet.next()) {
                User user = new User.Builder()
                        .setId(resultSet.getInt(FIELD_ID))
                        .setName(resultSet.getString(FIELD_NAME))
                        .setMail(resultSet.getString(FIELD_MAIL))
                        .setFirstName(resultSet.getString(FIELD_FIRST_NAME))
                        .setLastName(resultSet.getString(FIELD_LAST_NAME))
                        .setPassword(resultSet.getInt(FIELD_PASSWORD))
                        .build();
                users.add(user);
            }
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        return users;
    }

    private void prepareStatementToInsert(PreparedStatement statement, User user) throws DBException {
        try {
            statement.setString(1, user.getName());
            statement.setString(2, user.getMail());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setInt(5, user.getPassword());
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    private void prepareStatementToUpdate(PreparedStatement statement, User user) throws DBException {
        try {
            statement.setString(1, user.getName());
            statement.setString(2, user.getMail());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setInt(5, user.getPassword());
            statement.setInt(6, user.getId());
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    private void closeConnection() throws DBException {
        try {
            if (connection != null) connection.close();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }
}
