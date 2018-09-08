package controller.command;

import controller.command.util.Links;
import entities.Contract;
import exceptions.DBException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import utility.PriceTicket;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Home extends ServletHelper implements Command {
    
    private String date;
    private String searchLine;
    private int countExhibitions;
    private int numberOfPages;
    private int currentPage;
    private int start;
    PriceTicket priceTicket = new PriceTicket();

    private static final int recordsPerPage = 2;
    private static final Logger LOGGER = Logger.getLogger(Home.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.HOME_PAGE);

        findCurrentPagePagination(req);

        getDate(req);

        choseWhatToShow(req);

        saveDataToReq(req);

        dispatcher.forward(req, resp);
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

    private void saveDataToReq(HttpServletRequest req) {
        req.setAttribute("searchDate", req.getParameter("searchDate"));
        req.setAttribute("searchDateLine", date);
        req.setAttribute("searchField", req.getParameter("searchField"));
        req.setAttribute("numberOfPages", numberOfPages);
        req.setAttribute("currentPage", currentPage);
    }

    /**
     * Show all available exhibitions from DB if search field is empty or
     * search for what the searchField contain
     *
     * @param req
     */
    private void choseWhatToShow(HttpServletRequest req) {
        if (req.getParameter("searchField") != null
                && req.getParameter("searchField").length() > 0) {
            specificSearch(req);
        } else {
            showAll(req);
        }
    }

    private void specificSearch(HttpServletRequest req) {

        handleConnection(LOGGER);

        try {

            searchLine = req.getParameter("searchField");
            countExhibitions = factoryDB.createExhibitionContract(connection)
                    .getNumberOfContractsAfterSearch(searchLine, java.sql.Date.valueOf(date));
            countNumberOfPages();
            List<Contract> contractList = factoryDB.createExhibitionContract(connection)
                    .searchContactsWithExpoAndCenterLimit(searchLine, java.sql.Date.valueOf(date),
                            start, recordsPerPage);

            transformPrice(contractList);
            req.setAttribute("listForCustomer", contractList);

        } catch (Exception exception) {
            LOGGER.info("get date from req " + req);
        } finally {
            closeConnection(LOGGER);
        }
    }

    private void countNumberOfPages() {
        numberOfPages = countExhibitions / recordsPerPage;
        if (countExhibitions % recordsPerPage > 0) {
            numberOfPages++;
        }
    }

    private void showAll(HttpServletRequest req) {
        handleConnection(LOGGER);

        try {
            List<Contract> listContract = getAllContract();
            transformPrice(listContract);
            req.setAttribute("listForCustomer", listContract);
        } catch (Exception exception) {
            LOGGER.info("get date from req " + req);
        } finally {
            closeConnection(LOGGER);
        }
    }

    private void transformPrice(List<Contract> listContract) {
        for (Contract contract : listContract) {
            contract.setTicketPrice(priceTicket
                    .calculateSumTicketsPrice(contract.getTicketPrice(), 1));
        }
    }

    private List<Contract> getAllContract() throws DBException {
        countExhibitions = factoryDB.createExhibitionContract(connection)
                .getNumberOfContractsAfterDate(java.sql.Date.valueOf(date));
        countNumberOfPages();
        start = currentPage * recordsPerPage - recordsPerPage;
        return factoryDB.createExhibitionContract(connection)
                .getContractsAfterDateLimit(java.sql.Date.valueOf(date), start, recordsPerPage);
    }

    private void getDate(HttpServletRequest req) {
        date = req.getParameter("searchDate");
        if (date == null || date.length() < 1) {
            date = getTodayDate();
        }

    }

    private String getTodayDate() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date now = new Date();
        return format.format(now);
    }

}


