package controller.command.moderatorCommand;


import controller.command.Command;
import controller.command.util.Links;
import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.Ticket;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;

public class WaitApprovalTicket implements Command {

    private Connection connection;
    private FactoryMySql factoryMySql;
    //    private List<Ticket> ticketList;
    private int idTicket;
    private int idModerator;

    private static final Logger LOGGER = Logger.getLogger(WaitApprovalTicket.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.WAIT_APPROVAL_TICKETS_PAGE);

        LOGGER.info("getIdModerator");
        getIdModerator(req);

        LOGGER.info("onBtnClick");
        onBtnClick(req);

        LOGGER.info("readData");
        readData(req);

        LOGGER.info("forward");
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
        handleConnection();

        try {
            factoryMySql.createTicket(connection).setLockTicketTable();
            connection.setAutoCommit(false);
            factoryMySql.createTicket(connection).approveTicket(idTicket);

            Ticket ticket = factoryMySql.createTicket(connection).getTicketById(idTicket);
            ticket.setApprovedById(idModerator);
            factoryMySql.createTicket(connection).deleteTicket(ticket.getId());
            int quantity = ticket.getQuantity();

            ticket.setQuantity(1);
            for (int i = 0; i < quantity; i++) {
                factoryMySql.createTicket(connection).insertTicket(ticket);
            }

            connection.commit();
            // TODO: send message to user
        } catch (Exception exception) {
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (Exception e) {

            }
            try {
                factoryMySql.createTicket(connection).unlockTable();
            } catch (Exception e) {

            }
            closeConnection();
        }
    }

    private void deleteTicket() {
        handleConnection();

        try {
            factoryMySql.createTicket(connection).deleteTicket(idTicket);
            // TODO: send message to user
        } catch (Exception exception) {

        } finally {
            closeConnection();
        }
    }

    private void readData(HttpServletRequest req) {
        handleConnection();
        List<Ticket> ticketList = null;

        try {
            ticketList = factoryMySql.createTicket(connection).getAllWaitApproval();

        } catch (Exception exception) {

        } finally {
            closeConnection();
        }

        if (ticketList == null) {
            ticketList = Collections.emptyList();
        }
        setDataToReq(req, ticketList);
    }

    private void setDataToReq(HttpServletRequest req, List<Ticket> ticketList) {
        req.setAttribute("listTickets", ticketList);
    }

    private void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception exception) {

        }
    }

    private void handleConnection() {
        try {
            connection = ConnectionPoolMySql.getInstance().getConnection();
            factoryMySql = new FactoryMySql();
        } catch (Exception exception) {

        }
    }
}
