package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.Links;
import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.Contract;
import entities.Exhibition;
import entities.ExhibitionCenter;
import exceptions.DBException;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CreateContract implements Command {

    private Connection connection;
    private FactoryMySql factoryMySql;
    private String combineFrom;
    private Integer idExhibition;
    private Integer idExhibitionCenter;
    private String dateFromLine;
    private String dateToLine;
    private String priceLine;
    private String workTimeLine;
    private String maxTicketDayLine;

    private static final String INSRT_SUCCESSFUL = "Insert successful";
    private static final Logger LOGGER = Logger.getLogger(CreateContract.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher;

        if (!rolePermit(req)) {
            dispatcher = req.getRequestDispatcher(Links.HOME_PAGE);
        } else {
            dispatcher = req.getRequestDispatcher(Links.MODERATOR_CREATE_CONTRACT_PAGE);
        }

        collectDataFromRequest(req);

        showExhibitionInfo(req);
        showExhibitionCenterInfo(req);

        if(req.getParameter("addNewContract") != null) {
            saveContract(req);
        }

        dispatcher.forward(req, resp);
    }

    private void saveContract(HttpServletRequest req) {
        if(!contractDataIsValid()) {
            return;
        }
        handleConnection();

        try {
            LOGGER.info("contract insert");
            Contract contract = prepareContract();
            LOGGER.info("contract " + contract.toString());
            factoryMySql.createExhibitionContract(connection).insertContract(contract);
            req.setAttribute("insertInfo", "insert successful");
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection();
        }


    }

    private boolean contractDataIsValid() {
        // TODO: make check input data
        return true;
    }

    private Contract prepareContract() throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date dateFrom = format.parse(dateFromLine);
        Date dateTo = format.parse(dateToLine);
        BigDecimal price = new BigDecimal(maxTicketDayLine);
        Integer ticketPerDay = Integer.parseInt(maxTicketDayLine);

        return new Contract.Builder()
                        .setExhibitionId(idExhibition)
                        .setExCenterId(idExhibitionCenter)
                        .setDateFrom(new java.sql.Date(dateFrom.getTime()))
                        .setDateTo(new java.sql.Date(dateTo.getTime()))
                        .setTicketPrice(price)
                        .setWorkTime(workTimeLine)
                        .setMaxTicketPerDay(ticketPerDay)
                        .build();
    }

    private void showExhibitionInfo(HttpServletRequest req) {
        handleConnection();
        Exhibition exhibition;
        try {
            exhibition = factoryMySql.createExhibition(connection).getExhibitionById(idExhibition);
            if (exhibition != null) {
                getLangTagsFroExhibition(exhibition);

                req.setAttribute("exhibitionId", exhibition.getId());
                req.setAttribute("exhibitionTitle", exhibition.getTitle());
                req.setAttribute("exhibitionLangTags", exhibition.getLanguageTags());
            }
        } catch (Exception exception) {

        } finally {
            closeConnection();
        }
    }

    private void showExhibitionCenterInfo(HttpServletRequest req) {
        handleConnection();
        ExhibitionCenter exhibitionCenter;
        try {
            exhibitionCenter = factoryMySql.createExhibitionCenter(connection).getExhibitionCenterById(idExhibitionCenter);
            if (exhibitionCenter != null) {
                String phones = "";
                phones = getPhonesExhibitionCenter(exhibitionCenter, phones);

                req.setAttribute("exhibitionCenterId", exhibitionCenter.getId());
                req.setAttribute("exhibitionCenterTitle", exhibitionCenter.getTitle());
                req.setAttribute("exhibitionAddress", exhibitionCenter.getAddress());
                req.setAttribute("exhibitionMail", exhibitionCenter.geteMail());
                req.setAttribute("exhibitionWebPage", exhibitionCenter.getWebPage());
                req.setAttribute("exhibitionCenterPhones", phones);
            }
        } catch (Exception exception) {
        } finally {
            closeConnection();
        }
    }

    private String getPhonesExhibitionCenter(ExhibitionCenter exhibitionCenter, String phones) throws DBException {
        List<String> phonesExhibitionCenter = factoryMySql.createExhibitionCenterPhone(connection).getPhones(exhibitionCenter.getId());
        if(phonesExhibitionCenter != null) {
            for(String expoPhone : phonesExhibitionCenter) {
                phones += expoPhone + "; \n";
            }
        }
        return phones;
    }

    private void getLangTagsFroExhibition(Exhibition exhibition) throws DBException {
        Map<String, String> expoLang = factoryMySql.createDescriptionTable(connection).getAllDescription(exhibition);
        String langTags = "";
        for (Map.Entry<String, String> entry : expoLang.entrySet()) {
            langTags += entry.getKey() + " ";
        }
        exhibition.setLanguageTags(langTags);
    }

    private void collectDataFromRequest(HttpServletRequest req) {
        combineFrom = req.getParameter("combineFrom");
        if (combineFrom != null && combineFrom.equals("fromExpo")) {
            if (req.getParameter("combineFromId") != null) {
                idExhibition = Integer.parseInt(req.getParameter("combineFromId"));
            }
            if (req.getParameter("combineToId") != null) {
                idExhibitionCenter = Integer.parseInt(req.getParameter("combineToId"));
            }
        } else {
            if (req.getParameter("combineFromId") != null) {
                idExhibitionCenter = Integer.parseInt(req.getParameter("combineFromId"));
            }
            if (req.getParameter("combineToId") != null) {
                idExhibition = Integer.parseInt(req.getParameter("combineToId"));
            }
        }

        dateFromLine = req.getParameter("dateFrom");
        dateToLine = req.getParameter("dateTo");
        priceLine = req.getParameter("price");
        workTimeLine = req.getParameter("workTime");
        maxTicketDayLine = req.getParameter("maxTicketDay");

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
