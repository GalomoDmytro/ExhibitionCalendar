package dao.mysql;

import dao.interfaces.*;

import java.sql.Connection;
import java.util.ResourceBundle;

public class FactoryMySql implements FactoryDao {

    public FactoryMySql(){}

    @Override
    public UserDao createUser(Connection connection) {
        return new UserMySql(connection);
    }
    public UserDao createUser(Connection connection, ResourceBundle resourceBundle) {
        return new UserMySql(connection, resourceBundle);
    }

    @Override
    public ExhibitionDao createExhibition(Connection connection) {
        return new ExhibitionMySql(connection);
    }
    @Override
    public ExhibitionDao createExhibition(Connection connection, ResourceBundle queries) {
        return new ExhibitionMySql(connection, queries);
    }

    @Override
    public ExhibitionCenterDao createExhibitionCenter(Connection connection) {
        return new ExhibitionCenterMySql(connection);
    }
    @Override
    public ExhibitionCenterDao createExhibitionCenter(Connection connection, ResourceBundle resourceBundle) {
        return new ExhibitionCenterMySql(connection, resourceBundle);
    }

    @Override
    public PhoneExhibitionCenterMySql createExhibitionCenterPhone(Connection connection) {
        return new PhoneExhibitionCenterMySql(connection);
    }
    @Override
    public PhoneExhibitionCenterMySql createExhibitionCenterPhone(Connection connection, ResourceBundle resourceBundle) {
        return new PhoneExhibitionCenterMySql(connection, resourceBundle);
    }

    @Override
    public ExhibitionContractDao createExhibitionContract(Connection connection) {
        return new ExhibitionContractMySql(connection);
    }
    @Override
    public ExhibitionContractDao createExhibitionContract(Connection connection, ResourceBundle resourceBundle) {
        return new ExhibitionContractMySql(connection, resourceBundle);
    }

    @Override
    public TicketDao createTicket(Connection connection) {
        return new TicketMySql(connection);
    }
    @Override
    public TicketDao createTicket(Connection connection, ResourceBundle resourceBundle) {
        return new TicketMySql(connection, resourceBundle);
    }

    @Override
    public RoleDao createRole(Connection connection) {
        return new RoleMySql(connection);
    }
    @Override
    public RoleDao createRole(Connection connection, ResourceBundle resourceBundle) {
        return new RoleMySql(connection, resourceBundle);
    }

    @Override
    public UserPhoneDao createUserPhones(Connection connection) {
        return new UserPhoneMySql(connection);
    }
    @Override
    public UserPhoneDao createUserPhones(Connection connection, ResourceBundle resourceBundle) {
        return new UserPhoneMySql(connection, resourceBundle);
    }

    @Override
    public DescriptionTableDao createDescriptionTable(Connection connection) {
        return new DescriptionMySql(connection);
    }
    @Override
    public DescriptionTableDao createDescriptionTable(Connection connection, ResourceBundle queries) {
        return new DescriptionMySql(connection, queries);
    }
}
