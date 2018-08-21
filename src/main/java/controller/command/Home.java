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

    private static final Logger LOGGER = Logger.getLogger(Home.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.HOME_PAGE);

        showAll(req);

        if (req.getParameter("searchField") != null) {
            specificSearch(req);
        }

        dispatcher.forward(req, resp);
    }

    private void specificSearch(HttpServletRequest req) {

        handleConnection();

        try {
            // TODO: change date
            searchLine = req.getParameter("searchField");
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
            for (Contract contract : listContract) {
//                LOGGER.info(contract.getExhibitionTitle() + " ******** " + contract.getExhibitionCenterTitle());
            }
            req.setAttribute("listForCustomer", listContract);
        } catch (Exception exception) {
        } finally {
            closeConnection();
        }
    }

    private List<Contract> getAllContractFromToday() throws DBException {
        String strDate = getTodayDate();
        return factoryMySql.createExhibitionContract(connection).galAllContactsWithExpoAndCenter(java.sql.Date.valueOf(strDate));
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


