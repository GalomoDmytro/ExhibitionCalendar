package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.Links;
import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.Exhibition;
import entities.ExhibitionCenter;
import exceptions.DBException;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExhibitionManagement implements Command {

    private Connection connection;
    private FactoryMySql factoryMySql;

    private static final Logger log = Logger.getLogger(ExhibitionManagement.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher;
        handleConnection();

        if (!rolePermit(req)) {
            dispatcher = req.getRequestDispatcher(Links.HOME_PAGE);
        } else {
            dispatcher = req.getRequestDispatcher(Links.MODERATOR_MANAGE_EXPO_PAGE);
        }

        if(req.getParameter("search") != null && (req.getParameter("searchField") == null || req.getParameter("searchField").trim().length() == 0)) {
            showAll(req);
        } else if(req.getParameter("search") != null) {
//            specificSearch(req);
        }

        if(req.getParameter("idDelete") != null) {
            deleteById(req);
        }

        dispatcher.forward(req, resp);
    }

    private void deleteById(HttpServletRequest req) {
        // todo make available only for admin
        String id = req.getParameter("idDelete");
        try {
            log.info("try delete by id " + id);
            factoryMySql.createExhibition(connection).deleteById(Integer.valueOf(id));
        } catch (Exception exception) {
            log.error(exception);
        } finally {
            closeConnection();
        }
    }

    private void showAll(HttpServletRequest req) {
        try {
            List<Exhibition> exhibitionCenterList = getAllFromDb();
            for(Exhibition exhibition : exhibitionCenterList) {
                Map<String, String> expoLang = factoryMySql.createDescriptionTable(connection).getAllDescription(exhibition);
                String langTags = "";
                for(Map.Entry<String, String> entry : expoLang.entrySet()) {
                    langTags += entry.getKey() + " ";
                }
                exhibition.setLanguageTags(langTags);
            }

            if(exhibitionCenterList != null) {
                req.setAttribute("listExpo", exhibitionCenterList);
            }
        } catch (Exception exception) {

        } finally {
            closeConnection();
        }
    }



    private List<Exhibition> getAllFromDb() throws DBException {
        try {
            return factoryMySql.createExhibition(connection).getAllExhibition();
        } catch (Exception exception) {
            throw new DBException(exception);
        }
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
