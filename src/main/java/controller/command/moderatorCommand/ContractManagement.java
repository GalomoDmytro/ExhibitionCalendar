package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.ServletHelper;
import controller.command.util.Links;
import entities.Contract;
import entities.Ticket;
import exceptions.DBException;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Dmytro Galomko
 */
public class ContractManagement extends ServletHelper implements Command {

    private static final Logger LOGGER = Logger.getLogger(ContractManagement.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher dispatcher;

        dispatcher = req.getRequestDispatcher(Links.MODERATOR_CONTRACT_PAGE);

        choseWhatToShow(req);

        if (req.getParameter("idDelete") != null) {
            deleteById(req);
        }

        dispatcher.forward(req, resp);
    }

    private void choseWhatToShow(HttpServletRequest req) {
        if (req.getParameter("search") != null && (req.getParameter("searchField") == null
                || req.getParameter("searchField").trim().length() == 0)) {
            showAll(req);
        } else if (req.getParameter("search") != null) {
            specificSearch(req);
        }
    }

    private void showAll(HttpServletRequest req) {
        handleConnection(LOGGER);
        try {
            List<Contract> contracts = getAllContractsFromDb();
            if (contracts != null) {
                req.setAttribute("listContract", contracts);
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }
    }

    private void deleteById(HttpServletRequest req) {
        handleConnection(LOGGER);
        try {
            String idContractDel = req.getParameter("idDelete");
            if (idContractDel != null) {
                int idC = Integer.parseInt(idContractDel);
                List<Ticket> soldTickets = factoryDB
                        .createTicket(connection).getAllTicketsForContract(idC);
                if (soldTickets.isEmpty()) {
                    factoryDB.createExhibitionContract(connection)
                            .deleteContractById(idC);
                } else {
                    req.setAttribute("errorDeleting",
                            "Сan not be deleted. Already sold some tickets!");
                }
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }
    }

    private void specificSearch(HttpServletRequest req) {
        String looking = req.getParameter("searchField");

        handleConnection(LOGGER);
        try {
            List<Contract> contractList = factoryDB.createExhibitionContract(connection)
                    .getAllContractsBySearch(looking);

            if (contractList != null) {
                req.setAttribute("listContract", contractList);
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }
    }

    private List<Contract> getAllContractsFromDb() throws DBException {
        return factoryDB.createExhibitionContract(connection).getAllContracts();
    }
}