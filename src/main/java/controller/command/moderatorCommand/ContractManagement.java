package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.Links;
import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.Contract;
import entities.Exhibition;
import entities.ExhibitionCenter;
import entities.Ticket;
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

        if (req.getParameter("search") != null && (req.getParameter("searchField") == null
                || req.getParameter("searchField").trim().length() == 0)) {
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
                req.setAttribute("listContract", contracts);
            }
        } catch (Exception exception) {
        } finally {
            closeConnection();
        }
    }

    private void deleteById(HttpServletRequest req) {
        handleConnection();
        try {
            String idContractDel = req.getParameter("idDelete");
            if (idContractDel != null) {
                int idC = Integer.parseInt(idContractDel);
                List<Ticket> soldTickets = factoryMySql.createTicket(connection).getAllTicketsForContract(idC);
                if(soldTickets.isEmpty()) {
                    factoryMySql.createExhibitionContract(connection).deleteContractById(idC);
                } else {
                    req.setAttribute("errorDeleting", "Сan not be deleted. Already sold some tickets!");
                }
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
            List<Contract> contractList = factoryMySql.createExhibitionContract(connection)
                    .getAllContractsBySearch(looking);

            if (contractList != null) {
                req.setAttribute("listContract", contractList);
            }
        } catch (Exception exception) {
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