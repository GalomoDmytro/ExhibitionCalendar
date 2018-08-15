package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.Links;
import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.Exhibition;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditExposition implements Command {

    private Connection connection;
    private FactoryMySql factoryMySql;
    private Integer id;
    private String title;
    private String imgSrc;
    private String langTag;
    private String descriptLangText;
    private Map<String, String> langDescription = new HashMap<>();
    private Map<String, String> description;
    private Exhibition exhibition;

    private static final Logger LOGGER = Logger.getLogger(EditExposition.class);


    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher;

        readDataFromReq(req);
        setDataToReq(req);

        if (!rolePermit(req)) {
            dispatcher = req.getRequestDispatcher(Links.HOME_PAGE);
        } else {
            dispatcher = req.getRequestDispatcher(Links.MODERATOR_EDIT_EXHIBITION_PAGE);
        }

        if (req.getParameter("editExpo") != null) {
            changeExpoData(req);
            dispatcher = req.getRequestDispatcher(Links.MODERATOR_MANAGE_EXPO_PAGE);
        } else if (req.getParameter("denieEdit") != null) {
            dispatcher = req.getRequestDispatcher(Links.MODERATOR_PAGE);
        }

        dispatcher.forward(req, resp);
    }

    private void changeExpoData(HttpServletRequest req) {
        handleConnection();
        try {
            exhibition = new Exhibition();
            exhibition.setId(id);
            exhibition.setTitle(title);
            exhibition.setImgSrc(imgSrc);
            LOGGER.info("exhibit: " + exhibition.toString());

            Map<String, String> langTextToSave = new HashMap<>();
//            req.setAttribute("langText", "langText");
            // old tag to edit
            for (Map.Entry<String, String> entry : description.entrySet()) {
                langTextToSave.put(req.getParameter(entry.getKey()), req.getParameter(entry.getValue()));
                LOGGER.info("will save" + req.getParameter(entry.getKey()) + " " + req.getParameter(entry.getValue()));
            }

            // new tag to save
            if (req.getParameter("newTag") != null && req.getParameter("newTextDescription") != null) {
                if (req.getParameter("newTag").length() > 0 && req.getParameter("newTextDescription").length() > 0) {
                    langTextToSave.put(req.getParameter("newTag"), req.getParameter("newTextDescription"));
                }
            }

            factoryMySql.createExhibition(connection).updateExhibition(exhibition);

            factoryMySql.createDescriptionTable(connection).deleteAllDescriptionForExposition(exhibition);
            for (Map.Entry<String, String> entry : langTextToSave.entrySet()) {
                LOGGER.info("save" + req.getParameter(entry.getKey()) + " " + req.getParameter(entry.getValue()));
                factoryMySql.createDescriptionTable(connection).insertDescriptionById(entry.getValue(), entry.getKey(),  id);
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection();
        }
    }

    private void readDataFromReq(HttpServletRequest req) {
        if (req.getParameter("expoId") != null) {
            id = Integer.parseInt(req.getParameter("expoId"));
        }
        title = req.getParameter("title");
        imgSrc = req.getParameter("imgSrc");
    }

    private void setDataToReq(HttpServletRequest req) {
        req.setAttribute("title", title);
        req.setAttribute("idEdit", id);
        req.setAttribute("imgSrc", imgSrc);

        description = getDescription();
        req.setAttribute("mapLang", description);
    }

    private Map<String, String> getDescription() {
        handleConnection();
        Map<String, String> description = new HashMap<>();
        try {
            description = factoryMySql.createDescriptionTable(connection).getAllDescriptionById(id);
        } catch (Exception exception) {

        } finally {
            closeConnection();
        }

        return description;
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
