package dao.interfaces;

import entities.Contract;
import entities.Exhibition;
import entities.ExhibitionCenter;
import exceptions.DBException;

import java.util.List;

public interface ExhibitionContractDao {

    Contract getExhibitionContractById(Integer id) throws DBException;

    List<Contract> getAllContractsByExCenterWithExhibiton(ExhibitionCenter exhibitionCenter, Exhibition exhibition) throws DBException;

    List<Contract> getAllContractsForCenter(ExhibitionCenter exhibitionCenter) throws DBException;

    List<Contract> getAllContractsForExhibition(Exhibition exhibition) throws DBException;

    List<Contract> getAllContracts() throws DBException;

    Contract updateContract(Contract contract) throws DBException;

    Contract createContract(Contract contract) throws DBException;

    boolean deleteContract(Contract contract) throws DBException;


}
