package dao.interfaces;

import dao.mysql.PhoneExhibitionCenterMySql;

import java.sql.Connection;
import java.util.ResourceBundle;

/**
 * Interface realisation implies
 * returning objects to work with specific
 * table from database
 */
public interface FactoryDao {

    UserDao createUser(Connection connection);
    UserDao createUser(Connection connection, ResourceBundle queries);

    ExhibitionDao createExhibition(Connection connection, ResourceBundle queries);

    ExhibitionDao createExhibition(Connection connection);

    ExhibitionCenterDao createExhibitionCenter(Connection connection);

    ExhibitionCenterDao createExhibitionCenter(Connection connection, ResourceBundle resourceBundle);

    PhoneExhibitionCenterMySql createExhibitionCenterPhone(Connection connection);

    PhoneExhibitionCenterMySql createExhibitionCenterPhone(Connection connection, ResourceBundle resourceBundle);

    ExhibitionContractDao createExhibitionContract(Connection connection);

    ExhibitionContractDao createExhibitionContract(Connection connection, ResourceBundle resourceBundle);

    TicketDao createTicket(Connection connection);

    TicketDao createTicket(Connection connection, ResourceBundle resourceBundle);

    RoleDao createRole(Connection connection);

    RoleDao createRole(Connection connection, ResourceBundle resourceBundle);

    UserPhoneDao createUserPhones(Connection connection);

    UserPhoneDao createUserPhones(Connection connection, ResourceBundle resourceBundle);

    DescriptionTableDao createDescriptionTable(Connection connection);

    DescriptionTableDao createDescriptionTable(Connection connection, ResourceBundle resourceBundle);

}
