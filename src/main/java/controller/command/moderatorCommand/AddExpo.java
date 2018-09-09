package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.ServletHelper;
import controller.command.util.Links;
import entities.Exhibition;
import org.apache.log4j.Logger;
import utility.Patterns;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Dmytro Galomko
 */
public class AddExpo extends ServletHelper implements Command {

    private String expoTitle;
    private String expoImg;
    private String expoDescription;
    private String descriptionKeyLang;
    private Exhibition exhibition;
    private Integer idModerator;

    private static final Logger LOGGER = Logger.getLogger(AddExpo.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        getDataFromSession(req);

        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.MODERATOR_ADD_EXPO_PAGE);

        collectParamsFromRequest(req);

        if (req.getParameter("addNewExpo") != null) {
            addNewExpo(req);
        }

        dispatcher.forward(req, resp);
    }

    private void getDataFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        idModerator = (Integer) session.getAttribute("userId");
    }

    /**
     * Contain methods to put Exhibition in DB
     *
     * @param request
     */
    private void addNewExpo(HttpServletRequest request) {
        if (!inputDataIsValid()) {
            LOGGER.error("input val is not valid");
            return;
        }

        prepareExpo();

        insertInDB(request);
    }

    /**
     * Insert Exhibition and description for Exhibition in DB
     *
     * @param request
     */
    private void insertInDB(HttpServletRequest request) {
        handleConnection(LOGGER);
        try {
            factoryDB.createExhibition(connection).insertExhibition(exhibition);
            checkDescriptionLangKey();
            factoryDB.createDescriptionTable(connection)
                    .insertDescription(descriptionKeyLang, expoDescription, exhibition);
            LOGGER.info("Moderator id: " + idModerator
                    + " insert new exhibition: " + exhibition
                    + " with description: " + expoDescription
                    + " for language: " + descriptionKeyLang);
            request.setAttribute("confirmAdd", "Exhibition center added");
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }
    }

    // check description language key length.
    private void checkDescriptionLangKey() {
        if (descriptionKeyLang.length() > 15) {
            descriptionKeyLang = descriptionKeyLang.substring(0, 15);
        }
    }

    private void prepareExpo() {
        exhibition = new Exhibition();
        exhibition.setTitle(expoTitle);
        exhibition.setImgSrc(expoImg);
    }

    /**
     * Check data from request
     *
     * @return true if data valid
     */
    private boolean inputDataIsValid() {
        if (!titleValid()) {
            return false;
        }

        if (!expoDescriptionIsValid()) {
            return false;
        }

        return true;
    }

    private boolean expoDescriptionIsValid() {
        if (expoDescription == null) {
            return false;
        }

        return true;
    }

    private boolean titleValid() {
        if (expoTitle == null) {
            return false;
        }

        if (!expoTitle.matches(Patterns.TITLE_EXPO)) {
            return false;
        }
        return true;
    }

    private void collectParamsFromRequest(HttpServletRequest req) {
        expoTitle = req.getParameter("title");
        expoImg = req.getParameter("imgSrc");
        expoDescription = req.getParameter("description");
        descriptionKeyLang = req.getParameter("lang");
    }

}
