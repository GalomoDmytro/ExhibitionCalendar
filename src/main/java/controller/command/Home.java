package controller.command;

import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.Contract;
import exceptions.DBException;
import org.apache.log4j.Logger;
import utility.Patterns;
import utility.PriceTicket;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Home implements Command {
    private Connection connection;
    private FactoryMySql factoryMySql;
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

        toFindCurrentPage(req);

        getDate(req);

        choseWhatToShow(req);

        saveAtrToReq(req);

        dispatcher.forward(req, resp);
    }

    private void toFindCurrentPage(HttpServletRequest req) {
        if (req.getParameter("currentPage") == null) {
            currentPage = 1;
        } else {
            currentPage = Integer.valueOf(req.getParameter("currentPage"));
        }
    }

    private void saveAtrToReq(HttpServletRequest req) {
        req.setAttribute("searchDate", req.getParameter("searchDate"));
        req.setAttribute("searchDateLine", req.getParameter("searchDate"));
        req.setAttribute("searchField", req.getParameter("searchField"));
        req.setAttribute("numberOfPages", numberOfPages);
        req.setAttribute("currentPage", currentPage);
    }

    private void choseWhatToShow(HttpServletRequest req) {
        if (req.getParameter("searchField") != null
                && req.getParameter("searchField").length() > 0) {
            specificSearch(req);
        } else {
            showAll(req);
        }
    }


    private void specificSearch(HttpServletRequest req) {

        handleConnection();

        try {

            searchLine = req.getParameter("searchField");
            countExhibitions = factoryMySql.createExhibitionContract(connection)
                    .getNumberOfContractsAfterSearch(searchLine, java.sql.Date.valueOf(date));
            countNumberOfPages();
            List<Contract> contractList = factoryMySql.createExhibitionContract(connection)
                    .searchContactsWithExpoAndCenterLimit(searchLine, java.sql.Date.valueOf(date),
                            start, recordsPerPage);

            transformPrice(contractList);
            req.setAttribute("listForCustomer", contractList);

        } catch (Exception exception) {
        } finally {
            closeConnection();
        }
    }

    private void countNumberOfPages() {
        numberOfPages = countExhibitions / recordsPerPage;
        if (countExhibitions % recordsPerPage > 0) {
            numberOfPages++;
        }
    }

    private void showAll(HttpServletRequest req) {
        handleConnection();

        try {
            List<Contract> listContract = getAllContract();
            transformPrice(listContract);
            req.setAttribute("listForCustomer", listContract);
        } catch (Exception exception) {
        } finally {
            closeConnection();
        }
    }

    private void transformPrice(List<Contract> listContract) {
        for(Contract contract : listContract) {
            contract.setTicketPrice(priceTicket
                    .calculateSumTicketsPrice(contract.getTicketPrice(), 1));
        }
    }

    private List<Contract> getAllContract() throws DBException {
        countExhibitions = factoryMySql.createExhibitionContract(connection)
                .getNumberOfContractsAfterDate(java.sql.Date.valueOf(date));
        countNumberOfPages();
        start = currentPage * recordsPerPage - recordsPerPage;
        return factoryMySql.createExhibitionContract(connection)
                .getContractsAfterDateLimit(java.sql.Date.valueOf(date), start, recordsPerPage);
    }

    private void getDate(HttpServletRequest req) {
        date = req.getParameter("searchDate");
        if(date == null || date.length() < 1) {
            date = getTodayDate();
        }
    }

    private String getTodayDate() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date now = new Date();
        return format.format(now);
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


