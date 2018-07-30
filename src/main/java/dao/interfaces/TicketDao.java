package dao.interfaces;

import entities.Contract;
import entities.Ticket;
import entities.User;

import java.util.List;

public interface TicketDao extends DBHelperDao {

    Ticket getTicketById(Integer id);

    List<Ticket> getAllTickets();

    List<Ticket> getAllTicketsForDate(String dateString);

    List<Ticket> getAllTicketsForContract(Contract contract);

    List<Ticket> getAllTicketsForUser(User user);

    Ticket getTicketForUserOnContract(User user, Contract contract);

    Ticket createTicket(Ticket ticket);

    Ticket updateTicket(Ticket ticket);

    boolean deleteTicket(Ticket ticket);
}
