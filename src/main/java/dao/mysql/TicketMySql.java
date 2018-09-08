package dao.mysql;

import dao.interfaces.TicketDao;
import entities.Contract;
import entities.Ticket;
import entities.User;
import exceptions.DBException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class TicketMySql implements TicketDao {
    private final Connection connection;
    private final ResourceBundle QUERIES;
    private static final String FIELD_ID = "id";
    private static final String FIELD_DATE_TO_APPLY = "date_to_apply";
    private static final String FIELD_CONTRACT_ID = "contract_id";
    private static final String FIELD_DATE_TRANSACTION = "date_transaction";
    private static final String FIELD_USER_MAIL = "email";
    private static final String FIELD_QUANTITY = "quantity";
    private static final String FIELD_USER_ID = "id_user";
    private static final String FIELD_CHECKED = "is_confirmed";
    private static final String FIELD_APPROVED_BY = "approved_by_moderator_id";

    private static final Logger LOGGER = Logger.getLogger(TicketMySql.class);

    TicketMySql(Connection connection) {
        this.QUERIES = ResourceBundle.getBundle("QueriesMySql");
        this.connection = connection;
    }

    TicketMySql(Connection connection, ResourceBundle resourceBundle) {
        this.QUERIES = resourceBundle;
        this.connection = connection;
    }

    /**
     * Enables client sessions to acquire exhibition_center table locks
     * explicitly for the purpose of cooperating with other sessions
     * for access to tables, or to prevent other
     * sessions from modifying exhibition_center tables during periods when
     * a session requires exclusive access to them
     *
     * @throws DBException
     */
    @Override
    public void setLockTicketTable() throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement("lock table ticket write")) {
            statement.execute();
        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Releases any table locks held by the current session
     *
     * @throws DBException
     */
    @Override
    public void unlockTable() throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement("unlock table")) {
            statement.execute();

        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Get Ticket from ticket table
     *
     * @param id looking ticket
     * @return Ticket entity
     * @throws DBException
     */
    @Override
    public Ticket getTicketById(Integer id)
            throws DBException {
        List<Ticket> tickets;
        try (PreparedStatement statement =
                     connection.prepareStatement(QUERIES.getString("ticket.getById"))) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            tickets = parseTicketSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getTicketById(" + id + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (tickets == null || tickets.isEmpty()) {
            return new Ticket().emptyTicket();
        } else {
            return tickets.get(0);
        }
    }

    @Override
    public int getQuantityApproved()
            throws DBException {
        int count;
        try (PreparedStatement statement =
                     connection.prepareStatement(QUERIES
                             .getString("ticket.getQuantityApproved"))) {
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getQuantityApproved();");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        return count;
    }

    @Override
    public List<Ticket> getAllApprovedLimit(int startLimit, int endLimit)
            throws DBException {
        List<Ticket> tickets;
        try (PreparedStatement statement =
                     connection.prepareStatement(QUERIES
                             .getString("ticket.getAllApprovedLimit"))) {
            statement.setInt(1, startLimit);
            statement.setInt(2, endLimit);
            ResultSet resultSet = statement.executeQuery();
            tickets = parseTicketSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getAllApprovedLimit(" + startLimit + ", "
                    + endLimit + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (tickets == null || tickets.isEmpty()) {
            return Collections.emptyList();
        }

        return tickets;
    }

    /**
     * Get all tickets from ticket table
     *
     * @return List of Tickets or empty List
     * @throws DBException
     */
    @Override
    public List<Ticket> getAllTickets()
            throws DBException {
        List<Ticket> tickets;
        try (PreparedStatement statement =
                     connection.prepareStatement(QUERIES.getString("ticket.getAll"))) {
            ResultSet resultSet = statement.executeQuery();
            tickets = parseTicketSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (tickets == null || tickets.isEmpty()) {
            return Collections.emptyList();
        }

        return tickets;
    }

    /**
     * Get all checked(approved) tickets from ticket table
     *
     * @return List of checked Tickets or empty List
     * @throws DBException
     */
    @Override
    public List<Ticket> getAllApprovedTickets()
            throws DBException {
        List<Ticket> tickets;
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("ticket.getAllApproved"))) {
            ResultSet resultSet = statement.executeQuery();
            tickets = parseTicketSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (tickets == null || tickets.isEmpty()) {
            return Collections.emptyList();
        }

        return tickets;
    }

    /**
     * Get all not checked(not approved) tickets from ticket table
     *
     * @return List of not checked Tickets or empty List
     * @throws DBException
     */
    @Override
    public List<Ticket> getAllWaitApproval()
            throws DBException {
        List<Ticket> tickets;
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("ticket.getAllWaitApproval"))) {
            ResultSet resultSet = statement.executeQuery();
            tickets = parseTicketSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (tickets == null || tickets.isEmpty()) {
            return Collections.emptyList();
        }

        return tickets;
    }

    /**
     * Get quantity of sold tickets for specific date and for
     * specific Contract from ticket table
     *
     * @param date
     * @param idContract
     * @return
     * @throws DBException
     */
    @Override
    public int getCountSoldTicketForDate(Date date, Integer idContract)
            throws DBException {
        int countTickets = 0;

        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("ticket.countSoldTicket"))) {
            statement.setDate(1, java.sql.Date.valueOf(date.toString())
                    , java.util.Calendar.getInstance());
            statement.setInt(2, idContract);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            countTickets = resultSet.getInt(1);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getCountSoldTicketForDate(" + date +
                    "," + idContract + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        return countTickets;
    }

    /**
     * Get List of Tickets for specific Contract from ticket table
     *
     * @param contractId
     * @return List Contracts or empty List
     * @throws DBException
     */
    @Override
    public List<Ticket> getAllTicketsForContract(Integer contractId) throws DBException {
        List<Ticket> tickets;
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("ticket.getAllForContract"))) {
            statement.setInt(1, contractId);
            ResultSet resultSet = statement.executeQuery();
            tickets = parseTicketSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getAllTicketsForContract(" + contractId + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
        if (tickets == null || tickets.isEmpty()) {
            return Collections.emptyList();
        }
        return tickets;
    }

    /**
     * Get List of Tickets for specific User from ticket table
     *
     * @param user
     * @return List Contracts or empty List
     * @throws DBException
     */
    @Override
    public List<Ticket> getAllTicketsForUser(User user) throws DBException {
        List<Ticket> tickets;
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("ticket.getAllForUser"))) {
            statement.setString(1, user.getMail());
            ResultSet resultSet = statement.executeQuery();
            tickets = parseTicketSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getAllTicketsForUser(" + user + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
        if (tickets == null || tickets.isEmpty()) {
            return Collections.emptyList();
        }

        return tickets;
    }

    /**
     * Get List of Tickets for specific User to Specific Exhibition from ticket table
     *
     * @param user     entity
     * @param contract
     * @return List Contracts or empty List
     * @throws DBException
     */
    @Override
    public List<Ticket> getTicketForUserOnContract(User user, Contract contract)
            throws DBException {
        List<Ticket> tickets;
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("ticket.getTicketForUserOnContract"))) {
            statement.setInt(1, user.getId());
            statement.setInt(2, contract.getId());
            ResultSet resultSet = statement.executeQuery();
            tickets = parseTicketSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getTicketForUserOnContract(" + user
                    + ", " + contract + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (tickets == null || tickets.isEmpty()) {
            return Collections.emptyList();
        }

        return tickets;
    }

    /**
     * Insert Ticket in to ticket table
     *
     * @param ticket
     * @throws DBException
     */
    @Override
    public void insertTicket(Ticket ticket) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement
                (QUERIES.getString("ticket.create"), Statement.RETURN_GENERATED_KEYS)) {
            prepareStatementToInsert(statement, ticket);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            // get ticketId
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ticket.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When insertTicket(" + ticket + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Update data in ticket table
     *
     * @param ticket
     * @throws DBException
     */
    @Override
    public void updateTicket(Ticket ticket) throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("ticket.updateUser"))) {
            prepareStatementToUpdate(statement, ticket);
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When updateTicket(" + ticket + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Set true for is_confirmed column (set approved)
     *
     * @param idTicket
     * @throws DBException
     */
    @Override
    public void approveTicket(Integer idTicket) throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("ticket.approveTicket"))) {
            statement.setInt(1, idTicket);
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When approveTicket(" + idTicket + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Delete Ticket from ticket table
     *
     * @param ticketId
     * @throws DBException
     */
    @Override
    public void deleteTicket(Integer ticketId) throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("ticket.delete"))) {
            statement.setInt(1, ticketId);
            statement.executeUpdate();
            LOGGER.info("delete ticket id " + ticketId);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When deleteTicket(" + ticketId + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    private List<Ticket> parseTicketSet(ResultSet resultSet) throws DBException {
        List<Ticket> tickets = new ArrayList<>();

        try {
            while (resultSet.next()) {
                Ticket ticket = new Ticket.Builder()
                        .setId(resultSet.getInt(FIELD_ID))
                        .setDateToApply(resultSet.getDate(FIELD_DATE_TO_APPLY))
                        .setContractId(resultSet.getInt(FIELD_CONTRACT_ID))
                        .setDateTransaction(resultSet.getDate(FIELD_DATE_TRANSACTION))
                        .setUserMail(resultSet.getString(FIELD_USER_MAIL))
                        .setQuantity(resultSet.getInt(FIELD_QUANTITY))
                        .setUserId(resultSet.getInt(FIELD_USER_ID))
                        .setChecked(resultSet.getBoolean(FIELD_CHECKED))
                        .setAppruvedBy(resultSet.getInt(FIELD_APPROVED_BY))
                        .build();
                tickets.add(ticket);
            }
        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        return tickets;
    }

    private void prepareStatementToInsert(PreparedStatement statement, Ticket ticket) throws DBException {
        try {
            statement.setDate(1, ticket.getDateToApply(), java.util.Calendar.getInstance());
            statement.setInt(2, ticket.getContractId());
            statement.setDate(3, ticket.getDateTransaction(), java.util.Calendar.getInstance());
            statement.setString(4, ticket.getUserEMail());
            statement.setInt(5, ticket.getQuantity());
            statement.setInt(6, ticket.getUserId());
            statement.setBoolean(7, ticket.getHasChecked());
            statement.setInt(8, ticket.getApprovedById());
        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    private void prepareStatementToUpdate(PreparedStatement statement, Ticket ticket) throws DBException {
        try {
            statement.setDate(1, ticket.getDateToApply(), java.util.Calendar.getInstance());
            statement.setInt(2, ticket.getContractId());
            statement.setDate(3, ticket.getDateTransaction(), java.util.Calendar.getInstance());
            statement.setString(4, ticket.getUserEMail());
            statement.setInt(5, ticket.getQuantity());
            statement.setBoolean(6, ticket.getHasChecked());
            statement.setInt(7, ticket.getId());
        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }
}
