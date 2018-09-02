package controller.command;

import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.Contract;
import entities.Exhibition;
import entities.ExhibitionCenter;
import entities.User;
import org.apache.log4j.Logger;
import utility.PriceTicket;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;

public class Purchase implements Command {

    private Connection connection;
    private String userId;
    private FactoryMySql factoryMySql;
    private Integer contractId;
    private Contract contract;
    private Exhibition exhibition;
    private ExhibitionCenter exhibitionCenter;
    private String dateExhibitionStart;
    private String dateSearch;
    private Date date;
    private Integer quantityTickets;
    private HttpSession session;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//    private BigDecimal price;

    private static final Logger LOGGER = Logger.getLogger(Purchase.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.PURCHASE_PAGE);

        roleMail(req);

        getDataFromRequest(req);
        getDataFromSession(req);
        figureDateTicket();
        getDataFromTable();


//        if (req.getParameter("buy") != null) {
//                LOGGER.info("--------------buy pressed -------------");
//            if (isDataForPurchaseGood()) {
//                LOGGER.info("--------------data good -------------");
//
//                dispatcher = req.getRequestDispatcher(Links.CHECKOUT_PAGE);
//            } else {
//                // TODO: set error mes
//            }
//        }

        setDataToReq(req);
        setDataToSession();

        dispatcher.forward(req, resp);
    }

    private boolean isDataForPurchaseGood() {
        // TODO: do some logic
        return true;
    }

    private void getDataFromSession(HttpServletRequest request) {
        session = request.getSession();
        if (request.getParameter("dateFromExhibitionStart") == null
                || request.getParameter("dateFromExhibitionStart").isEmpty()) {
            dateExhibitionStart = (String) session.getAttribute("dateFromExhibitionStart");
        }

        if (request.getParameter("searchDateLine") == null
                || request.getParameter("searchDateLine").isEmpty()) {
            dateSearch = (String) session.getAttribute("searchDateLine");
        }

        try {
            if (request.getParameter("idContract") == null
                    || request.getParameter("idContract").isEmpty()) {
                contractId = (Integer) session.getAttribute("idContract");

            }
        } catch (Exception e) {
        }
    }

    private void getDataFromRequest(HttpServletRequest req) {
        if (req.getParameter("idContract") != null) {
            try {
                contractId = Integer.parseInt(req.getParameter("idContract"));
            } catch (NumberFormatException exception) {

            }
        }

//        LOGGER.info(req.getParameter("quantity") + " tickets");
        if (req.getParameter("quantity") == null) {
            quantityTickets = 1;
        } else {
            try {
                quantityTickets = Integer.parseInt(req.getParameter("quantity"));
            } catch (NumberFormatException exception) {
                LOGGER.error(exception);
            }
        }
//        LOGGER.info("quantityTickets: " + quantityTickets);

        dateExhibitionStart = req.getParameter("dateFromExhibitionStart");
        dateSearch = req.getParameter("searchDateLine");
//        LOGGER.info("Purchase ****** getDataFromRequest ***** finish");

    }

    private void setDataToReq(HttpServletRequest req) {
        req.setAttribute("dateFromExhibitionStart", dateExhibitionStart);
        req.setAttribute("searchDateLine", dateSearch);
//        req.setAttribute("idContract", contract.getId());

        req.setAttribute("dateTicketToApply", format.format(date));
//        req.setAttribute("price", contract.getTicketPrice());
        req.setAttribute("quantity", quantityTickets);

        req.setAttribute("contract", contract);
        req.setAttribute("exhibitionCenter", exhibitionCenter);
        req.setAttribute("exhibition", exhibition);
    }

    private void setDataToSession() {
        session.setAttribute("idContract", contractId);
        session.setAttribute("dateFromExhibitionStart", dateExhibitionStart);
        session.setAttribute("searchDateLine", dateSearch);
    }

    private void roleMail(HttpServletRequest req) {
        HttpSession session = req.getSession(true);

        if (session.getAttribute("userId") != null) {
            userId = session.getAttribute("userId").toString();
        } else {
            userId = "1";
        }

        handleConnection();
        try {
            User user = factoryMySql.createUser(connection).getById(Integer.parseInt(userId));
            req.setAttribute("eMailHold", user.getMail());
//            LOGGER.info("user:  " + user);
        } catch (Exception exception) {
        } finally {
            closeConnection();
        }

    }

    private void figureDateTicket() {
        if (dateSearch == null || dateSearch.isEmpty()) {
            dateSearch = format.format(new Date());
        }

        Date date1;
        Date date2;
        try {
            date1 = format.parse(dateSearch);
            date2 = format.parse(dateExhibitionStart);
            if (date1.compareTo(date2) <= 0) {
                date = date2;
            } else {
                date = date1;
            }

        } catch (ParseException e) {
            date = new Date();
        }

    }

    private void getDataFromTable() {
        handleConnection();

        try {
            if (contractId != null) {
                contract = factoryMySql.createExhibitionContract(connection)
                        .getExhibitionContractById(contractId);
                contract.setTicketPrice(new PriceTicket()
                        .calculateSumTicketsPrice(contract.getTicketPrice(), 1));
                exhibition = factoryMySql.createExhibition(connection)
                        .getExhibitionById(contract.getExhibitionId());
                exhibitionCenter = factoryMySql.createExhibitionCenter(connection)
                        .getExhibitionCenterById(contract.getExCenterId());
            }
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
