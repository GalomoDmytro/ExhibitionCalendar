package dao.mysql;

import dao.interfaces.ExhibitionContractDao;
import entities.Contract;
import entities.Exhibition;
import entities.ExhibitionCenter;
import exceptions.DBException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ExhibitionContractMySql implements ExhibitionContractDao {

    private final String FIELD_ID = "id";
    private final String FIELD_EXHIBITION_ID = "exhibition_id";
    private final String FIELD_CENTER_ID = "exhibition_center_id";
    private final String FIELD_DATE_FROM = "date_from";
    private final String FIELD_DATE_TO = "date_to";
    private final String FIELD_TICKET_PRICE = "ticket_price";
    private final String FIELD_WORK_TIME_EXHIBITION = "work_time_exhibition";
    private final String FIELD_MAX_TICKET_PER_DAY = "max_ticket";

    private Connection connection;
    private static final ResourceBundle QUERIES = ResourceBundle.getBundle("QueriesMySql");

    ExhibitionContractMySql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Contract getExhibitionContractById(Integer id) throws DBException {
        List<Contract> contracts;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("contract.getById"))) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            contracts = parseContractSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        if (contracts == null) {
            return null;
        } else {
            return contracts.get(0);
        }
    }

    @Override
    public List<Contract> getAllContractsByExCenterWithExhibiton(ExhibitionCenter exhibitionCenter, Exhibition exhibition) throws DBException {
        List<Contract> contracts;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("contract.getByCenterAndExhibition"))) {
            statement.setInt(1, exhibitionCenter.getId());
            statement.setInt(2, exhibition.getId());
            ResultSet resultSet = statement.executeQuery();
            contracts = parseContractSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        if (contracts == null) {
            return null;
        } else {
            return contracts;
        }
    }

    @Override
    public List<Contract> getAllContractsForCenter(ExhibitionCenter exhibitionCenter) throws DBException {
        List<Contract> contracts;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("contract.getAllForExhibitionCenter"))) {
            statement.setInt(1, exhibitionCenter.getId());
            ResultSet resultSet = statement.executeQuery();
            contracts = parseContractSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        if (contracts == null) {
            return null;
        } else {
            return contracts;
        }
    }

    @Override
    public List<Contract> getAllContractsForExhibition(Exhibition exhibition) throws DBException {
        List<Contract> contracts;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("contract.getAllForExhibition"))) {
            statement.setInt(1, exhibition.getId());
            ResultSet resultSet = statement.executeQuery();
            contracts = parseContractSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        if (contracts == null) {
            return null;
        } else {
            return contracts;
        }
    }

    @Override
    public List<Contract> getAllContracts() throws DBException {
        List<Contract> contracts;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("contract.getAll"))) {
            ResultSet resultSet = statement.executeQuery();
            contracts = parseContractSet(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        if (contracts == null) {
            return null;
        } else {
            return contracts;
        }
    }

    @Override
    public void updateContract(Contract contract) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("ticket.updateUser"))) {
            prepareStatementToUpdate(statement, contract);
            statement.executeQuery();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    @Override
    public void insertContract(Contract contract) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement
                (QUERIES.getString("contract.insertContract"), Statement.RETURN_GENERATED_KEYS)) {
            prepareStatementToInsert(statement, contract);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            // get contractId
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contract.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    @Override
    public void deleteContract(Contract contract) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("contract.deleteContract"))) {
            statement.setInt(1, contract.getId());
            statement.executeQuery();
            statement.executeQuery();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    private List<Contract> parseContractSet(ResultSet resultSet) throws DBException {
        List<Contract> contracts = new ArrayList<>();

        try {
            while (resultSet.next()) {
                Contract ticket = new Contract.Builder()
                        .setId(resultSet.getInt(FIELD_ID))
                        .setExhibitionId(resultSet.getInt(FIELD_EXHIBITION_ID))
                        .setExCenterId(resultSet.getInt(FIELD_CENTER_ID))
                        .setDateFrom(resultSet.getDate(FIELD_DATE_FROM))
                        .setDateTo(resultSet.getDate(FIELD_DATE_TO))
                        .setTicketPrice(resultSet.getBigDecimal(FIELD_TICKET_PRICE))
                        .setWorkTime(resultSet.getString(FIELD_WORK_TIME_EXHIBITION))
                        .setMaxTicketPerDay(resultSet.getInt(FIELD_MAX_TICKET_PER_DAY))
                        .build();

                contracts.add(ticket);
            }
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        return contracts;
    }

    private void prepareStatementToInsert(PreparedStatement statement, Contract contract) throws DBException {
        try {
            statement.setInt(1, contract.getExhibitionId());
            statement.setInt(2, contract.getExhibitionId());
            statement.setDate(3, contract.getDateFrom());
            statement.setDate(4, contract.getDateTo());
            statement.setBigDecimal(5, contract.getTicketPrice());
            statement.setString(6, contract.getWorkTime());
            statement.setInt(7, contract.getMaxTicketPerDay());
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    private void prepareStatementToUpdate(PreparedStatement statement, Contract contract) throws DBException {
        try {
            statement.setInt(1, contract.getExhibitionId());
            statement.setInt(2, contract.getExhibitionId());
            statement.setDate(3, contract.getDateFrom());
            statement.setDate(4, contract.getDateTo());
            statement.setBigDecimal(5, contract.getTicketPrice());
            statement.setString(6, contract.getWorkTime());
            statement.setInt(7, contract.getMaxTicketPerDay());
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }
}
