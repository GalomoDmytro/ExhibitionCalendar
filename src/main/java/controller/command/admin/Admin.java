package controller.command.admin;

import controller.command.Command;
import controller.command.util.Links;
import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.Role;
import entities.User;
import org.apache.log4j.Logger;
import utility.JSPError;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;

public class Admin implements Command {

    private String id;
    private String eMail;
    private String role;
    private Integer idAdmin;

    private Connection connection;
    private FactoryMySql factoryMySql;

    private static final Logger LOGGER = Logger.getLogger(Admin.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher dispatcher;

        getIdAdmin(req);

        collectParamsFromRequest(req);
        if (req.getParameter("action") != null) {
            if (req.getParameter("action").equals("changeRole")) {
                changeRoleHandler(req);
            } else if (req.getParameter("action").equals("getRole")) {
                showRole(req);
            }
        }
        dispatcher = req.getRequestDispatcher(Links.ADMIN_PAGE);

        setDataToReq(req);
        dispatcher.forward(req, resp);
    }

    private void getIdAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        idAdmin = (Integer) session.getAttribute("userId");
    }

    public void setDataToReq(HttpServletRequest req) {
        req.setAttribute("mess", "");
        req.setAttribute("showRole", "");
    }

    private void collectParamsFromRequest(HttpServletRequest req) {
        id = req.getParameter("id");
        eMail = req.getParameter("eMail");
        role = req.getParameter("role");
    }

    private void changeRoleHandler(HttpServletRequest req) {

        if (req.getParameter("action") != null
                && req.getParameter("action").equals("changeRole")) {
            if (!changeRole()) {
                req.setAttribute("mess", JSPError.MESSAGE_ROLE_CHANGE_TROUBLE);
            }
        }
    }

    private void handleConnection() {
        try {
            connection = ConnectionPoolMySql.getInstance().getConnection();
            factoryMySql = new FactoryMySql();
        } catch (Exception exception) {
            LOGGER.error(exception);
        }
    }

    /**
     * Use userId or userMail to find User and
     * change Role
     *
     * @return
     */
    private boolean changeRole() {
        handleConnection();
        try {
            if (id != null && id.trim().length() > 0) {
                factoryMySql.createRole(connection)
                        .updateRole(Integer.valueOf(id), getRole());
                LOGGER.info("Admin with id: " + idAdmin
                        + " has change role for user with id " + id
                        + " to Role: " + getRole());
            } else if (eMail != null) {
                User user = factoryMySql.createUser(connection).getByMail(eMail);
                factoryMySql.createRole(connection).updateRole(user.getId(), getRole());
                LOGGER.info("Admin with id: " + idAdmin
                        + " has change role for user with eMail " + eMail
                        + " to Role: " + getRole());
            } else return false;

        } catch (Exception exception) {
            LOGGER.error(exception);
            return false;
        } finally {
            closeConnection();
        }
        return true;
    }

    /**
     * Get Role of looking user from DB and
     * save it to HttpServletRequest
     *
     * @param req
     */
    private void showRole(HttpServletRequest req) {
        handleConnection();
        try {
            User user = null;
            if (id != null && id.trim().length() > 0) {
                user = factoryMySql.createUser(connection)
                        .getById(Integer.valueOf(id));
                if (user.getId() == 1) {
                    return; // unsigned user
                }
            } else if (eMail != null) {
                user = factoryMySql.createUser(connection)
                        .getByMail(eMail);
            }
            if (user != null) {
                user.setRole(factoryMySql.createRole(connection)
                        .getRoleById(user.getId()));
                req.setAttribute("showRole", user);
            }
        } catch (Exception exception) {

        } finally {
            closeConnection();
        }
    }

    private Role getRole() {
        switch (role) {
            case "admin":
                return Role.ADMIN;

            case "moderator":
                return Role.MODERATOR;

            case "user":
                return Role.USER;

            case "guest":
                return Role.GUEST;

            default:
                return Role.USER;
        }
    }

    private void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        }
    }

}
