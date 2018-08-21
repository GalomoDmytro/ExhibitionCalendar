package controller.command;

import dao.Connection.ConnectionPoolMySql;
import dao.mysql.ExhibitionContractMySql;
import dao.mysql.FactoryMySql;
import entities.Contract;
import entities.Exhibition;
import entities.ExhibitionCenter;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

public class ExpoInfo implements Command {

    private Connection connection;
    private FactoryMySql factoryMySql;
    private Integer idContact;
    private Exhibition exhibition;
    private ExhibitionCenter exhibitionCenter;
    private Contract contract;

    private static final Logger LOGGER = Logger.getLogger(ExhibitionContractMySql.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.EXHIBITION_INFO_PAGE);

        showExhibition(req);

        dispatcher.forward(req, resp);
    }

    private void showExhibition(HttpServletRequest request) {
        getExhibitionId(request);

        getEntities();

    }

    private void getExhibitionId(HttpServletRequest request) {
        if( request.getParameter("idContract") != null) {
            idContact = Integer.parseInt(request.getParameter("idContract"));
        }

    }

    private void getEntities() {
        handleConnection();
        contract = new Contract();
        exhibition = new Exhibition();
        exhibitionCenter = new ExhibitionCenter();

        try {
            if(idContact != null)
            {
                factoryMySql.createExhibitionContract(connection).prepareCEC(contract, exhibition,
                        exhibitionCenter, idContact);

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
