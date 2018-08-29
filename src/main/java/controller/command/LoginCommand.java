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

    private String nameOrMail;
    private String password;
    private User user;
    private PasswordHandler passwordHandler;

    private static final ResourceBundle QUERIES = ResourceBundle.getBundle("strings_error_eng");

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        collectParamsFromRequest(req);

        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.LOGIN_PAGE);

        if (req.getParameter("loginBtn") != null) {
            if (identificationUser(req)) {
                dispatcher = req.getRequestDispatcher(Links.HOME_PAGE);
            } else {
                req.setAttribute("errorLogin", QUERIES.getString("ERROR_LOGIN"));
            }
        }

        dispatcher.forward(req, resp);
    }

    private boolean identificationUser(HttpServletRequest req) {
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
            if(!factoryMySql.createUser(connection).isNameOrMailInTable(nameOrMail)) {
                return null;
            }
            LOGGER.info("name or mail in ");
            if(nameOrMail.contains("@")) {
                LOGGER.info(" mail in and pars");
                user = factoryMySql.createUser(connection).getByMail(nameOrMail);
            } else {
                LOGGER.info(" name in and pars");
                user = factoryMySql.createUser(connection).getByName(nameOrMail);
            }
            user.setRole(factoryMySql.createRole(connection).getRoleById(user.getId()));
            return user;
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection();
        }

        return null;
    }

    private boolean comparePassword() {
        passwordHandler = new PasswordHandler();
        return passwordHandler.encryptAndCompare(password, user.getPassword());
    }

    private void setRoleInSession(HttpServletRequest req) {
        HttpSession session = req.getSession(true);

        if (user != null) {
            session.setAttribute("role", user.getRole());
            session.setAttribute("idUser", user.getId());
        }
    }

    private void collectParamsFromRequest(HttpServletRequest request) {
        nameOrMail = request.getParameter("nameOrMail");
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
