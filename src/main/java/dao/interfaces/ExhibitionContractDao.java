package dao.interfaces;

import entities.Contract;
import entities.Exhibition;
import entities.ExhibitionCenter;
import exceptions.DBException;

import java.sql.Date;
import java.util.List;

/**
 * Defines methods for work with exhibition_contract table
 *
 * @author Dmytro Galomko
 */
public interface ExhibitionContractDao {

    /**
     * Enables client sessions to acquire exhibition_contract table locks
     * explicitly for the purpose of cooperating with other sessions
     * for access to tables, or to prevent other
     * sessions from modifying exhibition_contract tables during periods when
     * a session requires exclusive access to them
     *
     * @throws DBException
     */
    void setLockContractTable() throws DBException;

    /**
     * Releases any table locks held by the current session
     *
     * @throws DBException
     */
    void unlockTable() throws DBException;

    /**
     * Get ExhibitionContract entity from exhibition_contract table
     *
     * @param id of ExhibitionContract
     * @return ExhibitionContract entity
     * or will return ExhibitionContract().emptyContract) if have not matches
     * with looking exhibition_contract id
     * @throws DBException
     */
    Contract getExhibitionContractById(Integer id) throws DBException;


    /**
     * Get List of all Contracts entities from exhibition_contract table, witch
     * depend to specific exhibition and exhibition center
     *
     * @param exhibitionCenter entity
     * @param exhibition       entity
     * @return List of Contract entities witch bind to exhibitionCenter and exhibition
     * or return Collections.emptyList() if found nothing
     * @throws DBException
     */
    List<Contract> getAllContractsByExCenterWithExhibition
    (ExhibitionCenter exhibitionCenter, Exhibition exhibition) throws DBException;

    /**
     * Get List of all Contracts entities from exhibition_contract
     * witch bind to specific Exhibition Center
     *
     * @param idExCenter id Exhibition Center
     * @return List of Contract entities witch bind to Exhibition Center
     * or return Collections.emptyList() if found nothing
     * @throws DBException
     */
    List<Contract> getAllContractsForCenter(Integer idExCenter) throws DBException;

    /**
     * Get List of all Contracts entities from exhibition_contract
     * witch bind to specific Exhibition
     *
     * @param idExhibition id Exhibition
     * @return List of Contract entities witch bind to Exhibition
     * or return Collections.emptyList() if found nothing
     * @throws DBException
     */
    List<Contract> getAllContractsForExhibition(Integer idExhibition) throws DBException;

    /**
     * Get List of all Contracts from exhibition_contract
     * witch 'date start' or 'date end' in looking range
     *
     * @param date sql.Date - will search for a contract includes this date
     * @return List of Contract entities with Exhibition and ExhibitionContract title
     * or return Collections.emptyList() if found nothing
     * @throws DBException
     */
    List<Contract> getAllContractAfterDateWithExpoCenterAndExhibition(Date date)
            throws DBException;

    /**
     * Set value to Contract, Exhibition, ExhibitionCenter params witch
     * related to specific Contact
     *
     * @param contract         entity
     * @param exhibition       entity
     * @param exhibitionCenter entity
     * @param idContract       id looking Exhibition Contract
     * @throws DBException
     */
    void prepareCEC(Contract contract, Exhibition exhibition,
                    ExhibitionCenter exhibitionCenter, Integer idContract) throws DBException;

    /**
     * Get List of all Contracts from exhibition_contract
     * witch 'date start' or 'date end' in looking range
     * and 'line search' matches to date or address in related
     * exhibition_center or exhibition tables
     *
     * @param date   sql.Date - will search for a contract includes this date
     * @param search line - will search for a exhibition title or exhibition center title
     *               or address matches to this 'search' line
     * @return List of Contract entities with Exhibition and ExhibitionContract title
     * or return Collections.emptyList() if found nothing
     * @throws DBException
     */
    List<Contract> searchAfterDateWithExpoCenterAndExhibition(String search, Date date)
            throws DBException;

    /**
     * Get List of all Contracts from exhibition_contract
     * witch 'date start' or 'date end' in looking range
     * and 'line search' matches to date or address in related
     * exhibition_center or exhibition tables
     * with using limit table on result
     *
     * @param date       sql.Date - will search for a contract includes this date
     * @param search     line - will search for a exhibition title or exhibition center title
     *                   or address matches to this 'search' line
     * @param startLimit specifies the offset of the first row to return
     * @param endLimit   specifies the maximum number of rows to return
     * @return List of Contract entities with Exhibition and ExhibitionContract title
     * or return Collections.emptyList() if found nothing
     * @throws DBException
     */
    List<Contract> searchContactsWithExpoAndCenterLimit(String search, Date date,
                                                        int startLimit, int endLimit)
            throws DBException;

    /**
     * Get List of all Contracts from exhibition_contract table,
     * witch 'date start' or 'date end' in looking range
     *
     * @param date sql.Date - will search for all contracts includes this date
     * @return List of Contract entities
     * or return Collections.emptyList() if found nothing
     * @throws DBException
     */
    List<Contract> getAllAfterDate(Date date) throws DBException;

    /**
     * Get all contacts from exhibition_contract table,
     * witch data similar to search request
     *
     * @param search - will look data similar to this line
     * @return List of Contract entities
     * or return Collections.emptyList() if found nothing
     * @throws DBException
     */
    List<Contract> getAllContractsBySearch(String search) throws DBException;

    /**
     * Get all contacts from exhibition_contract table, witch data similar to search request
     *
     * @return List of Contract entities
     * or return Collections.emptyList() if table is empty
     * @throws DBException
     */
    List<Contract> getAllContracts() throws DBException;

    /**
     * Update exhibition_contract table
     *
     * @param contract entity to update
     * @throws DBException
     */
    void updateContract(Contract contract) throws DBException;

    /**
     * Insert in exhibition_contract table new contract entity
     *
     * @param contract entity to insert
     * @throws DBException
     */
    void insertContract(Contract contract) throws DBException;

    /**
     * Delete contract from exhibition_contract table
     *
     * @param contract entity to delete from table
     * @throws DBException
     */
    void deleteContract(Contract contract) throws DBException;

    /**
     * Delete contract from exhibition_contract table
     *
     * @param id contract to delete
     * @throws DBException
     */
    void deleteContractById(Integer id) throws DBException;

    /**
     * Get quantity of contracts witch include specific date
     * from exhibition_contract table
     *
     * @param date sql.Date - will search contracts witch includes this date
     * @return int quantity of contracts for this date
     * @throws DBException
     */
    int getNumberOfContractsAfterDate(Date date) throws DBException;

    /**
     * Get quantity of contracts witch include specific date
     * and include field matches to looking string
     * from exhibition_contract table
     *
     * @param date   sql.Date - will search contracts witch includes this date
     * @param search search contracts witch includes field whit matches this line
     * @return quantity of contracts for this date
     * @throws DBException
     */
    int getNumberOfContractsAfterSearch(String search, Date date) throws DBException;

    /**
     * Get List of Contract entities witch include specific date
     * with limited result
     * from exhibition_contract table
     *
     * @param date       sql.Date - will search contracts witch includes this date
     * @param startLimit specifies the offset of the first row to return
     * @param endLimit   specifies the maximum number of rows to return
     * @return List of Contract entities or emptyList
     * @throws DBException
     */
    List<Contract> getContractsAfterDateLimit(Date date, int startLimit, int endLimit)
            throws DBException;
}
