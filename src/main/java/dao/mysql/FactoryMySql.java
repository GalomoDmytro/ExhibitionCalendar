package dao.mysql;

import dao.interfaces.*;

import java.sql.Connection;

public class FactoryMySql implements FactoryDao {

    public FactoryMySql(){}

    @Override
    public UserDao createUser(Connection connection) {
        return new UserMySql(connection);
    }

    @Override
    public ExhibitionDao createExhibition(Connection connection) {
        return new ExhibitionMySql(connection);
    }

    @Override
    public ExhibitionCenterDao createExhibitionCenter(Connection connection) {
        return new ExhibitionCenterMySql(connection);
    }

    @Override
    public PhoneExhibitionCenterMySql createExhibitionCenterPhone(Connection connection) {
        return new PhoneExhibitionCenterMySql(connection);
    }

    @Override
    public ExhibitionContractDao createExhibitionContract(Connection connection) {
        return new ExhibitionContractMySql(connection);
    }

    @Override
    public TicketDao createTicket(Connection connection) {
        return new TicketMySql(connection);
    }

    @Override
    public RoleDao createRole(Connection connection) {
        return new RoleMySql(connection);
    }

    @Override
    public UserPhoneDao createUserPhones(Connection connection) {
        return new UserPhoneMySql(connection);
    }


}
