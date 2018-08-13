package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.Links;
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
import java.io.IOException;
import java.sql.Connection;

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
    private HttpServletRequest req;
    private ExhibitionCenter exCenter;

    private static final Logger log = Logger.getLogger(AddingExpoCenter.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.req = req;
        RequestDispatcher dispatcher;
        handleConnection();

        if (!rolePermit(req)) {
            dispatcher = req.getRequestDispatcher(Links.HOME_PAGE);
        } else {
            dispatcher = req.getRequestDispatcher(Links.MODERATOR_ADD_EXPO_CENTER_PAGE);
        }

        collectParamsFromRequest(req);

        if (req.getParameter("addNewExpoCenter") != null) {
            addNewExpoCenter();
        }

        dispatcher.forward(req, resp);
    }

    private void addNewExpoCenter() {
        if (!inputDataIsValid()) {
            return;
        }

        prepareExhibitionCenter();

        try {
            factoryMySql.createExhibitionCenter(connection).insertExhibitionCenter(exCenter);

            exCenter = factoryMySql.createExhibitionCenter(connection)
                    .getExhibitionCenterByTitle(exCenter.getTitle());
//            log.info(exCenter.getTitle());
            if (phone1 != null) {
                factoryMySql.createExhibitionCenterPhone(connection).insertPhone(exCenter.getId(), phone1);
            }
            if (phone2 != null) {
                factoryMySql.createExhibitionCenterPhone(connection).insertPhone(exCenter.getId(), phone2);
            }
        } catch (Exception exception) {
            log.error(exception);
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
    }

    private void handleConnection() {
        try {
            connection = ConnectionPoolMySql.getInstance().getConnection();
            factoryMySql = new FactoryMySql();
        } catch (Exception exception) {

        }
    }

    private boolean inputDataIsValid() {
        if (!titleIsValid()) {
            return false;
        }

        if (!addressIsValid()) {
            req.setAttribute("error", "Oops, some trouble with address");
            return false;
        }

        if (!mailIsValid()) {
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

    private boolean mailIsValid() {
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

    private boolean addressIsValid() {
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

    private boolean titleIsValid() {
        if (title == null) {
            log.info("title null");
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
        boolean isMatch = false;
        try {
            if (factoryMySql.createExhibitionCenter(connection).isTitleInTable(title)) {
                isMatch = true;
                return true;
            }
        } catch (Exception exception) {
            log.error(exception);
        } finally {
            if(isMatch){
                closeConnection();
            }
        }
        return false;
    }

    private void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception exception) {

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

    private boolean rolePermit(HttpServletRequest req) {
//        HttpSession session = req.getSession(true);
//        if (session.getAttribute("role") == null) {
//            return false;
//        }
//        if (session.getAttribute("role").equals(Role.ADMIN) ||
//                session.getAttribute("role").equals(Role.MODERATOR)) {
//            return true;
//        }
//
//        return false;
        return true;
    }
}
