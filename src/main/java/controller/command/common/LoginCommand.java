package controller.command.common;

import controller.command.Command;
import controller.command.ServletHelper;
import controller.command.util.Links;
import entities.User;
import org.apache.log4j.Logger;
import utility.PasswordHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author Dmytro Galomko
 */
public class LoginCommand extends ServletHelper implements Command {

    private String nameOrMail;
    private String password;
    private User user;
    private PasswordHandler passwordHandler;
    private static final Logger LOGGER = Logger.getLogger(LoginCommand.class);

    private static final ResourceBundle QUERIES = ResourceBundle
            .getBundle("strings_error_eng");

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        collectParamsFromRequest(req);

        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.LOGIN_PAGE);

        if (req.getParameter("loginBtn") != null) {
            if (identificationUser(req)) {
                LOGGER.info("LOG IN: " + user);
                dispatcher = req.getRequestDispatcher(Links.HOME_PAGE);
            } else {
                req.setAttribute("errorLogin", QUERIES
                        .getString("ERROR_LOGIN"));
            }
        }

        dispatcher.forward(req, resp);
    }

    /**
     * Check if User with data from request exists?
     *
     * @param req
     * @return true if exist
     */
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

    /**
     * Find and get User form DB
     *
     * @return
     */
    private User getUserFromDB() {
        handleConnection(LOGGER);

        try {
            if (!factoryDB.createUser(connection).isNameOrMailInTable(nameOrMail)) {
                return null;
            }
            if (nameOrMail.contains("@")) {
                user = factoryDB.createUser(connection).getByMail(nameOrMail);
            } else {
                user = factoryDB.createUser(connection).getByName(nameOrMail);
            }
            user.setRole(factoryDB.createRole(connection).getRoleById(user.getId()));
            return user;
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
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
            session.setAttribute("userId", user.getId());
        }
    }

    private void collectParamsFromRequest(HttpServletRequest request) {
        nameOrMail = request.getParameter("nameOrMail");
        password = request.getParameter("password");
    }
}
