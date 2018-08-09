package controller.command;

import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import dao.mysql.UserMySql;
import entities.User;
import utility.PasswordHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginCommand implements Command {
    private String name;
    private String password;
    private User user;
    private PasswordHandler passwordHandler;

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        name = req.getParameter("name");
        password = req.getParameter("password");

        RequestDispatcher dispatcher;
        if (identificateUser(req)) {

        } else {

        }
        dispatcher = req.getRequestDispatcher(Links.LOGIN_PAGE);
        dispatcher.forward(req, resp);
    }

    private boolean identificateUser(HttpServletRequest req) {
        user = getUserFromDB();
        if (user == null) {
            return false;
        }

        if (comparePassword()) {
            setRoleInSession(req);
            return true;
        }

        return false;
    }

    private User getUserFromDB() {
        try {
            Connection connection = ConnectionPoolMySql.getInstance().getConnection();
            FactoryMySql factoryMySql = new FactoryMySql();
            UserMySql userMySql = (UserMySql) factoryMySql.createUser(connection);
            return userMySql.getByName(name);
        } catch (Exception exception) {

        }

        return null;
    }

    private boolean comparePassword() {
        passwordHandler = new PasswordHandler();
        passwordHandler.comparePassword(password, user.getPassword());
        return false;
    }

    private void setRoleInSession(HttpServletRequest req) {
        HttpSession session = req.getSession(true);

        if (user != null) {
            session.setAttribute("role", user.getRole());
            session.setAttribute("idUser", user.getId());
        }

    }
}
