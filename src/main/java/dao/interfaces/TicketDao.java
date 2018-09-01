package dao.interfaces;

import entities.Contract;
import entities.Ticket;
import entities.User;
import exceptions.DBException;

import java.util.Date;
import java.util.List;

public interface TicketDao {

    Ticket getTicketById(Integer id) throws DBException;

    List<Ticket> getAllTickets() throws DBException;

    List<Ticket> getAllApprovedTickets() throws DBException;

    List<Ticket> getAllWaitApproval() throws DBException;

    void approveTicket(Integer idTicket) throws DBException;

    void setLockTicketTable() throws DBException;

    void unlockTable() throws DBException;

//    List<Ticket> getAllTicketsForDate(java.sql.Date date) throws DBException;

    List<Ticket> getAllTicketsForContract(Integer contractId) throws DBException;

    List<Ticket> getAllTicketsForUser(User user) throws DBException;

    List<Ticket> getTicketForUserOnContract(User user, Contract contract) throws DBException;

    void insertTicket(Ticket ticket) throws DBException;

    void updateTicket(Ticket ticket) throws DBException;

    void deleteTicket(Integer ticket) throws DBException;

    int getCountSoldTicketForDate(Date date, Integer id_contract) throws DBException;
}
