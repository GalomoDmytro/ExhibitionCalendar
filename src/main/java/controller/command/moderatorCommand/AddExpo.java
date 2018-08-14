package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.Links;
import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.Exhibition;
import org.apache.log4j.Logger;
import utility.Patterns;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

public class AddExpo implements Command {

    private Connection connection;
    private FactoryMySql factoryMySql;
    private String expoTitle;
    private String expoImg;
    private String expoDescription;
    private String descriptionLang;
    private Exhibition exhibition;

    private static final Logger log = Logger.getLogger(AddExpo.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        RequestDispatcher dispatcher;
        handleConnection();

        if (!rolePermit(req)) {
            dispatcher = req.getRequestDispatcher(Links.HOME_PAGE);
        } else {
            dispatcher = req.getRequestDispatcher(Links.MODERATOR_ADD_EXPO_PAGE);
        }

        collectParamsFromRequest(req);

        if (req.getParameter("addNewExpo") != null) {
            addNewExpo();
        }

        dispatcher.forward(req, resp);
    }

    private void addNewExpo() {
        if (!inputDataIsValid()) {
            log.error("input val is not valid");
            return;
        }

        prepareExpo();

        insertInDB();
    }

    private void insertInDB() {
        try {
            log.info("Exhibit: " + exhibition.toString());
            factoryMySql.createExhibition(connection).insertExhibition(exhibition);
            log.info("exhibit id " + exhibition.getId());
            factoryMySql.createDescriptionTable(connection)
                    .insertDescription(descriptionLang, expoDescription, exhibition);
        } catch (Exception exception) {
            log.error(exception);
        } finally {
            closeConnection();
        }
    }

    private void prepareExpo() {
        exhibition = new Exhibition();
        exhibition.setTitle(expoTitle);
        exhibition.setImgSrc(expoImg);
    }

    private boolean inputDataIsValid() {
        if (!titleValid()) {
            log.error("title not valid");
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

    private void collectParamsFromRequest(HttpServletRequest req) {
        expoTitle = req.getParameter("title");
        expoImg = req.getParameter("imgSrc");
        expoDescription = req.getParameter("description");
        descriptionLang = req.getParameter("lang");
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
}
