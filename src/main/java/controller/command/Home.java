package controller.command;

import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import dao.mysql.UserMySql;
import entities.User;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Home implements Command {
    private List<User> usersList = new ArrayList<>();

    private static final Logger log = Logger.getLogger(Home.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getExpoInfoFromDB();

        req.setAttribute("list", usersList);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/views/home.jsp");
        dispatcher.forward(req, resp);

    }

    // test code
    private void getExpoInfoFromDB() {
        try {
            Connection connection = ConnectionPoolMySql.getInstance().getConnection();
            FactoryMySql factoryMySql = new FactoryMySql();
            UserMySql userMySql
                    = (UserMySql) factoryMySql.createUser(connection);

            usersList = userMySql.getAllUsers();

        } catch (Exception exception) {
            log.error(exception);
        }
    }


}


