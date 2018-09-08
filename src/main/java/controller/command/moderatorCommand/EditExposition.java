package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.ServletHelper;
import controller.command.util.Links;
import entities.Exhibition;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EditExposition extends ServletHelper implements Command {

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

        getDataFromSession(req);

        getDataFromReq(req);

        getDataFromDB();

        checkIfBtnEditExpoHasPressed(req);

        setDataToReq(req);

        if (goModeratorHome) {
            goModeratorHome = false;
            dispatcher = req.getRequestDispatcher(Links.MODERATOR_MANAGE_EXPO_PAGE);
            dispatcher.forward(req, resp);
        } else {
            dispatcher.forward(req, resp);
        }
    }

    private void getDataFromSession(HttpServletRequest req) {
        HttpSession session = req.getSession();
        idModerator = (Integer) session.getAttribute("userId");
    }

    private void checkIfBtnEditExpoHasPressed(HttpServletRequest request) {
        if (request.getParameter("editExpo") != null) {
            collectDataToSave(request);
            goModeratorHome = saveData(request);
        }
    }

    /**
     * Update Exposition Table and Describe Table
     *
     * @param request
     * @return true if don't catch any exception
     */
    private boolean saveData(HttpServletRequest request) {
        handleConnection(LOGGER);
        Exhibition exhibition = prepareExhibition();

        try {
            factoryDB.createExhibition(connection).setLockExhibitionTable();
            connection.setAutoCommit(false);

            factoryDB.createExhibition(connection).updateExhibition(exhibition);

            factoryDB.createDescriptionTable(connection)
                    .deleteAllDescriptionForExposition(exhibition);
            for (Map.Entry<String, String> entry : descriptionToSave.entrySet()) {
                factoryDB.createDescriptionTable(connection)
                        .insertDescriptionById(entry.getKey(), entry.getValue(), exhibitionId);
            }
            connection.commit();
            connection.setAutoCommit(true);
            LOGGER.info("Moderator id: " + idModerator
                    + " has set new data to expoId: " + exhibition.getId()
                    + " New data: " + exhibition);
        } catch (Exception e) {
            request.setAttribute("error", "Some trouble with saving.");
            LOGGER.error(e);
            return false;
        } finally {
            try {
                factoryDB.createTicket(connection).unlockTable();
            } catch (Exception e) {
                LOGGER.error(e);
            }

            closeConnection(LOGGER);
        }

        return true;
    }

    private Exhibition prepareExhibition() {
        return new Exhibition.Builder()
                .setId(exhibitionId)
                .setTitle(title)
                .setImgSrc(imgSrc)
                .build();
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

        if (isDescribeParamGood(request)) {
            descriptionToSave.put(request.getParameter("newTextLabel"),
                    request.getParameter("newTextDescription"));
        }

        title = request.getParameter("title");
        imgSrc = request.getParameter("imgSrc");
    }

    private boolean isDescribeParamGood(HttpServletRequest request) {
        return request.getParameter("newTextLabel") != null &&
                request.getParameter("newTextDescription") != null
                && request.getParameter("newTextLabel").length() > 0
                && request.getParameter("newTextDescription").length() > 0
                && request.getParameter("newTextLabel").length() > 0;
    }

    private void getDataFromDB() {
        handleConnection(LOGGER);

        try {
            exhibition = factoryDB.createExhibition(connection)
                    .getExhibitionById(exhibitionId);
            descriptionMap = factoryDB.createDescriptionTable(connection)
                    .getAllDescription(exhibition);

        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
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

}
