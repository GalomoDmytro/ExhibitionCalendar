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
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExhibitionManagement implements Command {

    private Connection connection;
    private FactoryMySql factoryMySql;

    private static final Logger LOGGER = Logger.getLogger(ExhibitionManagement.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher;
        handleConnection();

        dispatcher = req.getRequestDispatcher(Links.MODERATOR_MANAGE_EXPO_PAGE);

        if (req.getParameter("search") != null && (req.getParameter("searchField") == null || req.getParameter("searchField").trim().length() == 0)) {
            showAll(req);
        } else if (req.getParameter("search") != null) {
            specificSearch(req);
        }

        if (req.getParameter("idDelete") != null) {
            deleteById(req);
        }

        dispatcher.forward(req, resp);
    }

    private void deleteById(HttpServletRequest req) {
        // todo make available only for admin
        String id = req.getParameter("idDelete");
        try {
            factoryMySql.createExhibition(connection).deleteById(Integer.valueOf(id));
        } catch (Exception exception) {
        } finally {
            closeConnection();
        }
    }

    private void showAll(HttpServletRequest req) {
        try {
            List<Exhibition> exhibitionCenterList = getAllFromDb();
            setLangTagsToExhibition(exhibitionCenterList);

            if (exhibitionCenterList != null) {
                req.setAttribute("listExpo", exhibitionCenterList);
            }
        } catch (Exception exception) {

        } finally {
            closeConnection();
        }
    }

    private void setLangTagsToExhibition(List<Exhibition> exhibitionCenterList) throws DBException {
        for (Exhibition exhibition : exhibitionCenterList) {
            Map<String, String> expoLang = factoryMySql.createDescriptionTable(connection).getAllDescription(exhibition);
            String langTags = "";
            for (Map.Entry<String, String> entry : expoLang.entrySet()) {
                langTags += entry.getKey() + " ";
            }
            exhibition.setLanguageTags(langTags);
        }
    }

    private void specificSearch(HttpServletRequest request) {
        String looking = request.getParameter("searchField");
//        looking = looking.toLowerCase();
//        List<Exhibition> exhibitionList = new ArrayList<>();
//        List<Exhibition> allExhibitionFromDb;

        try {
//            allExhibitionFromDb = getAllFromDb();
//            findMatchedWithLookingField(looking, exhibitionList, allExhibitionFromDb);
//
//
//
//
//            if(exhibitionList != null) {
//                request.setAttribute("listExpo", exhibitionList);
//            }
            LOGGER.info("try search");
            List<Exhibition> exhibitionList = factoryMySql.createExhibition(connection).getExhibitionBySearch(looking);
            setLangTagsToExhibition(exhibitionList);
            if (exhibitionList != null) {
                request.setAttribute("listExpo", exhibitionList);
            }
        } catch (Exception exception) {
        } finally {
            closeConnection();
        }

    }

    private void findMatchedWithLookingField(String looking, List<Exhibition> exhibitionList, List<Exhibition> allExhibitionFromDb) {
        for (Exhibition exhibition : allExhibitionFromDb) {
            if (exhibition.getTitle().toLowerCase().contains(looking) ||
                    String.valueOf(exhibition.getId()).equals(looking)) {
                exhibitionList.add(exhibition);
            }
        }
    }


    private List<Exhibition> getAllFromDb() throws DBException {
        try {
            return factoryMySql.createExhibition(connection).getAllExhibition();
        } catch (Exception exception) {
            throw new DBException(exception);
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

    private void handleConnection() {
        try {
            connection = ConnectionPoolMySql.getInstance().getConnection();
            factoryMySql = new FactoryMySql();
        } catch (Exception exception) {

        }
    }
}
