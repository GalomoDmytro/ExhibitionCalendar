package controller.command;

import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
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
import java.sql.Connection;

public class RegistrationCommand implements Command {

    private static final Logger log = Logger.getLogger(RegistrationCommand.class);

    // todo make checking pattern for all attributes
    private String name;
    private String password;
    private String passwordRepeat;
    private String firstName;
    private String lastName;
    private String eMail;
    private String eMailRepeat;
    private String phone1;
    private String phone2;

    private Connection connection;
    private FactoryMySql factoryMySql;
    private User user;

    // todo put to another place
    // todo make multi lang
    private final String ERROR_MATCHES_PASSWORD = "do not match passwords";
    private final String ERROR_MISS_PASSWORD = "missing passwords";
    private final String ERROR_PASSWORD_PATTERN = "at password: digit must occur at least once;\n"
            + " a lower case letter must occur at least once;\n" +
            " an upper case letter must occur at least once;\n" +
            " no whitespace allowed, at least 6 characters;";
    private final String ERROR_MATCHES_EMAIL = "do not match eMails";
    private final String ERROR_EMAIL_ALREADY_EXIST = "This eMail already exist";
    private final String ERROR_EMAIL_PATTERN = "Not valid form";
    private final String ERROR_MISS_EMAIL = "Missed email";
    private final String ERROR_MISS_NAME = "Missed name";
    private final String ERROR_NAME_ALREADY_EXIST = "The name already exist";
    private final String ERROR_MISS_PATTERN = "The name must begin with a letter and contain between 2 and 20";

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        collectParamsFromRequest(req);

        handleConnection();

        RequestDispatcher dispatcher;
        if (isDataGood(req, resp)) {
            addNewUserToDB();
            user.setRole(Role.USER);
            declareRole(req, resp);
            dispatcher = req.getRequestDispatcher(Links.HOME_PAGE);
        } else {
            dispatcher = req.getRequestDispatcher(Links.REGISTRATION_PAGE);
        }

        dispatcher.forward(req, resp);
    }

    private boolean isDataGood(HttpServletRequest req, HttpServletResponse resp) {
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

        return true;
    }

    private boolean primaryIsEmpty() {
        if (name == null
                && password == null
                && eMail == null) {
            return true;
        }
        return false;
    }

    private boolean nameIsGood(HttpServletRequest req) {
        if (name == null) {
            req.setAttribute("errorNameProfile", ERROR_MISS_NAME);
            return false;
        }

        if (!name.matches(Patterns.NAME)) {
            req.setAttribute("errorNameProfile", ERROR_MISS_PATTERN);
            return false;
        }

        // check if name already in table
        boolean alredyEx = false;
        try {
            if (factoryMySql.createUser(connection).isNameInTable(name)) {
                alredyEx = true;
                req.setAttribute("errorNameProfile", ERROR_NAME_ALREADY_EXIST);
                return false;
            }
        } catch (Exception exception) {
            return false;
        } finally {
            if(alredyEx) {
                closeConnection();
            }
        }

        return true;
    }

    private boolean passwordIsGood(HttpServletRequest req) {
        if (password == null || passwordRepeat == null) {
            req.setAttribute("errorPassword", ERROR_MISS_PASSWORD);
            return false;
        }

        if (!password.equals(passwordRepeat)) {
            req.setAttribute("errorPassword", ERROR_MATCHES_PASSWORD);
            return false;
        }

        if (!password.matches(Patterns.PASSWORD)) {
            req.setAttribute("errorPassword", ERROR_PASSWORD_PATTERN);
            return false;
        }

        return true;
    }

    private void collectParamsFromRequest(HttpServletRequest req) {
        name = req.getParameter("name");
        password = req.getParameter("password");
        passwordRepeat = req.getParameter("passwordRepeat");
        firstName = req.getParameter("firstName");
        lastName = req.getParameter("lastName");
        eMail = req.getParameter("eMail");
        eMailRepeat = req.getParameter("eMailRepeat");
        phone1 = req.getParameter("phone1");
        phone2 = req.getParameter("phone2");
    }

    private boolean eMailIsGood(HttpServletRequest request) {
        if (eMail == null || eMailRepeat == null) {
            request.setAttribute("errorMail", ERROR_MISS_EMAIL);
            return false;
        }

        if (!eMail.equals(eMailRepeat)) {
            request.setAttribute("errorMail", ERROR_MATCHES_EMAIL);
            return false;
        }

        if (!eMail.matches(Patterns.EMAIL)) {
            request.setAttribute("errorMail", ERROR_EMAIL_PATTERN);
            return false;
        }

        if (!eMail.matches(Patterns.EMAIL_LENGTH)) {
            request.setAttribute("errorMail", ERROR_EMAIL_PATTERN);
            return false;
        }

        // check if eMail already in table
        try {
            if (factoryMySql.createUser(connection).isMailInTable(eMail)) {
                request.setAttribute("errorMail", ERROR_EMAIL_ALREADY_EXIST);
                return false;
            }
        } catch (Exception exception) {
            return false;
        }

        return true;
    }

    private void handleConnection() {
        try {
            connection = ConnectionPoolMySql.getInstance().getConnection();
            factoryMySql = new FactoryMySql();
        } catch (Exception exception) {

        }
    }

    private void declareRole(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(true);
        session.setAttribute("role", Role.USER);

        if (user.getId() != null) {
            session.setAttribute("userId", user.getId());
        }
    }

    private void addNewUserToDB() {
        try {
            addUserToDbUser();
            setUserRoleInDB();
            insertUserPhones();
        } catch (Exception exception) {
            log.error(exception);
        } finally {
            closeConnection();
        }
    }

    private void addUserToDbUser() throws Exception {
        user = new User.Builder()
                .setName(name)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setPassword(new PasswordHandler().encryptPassword(password))
                .setMail(eMail)
                .setRole(Role.USER)
                .build();
        factoryMySql.createUser(connection).insertUser(user);
    }

    private void setUserRoleInDB() throws Exception {
        user = factoryMySql.createUser(connection).getByMail(user.getMail());
        factoryMySql.createRole(connection).insertRole(user, Role.USER);
    }

    private void insertUserPhones() throws Exception {
        if(phone1 != null) {
            factoryMySql.createUserPhones(connection).insertPhone(user.getMail(), phone1);
        }
        if(phone2 != null) {
            factoryMySql.createUserPhones(connection).insertPhone(user.getMail(), phone2);
        }
    }

    private void closeConnection() {
        try {
            if(connection != null) {
                connection.close();
            }
        } catch (Exception exception) {

        }
    }

}
