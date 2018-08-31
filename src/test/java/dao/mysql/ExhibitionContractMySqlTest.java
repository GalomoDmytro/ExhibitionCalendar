package dao.mysql;

import entities.Contract;
import entities.Exhibition;
import entities.ExhibitionCenter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;

public class ExhibitionContractMySqlTest {

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
    public void getExhibitionContractByIdTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        Contract contract = new Contract.Builder()
                .setMaxTicketPerDay(0)
                .setWorkTime("test")
                .setDateFrom(null)
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(null)
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();

        try {
            // insert exhibition center
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(new ExhibitionCenter().emptyCenter());
            // insert exhibition
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(new Exhibition().emptyExhibition());
            // insert contract
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);

            Contract returnContract = factoryMySql
                    .createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .getExhibitionContractById(1);

            assertEquals(contract, returnContract);

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getAllContractsByExCenterWithExhibitionTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        Contract contractFirst = new Contract.Builder()
                .setMaxTicketPerDay(0)
                .setWorkTime("test")
                .setDateFrom(null)
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(null)
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();
        Contract contractSecond = new Contract.Builder()
                .setMaxTicketPerDay(0)
                .setWorkTime("test")
                .setDateFrom(null)
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(null)
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();

        Exhibition exhibition = new Exhibition().emptyExhibition();
        ExhibitionCenter exhibitionCenter = new ExhibitionCenter().emptyCenter();

        try {
            // insert exhibition center
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);
            // insert exhibition
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition);
            // insert contracts
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contractFirst);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contractSecond);

            List<Contract> contractList = factoryMySql
                    .createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .getAllContractsByExCenterWithExhibition(exhibitionCenter, exhibition);

            assertEquals(2, contractList.size());
            assertTrue(contractList.contains(contractFirst));
            assertTrue(contractList.contains(contractSecond));

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void prepareCECTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        Contract contract = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(null)
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(null)
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();

        Exhibition exhibition = new Exhibition().emptyExhibition();
        ExhibitionCenter exhibitionCenter = new ExhibitionCenter().emptyCenter();

        Exhibition returnExhibition = new Exhibition().emptyExhibition();
        ExhibitionCenter returnExhibitionCenter = new ExhibitionCenter().emptyCenter();
        Contract returnContract = new Contract().emptyContract();

        try {
            // insert exhibition center
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);
            // insert exhibition
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition);
            // insert contract
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);

            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .prepareCEC(returnContract, returnExhibition, returnExhibitionCenter, 1);

            assertEquals(contract, returnContract);
            assertEquals(exhibition, returnExhibition);
            assertEquals(exhibitionCenter, returnExhibitionCenter);


        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getAllContractsForCenterTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        Contract contract_1 = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(null)
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(null)
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();
        Contract contract_2 = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(null)
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(null)
                .setExCenterId(2)
                .setExhibitionId(1)
                .build();

        Exhibition exhibition = new Exhibition().emptyExhibition();
        ExhibitionCenter exhibitionCenter_1 = new ExhibitionCenter().emptyCenter();
        ExhibitionCenter exhibitionCenter_2 = new ExhibitionCenter().emptyCenter();

        try {
            // insert exhibition centers
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter_1);
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter_2);
            // insert exhibition
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition);

            // insert contracts
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract_1);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract_1);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract_1);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract_2);

            List<Contract> contractListCenterFirst = factoryMySql
                    .createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .getAllContractsForCenter(1);

            assertEquals(3, contractListCenterFirst.size());
            assertTrue(contractListCenterFirst.contains(contract_1));

            List<Contract> contractListCenterSecond = factoryMySql
                    .createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .getAllContractsForCenter(2);

            assertEquals(1, contractListCenterSecond.size());
            assertTrue(contractListCenterSecond.contains(contract_2));

            List<Contract> contractListCenterEmpty = factoryMySql
                    .createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .getAllContractsForCenter(3);

            assertTrue(contractListCenterEmpty.isEmpty());

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getAllContractsForExhibitionTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        Contract contract_1 = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(null)
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(null)
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();
        Contract contract_2 = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(null)
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(null)
                .setExCenterId(1)
                .setExhibitionId(2)
                .build();

        Exhibition exhibition_1 = new Exhibition().emptyExhibition();
        Exhibition exhibition_2 = new Exhibition().emptyExhibition();
        ExhibitionCenter exhibitionCenter = new ExhibitionCenter().emptyCenter();

        try {
            // insert exhibition center
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);
            // insert exhibitions
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition_1);
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition_2);

            // insert contracts
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract_1);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract_1);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract_1);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract_2);

            List<Contract> contractListCenterFirst = factoryMySql
                    .createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .getAllContractsForExhibition(1);
            assertEquals(3, contractListCenterFirst.size());
            assertTrue(contractListCenterFirst.contains(contract_1));

            List<Contract> contractListCenterSecond = factoryMySql
                    .createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .getAllContractsForExhibition(2);
            assertEquals(1, contractListCenterSecond.size());
            assertTrue(contractListCenterSecond.contains(contract_2));

            List<Contract> contractListCenterEmpty = factoryMySql
                    .createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .getAllContractsForExhibition(3);
            assertTrue(contractListCenterEmpty.isEmpty());

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getAllContractsTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        Contract contract_1 = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(null)
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(null)
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();
        Contract contract_2 = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(null)
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(null)
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();

        Exhibition exhibition = new Exhibition().emptyExhibition();
        ExhibitionCenter exhibitionCenter = new ExhibitionCenter().emptyCenter();

        try {
            // insert exhibition center
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);
            // insert exhibition
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition);

            // insert contracts
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract_1);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract_1);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract_2);

            List<Contract> contractListCenterFirst = factoryMySql
                    .createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .getAllContracts();

            assertEquals(3, contractListCenterFirst.size());
            assertTrue(contractListCenterFirst.contains(contract_1));
            assertTrue(contractListCenterFirst.contains(contract_2));

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getAllContractAfterDateWithExpoCenterAndExhibitionTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        String lookingDate = "2001-01-01";
        String dateTo = "2002-01-01";
        String beforeLookingDate = "2000-01-01";

        Contract contract = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(java.sql.Date.valueOf(lookingDate))
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(java.sql.Date.valueOf(dateTo))
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();
        Contract contract_2 = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(java.sql.Date.valueOf(beforeLookingDate))
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(java.sql.Date.valueOf(dateTo))
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();

        Exhibition exhibition = new Exhibition().emptyExhibition();
        ExhibitionCenter exhibitionCenter = new ExhibitionCenter().emptyCenter();

        try {
            // insert exhibition center
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);
            // insert exhibition
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition);
            // insert contract
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);

            List<Contract> contractList = factoryMySql
                    .createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .getAllContractAfterDateWithExpoCenterAndExhibition(java.sql.Date.valueOf(lookingDate));

            assertEquals(1, contractList.size());
            assertTrue(contractList.contains(contract));
            assertFalse(contractList.contains(contract_2));

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void searchAfterDateWithExpoCenterAndExhibitionTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        String lookingDate = "2001-01-01";
        String dateTo = "2002-01-01";
        String afterLookingDate = "2001-02-01";
        String searchLine = "search";

        Contract contract = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(java.sql.Date.valueOf(lookingDate))
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(java.sql.Date.valueOf(dateTo))
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();
        Contract contract_2 = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(java.sql.Date.valueOf(afterLookingDate))
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(java.sql.Date.valueOf(dateTo))
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();
        Contract contract_3 = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(java.sql.Date.valueOf(afterLookingDate))
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(java.sql.Date.valueOf(dateTo))
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();

        Exhibition exhibition = new Exhibition().emptyExhibition();
        exhibition.setTitle("search");
        ExhibitionCenter exhibitionCenter = new ExhibitionCenter().emptyCenter();

        try {
            // insert exhibition center
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);
            // insert exhibition
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition);
            // insert contracts
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract_2);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract_3);

            List<Contract> contractList = factoryMySql
                    .createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .searchAfterDateWithExpoCenterAndExhibition(searchLine, java.sql.Date.valueOf(lookingDate));

            assertEquals(1, contractList.size());
            assertTrue(contractList.contains(contract));
            assertFalse(contractList.contains(contract_2));
            assertFalse(contractList.contains(contract_3));

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void searchContactsWithExpoAndCenterLimitTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        String lookingDate = "2001-01-01";
        String dateTo = "2002-01-01";
        String searchLine = "search";

        Contract contract = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(java.sql.Date.valueOf(lookingDate))
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(java.sql.Date.valueOf(dateTo))
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();

        Exhibition exhibition = new Exhibition().emptyExhibition();
        exhibition.setTitle("search");
        ExhibitionCenter exhibitionCenter = new ExhibitionCenter().emptyCenter();

        try {
            // insert exhibition center
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);
            // insert exhibition
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition);
            // insert contracts
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);

            List<Contract> contractList = factoryMySql
                    .createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .searchContactsWithExpoAndCenterLimit(searchLine, java.sql.Date.valueOf(lookingDate),
                            0, 2);

            assertEquals(2, contractList.size());

            contractList = factoryMySql
                    .createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .searchContactsWithExpoAndCenterLimit(searchLine, java.sql.Date.valueOf(lookingDate),
                            2, 10);
            assertEquals(2, contractList.size());
            assertTrue(contractList.contains(contract));

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getAllAfterDateTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        String lookingDate = "2001-01-01";
        String dateTo = "2002-01-01";
        String notIncludeLookingDate = "2001-02-01";

        Contract contract = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(java.sql.Date.valueOf(lookingDate))
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(java.sql.Date.valueOf(dateTo))
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();
        Contract contractNotSearch = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(java.sql.Date.valueOf(notIncludeLookingDate))
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(java.sql.Date.valueOf(dateTo))
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();

        Exhibition exhibition = new Exhibition().emptyExhibition();
        exhibition.setTitle("search");
        ExhibitionCenter exhibitionCenter = new ExhibitionCenter().emptyCenter();

        try {
            // insert exhibition center
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);
            // insert exhibition
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition);
            // insert contracts
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contractNotSearch);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);


            List<Contract> contractList = factoryMySql
                    .createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .getAllAfterDate(java.sql.Date.valueOf(lookingDate));

            assertEquals(2, contractList.size());
            assertTrue(contractList.contains(contract));
            assertFalse(contractList.contains(contractNotSearch));

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getAllContractsBySearchTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        String searchLine = "search";
        String searchMatchesLine = "some text search some text";

        Contract contract = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(null)
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(null)
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();
        Contract contractNotSearch = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(null)
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(null)
                .setExCenterId(2)
                .setExhibitionId(2)
                .build();

        Exhibition exhibition = new Exhibition().emptyExhibition();
        exhibition.setTitle(searchMatchesLine);
        ExhibitionCenter exhibitionCenter = new ExhibitionCenter().emptyCenter();

        Exhibition exhibitionNotSearch = new Exhibition().emptyExhibition();
        ExhibitionCenter exhibitionCenterNotSearch = new ExhibitionCenter().emptyCenter();

        try {
            // insert exhibition center
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);
            // insert exhibition
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition);
            // insert exhibition center
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenterNotSearch);
            // insert exhibition
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibitionNotSearch);
            // insert contract
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);

            List<Contract> contractList = factoryMySql
                    .createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .getAllContractsBySearch(searchLine);

            assertEquals(2, contractList.size());
            assertTrue(contractList.contains(contract));
            assertFalse(contractList.contains(contractNotSearch));

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void updateContractTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        Contract contract = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(null)
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(null)
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();

        Exhibition exhibition = new Exhibition().emptyExhibition();
        ExhibitionCenter exhibitionCenter = new ExhibitionCenter().emptyCenter();


        try {
            // insert exhibition center
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);
            // insert exhibition
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition);
            // insert contract
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);

            // change some data
            contract.setMaxTicketPerDay(ThreadLocalRandom.current().nextInt());
            // update contract in table
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .updateContract(contract);

            Contract returnContract = factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .getExhibitionContractById(1);

            assertEquals(contract, returnContract);

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void insertContractTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        Contract contract = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(null)
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(null)
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();

        Exhibition exhibition = new Exhibition().emptyExhibition();
        ExhibitionCenter exhibitionCenter = new ExhibitionCenter().emptyCenter();


        try {
            // insert exhibition center in db
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);
            // insert exhibition in db
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition);
            // insert contract
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);

            Contract returnContract = factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .getExhibitionContractById(1);

            assertEquals(contract, returnContract);

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void deleteContractTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        Contract contract = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(null)
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(null)
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();

        Exhibition exhibition = new Exhibition().emptyExhibition();
        ExhibitionCenter exhibitionCenter = new ExhibitionCenter().emptyCenter();


        try {
            // insert exhibition center in db
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);
            // insert exhibition in db
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition);
            // insert contract
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);
            // delete Contract
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .deleteContract(contract);

            Contract returnContract = factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .getExhibitionContractById(1);

            assertEquals(new Contract().emptyContract(), returnContract);

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void deleteContractByIdTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        Contract contract = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(null)
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(null)
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();

        Exhibition exhibition = new Exhibition().emptyExhibition();
        ExhibitionCenter exhibitionCenter = new ExhibitionCenter().emptyCenter();


        try {
            // insert exhibition center in db
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);
            // insert exhibition in db
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition);
            // insert contract
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);
            // delete Contract By Id
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .deleteContractById(1);

            Contract returnContract = factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .getExhibitionContractById(1);

            assertEquals(new Contract().emptyContract(), returnContract);

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getNumberOfContractsAfterDateTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        String lookingDate = "2001-01-01";
        String dateTo = "2002-01-01";
        String notMatchesDate = "2222-01-01";

        Contract contract = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(java.sql.Date.valueOf(lookingDate))
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(java.sql.Date.valueOf(dateTo))
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();
        Contract contractNotMatchesDate = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(java.sql.Date.valueOf(notMatchesDate))
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(java.sql.Date.valueOf(notMatchesDate))
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();

        Exhibition exhibition = new Exhibition().emptyExhibition();
        ExhibitionCenter exhibitionCenter = new ExhibitionCenter().emptyCenter();

        try {
            // insert exhibition center in db
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);
            // insert exhibition in db
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition);
            // insert 4 contract
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contractNotMatchesDate);

            int quantityContracts = factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .getNumberOfContractsAfterDate(java.sql.Date.valueOf(lookingDate));

            assertEquals(3, quantityContracts);

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getContractsAfterDateLimitTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        String lookingDate = "2001-01-01";
        String dateTo = "2002-01-01";
        String notIncludeLookingDate = "2001-02-01";

        Contract contract = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(java.sql.Date.valueOf(lookingDate))
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(java.sql.Date.valueOf(dateTo))
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();
        Contract contractNotSearch = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(java.sql.Date.valueOf(notIncludeLookingDate))
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(java.sql.Date.valueOf(dateTo))
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();

        Exhibition exhibition = new Exhibition().emptyExhibition();
        exhibition.setTitle("search");
        ExhibitionCenter exhibitionCenter = new ExhibitionCenter().emptyCenter();

        try {
            // insert exhibition center
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);
            // insert exhibition
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition);
            // insert contracts
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contractNotSearch);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);

            // result
            List<Contract> contractList = factoryMySql
                    .createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .getContractsAfterDateLimit(java.sql.Date.valueOf(lookingDate), 1, 5);

            assertEquals(3, contractList.size());
            assertTrue(contractList.contains(contract));

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getNumberOfContractsAfterSearchTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        String lookingDate = "2001-01-01";
        String dateTo = "2002-01-01";
        String notMatchesDate = "2222-01-01";

        String searchLine = "search";

        Contract contract = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(java.sql.Date.valueOf(lookingDate))
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(java.sql.Date.valueOf(dateTo))
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();
        Contract contractNotMatchesDate = new Contract.Builder()
                .setMaxTicketPerDay(ThreadLocalRandom.current().nextInt())
                .setWorkTime("test")
                .setDateFrom(java.sql.Date.valueOf(notMatchesDate))
                .setTicketPrice(new BigDecimal(0))
                .setDateTo(java.sql.Date.valueOf(notMatchesDate))
                .setExCenterId(1)
                .setExhibitionId(1)
                .build();

        Exhibition exhibition = new Exhibition().emptyExhibition();
        exhibition.setTitle(searchLine);
        ExhibitionCenter exhibitionCenter = new ExhibitionCenter().emptyCenter();

        try {
            // insert exhibition center in db
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibitionCenter(exhibitionCenter);
            // insert exhibition in db
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST)
                    .insertExhibition(exhibition);
            // insert 4 contract
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contract);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .insertContract(contractNotMatchesDate);

            int quantityContracts = factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST)
                    .getNumberOfContractsAfterDate(java.sql.Date.valueOf(lookingDate));

            assertEquals(3, quantityContracts);

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