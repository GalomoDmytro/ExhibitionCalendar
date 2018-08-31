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

        Exhibition exhibition1 = new Exhibition.Builder()
                .setTitle("test ex 1")
                .setImgSrc("img src 1")
                .build();
        Exhibition exhibition2 = new Exhibition.Builder()
                .setTitle("test ex 2")
                .setImgSrc("img src 2")
                .build();

        String expected_first = "text ru";
        String expected_second = "text eng";
        String description_first_insert_queried = "INSERT INTO description_test " +
                "(description, language, exhibition_id) " +
                "values ('" + expected_first + "', 'ru', '1');";
        String description_second_insert_queried = "INSERT INTO description_test " +
                "(description, language, exhibition_id) " +
                "values ('" + expected_second + "', 'eng', '2');";
        Statement statement = null;

        try {
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibition1);
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibition2);

            statement = connection.createStatement();
            statement.execute(description_first_insert_queried);
            statement.execute(description_second_insert_queried);

            Map<String, String> allDescriptionEx = factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST).getAllDescription(exhibition1);
            assertEquals(expected_first, allDescriptionEx.get("ru"));
            assertEquals(1, allDescriptionEx.size());

            allDescriptionEx = factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST).getAllDescription(exhibition2);
            assertEquals(expected_second, allDescriptionEx.get("eng"));
            assertEquals(1, allDescriptionEx.size());

        } catch (Exception e) {
            System.out.println(e);
            fail();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void getAllDescriptionByIdTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        Exhibition exhibition = new Exhibition.Builder()
                .setTitle("test ex 1")
                .setImgSrc("img src 1")
                .build();

        String expected_first = "text ru";
        String expected_second = "text eng";
        String description_first_insert_queried = "INSERT INTO description_test " +
                "(description, language, exhibition_id) " +
                "values ('" + expected_first + "', 'ru', '1');";
        String description_second_insert_queried = "INSERT INTO description_test " +
                "(description, language, exhibition_id) " +
                "values ('" + expected_second + "', 'eng', '1');";
        Statement statement = null;

        try {
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibition);

            statement = connection.createStatement();
            statement.execute(description_first_insert_queried);
            statement.execute(description_second_insert_queried);

            Map<String, String> allDescriptionEx = factoryMySql
                    .createDescriptionTable(connection, QUERIES_MY_SQL_TEST).getAllDescription(exhibition);
            assertEquals(expected_first, allDescriptionEx.get("ru"));
            assertEquals(expected_second, allDescriptionEx.get("eng"));
            assertEquals(2, allDescriptionEx.size());

        } catch (Exception e) {
            System.out.println(e);
            fail();
        } finally {
            closeStatement(statement);
        }
    }

    @Test
    public void getDescriptionTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        Exhibition exhibition = new Exhibition.Builder()
                .setTitle("test ex 1")
                .setImgSrc("img src 1")
                .build();

        String expected_first = "text ru";
        String expected_second = "text eng";
        String langKeyRu = "ru";
        String langKeyEng = "eng";
        String description_first_insert_queried = "INSERT INTO description_test " +
                "(description, language, exhibition_id) " +
                "values ('" + expected_first + "', '" + langKeyRu + "', '1');";
        String description_second_insert_queried = "INSERT INTO description_test " +
                "(description, language, exhibition_id) " +
                "values ('" + expected_second + "', '" + langKeyEng + "', '1');";
        Statement statement = null;

        try {
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibition);
            statement = connection.createStatement();
            statement.execute(description_first_insert_queried);
            statement.execute(description_second_insert_queried);

            String description = factoryMySql
                    .createDescriptionTable(connection, QUERIES_MY_SQL_TEST).getDescription(exhibition, langKeyRu);
            assertEquals(expected_first, description);
            description = factoryMySql
                    .createDescriptionTable(connection, QUERIES_MY_SQL_TEST).getDescription(exhibition, langKeyEng);
            assertEquals(expected_second, description);

        } catch (Exception e) {
            System.out.println(e);
            fail();
        } finally {
            closeStatement(statement);
        }
    }

    @Test
    public void insertDescriptionTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        Exhibition exhibition = new Exhibition.Builder()
                .setTitle("test ex 1")
                .setImgSrc("img src 1")
                .build();

        String expected_ru = "рус текст";
        String expected_eng = "text eng";
        String langKeyRu = "ru";
        String langKeyEng = "eng";

        Statement statement = null;

        try {
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibition);
            statement = connection.createStatement();

            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .insertDescription(expected_ru, langKeyRu, exhibition);
            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .insertDescription(expected_eng, langKeyEng, exhibition);

            Map<String, String> allDescriptionEx = factoryMySql
                    .createDescriptionTable(connection, QUERIES_MY_SQL_TEST).getAllDescription(exhibition);

            assertEquals(2, allDescriptionEx.size());
            assertEquals(expected_eng, allDescriptionEx.get(langKeyEng));
            assertEquals(expected_ru, allDescriptionEx.get(langKeyRu));

        } catch (Exception e) {
            System.err.println(e);
            fail();
        } finally {
            closeStatement(statement);
        }
    }

    @Test
    public void insertDescriptionByIdTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        Exhibition exhibition = new Exhibition.Builder()
                .setTitle("test ex 1")
                .setImgSrc("img src 1")
                .build();

        String expected_ru = "рус текст";
        String expected_eng = "text eng";
        String langKeyRu = "ru";
        String langKeyEng = "eng";
        Statement statement = null;

        try {
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibition);
            statement = connection.createStatement();

            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .insertDescriptionById(expected_ru, langKeyRu, 1);
            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .insertDescriptionById(expected_eng, langKeyEng, 1);

            Map<String, String> allDescriptionEx = factoryMySql
                    .createDescriptionTable(connection, QUERIES_MY_SQL_TEST).getAllDescription(exhibition);

            assertEquals(2, allDescriptionEx.size());
            assertEquals(expected_eng, allDescriptionEx.get(langKeyEng));
            assertEquals(expected_ru, allDescriptionEx.get(langKeyRu));

        } catch (Exception e) {
            System.err.println(e);
            fail();
        } finally {
            closeStatement(statement);
        }
    }

    @Test
    public void deleteAllDescriptionForExpositionTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        Exhibition exhibition = new Exhibition.Builder()
                .setTitle("test ex 1")
                .setImgSrc("img src 1")
                .build();

        String expected_ru = "рус текст";
        String expected_eng = "text eng";
        String langKeyRu = "ru";
        String langKeyEng = "eng";
        Statement statement = null;

        try {
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibition);
            statement = connection.createStatement();

            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .insertDescriptionById(expected_ru, langKeyRu, 1);
            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .insertDescriptionById(expected_eng, langKeyEng, 1);

            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .deleteAllDescriptionForExposition(exhibition);

            Map<String, String> allDescriptionEx = factoryMySql
                    .createDescriptionTable(connection, QUERIES_MY_SQL_TEST).getAllDescription(exhibition);

            assertEquals(0, allDescriptionEx.size());
            assertEquals(Collections.<String, String>emptyMap(), allDescriptionEx);
            assertNull( allDescriptionEx.get(langKeyEng));

        } catch (Exception e) {
            System.err.println(e);
            fail();
        } finally {
            closeStatement(statement);
        }
    }

    @Test
    public void deleteDescriptionForLangTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        Exhibition exhibition = new Exhibition.Builder()
                .setTitle("test ex 1")
                .setImgSrc("img src 1")
                .build();

        String expected_ru = "рус текст";
        String expected_eng = "text eng";
        String langKeyRu = "ru";
        String langKeyEng = "eng";
        Statement statement = null;

        try {
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibition);
            statement = connection.createStatement();

            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .insertDescriptionById(expected_ru, langKeyRu, 1);
            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .insertDescriptionById(expected_eng, langKeyEng, 1);

            factoryMySql.createDescriptionTable(connection, QUERIES_MY_SQL_TEST)
                    .deleteDescriptionForLang(exhibition, langKeyEng);

            Map<String, String> allDescriptionEx = factoryMySql
                    .createDescriptionTable(connection, QUERIES_MY_SQL_TEST).getAllDescription(exhibition);

            assertEquals(1, allDescriptionEx.size());
            assertEquals(expected_ru, allDescriptionEx.get(langKeyRu));
            assertNull( allDescriptionEx.get(langKeyEng));

        } catch (Exception e) {
            System.err.println(e);
            fail();
        } finally {
            closeStatement(statement);
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

    private void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
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