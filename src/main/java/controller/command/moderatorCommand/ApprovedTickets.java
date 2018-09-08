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
import java.io.IOException;
import java.util.List;

/**
 * Servlet show all approved ticket
 */
public class ApprovedTickets extends ServletHelper implements Command {

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
        handleConnection(LOGGER);

        try {
            ticketList = factoryDB.createTicket(connection)
                    .getAllApprovedTickets();
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }
    }

}
