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
import java.util.List;

public class ApprovedTickets implements Command {

    private Connection connection;
    private FactoryMySql factoryMySql;
    private List<Ticket> ticketList;

    private static final Logger LOGGER = Logger.getLogger(ApprovedTickets.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = req
                .getRequestDispatcher(Links.APPROVED_TICKETS_PAGE);

        readData();
        setDataToReq(req);

        dispatcher.forward(req, resp);
    }



    private void setDataToReq(HttpServletRequest req) {

        req.setAttribute("listTickets", ticketList);
    }

    private void readData() {
        handleConnection();

        try {
            ticketList = factoryMySql.createTicket(connection)
                    .getAllApprovedTickets();
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection();
        }
    }

    private void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        }
    }

    private void handleConnection() {
        try {
            connection = ConnectionPoolMySql.getInstance().getConnection();
            factoryMySql = new FactoryMySql();
        } catch (Exception exception) {
            LOGGER.error(exception);
        }
    }
}
