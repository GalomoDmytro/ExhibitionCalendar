package controller.command;

import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.Role;
import entities.User;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

public class Admin implements Command {

    private String id;
    private String eMail;
    private String role;

    private Connection connection;
    private FactoryMySql factoryMySql;

    private static final String MESSAGE_ROLE_CHANGE_TROUBLE = "Role hasn't changed";
    private static final Logger LOGGER = Logger.getLogger(Admin.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher;
        req.setAttribute("mess", "");
        req.setAttribute("showRole", "");

        collectParamsFromRequest(req);
        connectionHandler();
        if (req.getParameter("action") != null) {
            if (req.getParameter("action").equals("changeRole")) {
                changeRoleHandler(req);
            } else if (req.getParameter("action").equals("getRole")) {
                showRole(req);
            }
        }
        dispatcher = req.getRequestDispatcher(Links.ADMIN_PAGE);

        dispatcher.forward(req, resp);
    }

    private void collectParamsFromRequest(HttpServletRequest req) {
        id = req.getParameter("id");
        eMail = req.getParameter("eMail");
        role = req.getParameter("role");
    }

    private void changeRoleHandler(HttpServletRequest req) {

        if (req.getParameter("action") != null && req.getParameter("action").equals("changeRole")) {
            if (!changeRole()) {
                req.setAttribute("mess", MESSAGE_ROLE_CHANGE_TROUBLE);
            }
        }
    }

    private void connectionHandler() {
        try {
            connection = ConnectionPoolMySql.getInstance().getConnection();
            factoryMySql = new FactoryMySql();
        } catch (Exception exception) {
        }
    }

    private boolean changeRole() {
        try {
            if (id != null && id.trim().length() > 0) {
                factoryMySql.createRole(connection).updateRole(Integer.valueOf(id), getRole());
            } else if (eMail != null) {
                User user = factoryMySql.createUser(connection).getByMail(eMail);
                factoryMySql.createRole(connection).updateRole(user.getId(), getRole());
            } else return false;
        } catch (Exception exception) {
            LOGGER.error(exception);
            return false;
        } finally {
            try {
                connection.close();
            } catch (Exception exception) {
            }
        }
        return true;
    }

    private void showRole(HttpServletRequest req) {
        try {
            User user = null;
            if (id != null && id.trim().length() > 0) {
                user = factoryMySql.createUser(connection).getById(Integer.valueOf(id));
            } else if (eMail != null) {
                user = factoryMySql.createUser(connection).getByMail(eMail);
            }
            if (user != null) {
                req.setAttribute("showRole", "Id:" + user.getId() + " Mail:" + user.getMail() +
                        " Name:" + user.getName() + " Role:" + getRole());
            }
        } catch (Exception exception) {

        } finally {
            try {
                connection.close();
            } catch (Exception exception) {
            }
        }
    }


    private Role getRole() {
        switch (role) {
            case "admin":
                return Role.ADMIN;

            case "moderator":
                return Role.MODERATOR;

            case "author":
                return Role.AUTHOR;

            case "user":
                return Role.USER;

            case "guest":
                return Role.GUEST;

            default:
                return Role.USER;
        }
    }

}
