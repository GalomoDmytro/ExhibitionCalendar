package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.Links;
import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.Exhibition;
import exceptions.DBException;
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
    private Map<String, String> descriptioToSave;

    private String title;
    private String imgSrc;
    private boolean goModeratorHome;

    private static final Logger LOGGER = Logger.getLogger(EditExposition.class);


    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.MODERATOR_EDIT_EXHIBITION_PAGE);

        getDataFromReq(req);

        getDataFromDB();

        LOGGER.info("ready to chose action");
        choseAction(req);

        LOGGER.info("ready to set data to req");
        setDataToReq(req);

        if (goModeratorHome) {
            goModeratorHome = false;
            dispatcher = req.getRequestDispatcher(Links.MODERATOR_MANAGE_EXPO_PAGE);
            dispatcher.forward(req, resp);
        } else {
            dispatcher.forward(req, resp);
        }
    }

    private void choseAction(HttpServletRequest request)
            throws ServletException, IOException {
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
//            factoryMySql.createDescriptionTable(connection).setLockDescriptionTable();
            connection.setAutoCommit(false);

            factoryMySql.createExhibition(connection).updateExhibition(exhibition);

            factoryMySql.createDescriptionTable(connection).deleteAllDescriptionForExposition(exhibition);
            for (Map.Entry<String, String> entry : descriptioToSave.entrySet()) {
                factoryMySql.createDescriptionTable(connection)
                        .insertDescriptionById(entry.getKey(), entry.getValue(), exhibitionId);
            }

            connection.commit();

        } catch (Exception e) {
            request.setAttribute("error", "Some trouble with saving.");
            LOGGER.error(e);
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (Exception e) {

            }
            try {
                factoryMySql.createTicket(connection).unlockTable();
            } catch (Exception e) {

            }

            closeConnection();
        }

        return true;
    }

    private void collectDataToSave(HttpServletRequest request) {
        descriptioToSave = new HashMap<>();
        // get all descriptions
        for (Map.Entry<String, String> entry : descriptionMap.entrySet()) {
            String key = request.getParameter(entry.getKey());
            String value = request.getParameter("val:" + entry.getKey());
            LOGGER.info("key " + key + " value " + value);
            if (key != null && value != null
                    && key.length() > 0 && value.length() > 0) {
                descriptioToSave.put(key, value);
            }
        }

        if (request.getParameter("newTextLabel") != null &&
                request.getParameter("newTextDescription") != null
                && request.getParameter("newTextLabel").length() > 0
                && request.getParameter("newTextDescription").length() > 0
                && request.getParameter("newTextLabel").length() > 0
        ) {
            descriptioToSave.put(request.getParameter("newTextLabel"),
                    request.getParameter("newTextDescription"));
        }

        LOGGER.info("desc to save");
        LOGGER.info(descriptioToSave);
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
            LOGGER.info("form table: " + exhibition);
            LOGGER.info("form table: " + descriptionMap);


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

        LOGGER.info("id: " + exhibitionId);
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

    //    private Connection connection;
//    private FactoryMySql factoryMySql;
//    private Integer id;
//    private String title;
//    private String imgSrc;
//    private Map<String, String> description;
//    private Exhibition exhibition;
//    private static final Logger LOGGER = Logger.getLogger(EditExposition.class);
//
//    @Override
//    public void execute(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//        RequestDispatcher dispatcher;
//
//        readDataFromReq(req);
//        setDataToReq(req);
//
//        dispatcher = req.getRequestDispatcher(Links.MODERATOR_EDIT_EXHIBITION_PAGE);
//
//        if (req.getParameter("editExpo") != null) {
//            changeExpoData(req);
//            dispatcher = req.getRequestDispatcher(Links.MODERATOR_MANAGE_EXPO_PAGE);
//        } else if (req.getParameter("deniedEdit") != null) {
//            dispatcher = req.getRequestDispatcher(Links.MODERATOR_PAGE);
//        }
//
//        dispatcher.forward(req, resp);
//    }
//
//    private void changeExpoData(HttpServletRequest req) {
//        handleConnection();
//        try {
//            prepareExhibition();
//            Map<String, String> langTextToSave = new HashMap<>();
//
//            // will change language description witch exist, if necessary
//            forChangeDescription(req, langTextToSave);
//
//            // add new language tag and description
//            newTagToSave(req, langTextToSave);
//
//            factoryMySql.createExhibition(connection).updateExhibition(exhibition);
//
//            makeChangeInDescriptionTable(langTextToSave);
//        } catch (Exception exception) {
//            LOGGER.error(exception);
//        } finally {
//            closeConnection();
//        }
//    }
//
//    private void makeChangeInDescriptionTable(Map<String, String> langTextToSave) throws DBException {
//        factoryMySql.createDescriptionTable(connection).deleteAllDescriptionForExposition(exhibition);
//        for (Map.Entry<String, String> entry : langTextToSave.entrySet()) {
//            factoryMySql.createDescriptionTable(connection).insertDescriptionById(entry.getValue(),
//                    entry.getKey(), id);
//        }
//    }
//
//    private void newTagToSave(HttpServletRequest req, Map<String, String> langTextToSave) {
//        if (req.getParameter("newTag") != null && req.getParameter("newTextDescription") != null) {
//            if (req.getParameter("newTag").length() > 0 &&
//                    req.getParameter("newTextDescription").length() > 0) {
//                langTextToSave.put(req.getParameter("newTag"),
//                        req.getParameter("newTextDescription"));
//            }
//        }
//    }
//
//    private void forChangeDescription(HttpServletRequest req, Map<String, String> langTextToSave) {
//        for (Map.Entry<String, String> entry : description.entrySet()) {
//            langTextToSave.put(req.getParameter(entry.getKey()), req.getParameter(entry.getValue()));
//        }
//    }
//
//    private void prepareExhibition() {
//        exhibition = new Exhibition();
//        exhibition.setId(id);
//        exhibition.setTitle(title);
//        exhibition.setImgSrc(imgSrc);
//    }
//
//    private void readDataFromReq(HttpServletRequest req) {
//        if (req.getParameter("expoId") != null) {
//            id = Integer.parseInt(req.getParameter("expoId"));
//        }
//        title = req.getParameter("title");
//        imgSrc = req.getParameter("imageSrc");
//    }
//
//    private void setDataToReq(HttpServletRequest req) {
//        req.setAttribute("title", title);
//        req.setAttribute("idEdit", id);
//        req.setAttribute("imgSrc", imgSrc);
//
//        description = getDescription();
//        req.setAttribute("mapLang", description);
//    }
//
//    private Map<String, String> getDescription() {
//        handleConnection();
//        Map<String, String> description = new HashMap<>();
//        try {
//            description = factoryMySql.createDescriptionTable(connection).getAllDescriptionById(id);
//        } catch (Exception exception) {
//
//        } finally {
//            closeConnection();
//        }
//
//        return description;
//    }
//
//    private void closeConnection() {
//        try {
//            if (connection != null) {
//                connection.close();
//            }
//        } catch (Exception exception) {
//
//        }
//    }
//
//    private void handleConnection() {
//        try {
//            connection = ConnectionPoolMySql.getInstance().getConnection();
//            factoryMySql = new FactoryMySql();
//        } catch (Exception exception) {
//
//        }
//    }
}
