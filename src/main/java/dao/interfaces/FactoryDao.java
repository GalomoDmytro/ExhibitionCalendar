package dao.interfaces;

public interface FactoryDao extends DBHelperDao {

    UserDao createUser();

    ExhibitionDao createExhibition();

    ExhibitionCenterDao createExhibitionCenter();

    ExhibitionContractDao createExhibitionContract();

    TicketDao createTicket();

}
