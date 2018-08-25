package controller.command;

import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.Contract;
import entities.Exhibition;
import entities.ExhibitionCenter;
import entities.User;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;

public class Purchase implements Command {

    private Connection connection;
    private String userId;
    private FactoryMySql factoryMySql;
    private Integer contractId;
    private Contract contract;
    private ExhibitionCenter exhibitionCenter;
    private Exhibition exhibition;
    private String date;
    private Integer quantityTickets;

    private static final Logger LOGGER = Logger.getLogger(Purchase.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.PURCHASE_PAGE);

        roleMail(req);

        getDataFromRequest(req);
        getDataFromTable();

        if (req.getParameter("cancel") != null) {
            dispatcher = req.getRequestDispatcher(Links.HOME_PAGE);
        }
        if (req.getParameter("buy") != null) {
            if (isDataForPurchaseGood()) {
                // TODO: make purchase to form

                dispatcher = req.getRequestDispatcher(Links.CHECKOUT_PAGE);
            } else {
                // TODO: set error mes
            }
        }

        setDataToReq(req);

        dispatcher.forward(req, resp);
    }

    private boolean isDataForPurchaseGood() {
        // TODO: do some logic
        return true;
    }

    private void getDataFromRequest(HttpServletRequest req) {
        if (req.getParameter("idContract") != null) {
            try {
                contractId = Integer.parseInt(req.getParameter("idContract"));
            } catch (NumberFormatException exception) {

            }
        }

        if (req.getParameter("quantity") == null) {
            quantityTickets = 1;
        } else {
            try {
                quantityTickets = Integer.parseInt(req.getParameter("quantity"));
            } catch (NumberFormatException exception) {

            }
        }
    }

    private void setDataToReq(HttpServletRequest req) {
//        req.setAttribute("dateBuy", date);
//        req.setAttribute("nameExpo", exhibition.getTitle());
//        req.setAttribute("nameExpoCen", exhibitionCenter.getTitle());
//        req.setAttribute("addressExpoCen", exhibitionCenter.getAddress());
        req.setAttribute("price", contract.getTicketPrice());
        req.setAttribute("idContract", contract.getId());
//        req.setAttribute("ticketsAvailable", 0);
    }

    private void roleMail(HttpServletRequest req) {
        HttpSession session = req.getSession(true);

        if (session.getAttribute("idUser") != null) {
            userId = session.getAttribute("idUser").toString();
        }

        handleConnection();
        try {
            User user = factoryMySql.createUser(connection).getById(Integer.parseInt(userId));
            req.setAttribute("eMailHold", user.getMail());
        } catch (Exception exception) {
        } finally {
            closeConnection();
        }

    }

    private void getDataFromTable() {
        handleConnection();

        try {
            if (contractId != null) {
                contract = factoryMySql.createExhibitionContract(connection).getExhibitionContractById(contractId);
            }
        } catch (Exception exception) {

        } finally {
            closeConnection();
        }
    }

    private boolean hasTicketsToSold() {
//        handleConnection();
//
//        try {
//            if (date != null && quantityTickets != null) {
//                int soldTickets = factoryMySql.createTicket(connection)
//                        .getCountSoldTicketForDate(java.sql.Date.valueOf(date), contract.getId());
//                if (contract.getMaxTicketPerDay() >= soldTickets) {
//                    return false;
//                }
//                if (soldTickets + quantityTickets > contract.getMaxTicketPerDay()) {
//                    return false;
//                }
//                return true;
//            }
//        } catch (Exception exception) {
//
//        } finally {
//            closeConnection();
//        }

        return false;
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
