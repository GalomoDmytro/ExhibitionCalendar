package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.util.Links;
import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.ExhibitionCenter;
import exceptions.DBException;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class EditCenter implements Command {

    private Connection connection;
    private FactoryMySql factoryMySql;

    private Integer idCenter;
    private String title;
    private String address;
    private String webPage;
    private String eMail;
    private String phone1;
    private String phone2;
    private List<String> phoneList;
    private Integer idModerator;

    private ExhibitionCenter exhibitionCenter;

    private static final Logger LOGGER = Logger.getLogger(EditCenter.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher dispatcher;

        getIdModerator(req);

        dispatcher = choseRequestDestination(req);

        dispatcher.forward(req, resp);
    }

    private RequestDispatcher choseRequestDestination(HttpServletRequest req) {
        RequestDispatcher dispatcher;
        if (req.getParameter("editExpoCenter") != null) {
            editExhibitionCenter(req);
            dispatcher = req.getRequestDispatcher(Links.MODERATOR_MANAGE_CENTER_PAGE);
        } else if (req.getParameter("deniedEdit") != null) {
            dispatcher = req.getRequestDispatcher(Links.MODERATOR_PAGE);
        } else {
            readDataFromReqOnFirstStart(req);
            setExpoCenterDataToReq(req);
            dispatcher = req.getRequestDispatcher(Links.MODERATOR_EDIT_CENTER_PAGE);
        }
        return dispatcher;
    }

    private void getIdModerator(HttpServletRequest req) {
        HttpSession session = req.getSession();
        idModerator = (Integer) session.getAttribute("userId");
    }

    private void editExhibitionCenter(HttpServletRequest req) {
        readDataToUpdate(req);
        changeExpoCenterData();
    }

    private void readDataFromDB() {
        handleConnection();

        try {
            exhibitionCenter = factoryMySql.createExhibitionCenter(connection)
                    .getExhibitionCenterById(idCenter);
            phoneList = factoryMySql.createExhibitionCenterPhone(connection)
                    .getPhones(exhibitionCenter.getId());
            if (phoneList.size() < 2) {
                phoneList.add("");
            }
        } catch (Exception exception) {
            LOGGER.info(exception);
        } finally {
            closeConnection();
        }
    }

    private void changeExpoCenterData() {

        exhibitionCenter = new ExhibitionCenter.Builder()
                .setId(idCenter)
                .setTitle(title)
                .setAddress(address)
                .seteMail(eMail)
                .setWebPage(webPage)
                .build();
        handleConnection();
        try {
            factoryMySql.createExhibitionCenter(connection).setLockExhibitionCenterTable();
            connection.setAutoCommit(false);

            factoryMySql.createExhibitionCenter(connection)
                    .updateExhibitionCenter(exhibitionCenter);
            factoryMySql.createExhibitionCenterPhone(connection)
                    .deletePhone(exhibitionCenter.getId());
            if (phone1 != null) {
                factoryMySql.createExhibitionCenterPhone(connection)
                        .insertPhone(exhibitionCenter.getId(), phone1);
            }
            if (phone2 != null) {
                factoryMySql.createExhibitionCenterPhone(connection)
                        .insertPhone(exhibitionCenter.getId(), phone2);
            }
            connection.commit();
            connection.setAutoCommit(true);
            LOGGER.info("Moderator id: " + idModerator
                    + " has update Ex.Center with id: " + exhibitionCenter.getId()
                    + " with new data: " + exhibitionCenter);
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            try {
                factoryMySql.createExhibitionCenter(connection).unlockTable();
            } catch (DBException e) {
                LOGGER.info(e);
            }
            closeConnection();
        }
    }

    private void readDataFromReqOnFirstStart(HttpServletRequest req) {
        if (req.getParameter("idEdit") != null) {
            idCenter = Integer.parseInt(req.getParameter("idEdit"));
        }

        readDataFromDB();

    }

    private void readDataToUpdate(HttpServletRequest req) {

        title = req.getParameter("expoCTitle");
        address = req.getParameter("expoCAddress");
        webPage = req.getParameter("expoCWebPage");
        eMail = req.getParameter("expoCMail");
        phone1 = req.getParameter("phone0");
        phone2 = req.getParameter("phone1");
    }

    private void setExpoCenterDataToReq(HttpServletRequest req) {
        req.setAttribute("idEdit", idCenter);
        req.setAttribute("exhibitionCenter", exhibitionCenter);

        req.setAttribute("phones", phoneList);
    }

    private void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception exception) {
            LOGGER.info(exception);
        }
    }

    private void handleConnection() {
        try {
            connection = ConnectionPoolMySql.getInstance().getConnection();
            factoryMySql = new FactoryMySql();
        } catch (Exception exception) {
            LOGGER.info(exception);
        }
    }
}
