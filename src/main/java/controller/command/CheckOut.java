package controller.command;

import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
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
    private String oneTicketPrice;
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
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.CHECKOUT_PAGE);

        getDataFromRequest(req);

        calculatePrice();

        setDataToReq(req);

        if (req.getParameter("checkoutPurchase") != null) {
            dispatcher = finishPurchase(req);
        }

        dispatcher.forward(req, resp);
    }

    private void getDataFromRequest(HttpServletRequest req) {
        if (req.getParameter("priceTickets") != null) {
            priceLine = req.getParameter("priceTickets");
            LOGGER.info("priceTickets " + priceLine);
            price = new PriceTicket().getBigDecimalPriceVal(priceLine);
        }

//        dateToApplyTicket = req.getParameter("");
        dateToApplyTicket = req.getParameter("date");
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
    }

    private void calculatePrice() {
        price = new PriceTicket().calculateSumTicketsPrice(price, quantity);
        LOGGER.info("price after calc " + price);
    }

    private RequestDispatcher finishPurchase(HttpServletRequest req) {
        handleConnection();
        try {
            ticket = formTicket();
            return req.getRequestDispatcher(Links.PURCHASE_FINISH_PAGE);
        } catch (Exception exception) {
            return req.getRequestDispatcher(Links.ERROR_PAGE);

        } finally {
            closeConnection();
        }
    }

    private Ticket formTicket() {
        return new Ticket.Builder()
                .setUserMail(userMail)
                .setDateToApply(java.sql.Date.valueOf(dateToApplyTicket))
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
