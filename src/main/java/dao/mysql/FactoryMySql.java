package dao.mysql;

import dao.interfaces.*;

import java.sql.Connection;
import java.util.ResourceBundle;

/**
 * @author Dmytro Galomko
 */
public class FactoryMySql implements FactoryDao {

    public FactoryMySql() {
    }

    /**
     * Get the class responsible for working with 'user' table
     *
     * @param connection
     * @return UserMySql
     */
    @Override
    public UserDao createUser(Connection connection) {
        return new UserMySql(connection);
    }

    @Override
    public UserDao createUser(Connection connection, ResourceBundle resourceBundle) {
        return new UserMySql(connection, resourceBundle);
    }

    /**
     * Get the class responsible for working with 'exhibition' table
     *
     * @param connection
     * @return ExhibitionMySql
     */
    @Override
    public ExhibitionDao createExhibition(Connection connection) {
        return new ExhibitionMySql(connection);
    }

    @Override
    public ExhibitionDao createExhibition(Connection connection, ResourceBundle queries) {
        return new ExhibitionMySql(connection, queries);
    }

    /**
     * Get the class responsible for working with 'exhibition_center' table
     *
     * @param connection
     * @return ExhibitionCenterMySql
     */
    @Override
    public ExhibitionCenterDao createExhibitionCenter(Connection connection) {
        return new ExhibitionCenterMySql(connection);
    }

    @Override
    public ExhibitionCenterDao createExhibitionCenter(Connection connection, ResourceBundle resourceBundle) {
        return new ExhibitionCenterMySql(connection, resourceBundle);
    }

    /**
     * Get the class responsible for working with 'exhibition_center_phone' table
     *
     * @param connection
     * @return PhoneExhibitionCenterMySql
     */
    @Override
    public PhoneExhibitionCenterMySql createExhibitionCenterPhone(Connection connection) {
        return new PhoneExhibitionCenterMySql(connection);
    }

    @Override
    public PhoneExhibitionCenterMySql createExhibitionCenterPhone(Connection connection, ResourceBundle resourceBundle) {
        return new PhoneExhibitionCenterMySql(connection, resourceBundle);
    }

    /**
     * Get the class responsible for working with 'exhibition_contract' table
     *
     * @param connection
     * @return ExhibitionContractMySql
     */
    @Override
    public ExhibitionContractDao createExhibitionContract(Connection connection) {
        return new ExhibitionContractMySql(connection);
    }

    @Override
    public ExhibitionContractDao createExhibitionContract(Connection connection, ResourceBundle resourceBundle) {
        return new ExhibitionContractMySql(connection, resourceBundle);
    }

    /**
     * Get the class responsible for working with 'ticket' table
     *
     * @param connection
     * @return new TicketMySql
     */
    @Override
    public TicketDao createTicket(Connection connection) {
        return new TicketMySql(connection);
    }

    @Override
    public TicketDao createTicket(Connection connection, ResourceBundle resourceBundle) {
        return new TicketMySql(connection, resourceBundle);
    }

    /**
     * Get the class responsible for working with 'role' table
     *
     * @param connection
     * @return new RoleMySql
     */
    @Override
    public RoleDao createRole(Connection connection) {
        return new RoleMySql(connection);
    }

    @Override
    public RoleDao createRole(Connection connection, ResourceBundle resourceBundle) {
        return new RoleMySql(connection, resourceBundle);
    }

    /**
     * Get the class responsible for working with 'user_phone' table
     *
     * @param connection
     * @return UserPhoneMySql
     */
    @Override
    public UserPhoneDao createUserPhones(Connection connection) {
        return new UserPhoneMySql(connection);
    }

    @Override
    public UserPhoneDao createUserPhones(Connection connection, ResourceBundle resourceBundle) {
        return new UserPhoneMySql(connection, resourceBundle);
    }

    /**
     * Get the class responsible for working with 'description' table
     *
     * @param connection
     * @return new DescriptionMySql
     */
    @Override
    public DescriptionTableDao createDescriptionTable(Connection connection) {
        return new DescriptionMySql(connection);
    }

    @Override
    public DescriptionTableDao createDescriptionTable(Connection connection, ResourceBundle queries) {
        return new DescriptionMySql(connection, queries);
    }
}
