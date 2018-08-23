package dao.mysql;

import dao.interfaces.TicketDao;
import entities.Contract;
import entities.Ticket;
import entities.User;
import exceptions.DBException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class TicketMySql implements TicketDao {

    private final String FIELD_ID = "id";
    private final String FIELD_DATE_TO_APPLY = "date_to_apply";
    private final String FIELD_CONTRACT_ID = "contract_id";
    private final String FIELD_DATE_TRANSACTION = "date_transaction";
    private final String FIELD_USER_MAIL = "user_mail";

    private Connection connection;
    private static final ResourceBundle QUERIES = ResourceBundle.getBundle("QueriesMySql");

    TicketMySql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Ticket getTicketById(Integer id) throws DBException {
        List<Ticket> tickets;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("ticket.getById"))) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            tickets = parseTicketSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        if (tickets == null) {
            return null;
        } else {
            return tickets.get(0);
        }
    }

    @Override
    public List<Ticket> getAllTickets() throws DBException {
        List<Ticket> tickets;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("ticket.getAll"))) {
            ResultSet resultSet = statement.executeQuery();
            tickets = parseTicketSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        return tickets;
    }

    @Override
    public int getCountSoldTicketForDate(Date date, Integer id_contract) throws DBException {
        int countTickets = 0;

        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("ticket.countSoldTicket"))) {
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            countTickets = resultSet.getInt(1);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        return countTickets;
    }

    @Override
    public List<Ticket> getAllTicketsForDate(Date date) throws DBException {
        List<Ticket> tickets;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("ticket.getAllForDate"))) {
            statement.setString(1, date.toString());
            ResultSet resultSet = statement.executeQuery();
            tickets = parseTicketSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        return tickets;
    }

    @Override
    public List<Ticket> getAllTicketsForContract(Contract contract) throws DBException {
        List<Ticket> tickets;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("ticket.getAllForContract"))) {
            statement.setInt(1, contract.getId());
            ResultSet resultSet = statement.executeQuery();
            tickets = parseTicketSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        return tickets;
    }

    @Override
    public List<Ticket> getAllTicketsForUser(User user) throws DBException {
        List<Ticket> tickets;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("ticket.getAllForUser"))) {
            statement.setString(1, user.getMail());
            ResultSet resultSet = statement.executeQuery();
            tickets = parseTicketSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        return tickets;
    }

    @Override
    public List<Ticket> getTicketForUserOnContract(User user, Contract contract) throws DBException {
        List<Ticket> tickets;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("ticket.getTicketForUserOnContract"))) {
            statement.setInt(1, user.getId());
            statement.setInt(1, contract.getId());
            ResultSet resultSet = statement.executeQuery();
            tickets = parseTicketSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        return tickets;
    }

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
            throw new DBException(exception);
        }
    }

    @Override
    public void updateTicket(Ticket ticket) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("ticket.updateUser"))) {
            prepareStatementToUpdate(statement, ticket);
            ResultSet resultSet = statement.executeQuery();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    @Override
    public void deleteTicket(Ticket ticket) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("ticket.delete"))) {
            statement.setInt(1, ticket.getId());
            ResultSet resultSet = statement.executeQuery();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

    }

    private List<Ticket> parseTicketSet(ResultSet resultSet) throws DBException{
        List<Ticket> tickets = new ArrayList<>();

        try {
            while (resultSet.next()) {
                Ticket ticket = new Ticket.Builder()
                        .setId(resultSet.getInt(FIELD_ID))
                        .setDateToApply(resultSet.getDate(FIELD_DATE_TO_APPLY))
                        .setContractId(resultSet.getInt(FIELD_CONTRACT_ID))
                        .setDateTransaction(resultSet.getDate(FIELD_DATE_TRANSACTION))
                        .setUserMail(resultSet.getString(FIELD_USER_MAIL))
                        .build();
                tickets.add(ticket);
            }
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        return tickets;
    }

    private void prepareStatementToInsert(PreparedStatement statement, Ticket ticket) throws DBException {
        try {
            statement.setDate(1, ticket.getDateToApply());
            statement.setInt(2, ticket.getContractId());
            statement.setDate(3, ticket.getDateTransaction());
            statement.setString(4, ticket.getUserMail());
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    private void prepareStatementToUpdate(PreparedStatement statement, Ticket ticket) throws DBException {
        try {
            statement.setDate(1, ticket.getDateToApply());
            statement.setInt(2, ticket.getContractId());
            statement.setDate(3, ticket.getDateTransaction());
            statement.setString(4, ticket.getUserMail());
            statement.setInt(5, ticket.getId());
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }
}
