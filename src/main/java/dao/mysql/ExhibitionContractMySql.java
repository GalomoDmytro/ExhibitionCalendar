package dao.mysql;

import dao.interfaces.ExhibitionContractDao;
import entities.Contract;
import entities.Exhibition;
import entities.ExhibitionCenter;
import exceptions.DBException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class ExhibitionContractMySql implements ExhibitionContractDao {

    private final Connection connection;
    private final ResourceBundle QUERIES;
    private static final String FIELD_ID = "id";
    private static final String FIELD_EXHIBITION_ID = "exhibition_id";
    private static final String FIELD_CENTER_ID = "exhibition_center_id";
    private static final String FIELD_DATE_FROM = "date_from";
    private static final String FIELD_DATE_TO = "date_to";
    private static final String FIELD_TICKET_PRICE = "ticket_price";
    private static final String FIELD_WORK_TIME_EXHIBITION = "work_time_exhibition";
    private static final String FIELD_MAX_TICKET_PER_DAY = "max_ticket_per_day";
    private static final Logger LOGGER = Logger.getLogger(ExhibitionContractMySql.class);

    ExhibitionContractMySql(Connection connection) {
        this.QUERIES = ResourceBundle.getBundle("QueriesMySql");
        this.connection = connection;
    }

    ExhibitionContractMySql(Connection connection, ResourceBundle resourceBundle) {
        this.QUERIES = resourceBundle;
        this.connection = connection;
    }

    /**
     * Enables client sessions to acquire exhibition_contract table locks
     * explicitly for the purpose of cooperating with other sessions
     * for access to tables, or to prevent other
     * sessions from modifying exhibition_contract tables during periods when
     * a session requires exclusive access to them
     *
     * @throws DBException
     */
    @Override
    public void setLockContractTable() throws DBException {
        try (PreparedStatement statement = connection
                .prepareStatement("LOCK TABLE exhibition_contract WRITE")) {
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
        try (PreparedStatement statement = connection.prepareStatement("UNLOCK TABLES")) {
            statement.execute();

        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Get ExhibitionContract entity from exhibition_contract table
     *
     * @param id of ExhibitionContract
     * @return ExhibitionContract entity
     * or will return ExhibitionContract().emptyContract) if have not matches
     * with looking exhibition_contract id
     * @throws DBException
     */
    @Override
    public Contract getExhibitionContractById(Integer id) throws DBException {
        List<Contract> contracts;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES
                .getString("contract.getById"))) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            contracts = parseContractSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getExhibitionContractById(" + id + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (contracts == null || contracts.isEmpty()) {
            return new Contract().emptyContract();
        } else {
            return contracts.get(0);
        }
    }

    /**
     * Get List of all Contracts entities from exhibition_contract table, witch
     * depend to specific exhibition and exhibition center
     *
     * @param exhibitionCenter entity
     * @param exhibition       entity
     * @return List of Contract entities witch bind to exhibitionCenter and exhibition
     * or return Collections.emptyList() if found nothing
     * @throws DBException
     */
    @Override
    public List<Contract> getAllContractsByExCenterWithExhibition(ExhibitionCenter exhibitionCenter,
                                                                  Exhibition exhibition)
            throws DBException {
        List<Contract> contracts;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES
                .getString("contract.getByCenterAndExhibition"))) {
            statement.setInt(1, exhibitionCenter.getId());
            statement.setInt(2, exhibition.getId());
            ResultSet resultSet = statement.executeQuery();
            contracts = parseContractSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getAllContractsByExCenterWithExhibition(" +
                    exhibitionCenter + ", " + exhibition + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (contracts == null || contracts.isEmpty()) {
            return Collections.emptyList();
        } else {
            return contracts;
        }
    }

    /**
     * Set value to Contract, Exhibition, ExhibitionCenter params witch
     * related to specific Contact
     *
     * @param contract         entity
     * @param exhibition       entity
     * @param exhibitionCenter entity
     * @param idContract       id looking Exhibition Contract
     * @throws DBException
     */
    @Override
    public void prepareCEC(Contract contract, Exhibition exhibition,
                           ExhibitionCenter exhibitionCenter,
                           Integer idContract) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(
                QUERIES.getString("contract.getContractCenterExhibition"))) {
            statement.setInt(1, idContract);
            ResultSet resultSet = statement.executeQuery();
            fillCEC(resultSet, contract, exhibition, exhibitionCenter);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When prepareCEC(" +
                    contract + ", " + exhibition + ", " + exhibitionCenter +
                    ", " + idContract + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Get List of all Contracts entities from exhibition_contract
     * witch bind to specific Exhibition Center
     *
     * @param idExCenter id Exhibition Center
     * @return List of Contract entities witch bind to Exhibition Center
     * or return Collections.emptyList() if found nothing
     * @throws DBException
     */
    @Override
    public List<Contract> getAllContractsForCenter(Integer idExCenter) throws DBException {
        List<Contract> contracts;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES
                .getString("contract.getAllForExhibitionCenter"))) {
            statement.setInt(1, idExCenter);
            ResultSet resultSet = statement.executeQuery();
            contracts = parseContractSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getAllContractsForCenter(" +
                    idExCenter + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (contracts == null || contracts.isEmpty()) {
            return Collections.emptyList();
        } else {
            return contracts;
        }
    }

    /**
     * Get List of all Contracts entities from exhibition_contract
     * witch bind to specific Exhibition
     *
     * @param idExhibition id Exhibition
     * @return List of Contract entities witch bind to Exhibition
     * or return Collections.emptyList() if found nothing
     * @throws DBException
     */
    @Override
    public List<Contract> getAllContractsForExhibition(Integer idExhibition) throws DBException {
        List<Contract> contracts;
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("contract.getAllForExhibition"))) {
            statement.setInt(1, idExhibition);
            ResultSet resultSet = statement.executeQuery();
            contracts = parseContractSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getAllContractsForExhibition(" +
                    idExhibition + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (contracts == null || contracts.isEmpty()) {
            return Collections.emptyList();
        } else {
            return contracts;
        }
    }

    /**
     * Get all contacts from exhibition_contract table, witch data similar to search request
     *
     * @return List of Contract entities
     * or return Collections.emptyList() if table is empty
     * @throws DBException
     */
    @Override
    public List<Contract> getAllContracts() throws DBException {
        List<Contract> contracts;
        try (PreparedStatement statement = connection
                .prepareStatement(QUERIES.getString("contract.getAll"))) {
            ResultSet resultSet = statement.executeQuery();
            contracts = parseContractSetWithExpo(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getAllContracts();");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (contracts == null || contracts.isEmpty()) {
            return Collections.emptyList();
        } else {
            return contracts;
        }
    }

    /**
     * Get List of all Contracts from exhibition_contract
     * witch 'date start' or 'date end' in looking range
     *
     * @param date sql.Date - will search for a contract includes this date
     * @return List of Contract entities with Exhibition and ExhibitionContract title
     * or return Collections.emptyList() if found nothing
     * @throws DBException
     */
    @Override
    public List<Contract> getAllContractAfterDateWithExpoCenterAndExhibition(java.sql.Date date)
            throws DBException {
        List<Contract> contracts;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES
                .getString("contract.getAllAfterDateWithExpoCenterAndExhibition"))) {
            statement.setDate(1, date, java.util.Calendar.getInstance());
            statement.setDate(2, date, java.util.Calendar.getInstance());
            ResultSet resultSet = statement.executeQuery();
            contracts = parseContractSetWithExpo(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception." +
                    " When getAllContractAfterDateWithExpoCenterAndExhibition(" +
                    date + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (contracts == null || contracts.isEmpty()) {
            return Collections.emptyList();
        } else {
            return contracts;
        }
    }

    /**
     * Get List of all Contracts from exhibition_contract
     * witch 'date start' or 'date end' in looking range
     * and 'line search' matches to date or address in related
     * exhibition_center or exhibition tables
     *
     * @param date   sql.Date - will search for a contract includes this date
     * @param search line - will search for a exhibition title or exhibition center title
     *               or address matches to this 'search' line
     * @return List of Contract entities with Exhibition and ExhibitionContract title
     * or return Collections.emptyList() if found nothing
     * @throws DBException
     */
    @Override
    public List<Contract> searchAfterDateWithExpoCenterAndExhibition(String search, Date date)
            throws DBException {
        List<Contract> contracts;
        search = "%" + search + "%";
        try (PreparedStatement statement = connection.prepareStatement(QUERIES
                .getString("contract.searchAfterDateWithExpoCenterAndExhibition"))) {
            statement.setDate(1, date, java.util.Calendar.getInstance());
            statement.setDate(2, date, java.util.Calendar.getInstance());
            statement.setString(3, search);
            statement.setString(4, search);
            statement.setString(5, search);
            ResultSet resultSet = statement.executeQuery();
            contracts = parseContractSetWithExpo(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When searchAfterDateWithExpoCenterAndExhibition(" +
                    search + ", " + date + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (contracts == null || contracts.isEmpty()) {
            return Collections.emptyList();
        } else {
            return contracts;
        }
    }

    /**
     * Get List of all Contracts from exhibition_contract
     * witch 'date start' or 'date end' in looking range
     * and 'line search' matches to date or address in related
     * exhibition_center or exhibition tables
     * with using limit table on result
     *
     * @param date       sql.Date - will search for a contract includes this date
     * @param search     line - will search for a exhibition title or exhibition center title
     *                   or address matches to this 'search' line
     * @param startLimit specifies the offset of the first row to return
     * @param endLimit   specifies the maximum number of rows to return
     * @return List of Contract entities with Exhibition and ExhibitionContract title
     * or return Collections.emptyList() if found nothing
     * @throws DBException
     */
    @Override
    public List<Contract> searchContactsWithExpoAndCenterLimit(String search,
                                                               Date date, int startLimit,
                                                               int endLimit) throws DBException {
        List<Contract> contracts;
        search = "%" + search + "%";
        try (PreparedStatement statement = connection.prepareStatement(QUERIES
                .getString("contract.searchAfterDateWithExpoCenterAndExhibitionLimit"))) {
            prepareStatementForSearchWithExpoCenterAndExhibition(search, date, startLimit,
                    endLimit, statement);
            ResultSet resultSet = statement.executeQuery();
            contracts = parseContractSetWithExpo(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When searchContactsWithExpoAndCenterLimit(" +
                    search + ", " + date + ", " + startLimit + ", " + endLimit + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (contracts == null || contracts.isEmpty()) {
            return Collections.emptyList();
        } else {
            return contracts;
        }
    }

    public void prepareStatementForSearchWithExpoCenterAndExhibition
            (String search, Date date, int startLimit, int endLimit,
             PreparedStatement statement)
            throws SQLException {
        statement.setDate(1, date, java.util.Calendar.getInstance());
        statement.setString(2, search);
        statement.setString(3, search);
        statement.setString(4, search);
        statement.setInt(5, startLimit);
        statement.setInt(6, endLimit);
    }

    /**
     * Get List of all Contracts from exhibition_contract table,
     * witch 'date start' or 'date end' in looking range
     *
     * @param date sql.Date - will search for all contracts includes this date
     * @return List of Contract entities
     * or return Collections.emptyList() if found nothing
     * @throws DBException
     */
    @Override
    public List<Contract> getAllAfterDate(Date date) throws DBException {
        List<Contract> contracts;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES
                .getString("contract.getAllAfterDate"))) {
            statement.setDate(1, date, java.util.Calendar.getInstance());
            statement.setDate(2, date, java.util.Calendar.getInstance());
            ResultSet resultSet = statement.executeQuery();
            contracts = parseContractSet(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getAllAfterDate(" +
                    date + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (contracts == null || contracts.isEmpty()) {
            return Collections.emptyList();
        } else {
            return contracts;
        }
    }

    /**
     * Get all contacts from exhibition_contract table,
     * witch data similar to search request
     *
     * @param search - will look data similar to this line
     * @return List of Contract entities
     * or return Collections.emptyList() if found nothing
     * @throws DBException
     */
    @Override
    public List<Contract> getAllContractsBySearch(String search) throws DBException {
        search = "%" + search + "%";
        List<Contract> contracts;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES
                .getString("contract.search"))) {
            prepareStatementForSearchAll(search, statement);
            ResultSet resultSet = statement.executeQuery();
            contracts = parseContractSetWithExpo(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getAllContractsBySearch(" +
                    search + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (contracts == null || contracts.isEmpty()) {
            return Collections.emptyList();
        } else {
            return contracts;
        }
    }

    public void prepareStatementForSearchAll(String search, PreparedStatement statement)
            throws SQLException {
        statement.setString(1, search);
        statement.setString(2, search);
        statement.setString(3, search);
        statement.setString(4, search);
        statement.setString(5, search);
        statement.setString(6, search);
        statement.setString(7, search);
        statement.setString(8, search);
    }

    /**
     * Update exhibition_contract table
     *
     * @param contract entity to update
     * @throws DBException
     */
    @Override
    public void updateContract(Contract contract) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES
                .getString("contract.updateContract"))) {
            prepareStatementToUpdate(statement, contract);
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When updateContract(" +
                    contract + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Insert in exhibition_contract table new contract entity
     *
     * @param contract entity to insert
     * @throws DBException
     */
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
            LOGGER.info("Catch exception. When insertContract(" +
                    contract + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Delete contract from exhibition_contract table
     *
     * @param contract entity to delete from table
     * @throws DBException
     */
    @Override
    public void deleteContract(Contract contract) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES
                .getString("contract.deleteContract"))) {
            statement.setInt(1, contract.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When deleteContract(" +
                    contract + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Delete contract from exhibition_contract table
     *
     * @param id contract to delete
     * @throws DBException
     */
    @Override
    public void deleteContractById(Integer id) throws DBException {
        try (PreparedStatement statement = connection.prepareStatement(QUERIES
                .getString("contract.deleteContract"))) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When deleteContractById(" +
                    id + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    /**
     * Get quantity of contracts witch include specific date
     * from exhibition_contract table
     *
     * @param date sql.Date - will search contracts witch includes this date
     * @return int quantity of contracts for this date
     * @throws DBException
     */
    @Override
    public int getNumberOfContractsAfterDate(Date date) throws DBException {
        int count;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES
                .getString("contract.getAllAfterDateWithExpoCenterAndExhibitionCount"))) {
            statement.setDate(1, date, java.util.Calendar.getInstance());
            statement.setDate(2, date, java.util.Calendar.getInstance());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getNumberOfContractsAfterDate(" +
                    date + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        return count;
    }

    /**
     * Get List of Contract entities witch include specific date
     * with limited result
     * from exhibition_contract table
     *
     * @param date       sql.Date - will search contracts witch includes this date
     * @param startLimit specifies the offset of the first row to return
     * @param endLimit   specifies the maximum number of rows to return
     * @return List of Contract entities or emptyList
     * @throws DBException
     */
    @Override
    public List<Contract> getContractsAfterDateLimit(Date date, int startLimit,
                                                     int endLimit) throws DBException {
        List<Contract> contracts;
        try (PreparedStatement statement = connection.prepareStatement(QUERIES
                .getString("contract.getAllAfterDateWithExpoCenterAndExhibitionLimit"))) {
            statement.setDate(1, date, java.util.Calendar.getInstance());
            statement.setDate(2, date, java.util.Calendar.getInstance());
            statement.setInt(3, startLimit);
            statement.setInt(4, endLimit);
            ResultSet resultSet = statement.executeQuery();
            contracts = parseContractSetWithExpo(resultSet);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getContractsAfterDateLimit(" +
                    date + ", " + startLimit + ", " + endLimit + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        if (contracts == null || contracts.isEmpty()) {
            return Collections.emptyList();
        } else {
            return contracts;
        }
    }

    /**
     * Get quantity of contracts witch include specific date
     * and include field matches to looking string
     * from exhibition_contract table
     *
     * @param date   sql.Date - will search contracts witch includes this date
     * @param search search contracts witch includes field whit matches this line
     * @return quantity of contracts for this date
     * @throws DBException
     */
    @Override
    public int getNumberOfContractsAfterSearch(String search, Date date) throws DBException {
        int count = 0;
        search = "%" + search + "%";
        try (PreparedStatement statement = connection.prepareStatement(QUERIES
                .getString("contract.searchAfterDateWithExpoCenterAndExhibitionCount"))) {
            statement.setDate(1, date, java.util.Calendar.getInstance());
            statement.setDate(2, date, java.util.Calendar.getInstance());
            statement.setString(3, search);
            statement.setString(4, search);
            statement.setString(5, search);

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
        } catch (SQLException exception) {
            LOGGER.info("Catch exception. When getNumberOfContractsAfterSearch(" +
                    search + ", " + date + ");");
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        return count;
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
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    private List<Contract> parseContractSet(ResultSet resultSet) throws DBException {
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
                        .build();

                contracts.add(contract);
            }
        } catch (SQLException exception) {
            LOGGER.error(exception);
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
            LOGGER.error(exception);
            throw new DBException(exception);
        }

        return contracts;
    }

    private void prepareStatementToInsert(PreparedStatement statement, Contract contract)
            throws DBException {
        try {
            statement.setInt(1, contract.getExhibitionId());
            statement.setInt(2, contract.getExCenterId());
            statement.setDate(3, contract.getDateFrom(), java.util.Calendar.getInstance());
            statement.setDate(4, contract.getDateTo(), java.util.Calendar.getInstance());
            statement.setBigDecimal(5, contract.getTicketPrice());
            statement.setString(6, contract.getWorkTime());
            statement.setInt(7, contract.getMaxTicketPerDay());
        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    private void prepareStatementToUpdate(PreparedStatement statement, Contract contract)
            throws DBException {
        try {
            statement.setInt(1, contract.getExhibitionId());
            statement.setInt(2, contract.getExCenterId());
            statement.setDate(3, contract.getDateFrom(), java.util.Calendar.getInstance());
            statement.setDate(4, contract.getDateTo(), java.util.Calendar.getInstance());
            statement.setBigDecimal(5, contract.getTicketPrice());
            statement.setString(6, contract.getWorkTime());
            statement.setInt(7, contract.getMaxTicketPerDay());
            statement.setInt(8, contract.getId());
        } catch (SQLException exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }
}
