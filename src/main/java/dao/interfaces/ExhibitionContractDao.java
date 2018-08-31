package dao.interfaces;

import entities.Contract;
import entities.Exhibition;
import entities.ExhibitionCenter;
import exceptions.DBException;

import java.sql.Date;
import java.util.List;

public interface ExhibitionContractDao {

    Contract getExhibitionContractById(Integer id) throws DBException;

    List<Contract> getAllContractsByExCenterWithExhibition(ExhibitionCenter exhibitionCenter, Exhibition exhibition) throws DBException;

    List<Contract> getAllContractsForCenter(Integer integer) throws DBException;

    List<Contract> getAllContractsForExhibition(Integer integer) throws DBException;

    List<Contract> getAllContractAfterDateWithExpoCenterAndExhibition(Date date) throws DBException;

    void prepareCEC(Contract contract, Exhibition exhibition,
                    ExhibitionCenter exhibitionCenter, Integer idContract) throws DBException;

    List<Contract> searchAfterDateWithExpoCenterAndExhibition(String search, Date date) throws DBException;

    List<Contract> searchContactsWithExpoAndCenterLimit(String search, Date date, int startLimit, int endLimit) throws DBException;

    List<Contract> getAllAfterDate(Date date) throws DBException;

    List<Contract> getAllContractsBySearch(String search) throws DBException;

    List<Contract> getAllContracts() throws DBException;

    void updateContract(Contract contract) throws DBException;

    void insertContract(Contract contract) throws DBException;

    void deleteContract(Contract contract) throws DBException;

    void deleteContractById(Integer id ) throws DBException;

    int getNumberOfContractsAfterDate(Date date) throws DBException;

    int getNumberOfContractsAfterSearch(String search, Date date) throws DBException;

    List<Contract> getContractsAfterDateLimit(Date date, int startLimit, int endLimit) throws DBException;


}
