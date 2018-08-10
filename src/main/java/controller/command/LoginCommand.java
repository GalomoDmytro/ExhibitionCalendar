package controller.command;

import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import dao.mysql.UserMySql;
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

public class LoginCommand implements Command {

    private static final Logger log = Logger.getLogger(RegistrationCommand.class);
    private Connection connection;
    private FactoryMySql factoryMySql;

    private String eMail;
    private String password;
    private User user;
    private PasswordHandler passwordHandler;

    private static final String WRONG = "wrong password or eMail";

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        collectParamsFromRequest(req);
        handleConnection();

        RequestDispatcher dispatcher;
        if (identificateUser(req)) {
            req.setAttribute("errorLogin", "");
            dispatcher = req.getRequestDispatcher(Links.HOME_PAGE);
        } else {
            req.setAttribute("errorLogin", WRONG);
            dispatcher = req.getRequestDispatcher(Links.LOGIN_PAGE);
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
        try {
            user = factoryMySql.createUser(connection).getByMail(eMail);
            log.info("user : " + user);
            return user;
        } catch (Exception exception) {
            log.error(exception);
        }

        return null;
    }

    private boolean comparePassword() {
        passwordHandler = new PasswordHandler();
        log.info("compare password " + passwordHandler.comparePassword(password, user.getPassword()));
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
        log.info("get param " + eMail + " pas " + password);
    }
}
