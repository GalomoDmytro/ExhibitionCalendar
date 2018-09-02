package controller.command;

import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
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
import java.sql.Connection;
import java.sql.Date;

public class CheckOut implements Command {

    private Connection connection;
    private FactoryMySql factoryMySql;
    private String priceLine;
    private BigDecimal price;
    private int quantity;
    private String dateToApplyTicket;
    private String quantityTicketsLine;
    private String userMail;
    private int idContract;
    private Ticket ticket;
//    private PriceTicket mt = new PriceTicket();

    private static final Logger LOGGER = Logger.getLogger(CheckOut.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.CHECKOUT_PAGE);

        getDataFromRequest(req);

        calculatePrice();

        if (!hasTicketOnStock()) {
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

    private boolean hasTicketOnStock() {
        handleConnection();

        try {
            Contract contract = factoryMySql.createExhibitionContract(connection).getExhibitionContractById(idContract);
            int soldTickets = factoryMySql.createTicket(connection)
                    .getCountSoldTicketForDate(java.sql.Date.valueOf(dateToApplyTicket), idContract);
            if (contract.getMaxTicketPerDay() <= soldTickets + quantity) {
//                LOGGER.info("Ticket quantity info;");
//                LOGGER.info("sold ticket " + contract.getMaxTicketPerDay() + " >= "
//                    + " soldTickets:" + soldTickets + " quantity: " + quantity );
                return false;
            }
            if (soldTickets + quantity > contract.getMaxTicketPerDay()) {
                return false;
            }
            return true;

        } catch (Exception exception) {

        } finally {
            closeConnection();
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

    private RequestDispatcher finishPurchase(HttpServletRequest req) {
        handleConnection();
        try {
            ticket = formTicket();
            LOGGER.info("try to insert " + ticket);
            factoryMySql.createTicket(connection).insertTicket(ticket);
            return req.getRequestDispatcher(Links.PURCHASE_FINISH_PAGE);
        } catch (Exception exception) {
            LOGGER.error(exception);
            return req.getRequestDispatcher(Links.ERROR_PAGE);

        } finally {
            closeConnection();
        }
    }

    private Ticket formTicket() {
        LOGGER.info(
                "userMail" + userMail +
                "; dateToApplyTicket" + dateToApplyTicket +
                "; quantity" + quantity +
                "; transactionDate" + transactionDate() +
                "; idContract" + idContract
                );
        return new Ticket.Builder()
                .setUserMail(userMail)
                .setDateToApply(java.sql.Date.valueOf(dateToApplyTicket))
                .setQuantity(quantity)
                .setDateTransaction(transactionDate())
                .setContractId(idContract)
                .build();
    }

    private Date transactionDate() {
        return java.sql.Date.valueOf(java.time.LocalDate.now());
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
