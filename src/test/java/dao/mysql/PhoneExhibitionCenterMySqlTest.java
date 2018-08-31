package dao.mysql;

import entities.ExhibitionCenter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;

public class PhoneExhibitionCenterMySqlTest {

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
    public void getPhones() {
        FactoryMySql factoryMySql = new FactoryMySql();
        String phone1= "phone 1";
        String phone2= "phone 2";

        ExhibitionCenter exhibitionCenter = new ExhibitionCenter.Builder()
                .setWebPage("test")
                .seteMail("test")
                .setAddress("test")
                .setTitle("test")
                .build();

        try {
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);

            factoryMySql.createExhibitionCenterPhone(connection, QUERIES_MY_SQL_TEST)
                    .insertPhone(1, phone1);
            factoryMySql.createExhibitionCenterPhone(connection, QUERIES_MY_SQL_TEST)
                    .insertPhone(1, phone2);

            List<String> phones = factoryMySql
                    .createExhibitionCenterPhone(connection, QUERIES_MY_SQL_TEST)
                    .getPhones(1);

            System.out.println(phones);

            assertEquals(2, phones.size());
            assertTrue(phones.contains(phone1));
            assertTrue(phones.contains(phone2));

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void insertPhone() {
        FactoryMySql factoryMySql = new FactoryMySql();
        String phone1= "phone 1";
        String phone2= "phone 2";
        String phone3= "phone 3";

        try {
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(new ExhibitionCenter().emptyCenter());

            factoryMySql.createExhibitionCenterPhone(connection, QUERIES_MY_SQL_TEST)
                    .insertPhone(1, phone1);
            factoryMySql.createExhibitionCenterPhone(connection, QUERIES_MY_SQL_TEST)
                    .insertPhone(1, phone2);
            factoryMySql.createExhibitionCenterPhone(connection, QUERIES_MY_SQL_TEST)
                    .insertPhone(1, phone3);

            List<String> phones = factoryMySql
                    .createExhibitionCenterPhone(connection, QUERIES_MY_SQL_TEST)
                    .getPhones(1);

            System.out.println(phones);

            assertEquals(3, phones.size());
            assertTrue(phones.contains(phone2));

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void deletePhone() {
        FactoryMySql factoryMySql = new FactoryMySql();
        String phone1= "phone 1";
        String phone2= "phone 2";

        try {
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(new ExhibitionCenter().emptyCenter());

            factoryMySql.createExhibitionCenterPhone(connection, QUERIES_MY_SQL_TEST)
                    .insertPhone(1, phone1);
            factoryMySql.createExhibitionCenterPhone(connection, QUERIES_MY_SQL_TEST)
                    .insertPhone(1, phone2);

            factoryMySql.createExhibitionCenterPhone(connection, QUERIES_MY_SQL_TEST)
                    .deletePhone(1);

            List<String> phones = factoryMySql
                    .createExhibitionCenterPhone(connection, QUERIES_MY_SQL_TEST)
                    .getPhones(1);

            assertEquals(0, phones.size());

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