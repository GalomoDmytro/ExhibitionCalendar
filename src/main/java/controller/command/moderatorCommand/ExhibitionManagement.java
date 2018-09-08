package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.util.Links;
import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.Exhibition;
import exceptions.DBException;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class ExhibitionManagement implements Command {

    private Connection connection;
    private FactoryMySql factoryMySql;

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
        handleConnection();
        try {
            if (factoryMySql.createExhibitionContract(connection)
                    .getAllContractsForExhibition(idCenter).isEmpty()) {
                factoryMySql.createExhibition(connection)
                        .deleteById(Integer.valueOf(idCenter));
            } else {
                req.setAttribute("errorDeleting"
                        , "Have contract for this exhibition. Do Not Delete! ");
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection();
        }
    }

    private void showAll(HttpServletRequest req) {
        handleConnection();
        try {
            List<Exhibition> exhibitionCenterList = getAllFromDb();
            setLangTagsToExhibition(exhibitionCenterList);

            if (exhibitionCenterList != null) {
                req.setAttribute("listExpo", exhibitionCenterList);
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection();
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
            Map<String, String> expoLang = factoryMySql
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
        handleConnection();
        try {
            List<Exhibition> exhibitionList
                    = factoryMySql.createExhibition(connection)
                    .getExhibitionBySearch(looking);
            setLangTagsToExhibition(exhibitionList);
            if (exhibitionList != null) {
                request.setAttribute("listExpo", exhibitionList);
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection();
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
            return factoryMySql.createExhibition(connection).getAllExhibition();
        } catch (Exception exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    private void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        }
    }

    private void handleConnection() {
        try {
            connection = ConnectionPoolMySql.getInstance().getConnection();
            factoryMySql = new FactoryMySql();
        } catch (Exception exception) {
            LOGGER.error(exception);
        }
    }
}
