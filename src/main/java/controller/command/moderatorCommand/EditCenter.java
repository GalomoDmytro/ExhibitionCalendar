package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.Links;
import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.ExhibitionCenter;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

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

    private static final Logger log = Logger.getLogger(EditCenter.class);


    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        RequestDispatcher dispatcher;
        handleConnection();

        readDataFromReq(req);
        setExpoCenterDataToReq(req);

        if (!rolePermit(req)) {
            dispatcher = req.getRequestDispatcher(Links.HOME_PAGE);
        } else {
            dispatcher = req.getRequestDispatcher(Links.MODERATOR_EDIT_CENTER_PAGE);
        }


        if (req.getParameter("editExpoCenter") != null) {
            changeExpoCenterData(req);
            dispatcher = req.getRequestDispatcher(Links.MODERATOR_MANAGE_CENTER_PAGE);
        } else if (req.getParameter("denieEdit") != null) {
            dispatcher = req.getRequestDispatcher(Links.MODERATOR_PAGE);
        }

        dispatcher.forward(req, resp);
    }

    private void changeExpoCenterData(HttpServletRequest req) {

        ExhibitionCenter exhibitionCenter = new ExhibitionCenter.Builder()
                .setId(idCenter)
                .setTitle(title)
                .setAddress(address)
                .seteMail(eMail)
                .setWebPage(webPage)
                .build();
        //todo handle phone
        try {
            factoryMySql.createExhibitionCenter(connection).updateExhibitionCenter(exhibitionCenter);
            factoryMySql.createExhibitionCenterPhone(connection).deletePhone(exhibitionCenter.getId());
            factoryMySql.createExhibitionCenterPhone(connection).insertPhone(exhibitionCenter.getId(), phone1);
            factoryMySql.createExhibitionCenterPhone(connection).insertPhone(exhibitionCenter.getId(), phone2);
        } catch (Exception exception) {

        } finally {
            closeConnection();
        }
    }

    private void readDataFromReq(HttpServletRequest req) {
        if (req.getParameter("idEdit") != null) {
            idCenter = Integer.parseInt(req.getParameter("idEdit"));
        }
        title = req.getParameter("expoCTitle");
        address = req.getParameter("expoCAddress");
        webPage = req.getParameter("expoCMail");
        eMail = req.getParameter("expoCWebPage");
        phone1 = req.getParameter("phone1");
        phone2 = req.getParameter("phone2");
    }

    private void setExpoCenterDataToReq(HttpServletRequest req) {
        req.setAttribute("idEdit", idCenter);
        req.setAttribute("expoCTitle", title);
        req.setAttribute("expoCAddress", address);
        req.setAttribute("expoCWebPage", webPage);
        req.setAttribute("expoCMail", eMail);
        req.setAttribute("expoCPhone1", phone1);
        req.setAttribute("expoCPhone2", phone2);
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

    private void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception exception) {

        }
    }

    private void handleConnection() {
        try {
            connection = ConnectionPoolMySql.getInstance().getConnection();
            factoryMySql = new FactoryMySql();
        } catch (Exception exception) {

        }
    }
}
