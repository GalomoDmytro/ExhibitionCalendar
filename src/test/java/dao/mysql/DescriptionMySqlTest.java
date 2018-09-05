package dao.mysql;

import static junit.framework.TestCase.fail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import entities.Exhibition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DescriptionMySqlTest {

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
    public void getAllDescriptionTest() {

        FactoryMySql factoryMySql = new FactoryMySql();

        Exhibition exhibition1 = new Exhibition().emptyExhibition();
        Exhibition exhibition2 = new Exhibition().emptyExhibition();

        String keyRu = "ru";
        String keyEng = "eng";
        String expected_ru = "text ru";
        String expected_eng = "text eng";

        try {
            // insert exhibitions in table
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition1);
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition2);

            // insert descriptions
            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .insertDescriptionById(keyEng, expected_eng, 1);
            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .insertDescriptionById(keyRu, expected_ru, 2);

            // get all descriptions
            Map<String, String> allDescriptionEx
                    = factoryMySql.createDescriptionTable
                    (connection, QUERIES_MY_SQL_TEST).getAllDescription(exhibition1);
            assertEquals(expected_eng, allDescriptionEx.get(keyEng));
            assertEquals(1, allDescriptionEx.size());

            allDescriptionEx = factoryMySql.createDescriptionTable
                    (connection, QUERIES_MY_SQL_TEST).getAllDescription(exhibition2);
            assertEquals(expected_ru, allDescriptionEx.get(keyRu));
            assertEquals(1, allDescriptionEx.size());

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getAllDescriptionByIdTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        Exhibition exhibition = new Exhibition().emptyExhibition();

        String langKeyEng = "eng";
        String langKeyRu = "ru";
        String expected_ru = "text ru";
        String expected_eng = "text eng";

        try {
            // insert exhibition in table
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition);

            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .insertDescriptionById(langKeyEng, expected_eng, 1);
            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .insertDescriptionById(langKeyRu, expected_ru, 1);

            // get all descriptions
            Map<String, String> allDescriptionEx = factoryMySql
                    .createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .getAllDescriptionById(exhibition.getId());

            assertEquals(expected_ru, allDescriptionEx.get("ru"));
            assertEquals(expected_eng, allDescriptionEx.get("eng"));
            assertEquals(2, allDescriptionEx.size());

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getDescriptionTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        Exhibition exhibition = new Exhibition().emptyExhibition();

        String expected_ru = "текст ru";
        String expected_eng = "text eng";
        String langKeyRu = "ru";
        String langKeyEng = "eng";

        try {
            // insert exhibition
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition);

            // insert descriptions
            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .insertDescriptionById(langKeyEng, expected_eng, 1);
            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .insertDescriptionById(langKeyRu, expected_ru, 1);

            // get description from table for langKeyRu
            String description = factoryMySql
                    .createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .getDescription(exhibition, langKeyRu);
            assertEquals(expected_ru, description);

            // get description from table for langKeyEng
            description = factoryMySql
                    .createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .getDescription(exhibition, langKeyEng);
            assertEquals(expected_eng, description);

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void insertDescriptionTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        Exhibition exhibition = new Exhibition().emptyExhibition();

        String expected_ru = "рус текст";
        String expected_eng = "text eng";
        String langKeyRu = "ru";
        String langKeyEng = "eng";

        try {
            // insert exhibition in table
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition);

            // insert descriptions in table
            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .insertDescription(expected_ru, langKeyRu, exhibition);
            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .insertDescription(expected_eng, langKeyEng, exhibition);

            // get all descriptions
            Map<String, String> allDescriptionEx = factoryMySql
                    .createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .getAllDescription(exhibition);

            assertEquals(2, allDescriptionEx.size());
            assertEquals(expected_eng, allDescriptionEx.get(langKeyEng));
            assertEquals(expected_ru, allDescriptionEx.get(langKeyRu));

        } catch (Exception e) {
            System.err.println(e);
            fail();
        }
    }

    @Test
    public void insertDescriptionByIdTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        Exhibition exhibition = new Exhibition().emptyExhibition();

        String expected_ru = "рус текст";
        String expected_eng = "text eng";
        String langKeyRu = "ru";
        String langKeyEng = "eng";

        try {
            // insert exhibition in to db
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition);

            // insert Descriptions for table
            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .insertDescriptionById(langKeyRu, expected_ru, 1);
            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .insertDescriptionById(langKeyEng, expected_eng, 1);

            // get all description for exhibition
            Map<String, String> allDescriptionEx = factoryMySql
                    .createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .getAllDescription(exhibition);

            assertEquals(2, allDescriptionEx.size());
            assertEquals(expected_eng, allDescriptionEx.get(langKeyEng));
            assertEquals(expected_ru, allDescriptionEx.get(langKeyRu));

        } catch (Exception e) {
            System.err.println(e);
            fail();
        }
    }

    @Test
    public void deleteAllDescriptionForExpositionTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        Exhibition exhibition = new Exhibition().emptyExhibition();

        String expected_ru = "рус текст";
        String expected_eng = "text eng";
        String langKeyRu = "ru";
        String langKeyEng = "eng";

        try {
            // insert exhibition in table
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition);

            // insert descriptions
            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .insertDescriptionById(expected_ru, langKeyRu, 1);
            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .insertDescriptionById(expected_eng, langKeyEng, 1);

            // delete all descriptions
            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .deleteAllDescriptionForExposition(exhibition);

            // get all descriptions
            Map<String, String> allDescriptionEx = factoryMySql
                    .createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .getAllDescription(exhibition);

            assertEquals(0, allDescriptionEx.size());
            assertEquals(Collections.<String, String>emptyMap(), allDescriptionEx);
            assertNull(allDescriptionEx.get(langKeyEng));

        } catch (Exception e) {
            System.err.println(e);
            fail();
        }
    }

    @Test
    public void deleteDescriptionForLangTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        Exhibition exhibition = new Exhibition().emptyExhibition();

        String expected_ru = "рус текст";
        String expected_eng = "text eng";
        String langKeyRu = "ru";
        String langKeyEng = "eng";

        try {
            // insert exhibition
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition);

            // insert descriptions
            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .insertDescriptionById(langKeyRu, expected_ru, 1);
            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .insertDescriptionById(langKeyEng, expected_eng, 1);

            // delete description
            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .deleteDescriptionForLang(exhibition, langKeyEng);

            Map<String, String> allDescriptionEx = factoryMySql
                    .createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .getAllDescription(exhibition);

            assertEquals(1, allDescriptionEx.size());
            assertEquals(expected_ru, allDescriptionEx.get(langKeyRu));
            assertNull(allDescriptionEx.get(langKeyEng));

        } catch (Exception e) {
            System.err.println(e);
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