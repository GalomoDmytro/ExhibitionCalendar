package controller.command.common;

import controller.command.Command;
import controller.command.ServletHelper;
import controller.command.util.Links;
import entities.Contract;
import entities.Ticket;
import org.apache.log4j.Logger;
import utility.PriceTicket;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * Servlet processes payment
 *
 * @author Dmytro Galomko
 */
public class CheckOut extends ServletHelper implements Command {

    private String priceLine;
    private BigDecimal price;
    private int quantity;
    private String dateToApplyTicket;
    private String quantityTicketsLine;
    private String userMail;
    private int idContract;
    private Ticket ticket;

    private static final Logger LOGGER = Logger.getLogger(CheckOut.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.CHECKOUT_PAGE);

        getDataFromRequest(req);

        calculatePrice();

        if (!hasTicketOnStock()) {
            LOGGER.info("Not enough tickets for contract: " + idContract
                    + " for date: " + dateToApplyTicket);
            req.setAttribute("ticketsAvailable", "Ticket's sold out!");
            dispatcher = req.getRequestDispatcher(Links.PURCHASE_PAGE);
            dispatcher.forward(req, resp);
            return;
        }

        setDataToReq(req);
        if (req.getParameter("checkoutPurchase") != null) {
            dispatcher = finishPurchase(req);
        }

        dispatcher.forward(req, resp);
    }

    /**
     * Check or have available tickets for sale?
     *
     * @return true if have ticket for sale
     */
    private boolean hasTicketOnStock() {
        handleConnection(LOGGER);

        try {
            Contract contract = factoryDB
                    .createExhibitionContract(connection)
                    .getExhibitionContractById(idContract);
            int soldTickets = factoryDB.createTicket(connection)
                    .getCountSoldTicketForDate(java.sql.Date.valueOf(dateToApplyTicket), idContract);
            if (contract.getMaxTicketPerDay() <= soldTickets + quantity) {
                return false;
            }
            if (soldTickets + quantity > contract.getMaxTicketPerDay()) {
                return false;
            }
            return true;

        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }

        return true;
    }

    private void getDataFromRequest(HttpServletRequest req) {
        if (req.getParameter("priceTickets") != null) {
            priceLine = req.getParameter("priceTickets");
            price = new PriceTicket().getBigDecimalPriceVal(priceLine);
        }

        dateToApplyTicket = req.getParameter("dateTicketToApply");
        quantityTicketsLine = req.getParameter("quantity");
        if (quantityTicketsLine != null) {
            quantity = Integer.parseInt(quantityTicketsLine);
            LOGGER.info("quantity " + quantity);

        }
        userMail = req.getParameter("eMail");

        if (req.getParameter("idContract") != null) {
            idContract = Integer.parseInt(req.getParameter("idContract"));
        }
    }

    private void setDataToReq(HttpServletRequest req) {
        req.setAttribute("price", price);
        req.setAttribute("eMail", userMail);
        req.setAttribute("dateTicketToApply", dateToApplyTicket);
    }

    private void calculatePrice() {
        price = price.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * Insert ticket to DB
     *
     * @param req
     * @return requestDispatcher to PURCHASE_FINISH_PAGE or ERROR_PAGE
     */
    private RequestDispatcher finishPurchase(HttpServletRequest req) {
        handleConnection(LOGGER);
        try {
            ticket = formTicket();
            factoryDB.createTicket(connection).insertTicket(ticket);
            LOGGER.info("CHECK OUT " + ticket);
            return req.getRequestDispatcher(Links.PURCHASE_FINISH_PAGE);
        } catch (Exception exception) {
            LOGGER.error(exception);
            return req.getRequestDispatcher(Links.ERROR_PAGE);
        } finally {
            closeConnection(LOGGER);
        }
    }

    private Ticket formTicket() {
        Ticket ticket = new Ticket.Builder()
                .setUserMail(userMail)
                .setDateToApply(java.sql.Date.valueOf(dateToApplyTicket))
                .setQuantity(quantity)
                .setDateTransaction(transactionDate())
                .setContractId(idContract)
                .build();
        return ticket;
    }

    private Date transactionDate() {
        return java.sql.Date.valueOf(java.time.LocalDate.now());
    }


}
