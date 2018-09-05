package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.util.Links;
import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.ExhibitionCenter;
import org.apache.log4j.Logger;
import utility.JSPError;
import utility.Patterns;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class AddingExpoCenter implements Command {

    private Connection connection;
    private FactoryMySql factoryMySql;
    private String title;
    private String address;
    private String eMail;
    private String eMail_repeat;
    private String webPage;
    private String phone1;
    private String phone2;
    private ExhibitionCenter exCenter;
    private Integer idModerator;

    private static final Logger LOGGER = Logger.getLogger(AddingExpoCenter.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        getDataFromSession(req);
        RequestDispatcher dispatcher;

        dispatcher = req.getRequestDispatcher(Links.MODERATOR_ADD_EXPO_CENTER_PAGE);

        collectParamsFromRequest(req);

        if (req.getParameter("addNewExpoCenter") != null) {
            addNewExpoCenter(req);
        }

        dispatcher.forward(req, resp);
    }

    private void addNewExpoCenter(HttpServletRequest request) {
        if (!inputDataIsValid(request)) {
            return;
        }
        prepareExhibitionCenter();

        handleConnection();
        try {
            connection.setAutoCommit(false);
            factoryMySql.createExhibitionCenter(connection)
                    .insertExhibitionCenter(exCenter);

            exCenter = factoryMySql.createExhibitionCenter(connection)
                    .getExhibitionCenterByTitle(exCenter.getTitle());
            if (phone1 != null) {
                factoryMySql.createExhibitionCenterPhone(connection)
                        .insertPhone(exCenter.getId(), phone1);
            }
            if (phone2 != null) {
                factoryMySql.createExhibitionCenterPhone(connection)
                        .insertPhone(exCenter.getId(), phone2);
            }
            connection.commit();
            LOGGER.info("Moderator with id: " + idModerator +
                    " insert new Ex.Center " + exCenter);
            request.setAttribute("confirmAdd", "Exhibition center added ");
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection();
        }
    }

    private void prepareExhibitionCenter() {
        exCenter = new ExhibitionCenter.Builder()
                .setTitle(title)
                .setAddress(address)
                .seteMail(eMail)
                .build();
        if (webPage != null) {
            exCenter.setWebPage(webPage);
        }
        if (phone1 != null || phone2 != null) {
            List<String> phones = new ArrayList<>();
            if (phone1 != null) {
                phones.add(phone1);
            }
            if (phone2 != null) {
                phones.add(phone2);
            }
            exCenter.setPhone(phones);
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

    private void getDataFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        idModerator = (Integer) session.getAttribute("userId");
    }

    private boolean inputDataIsValid(HttpServletRequest req) {
        if (!titleIsValid(req)) {
            return false;
        }

        if (!addressIsValid(req)) {
            req.setAttribute("error", "Oops, some trouble with address");
            return false;
        }

        if (!mailIsValid(req)) {
            return false;
        }

        if (!webPageIsValid()) {
            return false;
        }

        return true;
    }

    private boolean webPageIsValid() {
        return true;
    }

    private boolean mailIsValid(HttpServletRequest req) {
        if (eMail == null || eMail_repeat == null) {
            req.setAttribute("error", JSPError.ERROR_MAIL_MISSING);
            return false;
        }

        if (!eMail.equals(eMail_repeat)) {
            req.setAttribute("error", JSPError.ERROR_MAIL_MATCHES);
            return false;
        }

        if (!eMail.matches(Patterns.EMAIL)) {
            req.setAttribute("error", JSPError.ERROR_MAIL);
            return false;
        }

        return true;
    }

    private boolean addressIsValid(HttpServletRequest req) {
        if (address == null) {
            req.setAttribute("error", JSPError.ERROR_ADDRESS);
            return false;
        }

        if (!address.matches(Patterns.ADDRESS_LENGTH)) {
            req.setAttribute("error", JSPError.ERROR_ADDRESS);
            return false;
        }

        return true;
    }

    private boolean titleIsValid(HttpServletRequest req) {
        if (title == null) {
            LOGGER.info("title null");
            return false;
        }

        if (!title.matches(Patterns.TITLE_EXPO_CENTER)) {
            req.setAttribute("error", JSPError.ERROR_TITLE);
            return false;
        }

        if (titleAlreadyExist()) {
            req.setAttribute("error", JSPError.ERROR_TITLE_EXIST);
            return false;
        }

        return true;
    }

    private boolean titleAlreadyExist() {
        handleConnection();
        try {
            if (factoryMySql.createExhibitionCenter(connection).isTitleInTable(title)) {
                return true;
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection();
        }
        return false;
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

    private void collectParamsFromRequest(HttpServletRequest req) {
        title = req.getParameter("title");
        address = req.getParameter("address");
        eMail = req.getParameter("eMail");
        eMail_repeat = req.getParameter("eMail_repeat");
        webPage = req.getParameter("webPage");
        phone1 = req.getParameter("phone1");
        phone2 = req.getParameter("phone2");
    }

}
