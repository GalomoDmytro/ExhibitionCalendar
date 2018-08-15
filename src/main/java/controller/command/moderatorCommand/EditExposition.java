package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.Links;
import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
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

        if (req.getParameter("editExpoCenter") != null) {
            changeExpoData(req);
            dispatcher = req.getRequestDispatcher(Links.MODERATOR_MANAGE_EXPO_PAGE);
        } else if (req.getParameter("denieEdit") != null) {
            dispatcher = req.getRequestDispatcher(Links.MODERATOR_PAGE);
        }

        dispatcher.forward(req, resp);
    }

    private void changeExpoData(HttpServletRequest req) {

    }

    private void readDataFromReq(HttpServletRequest req) {
        if (req.getParameter("expoId") != null) {
            id = Integer.parseInt(req.getParameter("expoId"));
        }
        title = req.getParameter("title");
    }

    private void setDataToReq(HttpServletRequest req) {
        req.setAttribute("expoTitle", title);
        req.setAttribute("idEdit", id);
        req.setAttribute("imgSrc", imgSrc);

        Map<String, String> description = getDescription();
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
