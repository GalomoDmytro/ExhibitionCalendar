package dao.interfaces;

import entities.Contract;
import entities.Exhibition;
import entities.ExhibitionCenter;

import java.util.List;

public interface ExhibitionContractDao extends DBHelperDao {

    Contract getExhibitionContractById(Integer id);

    List<Contract> getAllContractsByExCenterWithExhibiton(ExhibitionCenter exhibitionCenter, Exhibition exhibition);

    List<Contract> getAllContractsForCenter(ExhibitionCenter exhibitionCenter);

    List<Contract> getAllContractsForExhibition(Exhibition exhibition);

    List<Contract> getAllContracts();

    Contract updateContract(Contract contract);

    Contract createContract(Contract contract);

    boolean deleteContract(Contract contract);


}
