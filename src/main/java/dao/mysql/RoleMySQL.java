package dao.mysql;

import dao.interfaces.RoleDao;
import entities.Role;
import exceptions.DBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RoleMySQL implements RoleDao {

    private static final String FIELD_ID = "id";

    private Connection connection;
    private static final ResourceBundle QUERIES = ResourceBundle.getBundle("QueriesMySql");


    public RoleMySQL(Connection connection) {
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

    private void setRole(Role role, String stringRole) {
        switch (stringRole) {
            case "ADMINT":
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
