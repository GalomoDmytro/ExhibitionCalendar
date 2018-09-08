package controller.command.common;

import controller.command.Command;
import controller.command.ServletHelper;
import controller.command.util.Links;
import entities.Role;
import entities.User;
import org.apache.log4j.Logger;
import utility.PasswordHandler;
import utility.Patterns;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ResourceBundle;

public class RegistrationCommand extends ServletHelper implements Command {

    private String name;
    private String password;
    private String passwordRepeat;
    private String firstName;
    private String lastName;
    private String eMail;
    private String eMailRepeat;
    private String phone1;
    private String phone2;
    private User user;

    private static final Logger LOGGER = Logger.getLogger(RegistrationCommand.class);

    private static final ResourceBundle QUERIES = ResourceBundle.getBundle("strings_error_eng");

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        collectParamsFromRequest(req);

        RequestDispatcher dispatcher;
        dispatcher = tryToRegister(req);

        dispatcher.forward(req, resp);
    }

    private RequestDispatcher tryToRegister(HttpServletRequest req) {
        RequestDispatcher dispatcher;
        if (isDataGood(req)) {
            addNewUserToDB();
            declareRole(req);
            dispatcher = req.getRequestDispatcher(Links.HOME_PAGE);
        } else {
            dispatcher = req.getRequestDispatcher(Links.REGISTRATION_PAGE);
        }
        return dispatcher;
    }

    private boolean isDataGood(HttpServletRequest req) {
        if (primaryIsEmpty()) {

            return false;
        }

        if (!passwordIsGood(req)) {
            return false;
        }

        if (!eMailIsGood(req)) {
            return false;
        }
        if (!nameIsGood(req)) {
            return false;
        }

        if (!phoneIsGood()) {
            req.setAttribute("errorPhone", "bad phone number");
            return false;
        }
        return true;
    }

    private boolean phoneIsGood() {

        if (phone1 != null) {
            if (phone1.length() > Patterns.PHONE_LENGTH) {
                return false;
            }

        }

        if (phone2 != null) {
            if (phone2.length() > Patterns.PHONE_LENGTH) {
                return false;
            }
        }

        return true;
    }

    private boolean primaryIsEmpty() {

        if (name == null
                || password == null
                || eMail == null || eMail.isEmpty()) {
            return true;
        }

        return false;
    }

    private boolean nameIsGood(HttpServletRequest req) {
        if (name == null) {
            req.setAttribute("errorNameProfile", QUERIES.getString("ERROR_MISS_NAME"));
            return false;
        }

        if (!name.matches(Patterns.NAME)) {
            req.setAttribute("errorNameProfile", QUERIES.getString("ERROR_MISS_PATTERN"));
            return false;
        }

        // check if name already in table
        handleConnection(LOGGER);
        try {
            if (factoryDB.createUser(connection).isNameInTable(name)) {
                req.setAttribute("errorNameProfile", QUERIES
                        .getString("ERROR_NAME_ALREADY_EXIST"));
                return false;
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
            return false;
        } finally {

            closeConnection(LOGGER);
        }

        return true;
    }

    private boolean passwordIsGood(HttpServletRequest req) {
        if (password == null || passwordRepeat == null) {
            req.setAttribute("errorPassword", QUERIES.getString("ERROR_MISS_PASSWORD"));
            return false;
        }

        if (!password.equals(passwordRepeat)) {
            req.setAttribute("errorPassword", QUERIES.getString("ERROR_MATCHES_PASSWORD"));
            return false;
        }

        if (!password.matches(Patterns.PASSWORD)) {
            req.setAttribute("errorPassword", QUERIES.getString("ERROR_PASSWORD_PATTERN"));
            return false;
        }

        return true;
    }

    private void collectParamsFromRequest(HttpServletRequest req) {
        if (req.getParameter("name") != null) {
            name = req.getParameter("name");
        }

        if (req.getParameter("password") != null) {
            password = req.getParameter("password");
        }

        if (req.getParameter("passwordRepeat") != null) {
            passwordRepeat = req.getParameter("passwordRepeat");
        }

        if (req.getParameter("firstName") != null) {
            firstName = req.getParameter("firstName");
        }

        if (req.getParameter("lastName") != null) {
            lastName = req.getParameter("lastName");
        }

        if (req.getParameter("eMailRegistration") != null) {
            eMail = req.getParameter("eMailRegistration");
        } else {
            eMail = null;
        }

        if (req.getParameter("eMailRepeat") != null) {
            eMailRepeat = req.getParameter("eMailRepeat");
        }

        if (req.getParameter("phone1") != null) {
            phone1 = req.getParameter("phone1");
        }

        if (req.getParameter("phone2") != null) {
            phone2 = req.getParameter("phone2");
        }

    }

    private boolean eMailIsGood(HttpServletRequest request) {

        if (eMail == null || eMailRepeat == null || eMail.trim().length() < 1) {
            request.setAttribute("errorMail", QUERIES
                    .getString("ERROR_MISS_EMAIL"));
            return false;
        }

        if (!eMail.equals(eMailRepeat)) {
            request.setAttribute("errorMail", QUERIES
                    .getString("ERROR_MATCHES_EMAIL"));
            return false;
        }

        if (!eMail.matches(Patterns.EMAIL)) {
            request.setAttribute("errorMail", QUERIES
                    .getString("ERROR_EMAIL_PATTERN"));
            return false;
        }

        if (!eMail.matches(Patterns.EMAIL_LENGTH)) {
            request.setAttribute("errorMail", QUERIES
                    .getString("ERROR_EMAIL_PATTERN"));
            return false;
        }

        // check if eMail already in table
        handleConnection(LOGGER);
        try {
            if (factoryDB.createUser(connection).isMailInTable(eMail)) {
                request.setAttribute("errorMail", QUERIES
                        .getString("ERROR_EMAIL_ALREADY_EXIST"));
                return false;
            }
        } catch (Exception exception) {
            return false;
        } finally {
            closeConnection(LOGGER);
        }

        return true;
    }

    private void declareRole(HttpServletRequest req) {
        HttpSession session = req.getSession(true);
        session.setAttribute("role", Role.USER);

        if (user.getId() != null) {
            session.setAttribute("userId", user.getId());
        }
    }

    private void addNewUserToDB() {
        handleConnection(LOGGER);

        try {
            addUserToDbUser();
            setUserRoleInDB();
            insertUserPhones();
            LOGGER.info("New user registered: " + user);
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }
    }

    private void addUserToDbUser() throws Exception {
        user = new User.Builder()
                .setName(name)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setPassword(new PasswordHandler().encrypt(password))
                .setMail(eMail)
                .setRole(Role.USER)
                .build();
        factoryDB.createUser(connection).insertUser(user);
    }

    private void setUserRoleInDB() throws Exception {
        user = factoryDB.createUser(connection).getByMail(user.getMail());
        factoryDB.createRole(connection).insertRole(user, Role.USER);
    }

    private void insertUserPhones() throws Exception {
        if (phone1 != null) {
            factoryDB.createUserPhones(connection).insertPhone(user.getMail(), phone1);
        }
        if (phone2 != null) {
            factoryDB.createUserPhones(connection).insertPhone(user.getMail(), phone2);
        }
    }

}