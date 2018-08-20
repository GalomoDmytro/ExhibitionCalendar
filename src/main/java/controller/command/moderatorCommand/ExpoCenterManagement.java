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
import java.util.ArrayList;
import java.util.List;

public class ExpoCenterManagement implements Command {

    private Connection connection;
    private FactoryMySql factoryMySql;

    private static final Logger log = Logger.getLogger(ExpoCenterManagement.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        RequestDispatcher dispatcher;
        handleConnection();

        dispatcher = req.getRequestDispatcher(Links.MODERATOR_MANAGE_CENTER_PAGE);

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

    private void showAll(HttpServletRequest req) {
        try {
            List<ExhibitionCenter> exhibitionCenterList = getAllFromDb();

            setPhones(exhibitionCenterList);
            if (exhibitionCenterList != null) {
                req.setAttribute("listExpoCenter", exhibitionCenterList);
            }

        } catch (Exception exception) {
            log.error(exception);
        } finally {
            closeConnection();
        }
    }

    private void setPhones(List<ExhibitionCenter> exhibitionCenterList) throws DBException {
        for (ExhibitionCenter exhibitionCenter : exhibitionCenterList) {
            exhibitionCenter.setPhone(factoryMySql.createExhibitionCenterPhone(connection).getPhones(exhibitionCenter.getId()));
        }
    }

    private void deleteById(HttpServletRequest req) {
        // todo: make available only for admin
        // TODO: make multi access safe
        String id = req.getParameter("idDelete");
        try {
            factoryMySql.createExhibitionCenter(connection).deleteExhibitionCenterById(Integer.parseInt(id));
        } catch (Exception exception) {
        } finally {
            closeConnection();
        }
    }

    private void specificSearch(HttpServletRequest request) {

        // TODO: refactor search
        String looking = request.getParameter("searchField");
//        looking = looking.toLowerCase();

//        List<ExhibitionCenter> exhibitionCenterListAll;

        try {
//            exhibitionCenterListAll = getAllFromDb();
            List<ExhibitionCenter> exhibitionCenterListResult = factoryMySql.createExhibitionCenter(connection).getExhibitionCentersBySearch(looking);
            setPhones(exhibitionCenterListResult);
            //            for(ExhibitionCenter center : exhibitionCenterListAll) {
//                if(center.getTitle().toLowerCase().contains(looking) ||
//                    center.getAddress().toLowerCase().contains(looking) ||
//                    center.getWebPage().toLowerCase().contains(looking) ||
//                    String.valueOf(center.getId()).equals(looking)) {
//                    exhibitionCenterListResult.add(center);
//                }
//            }


            if (exhibitionCenterListResult != null) {
                request.setAttribute("listExpoCenter", exhibitionCenterListResult);
            }
        } catch (Exception exception) {

        } finally {
            closeConnection();
        }

    }

    private List<ExhibitionCenter> getAllFromDb() throws DBException {
        try {
            return factoryMySql.createExhibitionCenter(connection).getAllExhibitionCenter();
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
