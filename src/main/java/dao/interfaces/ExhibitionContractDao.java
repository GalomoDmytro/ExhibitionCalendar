package dao.interfaces;

import entities.Contract;
import entities.Exhibition;
import entities.ExhibitionCenter;
import exceptions.DBException;

import java.util.List;

public interface ExhibitionContractDao {

    Contract getExhibitionContractById(Integer id) throws DBException;

    List<Contract> getAllContractsByExCenterWithExhibition(ExhibitionCenter exhibitionCenter, Exhibition exhibition) throws DBException;

    List<Contract> getAllContractsForCenter(ExhibitionCenter exhibitionCenter) throws DBException;

    List<Contract> getAllContractsForExhibition(Exhibition exhibition) throws DBException;

    List<Contract> getAllContractsBySearch(String search) throws DBException;

    List<Contract> getAllContracts() throws DBException;

    void updateContract(Contract contract) throws DBException;

    void insertContract(Contract contract) throws DBException;

    void deleteContract(Contract contract) throws DBException;

    void deleteContractById(Integer id ) throws DBException;


}
