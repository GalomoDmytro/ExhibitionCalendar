package dao.interfaces;

import entities.Contract;
import entities.Ticket;
import entities.User;
import exceptions.DBException;

import java.util.Date;
import java.util.List;

/**
 * Defines methods for work with ticket table
 */
public interface TicketDao {

    /**
     * Get Ticket from ticket table
     *
     * @param id looking ticket
     * @return Ticket entity
     * @throws DBException
     */
    Ticket getTicketById(Integer id) throws DBException;

    /**
     * Get all tickets from ticket table
     *
     * @return List of Tickets or empty List
     * @throws DBException
     */
    List<Ticket> getAllTickets() throws DBException;

    /**
     * Get all checked(approved) tickets from ticket table
     *
     * @return List of checked Tickets or empty List
     * @throws DBException
     */
    List<Ticket> getAllApprovedTickets() throws DBException;

    /**
     * Get all not checked(not approved) tickets from ticket table
     *
     * @return List of not checked Tickets or empty List
     * @throws DBException
     */
    List<Ticket> getAllWaitApproval() throws DBException;

    /**
     * Set true for is_confirmed column (set approved)
     *
     * @param idTicket
     * @throws DBException
     */
    void approveTicket(Integer idTicket) throws DBException;

    /**
     * Enables client sessions to acquire exhibition_center table locks
     * explicitly for the purpose of cooperating with other sessions
     * for access to tables, or to prevent other
     * sessions from modifying exhibition_center tables during periods when
     * a session requires exclusive access to them
     *
     * @throws DBException
     */
    void setLockTicketTable() throws DBException;

    /**
     * Releases any table locks held by the current session
     *
     * @throws DBException
     */
    void unlockTable() throws DBException;

    /**
     * Get List of Tickets for specific Contract from ticket table
     *
     * @param contractId
     * @return List Contracts or empty List
     * @throws DBException
     */
    List<Ticket> getAllTicketsForContract(Integer contractId) throws DBException;

    /**
     * Get List of Tickets for specific User from ticket table
     *
     * @param user
     * @return List Contracts or empty List
     * @throws DBException
     */
    List<Ticket> getAllTicketsForUser(User user) throws DBException;

    /**
     * Get List of Tickets for specific User to Specific Exhibition from ticket table
     *
     * @param user     entity
     * @param contract
     * @return List Contracts or empty List
     * @throws DBException
     */
    List<Ticket> getTicketForUserOnContract(User user, Contract contract) throws DBException;

    /**
     * Insert Ticket in to ticket table
     *
     * @param ticket
     * @throws DBException
     */
    void insertTicket(Ticket ticket) throws DBException;

    /**
     * Update data in ticket table
     *
     * @param ticket
     * @throws DBException
     */
    void updateTicket(Ticket ticket) throws DBException;

    /**
     * Delete Ticket from ticket table
     *
     * @param ticketId
     * @throws DBException
     */
    void deleteTicket(Integer ticketId) throws DBException;

    /**
     * Get quantity of sold tickets for specific date and for
     * specific Contract from ticket table
     *
     * @param date
     * @param idContract
     * @return
     * @throws DBException
     */
    int getCountSoldTicketForDate(Date date, Integer idContract) throws DBException;
}
