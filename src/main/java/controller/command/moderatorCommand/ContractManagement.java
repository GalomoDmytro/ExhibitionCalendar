package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.Links;
import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.Contract;
import entities.Exhibition;
import entities.ExhibitionCenter;
import exceptions.DBException;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ContractManagement implements Command {

    private Connection connection;
    private FactoryMySql factoryMySql;

    private static final Logger LOGGER = Logger.getLogger(ContractManagement.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher;

        dispatcher = req.getRequestDispatcher(Links.MODERATOR_CONTRACT_PAGE);

        if (req.getParameter("search") != null && (req.getParameter("searchField") == null || req.getParameter("searchField").trim().length() == 0)) {
            showAll(req);
        } else if (req.getParameter("search") != null) {
            specificSearch(req);
        }

        if (req.getParameter("idDelete") != null) {
            deleteById(req);
        }

        dispatcher.forward(req, resp);
    }

    private void showAll(HttpServletRequest req) {
        handleConnection();
        try {
            List<Contract> contracts = getAllContractsFromDb();
            if (contracts != null) {
                setSupportContractInfo(contracts);
                req.setAttribute("listContract", contracts);
            }
        } catch (Exception exception) {
        } finally {
            closeConnection();
        }
    }

    // get and set title from exhibition and exhibition center
    private void setSupportContractInfo(List<Contract> contractList) throws DBException {
        for (Contract contract : contractList) {
            Exhibition exhibition = factoryMySql.createExhibition(connection).getExhibitionById(contract.getExhibitionId());
            contract.setExhibitionTitle(exhibition.getTitle());
            ExhibitionCenter center = factoryMySql.createExhibitionCenter(connection).getExhibitionCenterById(contract.getExCenterId());
            contract.setExhibitionCenterTitle(center.getTitle());
        }
    }

    private void deleteById(HttpServletRequest req) {
        handleConnection();
        try {
            // TODO: make available only for admin
            String id = req.getParameter("idDelete");
            if (id != null) {
                factoryMySql.createExhibitionContract(connection).deleteContractById(Integer.valueOf(id));
            }
        } catch (Exception exception) {
        } finally {
            closeConnection();
        }
    }

    private void specificSearch(HttpServletRequest req) {
        String looking = req.getParameter("searchField");

        handleConnection();
        try {
            // TODO make search with exhibition table and center table
            List<Contract> contractList = factoryMySql.createExhibitionContract(connection).getAllContractsBySearch(looking);

            setSupportContractInfo(contractList);

            if (contractList != null) {
                req.setAttribute("listContract", contractList);
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection();
        }
    }

    private List<Contract> getAllContractsFromDb() throws DBException {
        return factoryMySql.createExhibitionContract(connection).getAllContracts();
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