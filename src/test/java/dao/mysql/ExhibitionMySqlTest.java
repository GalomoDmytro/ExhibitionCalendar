package dao.mysql;

import entities.Exhibition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;

public class ExhibitionMySqlTest {

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
    public void getExhibitionByIdTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        Exhibition exhibitionInsert = new Exhibition.Builder()
                .setTitle("test exhibition")
                .setImgSrc("img source")
                .build();

        try {
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibitionInsert);
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibitionInsert);

            Exhibition returnExhibition = factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .getExhibitionById(2);

            assertTrue(exhibitionInsert.equals(returnExhibition));

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getExhibitionBySearchTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        String title = "exhibition title";

        Exhibition exhibitionInsert_1 = new Exhibition.Builder()
                .setTitle(title + 1)
                .setImgSrc("img source")
                .build();
        Exhibition exhibitionInsert_2 = new Exhibition.Builder()
                .setTitle(title + 2)
                .setImgSrc("img source")
                .build();
        Exhibition exhibitionInsert_3 = new Exhibition.Builder()
                .setTitle("3")
                .setImgSrc("img source")
                .build();

        try {
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibitionInsert_1);
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibitionInsert_2);
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibitionInsert_3);


            List<Exhibition> returnExhibition = factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .getExhibitionBySearch(title);

            assertTrue(returnExhibition.contains(exhibitionInsert_1));
            assertTrue(returnExhibition.contains(exhibitionInsert_2));
            assertFalse(returnExhibition.contains(exhibitionInsert_3));
            assertEquals(2, returnExhibition.size());

            List<Exhibition> returnExhibitionEmpty = factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .getExhibitionBySearch("unique unrelated string");
            assertEquals(Collections.emptyList(), returnExhibitionEmpty);

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getExhibitionByTitleTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        String title = "exhibition title";

        Exhibition exhibitionInsert = new Exhibition.Builder()
                .setTitle(title)
                .setImgSrc("img source")
                .build();

        try {
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibitionInsert);

            Exhibition returnExhibition = factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .getExhibitionByTitle(title);

            assertTrue(exhibitionInsert.equals(returnExhibition));

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void insertExhibitionTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        Exhibition exhibitionInsert = new Exhibition.Builder()
                .setTitle("test exhibition")
                .setImgSrc("img source")
                .build();

        try {
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibitionInsert);
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibitionInsert);

            Exhibition returnExhibition = factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .getExhibitionById(2);

            assertTrue(exhibitionInsert.equals(returnExhibition));

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void updateExhibition() {
        FactoryMySql factoryMySql = new FactoryMySql();

        Exhibition exhibition = new Exhibition.Builder()
                .setTitle("test exhibition")
                .setImgSrc("img source")
                .build();

        try {
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibition);

            Exhibition changedExhibition = exhibition;
            String newTitle = "new title";
            changedExhibition.setTitle(newTitle);

            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).updateExhibition(changedExhibition);

            Exhibition returnExhibition = factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .getExhibitionById(1);

            assertEquals(returnExhibition, changedExhibition);

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getAllExhibition() {
        FactoryMySql factoryMySql = new FactoryMySql();

        Exhibition exhibitionInsert = new Exhibition.Builder()
                .setTitle("test exhibition")
                .setImgSrc("img source")
                .build();

        try {
            List<Exhibition> exhibitions = factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .getAllExhibition();

            assertEquals(Collections.emptyList(), exhibitions);

            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibitionInsert);
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibitionInsert);
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibitionInsert);

            exhibitions = factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .getAllExhibition();

            assertEquals(3, exhibitions.size());
            assertTrue(exhibitions.contains(exhibitionInsert));

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void deleteExhibition() {
        FactoryMySql factoryMySql = new FactoryMySql();

        Exhibition exhibition = new Exhibition.Builder()
                .setTitle("test exhibition")
                .setImgSrc("img source")
                .build();

        try {

            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibition);
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibition);
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibition);

            List<Exhibition> exhibitions = factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .getAllExhibition();

            assertEquals(3, exhibitions.size());

            // delete last one
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).deleteExhibition(exhibition);
            exhibitions = factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .getAllExhibition();
            assertEquals(2, exhibitions.size());
            assertFalse(exhibitions.contains(exhibition));


        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void deleteById() {
        FactoryMySql factoryMySql = new FactoryMySql();

        Exhibition exhibitionInsert = new Exhibition.Builder()
                .setTitle("test exhibition")
                .setImgSrc("img source")
                .build();

        try {

            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibitionInsert);
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibitionInsert);
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibitionInsert);

            List<Exhibition> exhibitions = factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .getAllExhibition();

            assertEquals(3, exhibitions.size());

            // delete second (id = 2)
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).deleteById(2);
            exhibitions = factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .getAllExhibition();
            assertEquals(2, exhibitions.size());

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