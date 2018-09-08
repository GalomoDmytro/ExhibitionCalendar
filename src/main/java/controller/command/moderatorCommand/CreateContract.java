package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.ServletHelper;
import controller.command.util.Links;
import entities.Contract;
import entities.Exhibition;
import entities.ExhibitionCenter;
import exceptions.DBException;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CreateContract extends ServletHelper implements Command {

    private String combineFrom;
    private Integer idExhibition;
    private Integer idExhibitionCenter;
    private String dateFromLine;
    private String dateToLine;
    private String priceLine;
    private String workTimeLine;
    private String maxTicketDayLine;
    private int idModerator;

    private static final Logger LOGGER = Logger.getLogger(CreateContract.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        RequestDispatcher dispatcher = req
                .getRequestDispatcher(Links.MODERATOR_CREATE_CONTRACT_PAGE);

        getIdModerator(req);
        collectDataFromRequest(req);
        showExhibitionInfo(req);
        showExhibitionCenterInfo(req);

        if (req.getParameter("addNewContract") != null) {
            saveContract(req);
        }

        dispatcher.forward(req, resp);
    }

    private void getIdModerator(HttpServletRequest req) {
        HttpSession session = req.getSession();
        idModerator = (Integer) session.getAttribute("userId");
    }

    private void saveContract(HttpServletRequest req) {
        if (!contractDataIsValid()) {
            return;
        }
        handleConnection(LOGGER);

        try {
            Contract contract = prepareContract();
            factoryDB.createExhibitionContract(connection).insertContract(contract);
            req.setAttribute("insertInfo", "insert successful");
            LOGGER.info("Id moderator: " + idModerator
                    + " insert new contract: " + contract);
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
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
        handleConnection(LOGGER);
        Exhibition exhibition;
        try {
            exhibition = factoryDB.createExhibition(connection)
                    .getExhibitionById(idExhibition);
            if (exhibition != null) {
                getLangTagsFroExhibition(exhibition);

                req.setAttribute("exhibitionId", exhibition.getId());
                req.setAttribute("exhibitionTitle", exhibition.getTitle());
                req.setAttribute("exhibitionLangTags", exhibition.getLanguageTags());
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }
    }

    private void showExhibitionCenterInfo(HttpServletRequest req) {
        handleConnection(LOGGER);
        ExhibitionCenter exhibitionCenter;
        try {
            exhibitionCenter = factoryDB.createExhibitionCenter(connection)
                    .getExhibitionCenterById(idExhibitionCenter);
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
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }
    }

    private String getPhonesExhibitionCenter(ExhibitionCenter exhibitionCenter, String phones)
            throws DBException {
        List<String> phonesExhibitionCenter = factoryDB
                .createExhibitionCenterPhone(connection)
                .getPhones(exhibitionCenter.getId());
        if (phonesExhibitionCenter != null) {
            for (String expoPhone : phonesExhibitionCenter) {
                phones += expoPhone + "; \n";
            }
        }
        return phones;
    }

    private void getLangTagsFroExhibition(Exhibition exhibition) throws DBException {
        Map<String, String> expoLang = factoryDB
                .createDescriptionTable(connection)
                .getAllDescription(exhibition);
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

}
