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

        if (!rolePermit(req)) {
            dispatcher = req.getRequestDispatcher(Links.HOME_PAGE);
        } else {
            dispatcher = req.getRequestDispatcher(Links.MODERATOR_CONTRACT_PAGE);
        }

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
    private void setSupportContractInfo(List<Contract> contractList) throws DBException{
        for(Contract contract : contractList) {
            Exhibition exhibition = factoryMySql.createExhibition(connection).getExhibitionById(contract.getExhibitionId());
            contract.setExhibitionTitle(exhibition.getTitle());
            ExhibitionCenter center = factoryMySql.createExhibitionCenter(connection).getExhibitionCenterById(contract.getExCenterId());
            contract.setExhibitionCenterTitle(center.getTitle());
        }
    }

    private void deleteById(HttpServletRequest req) {
        handleConnection();
        try{
            // todo make available only for admin
            String id = req.getParameter("idDelete");
            if(id != null) {
                factoryMySql.createExhibitionContract(connection).deleteContractById(Integer.valueOf(id));
            }
        } catch (Exception exception) {
        } finally {
            closeConnection();
        }
    }

    private void specificSearch(HttpServletRequest req) {
        String looking = req.getParameter("searchField");
        looking = looking.toLowerCase();

        List<Contract> contractList = new ArrayList<>();
        List<Contract> allContractInDb;

        handleConnection();
        try {
            allContractInDb = getAllContractsFromDb();
            setSupportContractInfo(allContractInDb);

            findMatchedWithLookingField(looking, contractList, allContractInDb);
            if(contractList != null) {
                req.setAttribute("listContract", contractList);
            }
        } catch (Exception exception) {
        } finally {
            closeConnection();
        }
    }

    private void findMatchedWithLookingField(String looking, List<Contract> contractList,
                                             List<Contract> allContract) {
        for(Contract contract : allContract) {
            if(contract.getExCenterId().toString().equals(looking) ||
                    contract.getExhibitionId().toString().equals(looking) ||
                    contract.getExhibitionTitle().contains(looking) ||
                    contract.getExhibitionCenterTitle().contains(looking) ||
                    contract.getDateFrom().toString().equals(looking) ||
                    contract.getDateTo().toString().equals(looking)) {
                contractList.add(contract);
            }
        }
    }

    private List<Contract> getAllContractsFromDb() throws DBException {
        return factoryMySql.createExhibitionContract(connection).getAllContracts();
    }

    private boolean rolePermit(HttpServletRequest req) {
//        HttpSession session = req.getSession(true);
//        if (session.getAttribute("role") == null) {
//            return false;
//        }
//        if (session.getAttribute("role").equals(Role.ADMIN) ||
//                session.getAttribute("role").equals(Role.MODERATOR)) {
//            return true;
//        }
//
//        return false;
        return true;
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
