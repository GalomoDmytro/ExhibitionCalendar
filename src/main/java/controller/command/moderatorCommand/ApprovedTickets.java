package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.ServletHelper;
import controller.command.util.Links;
import entities.Ticket;
import org.apache.commons.lang3.StringUtils;
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
    private int currentPage;

    private static final int recordsPerPage = 3;
    private int numberOfPages;

    private static final Logger LOGGER = Logger.getLogger(ApprovedTickets.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = req
                .getRequestDispatcher(Links.APPROVED_TICKETS_PAGE);

        findCurrentPagePagination(req);
        readData();
        setDataToReq(req);

        dispatcher.forward(req, resp);
    }

    private void setDataToReq(HttpServletRequest req) {

        req.setAttribute("listTickets", ticketList);
        req.setAttribute("numberOfPages", numberOfPages);
        req.setAttribute("currentPage", currentPage);

    }

    private void readData() {
        handleConnection(LOGGER);

        try {
            int quantityApprovedTickets = factoryDB.createTicket(connection)
                    .getQuantityApproved();
            countNumberOfPages(quantityApprovedTickets);
            int start = currentPage * recordsPerPage - recordsPerPage;
            ticketList = factoryDB.createTicket(connection)
                    .getAllApprovedLimit(start, recordsPerPage);
            LOGGER.info("ticketList" + ticketList + " s " + start + " e" +recordsPerPage);
            LOGGER.info("total" + quantityApprovedTickets);
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }
    }

    /**
     * Find the position of the current pagination page
     *
     * @param req
     */
    private void findCurrentPagePagination(HttpServletRequest req) {
        if (req.getParameter("currentPage") == null) {
            currentPage = 1;
        } else {

            String curPageLine = req.getParameter("currentPage");
            if (StringUtils.isNumeric(curPageLine)) {
                currentPage = Integer.valueOf(req.getParameter("currentPage"));
            } else {
                currentPage = 1;
            }
        }
    }

    private void countNumberOfPages(int quantityTickets) {
        numberOfPages = quantityTickets / recordsPerPage;
        if (quantityTickets % recordsPerPage > 0) {
            numberOfPages++;
        }
    }

}
