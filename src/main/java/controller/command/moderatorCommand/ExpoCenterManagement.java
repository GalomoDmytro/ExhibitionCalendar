package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.ServletHelper;
import controller.command.util.Links;
import entities.ExhibitionCenter;
import exceptions.DBException;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ExpoCenterManagement extends ServletHelper implements Command {

    private static final Logger LOGGER = Logger.getLogger(ExpoCenterManagement.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        RequestDispatcher dispatcher = req
                .getRequestDispatcher(Links.MODERATOR_MANAGE_CENTER_PAGE);

        getExhibitionsCentersToShow(req);

        if (req.getParameter("idDelete") != null) {
            deleteById(req);
        }

        dispatcher.forward(req, resp);
    }

    private void getExhibitionsCentersToShow(HttpServletRequest req) {
        if (req.getParameter("search") != null
                && (req.getParameter("searchField") == null)) {
            showAll(req);
        } else if (req.getParameter("search") != null) {
            specificSearch(req);
        }
    }

    private void showAll(HttpServletRequest req) {
        handleConnection(LOGGER);
        try {
            List<ExhibitionCenter> exhibitionCenterList = getAllFromDb();

            for (ExhibitionCenter exhibitionCenter : exhibitionCenterList) {
            }

            setPhones(exhibitionCenterList);
            if (exhibitionCenterList != null) {
                req.setAttribute("listExpoCenter", exhibitionCenterList);
            }

        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }
    }

    private void setPhones(List<ExhibitionCenter> exhibitionCenterList) throws DBException {
        for (ExhibitionCenter exhibitionCenter : exhibitionCenterList) {
            exhibitionCenter.setPhone(factoryDB
                    .createExhibitionCenterPhone(connection)
                    .getPhones(exhibitionCenter.getId()));
        }
    }

    private void deleteById(HttpServletRequest req) {

        int idExhibitionCenterDeletion = Integer
                .parseInt(req.getParameter("idDelete"));
        handleConnection(LOGGER);
        try {

            if (factoryDB.createExhibitionContract(connection)
                    .getAllContractsForCenter(idExhibitionCenterDeletion).isEmpty()) {
                factoryDB.createExhibitionCenter(connection)
                        .deleteExhibitionCenterById(idExhibitionCenterDeletion);
            } else {
                req.setAttribute("errorDeleting"
                        , "Have contract for this exhibition center. Can't Delete! ");
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }
    }

    private void specificSearch(HttpServletRequest request) {

        String looking = request.getParameter("searchField");
        handleConnection(LOGGER);
        try {
            List<ExhibitionCenter> exhibitionCenterListResult
                    = factoryDB.createExhibitionCenter(connection)
                    .getExhibitionCentersBySearch(looking);
            setPhones(exhibitionCenterListResult);

            if (exhibitionCenterListResult != null) {
                request.setAttribute("listExpoCenter", exhibitionCenterListResult);
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }

    }

    private List<ExhibitionCenter> getAllFromDb() throws DBException {
        try {
            return factoryDB.createExhibitionCenter(connection)
                    .getAllExhibitionCenter();
        } catch (Exception exception) {
            LOGGER.info(exception);
            throw new DBException(exception);
        }
    }

}
