package controller.command.common;

import controller.command.Command;
import controller.command.util.Links;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

public class ExpoInfo implements Command {

    private Connection connection;
    private FactoryMySql factoryMySql;
    private Integer idContact;
    private Exhibition exhibition;
    private ExhibitionCenter exhibitionCenter;
    private Contract contract;
    private String description;
    private List<String> listDescriptions;

    private String lang;

    private static final Logger LOGGER = Logger.getLogger(ExhibitionContractMySql.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.EXHIBITION_INFO_PAGE);

        showExhibition(req);

        setDataToReq(req);

        dispatcher.forward(req, resp);
    }

    private void showExhibition(HttpServletRequest request) {
        getExhibitionId(request);

        getEntities();
    }

    private void getExhibitionId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (request.getParameter("idContract") != null) {
            idContact = Integer.parseInt(request.getParameter("idContract"));
            session.setAttribute("idContract", idContact);
        } else {
            idContact = (Integer) session.getAttribute("idContract");
        }

        if (session.getAttribute("langBundle") != null) {
            lang = (String) session.getAttribute("langBundle");
        } else {
            lang = "ENG";
        }
    }

    private void setDataToReq(HttpServletRequest request) {
        request.setAttribute("exhibition", exhibition);
        request.setAttribute("ExhibitionCenter", exhibitionCenter);
        request.setAttribute("contract", contract);
        request.setAttribute("description", listDescriptions);
    }

    private void getEntities() {
        handleConnection();
        contract = new Contract();
        exhibition = new Exhibition();
        exhibitionCenter = new ExhibitionCenter();

        try {
            if (idContact != null) {
                factoryMySql.createExhibitionContract(connection).prepareCEC(contract, exhibition,
                        exhibitionCenter, idContact);
                getDescription();
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection();
        }
    }

    private void getDescription() throws Exception {
        Map<String, String> descriptionAll = factoryMySql.createDescriptionTable(connection)
                    .getAllDescriptionById(exhibition.getId());

        listDescriptions = new ArrayList<>(descriptionAll.values());
        if(listDescriptions == null) {
            listDescriptions = Collections.emptyList();
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
