package controller.command;

import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import dao.mysql.UserMySql;
import entities.Role;
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

public class RegistrationCommand implements Command {

    private static final Logger log = Logger.getLogger(RegistrationCommand.class);

    private String name;
    private String password;
    private String passwordRepeat;
    private String firstName;
    private String lastName;
    private String eMail;
    private String eMailRepeat;
    private String phone;

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
    private final String ERROR_NAME_ALREADY_EXIST = "Thie name already exist";
    private final String ERROR_MISS_PATTERN = "The name must begin with a letter and contain between 2 and 20";

    /*start-of-string
    * a digit must occur at least once
    * a lower case letter must occur at least once
    * an upper case letter must occur at least once
    * no whitespace allowed in the entire string
    * anything, at least 6 places though
    end-of-string */
    private final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$";
    //    private final String EMAIL_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    private final String EMAIL_PATTERN = "^[^@]*@[^@]+.[^@]+$"; // contain one '@' and after, one '.'
    private final String NAME_PATTERN = "^[a-zA-Z][a-zA-Z0-9-_\\.]{1,20}$"; // first letter, with 2-20 chars


    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        collectParamsFromRequest(req);

        handleConnection();

        if (isDataGood(req, resp)) {
            addNewUserToDB();
            declareRole(req, resp);
        }

        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.REGISTRATION_PAGE);
        dispatcher.forward(req, resp);

    }

    private boolean isDataGood(HttpServletRequest req, HttpServletResponse resp) {
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

    private boolean nameIsGood(HttpServletRequest req) {
        if (name == null) {
            req.setAttribute("errorNameProfile", ERROR_MISS_NAME);
            return false;
        }

        if (!name.matches(NAME_PATTERN)) {
            req.setAttribute("errorNameProfile", ERROR_MISS_PATTERN);
            return false;
        }

        // check if name already in table
        try {
            if (factoryMySql.createUser(connection).isNameInTable(name)) {
                req.setAttribute("errorNameProfile", ERROR_NAME_ALREADY_EXIST);
                return false;
            }
        } catch (Exception exception) {
            return false;
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

        if (!password.matches(PASSWORD_PATTERN)) {
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
        phone = req.getParameter("phone");
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

        if (!eMail.matches(EMAIL_PATTERN)) {
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

        if(user.getId() != null) {
            session.setAttribute("userId", user.getId());
        }
    }

    private void addNewUserToDB() {
        try {
            user = new User.Builder()
                    .setName(name)
                    .setFirstName(firstName)
                    .setLastName(lastName)
                    .setPassword(new PasswordHandler().encryptPassword(password))
                    .setMail(eMail)
                    .setRole(Role.USER)
                    .build();
            // todo make save phone
            factoryMySql.createUser(connection).insertUser(user);
        } catch (Exception exception) {
            log.error(exception);
        }
    }

}
