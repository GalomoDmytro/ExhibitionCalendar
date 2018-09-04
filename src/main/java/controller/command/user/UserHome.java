package controller.command.user;

import controller.command.Command;
import controller.command.util.Links;
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
import java.util.List;

public class UserHome implements Command {
    private Connection connection;
    private FactoryMySql factoryMySql;
    private Integer idUser;
    private User user;
    private List<String> phonesList;
    private static final Logger LOGGER = Logger.getLogger(UserHome.class);


    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.USER_INFO_PAGE);

        getDataFromSession(req);
        getUserFromDB();
        setDataToReq(req);

        dispatcher.forward(req, resp);
    }

    private void setDataToReq(HttpServletRequest request) {
        request.setAttribute("user", user);
        request.setAttribute("phonesList", phonesList);
    }

    private void getDataFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        idUser = (Integer) session.getAttribute("userId");
    }

    private void getUserFromDB() {
        handleConnection();
        try {
            user = factoryMySql.createUser(connection).getById(idUser);
            phonesList = factoryMySql.createUserPhones(connection).getPhones(user.getMail());
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
