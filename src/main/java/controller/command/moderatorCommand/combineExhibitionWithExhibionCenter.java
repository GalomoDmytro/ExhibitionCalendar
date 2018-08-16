package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.Links;
import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.Exhibition;
import entities.ExhibitionCenter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class combineExhibitionWithExhibionCenter implements Command {

    private Connection connection;
    private FactoryMySql factoryMySql;
    private String idExhibition;
    private String expoCenterId;

    private static final String FROM_EXPO = "fromExpo";
    private static final String FROM_CENTER = "fromCenter";

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher;

        if (!rolePermit(req)) {
            dispatcher = req.getRequestDispatcher(Links.HOME_PAGE);
        } else {
            dispatcher = req.getRequestDispatcher(Links.MODERATOR_COMBO_EXPO_WITH_HALL_PAGE);
        }

        if (req.getParameter("expoCenterId") != null) {
            combineWithCenter(req);
        } else {
            combineWithExpo(req);
        }

        dispatcher.forward(req, resp);
    }

    private void combineWithExpo(HttpServletRequest req) {
        idExhibition = req.getParameter("idExhibition");
        req.setAttribute("combFrom", FROM_EXPO);
        req.setAttribute("combFromId", idExhibition);

        handleConnection();
        try {
            List<ExhibitionCenter> exhibitionCentersList
                    = factoryMySql.createExhibitionCenter(connection).getAllExhibitionCenter();
            req.setAttribute("list", exhibitionCentersList);
        } catch (Exception exception) {

        } finally {
            closeConnection();
        }
    }

    private void combineWithCenter(HttpServletRequest req) {
        expoCenterId = req.getParameter("expoCenterId");
        req.setAttribute("combFrom", FROM_CENTER);
        req.setAttribute("combFromId", expoCenterId);

        handleConnection();
        try {
            List<Exhibition> exhibitionList
                    = factoryMySql.createExhibition(connection).getAllExhibition();
            req.setAttribute("list", exhibitionList);
        } catch (Exception exception) {

        } finally {
            closeConnection();
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
