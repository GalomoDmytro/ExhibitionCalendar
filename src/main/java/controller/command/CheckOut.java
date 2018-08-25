package controller.command;

import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.Ticket;
import org.apache.log4j.Logger;

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
    private BigDecimal priceTicket;
    private int quantity;
    private String dateToApplyTicket;
    private String quantityTicketsLine;
    private String userMail;
    private int idContract;
    private Ticket ticket;

    private static final Logger LOGGER = Logger.getLogger(CheckOut.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.CHECKOUT_PAGE);

        getDataFromRequest(req);

        setDataToReq(req);

        if(req.getParameter("checkoutPurchase") != null) {
            dispatcher = finishPurchase(req);
        }

        dispatcher.forward(req, resp);
    }

    private void getDataFromRequest(HttpServletRequest req) {
        if(req.getParameter("priceOneTicket") != null) {
            // TODO make some awesome method to transofm currency
            priceTicket = new BigDecimal(req.getParameter("priceOneTicket"));
            LOGGER.info("priceTicket " + priceTicket);

        }

//        dateToApplyTicket = req.getParameter("");
        dateToApplyTicket = "2018-08-30";
        quantityTicketsLine = req.getParameter("quantity");
        if(quantityTicketsLine != null) {
            quantity = Integer.parseInt(quantityTicketsLine);
        }
        LOGGER.info("quantity " + quantityTicketsLine);
        LOGGER.info("quantity int " + quantity);
        userMail = req.getParameter("eMail");
        LOGGER.info("mail " + userMail);


        if(req.getParameter("idContract") != null) {
            idContract = Integer.parseInt(req.getParameter("idContract"));
            LOGGER.info("idContact " + idContract);
        }
    }

    private void setDataToReq(HttpServletRequest req) {
        req.setAttribute("price", priceTicket);
        req.setAttribute("eMail", userMail);
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
