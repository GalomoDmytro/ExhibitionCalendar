package dao.mysql;

import entities.ExhibitionCenter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;

public class ExhibitionCenterMySqlTest {

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
    public void getExhibitionCenterByIdTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        ExhibitionCenter exhibitionCenter = new ExhibitionCenter.Builder()
                .setWebPage("test")
                .seteMail("test")
                .setAddress("test")
                .setTitle("test")
                .setPhone(Collections.emptyList())
                .build();

        try {
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);

            ExhibitionCenter returnExhibitionCenter = factoryMySql
                    .createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .getExhibitionCenterById(1);

            ExhibitionCenter notExistedExhibitionCenter = factoryMySql
                    .createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .getExhibitionCenterById(2);

            assertTrue(exhibitionCenter.equals(returnExhibitionCenter));
            assertTrue(notExistedExhibitionCenter.equals(new ExhibitionCenter().emptyCenter()));

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getExhibitionCenterByTitleTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        ExhibitionCenter exhibitionCenter = new ExhibitionCenter.Builder()
                .setWebPage("test")
                .seteMail("test")
                .setAddress("test")
                .setTitle("test")
                .build();

        try {
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);

            ExhibitionCenter returnExhibitionCenter = factoryMySql
                    .createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .getExhibitionCenterByTitle(exhibitionCenter.getTitle());

            ExhibitionCenter notExistedExhibitionCenter = factoryMySql
                    .createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .getExhibitionCenterByTitle("not existed title");

            assertTrue(exhibitionCenter.equals(returnExhibitionCenter));
            assertTrue(notExistedExhibitionCenter.equals(new ExhibitionCenter().emptyCenter()));

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }

    }

    @Test
    public void getExhibitionBySearchTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        ExhibitionCenter exhibitionCenter = new ExhibitionCenter.Builder()
                .setWebPage("test")
                .seteMail("test")
                .setAddress("test")
                .setTitle("test")
                .build();

        ExhibitionCenter exhibitionCenterNotFound = new ExhibitionCenter.Builder()
                .setWebPage("NotFound")
                .seteMail("NotFound")
                .setAddress("NotFound")
                .setTitle("NotFound")
                .build();

        try {
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);

            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenterNotFound);
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenterNotFound);

            List<ExhibitionCenter> exhibitionCenters = factoryMySql
                    .createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .getExhibitionCentersBySearch("test");

            assertEquals(2, exhibitionCenters.size());
            assertTrue(exhibitionCenters.contains(exhibitionCenter));
            assertFalse(exhibitionCenters.contains(exhibitionCenterNotFound));

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }

    }

    @Test
    public void getExhibitionCenterByMailTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        String email = "mail@test.test";

        ExhibitionCenter exhibitionCenter = new ExhibitionCenter.Builder()
                .setWebPage("test")
                .seteMail(email)
                .setAddress("test")
                .setTitle("test")
                .build();

        ExhibitionCenter exhibitionCenterNotFound = new ExhibitionCenter.Builder()
                .setWebPage("NotFound")
                .seteMail("NotFound")
                .setAddress("NotFound")
                .setTitle("NotFound")
                .build();

        try {
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenterNotFound);

            ExhibitionCenter exhibitionCenterReturn = factoryMySql
                    .createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .getExhibitionCenterByMail(email);

            assertTrue(exhibitionCenter.equals(exhibitionCenterReturn));
            assertFalse(exhibitionCenterReturn.equals(exhibitionCenterNotFound));

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getAllExhibitionCenterTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        ExhibitionCenter exhibitionCenter = new ExhibitionCenter.Builder()
                .setWebPage("test")
                .seteMail("test")
                .setAddress("test")
                .setTitle("test")
                .build();

        ExhibitionCenter exhibitionCenterAnother = new ExhibitionCenter.Builder()
                .setWebPage("NotFound")
                .seteMail("NotFound")
                .setAddress("NotFound")
                .setTitle("NotFound")
                .build();

        try {
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenterAnother);


            List<ExhibitionCenter> exhibitionCenterList = factoryMySql
                    .createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .getAllExhibitionCenter();

            assertEquals(3, exhibitionCenterList.size());
            assertTrue(exhibitionCenterList.contains(exhibitionCenter));
            assertTrue(exhibitionCenterList.contains(exhibitionCenterAnother));
        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void deleteExhibitionCenterTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        ExhibitionCenter exhibitionCenter = new ExhibitionCenter.Builder()
                .setWebPage("test")
                .seteMail("test")
                .setAddress("test")
                .setTitle("test")
                .build();

        try {
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);

            List<ExhibitionCenter> exhibitionCenterList = factoryMySql
                    .createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .getAllExhibitionCenter();
            // result before delete
            assertEquals(2, exhibitionCenterList.size());

            // delete last one
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .deleteExhibitionCenter(exhibitionCenter);

            List<ExhibitionCenter> exhibitionCenterListAfterDelete = factoryMySql
                    .createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .getAllExhibitionCenter();

            assertEquals(1, exhibitionCenterListAfterDelete.size());


        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void insertExhibitionCenterTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        ExhibitionCenter exhibitionCenter = new ExhibitionCenter.Builder()
                .setWebPage("test")
                .seteMail("test")
                .setAddress("test")
                .setTitle("test")
                .build();

        ExhibitionCenter exhibitionCenterSecond = new ExhibitionCenter.Builder()
                .setWebPage("second")
                .seteMail("second")
                .setAddress("second")
                .setTitle("second")
                .build();

        try {
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenterSecond);

            List<ExhibitionCenter> exhibitionCenterList = factoryMySql
                    .createExhibitionCenter(connection, QUERIES_MY_SQL_TEST).getAllExhibitionCenter();

            assertTrue(exhibitionCenterList.contains(exhibitionCenter));
            assertTrue(exhibitionCenterList.contains(exhibitionCenterSecond));
            assertEquals(2, exhibitionCenterList.size());

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void updateExhibitionCenterTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        ExhibitionCenter exhibitionCenter = new ExhibitionCenter.Builder()
                .setWebPage("test")
                .seteMail("test")
                .setAddress("test")
                .setTitle("test")
                .build();

        try {
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);

            exhibitionCenter.setTitle("new title");
            exhibitionCenter.seteMail("new email");
            exhibitionCenter.setAddress("new address");
            exhibitionCenter.setWebPage("new web");

            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .updateExhibitionCenter(exhibitionCenter);

            ExhibitionCenter exhibitionCenterUpdated = factoryMySql
                    .createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .getExhibitionCenterById(1);

            assertEquals(exhibitionCenter, exhibitionCenterUpdated);

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void isTitleInTableTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        String title = "title";
        String titleNotInTable = "bad title";

        ExhibitionCenter exhibitionCenter = new ExhibitionCenter.Builder()
                .setWebPage("test")
                .seteMail("test")
                .setAddress("test")
                .setTitle("title")
                .build();

        try {
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);

            boolean titleInTable = factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .isTitleInTable(titleNotInTable);

            assertFalse(titleInTable);

            titleInTable = factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .isTitleInTable(title);

            assertTrue(titleInTable);

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void deleteExhibitionCenterByIdTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        ExhibitionCenter exhibitionCenter = new ExhibitionCenter.Builder()
                .setWebPage("test")
                .seteMail("test")
                .setAddress("test")
                .setTitle("test")
                .build();

        try {
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);

            ExhibitionCenter exhibitionCenterFromDB = factoryMySql
                    .createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .getExhibitionCenterById(1);

            assertEquals(exhibitionCenter, exhibitionCenterFromDB);

            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .deleteExhibitionCenterById(1);

            exhibitionCenterFromDB = factoryMySql
                    .createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .getExhibitionCenterById(1);

            assertEquals(new ExhibitionCenter().emptyCenter(), exhibitionCenterFromDB);

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