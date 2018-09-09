package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.ServletHelper;
import controller.command.util.Links;
import entities.Exhibition;
import exceptions.DBException;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Dmytro Galomko
 */
public class ExhibitionManagement extends ServletHelper implements Command {

    private static final Logger LOGGER = Logger.getLogger(ExhibitionManagement.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = req
                .getRequestDispatcher(Links.MODERATOR_MANAGE_EXPO_PAGE);

        getExhibitionsToShow(req);

        dispatcher.forward(req, resp);
    }

    private void getExhibitionsToShow(HttpServletRequest req) {
        if (req.getParameter("search") != null
                && (req.getParameter("searchField") == null
                || req.getParameter("searchField").trim().length() == 0)) {
            showAll(req);
        } else if (req.getParameter("search") != null) {
            specificSearch(req);
        }

        if (req.getParameter("idDelete") != null) {
            deleteById(req);
        }
    }

    private void deleteById(HttpServletRequest req) {
        Integer idCenter = Integer.parseInt(req.getParameter("idDelete"));
        handleConnection(LOGGER);
        try {
            if (factoryDB.createExhibitionContract(connection)
                    .getAllContractsForExhibition(idCenter).isEmpty()) {
                factoryDB.createExhibition(connection)
                        .deleteById(Integer.valueOf(idCenter));
            } else {
                req.setAttribute("errorDeleting"
                        , "Have contract for this exhibition. Do Not Delete! ");
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }
    }

    private void showAll(HttpServletRequest req) {
        handleConnection(LOGGER);
        try {
            List<Exhibition> exhibitionCenterList = getAllFromDb();
            setLangTagsToExhibition(exhibitionCenterList);

            if (exhibitionCenterList != null) {
                req.setAttribute("listExpo", exhibitionCenterList);
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }
    }

    /**
     * Find all Language Key for Descriptions
     *
     * @param exhibitionList List of Exhibitions
     * @throws DBException
     */
    private void setLangTagsToExhibition(List<Exhibition> exhibitionList)
            throws DBException {
        for (Exhibition exhibition : exhibitionList) {
            Map<String, String> expoLang = factoryDB
                    .createDescriptionTable(connection)
                    .getAllDescription(exhibition);
            String langTags = "";
            for (Map.Entry<String, String> entry : expoLang.entrySet()) {
                langTags += entry.getKey() + " ";
            }
            exhibition.setLanguageTags(langTags);
        }
    }

    /**
     * Get search parameter from search field  and
     * look matches in Exhibition Table for this parameter
     *
     * @param request
     */
    private void specificSearch(HttpServletRequest request) {
        String looking = request.getParameter("searchField");
        handleConnection(LOGGER);
        try {
            List<Exhibition> exhibitionList
                    = factoryDB.createExhibition(connection)
                    .getExhibitionBySearch(looking);
            setLangTagsToExhibition(exhibitionList);
            if (exhibitionList != null) {
                request.setAttribute("listExpo", exhibitionList);
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }

    }

    /**
     * Get all Exhibitions from DB
     *
     * @return List of Exhibitions
     * @throws DBException
     */
    private List<Exhibition> getAllFromDb() throws DBException {
        try {
            return factoryDB.createExhibition(connection).getAllExhibition();
        } catch (Exception exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

}
