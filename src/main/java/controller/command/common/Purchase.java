package controller.command.common;

import controller.command.Command;
import controller.command.ServletHelper;
import controller.command.util.Links;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Dmytro Galomko
 */
public class Purchase extends ServletHelper implements Command {

    private String userId;
    private Integer contractId;
    private Contract contract;
    private Exhibition exhibition;
    private ExhibitionCenter exhibitionCenter;
    private String dateExhibitionStart;
    private String dateSearch;
    private String dateToApply;
    private Date date;
    private Integer quantityTickets;
    private HttpSession session;
    private final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static final Logger LOGGER = Logger.getLogger(Purchase.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.PURCHASE_PAGE);

        userMail(req);

        getDataFromRequest(req);
        getDataFromSession(req);
        figureDateTicket();
        getDataFromTable();

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
                LOGGER.error(exception);
            }
        }

        if (req.getParameter("quantity") == null) {
            quantityTickets = 1;
        } else {
            try {
                quantityTickets = Integer.parseInt(req.getParameter("quantity"));
            } catch (NumberFormatException exception) {
                LOGGER.error(exception);
            }
        }

        dateExhibitionStart = req.getParameter("dateFromExhibitionStart");
        dateSearch = req.getParameter("searchDateLine");

    }

    private void setDataToReq(HttpServletRequest req) {
        req.setAttribute("dateFromExhibitionStart", dateExhibitionStart);
        req.setAttribute("searchDateLine", dateSearch);

        req.setAttribute("dateTicketToApply", dateToApply);
        req.setAttribute("quantity", quantityTickets);

        req.setAttribute("contract", contract);
        req.setAttribute("exhibitionCenter", exhibitionCenter);
        req.setAttribute("exhibition", exhibition);
    }

    private void setDataToSession() {
        session.setAttribute("idContract", contractId);
        session.setAttribute("dateFromExhibitionStart", dateExhibitionStart);
        session.setAttribute("searchDateLine", dateToApply);
    }

    /**
     * If user is signIn than get Mail form DB
     *
     * @param req
     */
    private void userMail(HttpServletRequest req) {
        HttpSession session = req.getSession(true);

        if (session.getAttribute("userId") != null) {
            userId = session.getAttribute("userId").toString();
        } else {
            userId = "1";
        }

        handleConnection(LOGGER);
        try {
            User user = factoryDB.createUser(connection)
                    .getById(Integer.parseInt(userId));
            req.setAttribute("eMailHold", user.getMail());
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }

    }

    /**
     * Find on what date will be a ticket available
     */
    private void figureDateTicket() {
        if (dateSearch == null || dateSearch.isEmpty()) {
            dateSearch = FORMAT.format(new Date());
        }

        Date dateUserChose;
        Date dateWhenExpoStart;
        Date dateToday;
        String todayDate = FORMAT.format(new Date());
        ;
        try {
            dateUserChose = FORMAT.parse(dateSearch);
            dateWhenExpoStart = FORMAT.parse(dateExhibitionStart);
            dateToday = FORMAT.parse(todayDate);
            if (dateUserChose.compareTo(dateWhenExpoStart) <= 0) {
                date = dateWhenExpoStart;
            } else {
                date = dateUserChose;
            }

            if (date.compareTo(dateToday) <= 0) {
                date = dateToday;
            }

        } catch (ParseException e) {
            LOGGER.error(e);
            date = new Date();
        }

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        dateToApply = df.format(date);
    }

    /**
     * Get and prepare Exhibition, ExhibitionCenter, Contract from DB
     */
    private void getDataFromTable() {
        handleConnection(LOGGER);

        try {
            if (contractId != null) {
                contract = factoryDB.createExhibitionContract(connection)
                        .getExhibitionContractById(contractId);
                contract.setTicketPrice(new PriceTicket()
                        .calculateSumTicketsPrice(contract.getTicketPrice(), 1));
                exhibition = factoryDB.createExhibition(connection)
                        .getExhibitionById(contract.getExhibitionId());
                exhibitionCenter = factoryDB.createExhibitionCenter(connection)
                        .getExhibitionCenterById(contract.getExCenterId());
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }

    }

}
