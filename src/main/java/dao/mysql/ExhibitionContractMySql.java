package dao.mysql;

import dao.interfaces.ExhibitionContractDao;
import entities.Contract;
import entities.Exhibition;
import entities.ExhibitionCenter;
import exceptions.DBException;
import org.apache.log4j.Logger;

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
    private final String FIELD_MAX_TICKET_PER_DAY = "max_ticket_per_day";

    private Connection connection;
    private static final ResourceBundle QUERIES = ResourceBundle.getBundle("QueriesMySql");

    private static final Logger LOGGER = Logger.getLogger(ExhibitionContractMySql.class);

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
    public List<Contract> getAllContractsByExCenterWithExhibition(ExhibitionCenter exhibitionCenter,
                                                                  Exhibition exhibition) throws DBException {
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
    public void prepareCEC(Contract contract, Exhibition exhibition, ExhibitionCenter exhibitionCenter,
                           Integer idContract) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(
                QUERIES.getString("contract.getContractCenterExhibition"))) {
            statement.setInt(1, idContract);
            ResultSet resultSet = statement.executeQuery();
            fillCEC(resultSet, contract, exhibition, exhibitionCenter);
        } catch (SQLException exception) {
            throw new DBException(exception);
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
            contracts = parseContractSetWithExpo(resultSet);
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
    public List<Contract> galAllContactsWithExpoAndCenter(Date date) throws DBException {
        List<Contract> contracts;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("contract.getAllAfterDateWithExpoCenterAndExhibition"))) {
            statement.setDate(1, date);
            ResultSet resultSet = statement.executeQuery();
            contracts = parseContractSetWithExpo(resultSet);
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
    public List<Contract> searchContactsWithExpoAndCenter(String search, Date date) throws DBException {
        List<Contract> contracts;
        search = "%" + search + "%";
        try (PreparedStatement statement = connection.prepareStatement(QUERIES
                .getString("contract.searchAfterDateWithExpoCenterAndExhibition"))) {
            statement.setDate(1, date);
            statement.setString(2, search);
            statement.setString(3, search);
            statement.setString(4, search);
            ResultSet resultSet = statement.executeQuery();
            contracts = parseContractSetWithExpo(resultSet);
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
    public List<Contract> getAllAfterDate(Date date) throws DBException {
        List<Contract> contracts;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("contract.getAllAfterDate"))) {
            statement.setDate(1, date);
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
    public List<Contract> getAllContractsBySearch(String search) throws DBException {
        search = "%" + search + "%";
        List<Contract> contracts;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("contract.search"))) {
            statement.setString(1, search);
            statement.setString(2, search);
            statement.setString(3, search);
            statement.setString(4, search);
            statement.setString(5, search);
            statement.setString(6, search);
            statement.setString(7, search);
            statement.setString(8, search);
            ResultSet resultSet = statement.executeQuery();
            contracts = parseContractSetWithExpo(resultSet);
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
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("contract.updateContract"))) {
            prepareStatementToUpdate(statement, contract);
            statement.executeUpdate();
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
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    @Override
    public void deleteContractById(Integer id) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("contract.deleteContract"))) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }

    @Override
    public int getNumberOfContractsAfterDate(Date date) throws DBException {
        int count = 0;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("contract.getAllAfterDateWithExpoCenterAndExhibitionCount"))) {
            statement.setDate(1, date);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        return count;
    }

    @Override
    public List<Contract> getContractsAfterDateLimit(Date date, int startLimit, int endLimit) throws DBException {
        List<Contract> contracts;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES.getString("contract.getAllAfterDateWithExpoCenterAndExhibitionLimit"))) {
            statement.setDate(1, date);
            statement.setInt(2, startLimit);
            statement.setInt(3, endLimit);
            ResultSet resultSet = statement.executeQuery();
            contracts = parseContractSetWithExpo(resultSet);
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        if (contracts == null) {
            return null;
        } else {
            return contracts;
        }
    }

    private void fillCEC(ResultSet resultSet, Contract contract, Exhibition exhibition,
                         ExhibitionCenter exhibitionCenter) throws DBException {
        try {
            while (resultSet.next()) {
                contract.setId(resultSet.getInt(1));
                contract.setExhibitionId(resultSet.getInt(2));
                contract.setExCenterId(resultSet.getInt(3));
                contract.setDateFrom(resultSet.getDate(4));
                contract.setDateTo(resultSet.getDate(5));
                contract.setTicketPrice(resultSet.getBigDecimal(6));
                contract.setWorkTime(resultSet.getString(7));
                contract.setMaxTicketPerDay(resultSet.getInt(8));

                exhibition.setId(resultSet.getInt(9));
                exhibition.setTitle(resultSet.getString(10));
                exhibition.setImgSrc(resultSet.getString(11));

                exhibitionCenter.setId(resultSet.getInt(12));
                exhibitionCenter.setTitle(resultSet.getString(13));
                exhibitionCenter.setAddress(resultSet.getString(14));
                exhibitionCenter.seteMail(resultSet.getString(15));
                exhibitionCenter.setWebPage(resultSet.getString(16));
            }
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

    private List<Contract> parseContractSetWithExpo(ResultSet resultSet) throws DBException {
        List<Contract> contracts = new ArrayList<>();

        try {
            while (resultSet.next()) {
                Contract contract = new Contract.Builder()
                        .setId(resultSet.getInt(FIELD_ID))
                        .setExhibitionId(resultSet.getInt(FIELD_EXHIBITION_ID))
                        .setExCenterId(resultSet.getInt(FIELD_CENTER_ID))
                        .setDateFrom(resultSet.getDate(FIELD_DATE_FROM))
                        .setDateTo(resultSet.getDate(FIELD_DATE_TO))
                        .setTicketPrice(resultSet.getBigDecimal(FIELD_TICKET_PRICE))
                        .setWorkTime(resultSet.getString(FIELD_WORK_TIME_EXHIBITION))
                        .setMaxTicketPerDay(resultSet.getInt(FIELD_MAX_TICKET_PER_DAY))
                        .setExhibitionTitle(resultSet.getString(10))
                        .setExhibitionCenterTitle(resultSet.getString(13))
                        .build();
                contracts.add(contract);
            }
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        return contracts;
    }

    private void prepareStatementToInsert(PreparedStatement statement, Contract contract) throws DBException {
        try {
            statement.setInt(1, contract.getExhibitionId());
            statement.setInt(2, contract.getExCenterId());
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
            statement.setInt(2, contract.getExCenterId());
            statement.setDate(3, contract.getDateFrom());
            statement.setDate(4, contract.getDateTo());
            statement.setBigDecimal(5, contract.getTicketPrice());
            statement.setString(6, contract.getWorkTime());
            statement.setInt(7, contract.getMaxTicketPerDay());
            statement.setInt(8, contract.getId());
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }
}
