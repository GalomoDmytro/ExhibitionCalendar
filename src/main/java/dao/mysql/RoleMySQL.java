package dao.mysql;

import dao.interfaces.RoleDao;
import entities.Role;
import entities.User;
import exceptions.DBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RoleMySql implements RoleDao {

    private static final String FIELD_ID = "id";

    private Connection connection;
    private static final ResourceBundle QUERIES = ResourceBundle.getBundle("QueriesMySql");


    public RoleMySql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Role getRoleById(Integer id) throws DBException {
        Role role = null;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("role.getRole"))) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            String stringRole = resultSet.getString("FIELD_ID");
            setRole(role, stringRole);

        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        return role;
    }

    @Override
    public void insertRole(User user, Role role) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("role.insert"))) {
            statement.setInt(1, user.getId());
            statement.setString(2, role.toString());
            statement.executeQuery();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    @Override
    public void delete(User user) throws DBException{
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("role.delete"))) {
            statement.setInt(1, user.getId());
            statement.executeQuery();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    private void setRole(Role role, String stringRole) {
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
                return;
        }
    }


}
