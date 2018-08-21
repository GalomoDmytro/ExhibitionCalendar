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
import java.util.ResourceBundle;

public class RegistrationCommand implements Command {

    private static final Logger log = Logger.getLogger(RegistrationCommand.class);

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

    // todo make multi lang
    private static final ResourceBundle QUERIES = ResourceBundle.getBundle("strings_error_eng");

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

        if(!phoneIsGood(req)) {
           return false;
        }

        return true;
    }

    private boolean phoneIsGood(HttpServletRequest req) {
        if(phone1 != null) {
            if(!phone1.matches(Patterns.PHONE_LENGTH)) {
                return false;
            }
        }

        if(phone2 != null) {
            if(!phone2.matches(Patterns.PHONE_LENGTH)) {
                return false;
            }
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
            req.setAttribute("errorNameProfile", QUERIES.getString("ERROR_MISS_NAME"));
            return false;
        }

        if (!name.matches(Patterns.NAME)) {
            req.setAttribute("errorNameProfile", QUERIES.getString("ERROR_MISS_PATTERN"));
            return false;
        }

        // check if name already in table
        boolean alredyEx = false;
        try {
            if (factoryMySql.createUser(connection).isNameInTable(name)) {
                alredyEx = true;
                req.setAttribute("errorNameProfile", QUERIES.getString("ERROR_NAME_ALREADY_EXIST"));
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
            request.setAttribute("errorMail", QUERIES.getString("ERROR_MISS_EMAIL"));
            return false;
        }

        if (!eMail.equals(eMailRepeat)) {
            request.setAttribute("errorMail", QUERIES.getString("ERROR_MATCHES_EMAIL"));
            return false;
        }

        if (!eMail.matches(Patterns.EMAIL)) {
            request.setAttribute("errorMail", QUERIES.getString("ERROR_EMAIL_PATTERN"));
            return false;
        }

        if (!eMail.matches(Patterns.EMAIL_LENGTH)) {
            request.setAttribute("errorMail", QUERIES.getString("ERROR_EMAIL_PATTERN"));
            return false;
        }

        // check if eMail already in table
        try {
            if (factoryMySql.createUser(connection).isMailInTable(eMail)) {
                request.setAttribute("errorMail", QUERIES.getString("ERROR_EMAIL_ALREADY_EXIST"));
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
            session.setAttribute("idUser", user.getId());
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