package dao.mysql;

import entities.Role;
import entities.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ResourceBundle;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;

public class RoleMySqlTest {

    private Connection connection;
    private static final ResourceBundle QUERIES_MY_SQL_TEST = ResourceBundle
            .getBundle("QueriesMySql_Test");
    private static final ResourceBundle DB_TEST = ResourceBundle.getBundle("MySqlDB_TEST");

    @Before
    public void setUp() throws Exception {
        Class.forName(DB_TEST.getString("mysql_test.driver")).newInstance();
        connection = DriverManager.getConnection(prepareURL(), DB_TEST.getString("mysql_test.username"),
                DB_TEST.getString("mysql_test.password"));
        crateTestTables();
    }

    @After
    public void tearDown() {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(DB_TEST.getString("mysql_test.drop_all_test_tables"));
        } catch (Exception e) {
            System.out.println(e);
            fail();
        }

        closeConnection();
    }

    @Test
    public void getRoleByIdTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        User user = new User().emptyUser();

        try {
            // insert dependent user
            factoryMySql.createUser(connection, QUERIES_MY_SQL_TEST)
                    .insertUser(user);

            // ser role
            factoryMySql.createRole(connection, QUERIES_MY_SQL_TEST)
                    .insertRole(user, Role.MODERATOR);

            Role roleReturn = factoryMySql.createRole(connection, QUERIES_MY_SQL_TEST)
                    .getRoleById(1);

            assertEquals(Role.MODERATOR, roleReturn);
        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void insertRoleTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        User user = new User().emptyUser();

        try {
            // insert dependent user
            factoryMySql.createUser(connection, QUERIES_MY_SQL_TEST)
                    .insertUser(user);

            // ser role
            factoryMySql.createRole(connection, QUERIES_MY_SQL_TEST)
                    .insertRole(user, Role.ADMIN);

            Role roleReturn = factoryMySql.createRole(connection, QUERIES_MY_SQL_TEST)
                    .getRoleById(1);

            assertEquals(Role.ADMIN, roleReturn);
        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void deleteTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        User user = new User().emptyUser();

        try {
            // insert dependent user
            factoryMySql.createUser(connection, QUERIES_MY_SQL_TEST)
                    .insertUser(user);

            // ser role
            factoryMySql.createRole(connection, QUERIES_MY_SQL_TEST)
                    .insertRole(user, Role.USER);

            // delete role
            factoryMySql.createRole(connection, QUERIES_MY_SQL_TEST)
                    .delete(user);

            Role roleReturn = factoryMySql.createRole(connection, QUERIES_MY_SQL_TEST)
                    .getRoleById(1);

            assertEquals(Role.GUEST, roleReturn);
        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void updateRoleTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        User user = new User().emptyUser();

        try {
            // insert dependent user
            factoryMySql.createUser(connection, QUERIES_MY_SQL_TEST)
                    .insertUser(user);

            // ser role
            factoryMySql.createRole(connection, QUERIES_MY_SQL_TEST)
                    .insertRole(user, Role.USER);

            // update role
            factoryMySql.createRole(connection, QUERIES_MY_SQL_TEST)
                    .updateRole(1, Role.ADMIN);

            Role roleReturn = factoryMySql.createRole(connection, QUERIES_MY_SQL_TEST)
                    .getRoleById(1);

            assertEquals(Role.ADMIN, roleReturn);
        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    private void crateTestTables() {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(DB_TEST.getString("mysql_test.create_tables_exhibition_center"));
            statement.executeUpdate(DB_TEST.getString("mysql_test.create_tables_exhibition_center_phone"));
            statement.executeUpdate(DB_TEST.getString("mysql_test.create_tables_exhibition"));
            statement.executeUpdate(DB_TEST.getString("mysql_test.create_tables_description"));
            statement.executeUpdate(DB_TEST.getString("mysql_test.create_tables_exhibition_contract"));
            statement.executeUpdate(DB_TEST.getString("mysql_test.create_tables_user"));
            statement.executeUpdate(DB_TEST.getString("mysql_test.create_tables_user_phone"));
            statement.executeUpdate(DB_TEST.getString("mysql_test.create_tables_role"));
            statement.executeUpdate(DB_TEST.getString("mysql_test.create_tables_ticket"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private String prepareURL() {
        StringBuilder buildUrl = new StringBuilder();
        buildUrl.append(DB_TEST.getString("mysql_test.url")).append("&");
        buildUrl.append(DB_TEST.getString("mysql_test.useJDBCCompliantTimezoneShift")).append("&");
        buildUrl.append(DB_TEST.getString("mysql_test.useLegacyDatetimeCode")).append("&");
        buildUrl.append(DB_TEST.getString("mysql_test.serverTimeZone")).append("&");
        buildUrl.append(DB_TEST.getString("mysql_test.autoReconnect")).append("&");
        buildUrl.append(DB_TEST.getString("mysql_test.autoReconnect")).append("&");

        buildUrl.append(DB_TEST.getString("mysql_test.useSSL"));
        return buildUrl.toString();
    }

    private void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception exception) {
            System.err.println(exception);
        }
    }
}