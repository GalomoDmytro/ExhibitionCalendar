package controller.command.common;

import controller.command.Command;
import controller.command.ServletHelper;
import controller.command.util.Links;
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
import java.util.*;

public class ExpoInfo extends ServletHelper implements Command {

    private Integer idContact;
    private Exhibition exhibition;
    private ExhibitionCenter exhibitionCenter;
    private Contract contract;
    private List<String> listDescriptions;

    private static final Logger LOGGER = Logger.getLogger(ExpoInfo.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = req
                .getRequestDispatcher(Links.EXHIBITION_INFO_PAGE);

        getData(req);

        setDataToReq(req);

        dispatcher.forward(req, resp);
    }

    private void getData(HttpServletRequest request) {
        getDataFromReq(request);

        getEntities();
    }


    private void getDataFromReq(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (request.getParameter("idContract") != null) {
            idContact = Integer.parseInt(request.getParameter("idContract"));
            session.setAttribute("idContract", idContact);
        } else {
            idContact = (Integer) session.getAttribute("idContract");
        }

    }

    private void setDataToReq(HttpServletRequest request) {
        request.setAttribute("exhibition", exhibition);
        request.setAttribute("ExhibitionCenter", exhibitionCenter);
        request.setAttribute("contract", contract);
        request.setAttribute("description", listDescriptions);
    }

    /**
     * Get Contract, Exhibition, ExhibitionCenter from DB
     */
    private void getEntities() {
        handleConnection(LOGGER);
        contract = new Contract();
        exhibition = new Exhibition();
        exhibitionCenter = new ExhibitionCenter();

        try {
            if (idContact != null) {
                factoryDB.createExhibitionContract(connection).prepareCEC(contract, exhibition,
                        exhibitionCenter, idContact);
                getDescription();
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }
    }

    private void getDescription() throws Exception {
        Map<String, String> descriptionAll = factoryDB.createDescriptionTable(connection)
                .getAllDescriptionById(exhibition.getId());

        listDescriptions = new ArrayList<>(descriptionAll.values());
        if (listDescriptions == null) {
            listDescriptions = Collections.emptyList();
        }
    }

}
