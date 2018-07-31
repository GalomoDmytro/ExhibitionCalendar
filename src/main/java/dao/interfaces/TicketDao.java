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

    List<Ticket> getAllTicketsForDate(Date date) throws DBException;

    List<Ticket> getAllTicketsForContract(Contract contract) throws DBException;

    List<Ticket> getAllTicketsForUser(User user) throws DBException;

    List<Ticket> getTicketForUserOnContract(User user, Contract contract) throws DBException;

    void insertTicket(Ticket ticket) throws DBException;

    void updateTicket(Ticket ticket) throws DBException;

    void deleteTicket(Ticket ticket) throws DBException;
}
