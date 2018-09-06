package dao.mysql;

import controller.command.common.RegistrationCommand;
import dao.interfaces.RoleDao;
import entities.Role;
import entities.User;
import exceptions.DBException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RoleMySql implements RoleDao {

    private Role role;
    private final Connection connection;
    private final ResourceBundle QUERIES;
    private static final Logger LOGGER = Logger.getLogger(RegistrationCommand.class);
    private static final String FIELD_ID = "id";
    private static final String FIELD_ROLE = "role";

    public RoleMySql(Connection connection) {
        this.QUERIES = ResourceBundle.getBundle("QueriesMySql");
        this.connection = connection;
    }

    public RoleMySql(Connection connection, ResourceBundle resourceBundle) {
        this.QUERIES = resourceBundle;
        this.connection = connection;
    }

    /**
     * Get role for specific user from role table
     *
     * @param id User
     * @return Enum Role
     * @throws DBException
     */
    @Override
    public Role getRoleById(Integer id) throws DBException {

        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("role.getRole"))) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            String stringRole = "";
            if(resultSet.next()) {
                stringRole = resultSet.getString(FIELD_ROLE);
            }
            setRole(stringRole);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getRoleById(" + id + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        return role;
    }

    /**
     * Insert tole for user in to role table
     *
     * @param user entity
     * @param role Enum
     * @throws DBException
     */
    @Override
    public void insertRole(User user, Role role) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("role.insert"))) {
            statement.setInt(1, user.getId());
            statement.setString(2, role.toString());
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getRoleById(" + user +
                    "," + role + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Delete role for user from role table
     *
     * @param user entity
     * @throws DBException
     */
    @Override
    public void delete(User user) throws DBException{
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("role.delete"))) {
            statement.setInt(1, user.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getRoleById(" + user + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Change role for user in role table
     *
     * @param id   if User
     * @param role Enum
     * @throws DBException
     */
    @Override
    public void updateRole(Integer id, Role role) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("role.updateById"))) {
            statement.setString(1, role.toString());
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getRoleById(" + id +
                    "," + role + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    private void setRole(String stringRole) {
        switch (stringRole) {
            case "ADMIN":
                role = Role.ADMIN;
                return;

            case "MODERATOR":
                role = Role.MODERATOR;
                return;

            case "AUTHOR":
                role = Role.AUTHOR;
                return;

            case "USER":
                role = Role.USER;
                return;

            case "GUEST":
                role = Role.GUEST;
                return;

            default:
                role = Role.GUEST;
                return;
        }
    }


}
