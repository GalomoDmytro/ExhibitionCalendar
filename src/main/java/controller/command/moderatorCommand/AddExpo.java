package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.util.Links;
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

    private static final Logger LOGGER = Logger.getLogger(AddExpo.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        RequestDispatcher dispatcher;
        handleConnection();

        dispatcher = req.getRequestDispatcher(Links.MODERATOR_ADD_EXPO_PAGE);

        collectParamsFromRequest(req);

        if (req.getParameter("addNewExpo") != null) {
            addNewExpo();
        }

        dispatcher.forward(req, resp);
    }

    private void addNewExpo() {
        if (!inputDataIsValid()) {
            LOGGER.error("input val is not valid");
            return;
        }

        prepareExpo();

        insertInDB();
    }

    private void insertInDB() {
        try {
            factoryMySql.createExhibition(connection).insertExhibition(exhibition);
            if (descriptionLang.length() > 15) {
                descriptionLang = descriptionLang.substring(0, 15);
            }
            factoryMySql.createDescriptionTable(connection)
                    .insertDescription(expoDescription, descriptionLang, exhibition);
        } catch (Exception exception) {
            LOGGER.error(exception);
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
            LOGGER.error("title not valid");
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

}
