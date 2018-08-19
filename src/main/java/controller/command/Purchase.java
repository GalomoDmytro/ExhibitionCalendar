package controller.command;

import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.User;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;

public class Purchase implements Command {
    private Connection connection;
    private String userId;
    private FactoryMySql factoryMySql;

    private static final Logger LOGGER = Logger.getLogger(Purchase.class);


    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.PURCHASE_PAGE);

        roleMail(req);

        if(req.getParameter("cancel") != null) {
            dispatcher = req.getRequestDispatcher(Links.HOME_PAGE);
        } if(req.getParameter("buy") != null) {

        }

        dispatcher.forward(req, resp);
    }

    private void roleMail(HttpServletRequest req) {
        HttpSession session = req.getSession(true);

        if (session.getAttribute("idUser") != null) {
           userId = session.getAttribute("idUser").toString();
        }

        LOGGER.info("id user" + userId);

        handleConnection();
        try {
            User user = factoryMySql.createUser(connection).getById(Integer.parseInt(userId));
            LOGGER.info(user);
            req.setAttribute("eMailHold", user.getMail());
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection();
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
