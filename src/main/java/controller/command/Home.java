package controller.command;

import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.Contract;
import exceptions.DBException;
import org.apache.log4j.Logger;

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
    private String dateLine;
    private String searchLine;
    private int countExhibitions;
    private int numberOfPages;
    private int currentPage;

    private static final int recordsPerPage = 2;
    private static final Logger LOGGER = Logger.getLogger(Home.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.HOME_PAGE);

        if(req.getParameter("currentPage") == null) {
            LOGGER.info("currentPage null");
            currentPage = 1;
        } else {
            currentPage = Integer.valueOf(req.getParameter("currentPage"));
            LOGGER.info("currentPage are" + currentPage);
        }

        showAll(req);

//        if (req.getParameter("searchField") != null) {
//            req.setAttribute("searchField", req.getParameter("searchField"));
//            specificSearch(req);
//        }

        req.setAttribute("numberOfPages", numberOfPages);

        dispatcher.forward(req, resp);
    }

    private void specificSearch(HttpServletRequest req) {

        handleConnection();

        try {
            // TODO: change date
            searchLine = req.getParameter("searchField");
            countExhibitions = factoryMySql.createExhibitionContract(connection).getNumberOfContractsAfterDate(java.sql.Date.valueOf(getTodayDate()));
            numberOfPages = countExhibitions / recordsPerPage;
            if(countExhibitions % recordsPerPage > 0) {
                numberOfPages++;
            }
            List<Contract> contractList = factoryMySql.createExhibitionContract(connection)
                    .searchContactsWithExpoAndCenter(searchLine, java.sql.Date.valueOf(getTodayDate()));

            req.setAttribute("listForCustomer", contractList);

        } catch (Exception exception) {

        } finally {
            closeConnection();
        }
    }

    private void showAll(HttpServletRequest req) {

        handleConnection();

        try {
            List<Contract> listContract = getAllContractFromToday();

            for(Contract contract : listContract) {
                LOGGER.info(contract);
            }

            req.setAttribute("listForCustomer", listContract);
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection();
        }
    }

    private List<Contract> getAllContractFromToday() throws DBException {
        String strDate = getTodayDate();
        countExhibitions = factoryMySql.createExhibitionContract(connection).getNumberOfContractsAfterDate(java.sql.Date.valueOf(strDate));
        numberOfPages = countExhibitions / recordsPerPage;
        if(countExhibitions % recordsPerPage > 0) {
            numberOfPages++;
        } // remove form here
        int start = currentPage * recordsPerPage - recordsPerPage;
        LOGGER.info("cur page " + currentPage + " show from " + start + " record per page " + recordsPerPage);
        return factoryMySql.createExhibitionContract(connection).getContractsAfterDateLimit(java.sql.Date.valueOf(strDate), start, recordsPerPage);
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


