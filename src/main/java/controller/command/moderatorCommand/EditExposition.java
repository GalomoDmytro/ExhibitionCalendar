package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.util.Links;
import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.Exhibition;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EditExposition implements Command {

    private Connection connection;
    private FactoryMySql factoryMySql;
    private Integer exhibitionId;
    private Exhibition exhibition;
    private Map<String, String> descriptionMap;
    private Map<String, String> descriptionToSave;

    private String title;
    private String imgSrc;
    private boolean goModeratorHome;

    private Integer idModerator;

    private static final Logger LOGGER = Logger.getLogger(EditExposition.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        RequestDispatcher dispatcher = req
                .getRequestDispatcher(Links.MODERATOR_EDIT_EXHIBITION_PAGE);

        getIdModerator(req);

        getDataFromReq(req);

        getDataFromDB();

        choseAction(req);

        setDataToReq(req);

        if (goModeratorHome) {
            goModeratorHome = false;
            dispatcher = req.getRequestDispatcher(Links.MODERATOR_MANAGE_EXPO_PAGE);
            dispatcher.forward(req, resp);
        } else {
            dispatcher.forward(req, resp);
        }
    }

    private void getIdModerator(HttpServletRequest req) {
        HttpSession session = req.getSession();
        idModerator = (Integer) session.getAttribute("userId");
    }

    private void choseAction(HttpServletRequest request) {
        if (request.getParameter("editExpo") != null) {

            collectDataToSave(request);

            goModeratorHome = saveData(request);

            LOGGER.info("btn save pressed");
        }
    }

    private boolean saveData(HttpServletRequest request) {
        handleConnection();
        Exhibition exhibition = new Exhibition.Builder()
                .setId(exhibitionId)
                .setTitle(title)
                .setImgSrc(imgSrc)
                .build();

        try {
            factoryMySql.createExhibition(connection).setLockExhibitionTable();
            connection.setAutoCommit(false);

            factoryMySql.createExhibition(connection).updateExhibition(exhibition);

            factoryMySql.createDescriptionTable(connection)
                    .deleteAllDescriptionForExposition(exhibition);
            for (Map.Entry<String, String> entry : descriptionToSave.entrySet()) {
                factoryMySql.createDescriptionTable(connection)
                        .insertDescriptionById(entry.getKey(), entry.getValue(), exhibitionId);
            }
            connection.commit();
            LOGGER.info("Moderator id: " + idModerator
                    + " has set new data to expoId: " + exhibition.getId()
                    + " New data: " + exhibition);
        } catch (Exception e) {
            request.setAttribute("error", "Some trouble with saving.");
            LOGGER.error(e);
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (Exception e) {
                LOGGER.error(e);
            }
            try {
                factoryMySql.createTicket(connection).unlockTable();
            } catch (Exception e) {
                LOGGER.error(e);
            }

            closeConnection();
        }

        return true;
    }

    private void collectDataToSave(HttpServletRequest request) {
        descriptionToSave = new HashMap<>();
        // get all descriptions
        for (Map.Entry<String, String> entry : descriptionMap.entrySet()) {
            String key = request.getParameter(entry.getKey());
            String value = request.getParameter("val:" + entry.getKey());
            if (key != null && value != null
                    && key.length() > 0 && value.length() > 0) {
                descriptionToSave.put(key, value);
            }
        }

        if (request.getParameter("newTextLabel") != null &&
                request.getParameter("newTextDescription") != null
                && request.getParameter("newTextLabel").length() > 0
                && request.getParameter("newTextDescription").length() > 0
                && request.getParameter("newTextLabel").length() > 0
        ) {
            descriptionToSave.put(request.getParameter("newTextLabel"),
                    request.getParameter("newTextDescription"));
        }

        title = request.getParameter("title");
        imgSrc = request.getParameter("imgSrc");
    }

    private void getDataFromDB() {
        handleConnection();

        try {
            exhibition = factoryMySql.createExhibition(connection)
                    .getExhibitionById(exhibitionId);
            descriptionMap = factoryMySql.createDescriptionTable(connection)
                    .getAllDescription(exhibition);

        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection();
        }
    }

    private void getDataFromReq(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (request.getParameter("expoId") != null) {
            exhibitionId = Integer.parseInt(request.getParameter("expoId"));
            session.setAttribute("expoId", exhibitionId);
        } else {
            exhibitionId = (Integer) session.getAttribute("expoId");
        }
    }

    private void setDataToReq(HttpServletRequest request) {
        if (exhibition != null) {
            request.setAttribute("exhibition", exhibition);
        }

        if (descriptionMap != null) {
            request.setAttribute("mapLang", descriptionMap);
        } else {
            request.setAttribute("mapLang", Collections.EMPTY_MAP);
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

    private void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        }
    }
}
