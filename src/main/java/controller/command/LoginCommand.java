package controller.command;

import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.User;
import org.apache.log4j.Logger;
import utility.PasswordHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.ResourceBundle;

public class LoginCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(LoginCommand.class);
    private Connection connection;
    private FactoryMySql factoryMySql;

    private String eMail;
    private String password;
    private User user;
    private PasswordHandler passwordHandler;

    // TODO: make multi lang
    private static final ResourceBundle QUERIES = ResourceBundle.getBundle("strings_error_eng");

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        collectParamsFromRequest(req);

        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.LOGIN_PAGE);

        if (req.getParameter("loginBtn") != null) {
            if (identificateUser(req)) {
                dispatcher = req.getRequestDispatcher(Links.HOME_PAGE);
            } else {
                req.setAttribute("errorLogin", QUERIES.getString("ERROR_LOGIN"));
            }
        }

        dispatcher.forward(req, resp);
    }

    private boolean identificateUser(HttpServletRequest req) {
        user = getUserFromDB();
        if (user == null) {
            return false;
        }

        if (!comparePassword()) {
            return false;
        }

        setRoleInSession(req);

        return true;
    }

    private void handleConnection() {
        try {
            connection = ConnectionPoolMySql.getInstance().getConnection();
            factoryMySql = new FactoryMySql();
        } catch (Exception exception) {
        }
    }

    private User getUserFromDB() {
        handleConnection();
        try {
            user = factoryMySql.createUser(connection).getByMail(eMail);
            user.setRole(factoryMySql.createRole(connection).getRoleById(user.getId()));
            return user;
        } catch (Exception exception) {
        } finally {
            closeConnection();
        }

        return null;
    }

    private boolean comparePassword() {
        passwordHandler = new PasswordHandler();
        return passwordHandler.comparePassword(password, user.getPassword());
    }

    private void setRoleInSession(HttpServletRequest req) {
        HttpSession session = req.getSession(true);

        if (user != null) {
            session.setAttribute("role", user.getRole());
            session.setAttribute("idUser", user.getId());
        }
    }

    private void collectParamsFromRequest(HttpServletRequest request) {
        eMail = request.getParameter("eMail");
        password = request.getParameter("password");
    }

    private void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception exception) {

        }
    }
}
