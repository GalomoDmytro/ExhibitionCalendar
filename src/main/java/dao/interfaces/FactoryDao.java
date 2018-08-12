package dao.interfaces;

import dao.mysql.PhoneExhibitionCenterMySql;

import java.sql.Connection;

public interface FactoryDao{

    UserDao createUser(Connection connection);

    ExhibitionDao createExhibition(Connection connection);

    ExhibitionCenterDao createExhibitionCenter(Connection connection);

    PhoneExhibitionCenterMySql createExhibitionCenterPhone(Connection connection);

    ExhibitionContractDao createExhibitionContract(Connection connection);

    TicketDao createTicket(Connection connection);

    RoleDao createRole(Connection connection);

    UserPhoneDao createUserPhones(Connection connection);

}
