package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.ServletHelper;
import controller.command.util.Links;
import entities.Ticket;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Work with tickets waiting for approval by the moderator
 *
 * @author Dmytro Galomko
 */
public class WaitApprovalTicket extends ServletHelper implements Command {

    private int idTicket;
    private int idModerator;

    private static final Logger LOGGER = Logger.getLogger(WaitApprovalTicket.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = req
                .getRequestDispatcher(Links.WAIT_APPROVAL_TICKETS_PAGE);

        getIdModerator(req);

        onBtnClick(req);

        readData(req);

        dispatcher.forward(req, resp);
    }

    private void getIdModerator(HttpServletRequest req) {
        HttpSession session = req.getSession();
        idModerator = (Integer) session.getAttribute("userId");
    }


    private void onBtnClick(HttpServletRequest req) {
        if (req.getParameter("idTicket") != null) {
            idTicket = Integer.parseInt(req.getParameter("idTicket"));
        } else {
            return;
        }

        String actionBtn = req.getParameter("action");
        if (actionBtn != null) {

            if (actionBtn.equals("Approve")) {
                approveTicket();
            } else if (actionBtn.equals("Delete")) {
                deleteTicket();
            }
        }

    }

    private void approveTicket() {
        handleConnection(LOGGER);

        try {
            factoryDB.createTicket(connection).setLockTicketTable();
            connection.setAutoCommit(false);
            factoryDB.createTicket(connection).approveTicket(idTicket);

            Ticket ticket = factoryDB.createTicket(connection)
                    .getTicketById(idTicket);
            ticket.setApprovedById(idModerator);
            LOGGER.info("New approved ticket(s): " + ticket);
            factoryDB.createTicket(connection).deleteTicket(ticket.getId());
            int quantity = ticket.getQuantity();

            ticket.setQuantity(1);
            for (int i = 0; i < quantity; i++) {
                factoryDB.createTicket(connection).insertTicket(ticket);
            }

            connection.commit();
            // TODO: send message to user
        } catch (Exception exception) {
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (Exception e) {
                LOGGER.error(e);
            }
            try {
                factoryDB.createTicket(connection).unlockTable();
            } catch (Exception e) {
                LOGGER.error(e);
            }
            closeConnection(LOGGER);
        }
    }

    private void deleteTicket() {
        handleConnection(LOGGER);

        try {
            factoryDB.createTicket(connection).deleteTicket(idTicket);
            LOGGER.info("Moderator id: " + idModerator
                    + " has deleted ticket id: " + idTicket);
            // TODO: send message to user
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }
    }

    private void readData(HttpServletRequest req) {
        handleConnection(LOGGER);
        List<Ticket> ticketList = null;

        try {
            ticketList = factoryDB.createTicket(connection)
                    .getAllWaitApproval();

        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }

        if (ticketList == null) {
            ticketList = Collections.emptyList();
        }
        setDataToReq(req, ticketList);
    }

    private void setDataToReq(HttpServletRequest req, List<Ticket> ticketList) {
        req.setAttribute("listTickets", ticketList);
    }

}
