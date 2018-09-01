package dao.mysql;

import entities.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import static junit.framework.TestCase.*;

public class TicketMySqlTest {

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
    public void getTicketByIdTestTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        insertDependedTables(factoryMySql);

        Ticket ticket = new Ticket().emptyTicket();
        ticket.setContractId(1);
        ticket.setUserId(1);

        try {
            // insert ticket in table
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);

            // look existent ticket
            Ticket ticketResult = factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).getTicketById(1);
            assertEquals(ticket, ticketResult);

            // look for a non-existent ticket
            ticketResult = factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).getTicketById(2);
            assertEquals(new Ticket().emptyTicket(), ticketResult);
        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getAllTicketsTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        insertDependedTables(factoryMySql);

        Ticket ticket = new Ticket().emptyTicket();
        ticket.setContractId(1);
        ticket.setUserId(1);

        try {
            // insert tickets in table
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);

            // look all tickets
            List<Ticket> ticketList = factoryMySql
                    .createTicket(connection, QUERIES_MY_SQL_TEST).getAllTickets();

            assertEquals(3, ticketList.size());
            assertTrue(ticketList.contains(ticket));
        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getAllApprovedTicketsTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        insertDependedTables(factoryMySql);

        Ticket ticket = new Ticket().emptyTicket();
        ticket.setHasChecked(true);
        ticket.setContractId(1);
        ticket.setUserId(1);

        try {
            // insert approved tickets in table
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);
            // insert not approved ticket
            ticket.setHasChecked(false);
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);

            // look all approved tickets
            List<Ticket> ticketList = factoryMySql
                    .createTicket(connection, QUERIES_MY_SQL_TEST).getAllApprovedTickets();

            assertEquals(2, ticketList.size());

            // check if approved
            for (Ticket t : ticketList) {
                assertTrue(t.getHasChecked());
            }

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getAllWaitApprovalTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        insertDependedTables(factoryMySql);

        Ticket ticket = new Ticket().emptyTicket();
        ticket.setHasChecked(true);
        ticket.setContractId(1);
        ticket.setUserId(1);

        try {
            // insert approved tickets in table
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);
            // insert not approved ticket
            ticket.setHasChecked(false);
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);

            // look all not approved tickets
            List<Ticket> ticketList = factoryMySql
                    .createTicket(connection, QUERIES_MY_SQL_TEST).getAllWaitApproval();

            assertEquals(2, ticketList.size());

            // check if approved
            for (Ticket t : ticketList) {
                assertFalse(t.getHasChecked());
            }

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getCountSoldTicketForDateTest() {
        FactoryMySql factoryMySql = new FactoryMySql();

        insertDependedTables(factoryMySql);
        int quantitySoldTicket = 5;
        String date = "2000-01-01";

        Ticket ticket = new Ticket().emptyTicket();
        ticket.setDateToApply(java.sql.Date.valueOf(date));
        ticket.setHasChecked(true);
        ticket.setQuantity(quantitySoldTicket);
        ticket.setUserId(1);
        ticket.setContractId(1);

        try {
            // insert approved tickets in table = 5 tickets
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);
            // insert not approved ticket = 5
            ticket.setHasChecked(false);
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);


            int countTickets = factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST)
                    .getCountSoldTicketForDate(
                            java.sql.Date.valueOf(date)
                            , 1);

            // expect 10 tickets from two customers transaction
            assertEquals((quantitySoldTicket * 2), countTickets);

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getAllTicketsForContractTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        insertDependedTables(factoryMySql);

        Ticket ticket = new Ticket().emptyTicket();
        ticket.setUserId(1);
        ticket.setContractId(1);

        try {
            // insert approved ticket in table
            ticket.setHasChecked(true);
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);
            // insert not approved ticket
            ticket.setHasChecked(false);
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);

            List<Ticket> ticketList = factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST)
                    .getAllTicketsForContract(1);

            // expect 2 tickets for contract id=1
            assertEquals(2, ticketList.size());

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getAllTicketsForUserTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        insertDependedTables(factoryMySql);

        User lookingUser = new User().emptyUser();
        lookingUser.setMail("user@mail");
        lookingUser.setId(1);

        Ticket ticket = new Ticket().emptyTicket();
        ticket.setUserEMail("user@mail");
        ticket.setUserId(1);
        ticket.setContractId(1);

        try {
            // insert ticket in table
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);

            List<Ticket> ticketList = factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST)
                    .getAllTicketsForUser(lookingUser);

            // expect 2 tickets for user id = 1 and userMail = "user@mail"
            assertEquals(3, ticketList.size());

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void getTicketForUserOnContractTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        insertDependedTables(factoryMySql);

        User lookingUser = new User().emptyUser();
        lookingUser.setId(1);
        Contract contract = new Contract().emptyContract();
        contract.setId(1);

        Ticket ticket = new Ticket().emptyTicket();
        ticket.setUserId(1);
        ticket.setContractId(1);

        try {
            // insert ticket in table
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);

            List<Ticket> ticketList = factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST)
                    .getTicketForUserOnContract(lookingUser, contract);

            // expect 2 tickets for user id = 1 and contractId = 1
            assertEquals(2, ticketList.size());

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void insertTicketTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        insertDependedTables(factoryMySql);

        Ticket ticket = new Ticket().emptyTicket();
        ticket.setUserId(1);
        ticket.setContractId(1);

        try {
            // insert ticket in table
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);

            List<Ticket> ticketList = factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST)
                    .getAllTickets();

            assertEquals(3, ticketList.size());
            assertTrue(ticketList.contains(ticket));

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void updateTicketTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        insertDependedTables(factoryMySql);

        Ticket ticket = new Ticket().emptyTicket();
        ticket.setUserId(1);
        ticket.setContractId(1);

        try {
            // insert ticket in table
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);

            // make some change in ticket
            ticket.setQuantity(ThreadLocalRandom.current().nextInt());

            // update ticket
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).updateTicket(ticket);

            Ticket resultTicket = factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST)
                    .getTicketById(1);

            assertEquals(ticket, resultTicket);

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void approveTicketTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        insertDependedTables(factoryMySql);

        Ticket ticket = new Ticket().emptyTicket();
        ticket.setHasChecked(false);
        ticket.setUserId(1);
        ticket.setContractId(1);

        try {
            // insert not approved ticket in table
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);

            // approve 2 ticket
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).approveTicket(1);
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).approveTicket(3);

            List<Ticket> approvedTicketList = factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST)
                    .getAllApprovedTickets();

            assertEquals(2, approvedTicketList.size());

            List<Ticket> waitApprovedTicketList = factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST)
                    .getAllWaitApproval();

            assertEquals(1, waitApprovedTicketList.size());

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

    @Test
    public void deleteTicketTest() {
        FactoryMySql factoryMySql = new FactoryMySql();
        insertDependedTables(factoryMySql);

        Ticket ticket = new Ticket().emptyTicket();
        ticket.setUserId(1);
        ticket.setContractId(1);

        try {
            // insert 3 ticket in table
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).insertTicket(ticket);

            List<Ticket> ticketList = factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST)
                    .getAllTickets();
            assertEquals(3, ticketList.size());

            // delete ticket from table, id 1 and 3
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).deleteTicket(1);
            factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST).deleteTicket(3);

            ticketList = factoryMySql.createTicket(connection, QUERIES_MY_SQL_TEST)
                    .getAllTickets();
            Ticket ticketLeft = ticketList.get(0);
            // number of surviving tickets
            assertEquals(1, ticketList.size());
            // id of surviving tickets
            assertEquals(2, (int)ticketLeft.getId());
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

    private void insertDependedTables(FactoryMySql factoryMySql) {
        ExhibitionCenter exhibitionCenter = new ExhibitionCenter().emptyCenter();
        Exhibition exhibition = new Exhibition().emptyExhibition();
        User user = new User().emptyUser();
        Contract contract = new Contract().emptyContract();
        contract.setExhibitionId(1);
        contract.setExCenterId(1);

        try {
            factoryMySql.createExhibition(connection, QUERIES_MY_SQL_TEST).insertExhibition(exhibition);
            factoryMySql.createExhibitionCenter(connection, QUERIES_MY_SQL_TEST).insertExhibitionCenter(exhibitionCenter);
            factoryMySql.createUser(connection, QUERIES_MY_SQL_TEST).insertUser(user);
            factoryMySql.createExhibitionContract(connection, QUERIES_MY_SQL_TEST).insertContract(contract);

        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }

}