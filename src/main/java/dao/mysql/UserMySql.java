package dao.mysql;

import dao.interfaces.UserDao;
import entities.User;
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
public class UserMySql implements UserDao {

    private final Connection connection;
    private final ResourceBundle QUERIES;
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_MAIL = "email";
    private static final String FIELD_FIRST_NAME = "first_name";
    private static final String FIELD_LAST_NAME = "last_name";
    private static final String FIELD_PASSWORD = "password";

    private static final Logger LOGGER = Logger.getLogger(UserMySql.class);

    public UserMySql(Connection connection) {
        this.QUERIES = ResourceBundle.getBundle("QueriesMySql");
        this.connection = connection;
    }

    public UserMySql(Connection connection, ResourceBundle resourceBundle) {
        this.QUERIES = resourceBundle;
        this.connection = connection;
    }

    /**
     * Get User from user table
     *
     * @param id of looking User, search parameter
     * @return User or User().empty();
     * @throws DBException
     */
    @Override
    public User getById(Integer id) throws DBException {
        List<User> user = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES
                .getString("user.getById"))) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            user = parseUsersSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getById(" + id + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (user == null) {
            return new User().emptyUser();
        } else {
            return user.get(0);
        }
    }

    /**
     * Get User from user table
     *
     * @param name of looking User, search parameter
     * @return User or User().empty()
     * @throws DBException
     */
    @Override
    public User getByName(String name) throws DBException {
        List<User> user = null;
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("user.getByName"))) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            user = parseUsersSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getByName(" + name + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (user == null) {
            return new User().emptyUser();
        } else {
            return user.get(0);
        }
    }

    /**
     * Get User from user table
     *
     * @param eMail of looking User, search parameter
     * @return User or User().empty()
     * @throws DBException
     */
    @Override
    public User getByMail(String eMail) throws DBException {
        List<User> user;
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("user.getByEMail"))) {
            statement.setString(1, eMail);
            ResultSet resultSet = statement.executeQuery();
            user = parseUsersSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getByMail(" + eMail + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (user == null) {
            return new User().emptyUser();
        } else if (user.size() < 1) {
            return new User().emptyUser();
        } else {
            return user.get(0);
        }
    }

    /**
     * Get all Users from user table
     *
     * @return List of Usert or empty List
     * @throws DBException
     */
    @Override
    public List<User> getAllUsers() throws DBException {
        List<User> user;
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("user.getAll"))) {
            ResultSet resultSet = statement.executeQuery();
            user = parseUsersSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (user == null) {
            return Collections.emptyList();
        } else {
            return user;
        }
    }

    /**
     * Update user table
     *
     * @param user
     * @throws DBException
     */
    @Override
    public void updateUser(User user) throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("user.updateUser"))) {
            prepareStatementToUpdate(statement, user);
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When updateUser(" + user + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Insert User in user table
     *
     * @param user
     * @throws DBException
     */
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
            LOGGER.info("Catch exception. When insertUser(" + user + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Delete User from user table
     *
     * @param mail of User
     * @throws DBException
     */
    @Override
    public void deleteUser(String mail) throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("user.delete"))) {
            statement.setString(1, mail);
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When deleteUser(" + mail + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Check if chosen name already in user table
     *
     * @param name to search in table
     * @return true if name exist in table, else return false
     * @throws DBException
     */
    @Override
    public boolean isNameInTable(String name) throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("user.nameInTable"))) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When isNameInTable(" + name + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Check if chosen mail already exist in user table
     *
     * @param eMail to search in user table
     * @return true if mail exist in table, else return false
     * @throws DBException
     */
    @Override
    public boolean isMailInTable(String eMail) throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("user.mailInTable"))) {
            statement.setString(1, eMail);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When isMailInTable(" + eMail + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Check if name or mail already exist in user table
     *
     * @param nameOrMail contain string with mail or name for looking
     *                   matches on user table
     * @return true if name or mail already in user table
     * @throws DBException
     */
    @Override
    public boolean isNameOrMailInTable(String nameOrMail) throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("user.nameOrMailInTable"))) {
            statement.setString(1, nameOrMail);
            statement.setString(2, nameOrMail);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When isNameOrMailInTable(" + nameOrMail + ");");
            LOGGER.error(exception);
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
                        .setPassword(resultSet.getString(FIELD_PASSWORD))
                        .build();
                users.add(user);
            }
        } catch (SQLException exception) {
            LOGGER.error(exception);
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
            statement.setString(5, user.getPassword());
        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    private void prepareStatementToUpdate(PreparedStatement statement, User user) throws DBException {
        try {
            statement.setString(1, user.getName());
            statement.setString(2, user.getMail());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setString(5, user.getPassword());
            statement.setInt(6, user.getId());
        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

}
