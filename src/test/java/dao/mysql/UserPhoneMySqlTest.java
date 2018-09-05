package dao.mysql;

import entities.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;

public class UserPhoneMySqlTest {

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
    public void getPhonesTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        String userMail = "user@mail";
        User user = new User().emptyUser();
        user.setMail(userMail);

        String phone1 = "(0)123-456";
        String phone2 = "987-987-987";

        try {
            // insert user to table
            factoryMySql.createUser(connection, QUERIES_MY_SQL_TEST)
                    .insertUser(user);

            // insert phones to table
            factoryMySql.createUserPhones(connection, QUERIES_MY_SQL_TEST)
                    .insertPhone(userMail, phone1);
            factoryMySql.createUserPhones(connection, QUERIES_MY_SQL_TEST)
                    .insertPhone(userMail, phone2);

            List<String> phoneList = factoryMySql.createUserPhones(connection, QUERIES_MY_SQL_TEST)
                    .getPhones(userMail);

            assertEquals(2, phoneList.size());
            assertTrue(phoneList.contains(phone1));
            assertTrue(phoneList.contains(phone2));

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void insertPhoneTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        String userMail = "user@mail";
        User user = new User().emptyUser();
        user.setMail(userMail);

        String phone1 = "(0)123-456";

        try {
            // insert user to table
            factoryMySql.createUser(connection, QUERIES_MY_SQL_TEST)
                    .insertUser(user);

            // insert phones to table
            factoryMySql.createUserPhones(connection, QUERIES_MY_SQL_TEST)
                    .insertPhone(userMail, phone1);

            List<String> phoneList = factoryMySql.createUserPhones(connection, QUERIES_MY_SQL_TEST)
                    .getPhones(userMail);

            assertEquals(1, phoneList.size());
            assertTrue(phoneList.contains(phone1));

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void deletePhoneTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        String userMail = "user@mail";
        User user = new User().emptyUser();
        user.setMail(userMail);

        String phone1 = "(0)123-456";
        String phone2 = "987-987-987";

        try {
            // insert user to table
            factoryMySql.createUser(connection, QUERIES_MY_SQL_TEST)
                    .insertUser(user);

            // insert phones to table
            factoryMySql.createUserPhones(connection, QUERIES_MY_SQL_TEST)
                    .insertPhone(userMail, phone1);
            factoryMySql.createUserPhones(connection, QUERIES_MY_SQL_TEST)
                    .insertPhone(userMail, phone2);

            // delete phone
            factoryMySql.createUserPhones(connection, QUERIES_MY_SQL_TEST)
                    .deletePhone(userMail, phone1);

            List<String> phoneList = factoryMySql.createUserPhones(connection, QUERIES_MY_SQL_TEST)
                    .getPhones(userMail);

            assertEquals(1, phoneList.size());
            assertFalse(phoneList.contains(phone1));
            assertTrue(phoneList.contains(phone2));

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