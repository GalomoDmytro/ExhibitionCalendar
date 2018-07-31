package dao.interfaces;

import entities.Contract;
import entities.Ticket;
import entities.User;
import exceptions.DBException;

import java.util.List;

public interface TicketDao {

    Ticket getTicketById(Integer id) throws DBException;

    List<Ticket> getAllTickets() throws DBException;

    List<Ticket> getAllTicketsForDate(String dateString) throws DBException;

    List<Ticket> getAllTicketsForContract(Contract contract) throws DBException;

    List<Ticket> getAllTicketsForUser(User user) throws DBException;

    Ticket getTicketForUserOnContract(User user, Contract contract) throws DBException;

    Ticket createTicket(Ticket ticket) throws DBException;

    Ticket updateTicket(Ticket ticket) throws DBException;

    boolean deleteTicket(Ticket ticket) throws DBException;
}
