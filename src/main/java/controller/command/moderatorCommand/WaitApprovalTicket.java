package controller.command.moderatorCommand;


import controller.command.Command;
import controller.command.Links;
import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.Ticket;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.concurrent.locks.Lock;

public class WaitApprovalTicket implements Command {

    private Connection connection;
    private FactoryMySql factoryMySql;
    private List<Ticket> ticketList;
    private int idContract;

    private static final Logger LOGGER = Logger.getLogger(WaitApprovalTicket.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.WAIT_APPROVAL_TICKETS_PAGE);
        LOGGER.info("WaitApprovalTicket");

        onBtnClick(req);

        readData();

        setDataToReq(req);

        dispatcher.forward(req, resp);
    }

    private void onBtnClick(HttpServletRequest req) {
        if(req.getParameter("idContract") != null) {
            idContract = Integer.parseInt(req.getParameter("idContract"));
        } else {
            return;
        }

        String actionBtn = req.getParameter("action");
        if(actionBtn != null) {

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
            LOGGER.info("Approve ticket");

            factoryMySql.createTicket(connection).approveTicket(idContract);
            // TODO: send message to user
        } catch (Exception exception) {
            LOGGER.info(exception);
        } finally {
            closeConnection();
        }
    }

    private void deleteTicket() {
        handleConnection();

        try {
            factoryMySql.createTicket(connection).deleteTicket(idContract);
            // TODO: send message to user
        } catch (Exception exception) {

        } finally {
            closeConnection();
        }
    }

    private void readData() {
        handleConnection();

        try {
            ticketList = factoryMySql.createTicket(connection).getAllWaitApproval();
        } catch (Exception exception) {

        } finally {
            closeConnection();
        }
    }

    private void setDataToReq(HttpServletRequest req) {
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
