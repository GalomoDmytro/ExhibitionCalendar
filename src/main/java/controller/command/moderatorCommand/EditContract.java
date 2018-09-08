package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.util.Links;
import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.Contract;
import entities.Exhibition;
import entities.ExhibitionCenter;
import exceptions.DBException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import utility.Patterns;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

public class EditContract implements Command {

    private Connection connection;
    private FactoryMySql factoryMySql;
    private Integer idContract;
    private String dateFromLine;
    private String dateToLine;
    private String ticketPriceLine;
    private String workTimeExhibitionLine;
    private String maxTicketPerDayLine;
    private Integer exhibitionId;
    private Integer exCenterId;
    private boolean changeSuccessful;
    private Integer idModerator;

    private static final Logger LOGGER = Logger.getLogger(EditContract.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        getIdModerator(req);
        RequestDispatcher dispatcher = req
                .getRequestDispatcher(Links.MODERATOR_EDIT_CONTRACT_PAGE);

        collectDataFromReq(req);
        getDataForRequest(req);

        if (req.getParameter("saveChangesContract") != null) {
            updateContractInfo(req);
            if (changeSuccessful) {
                dispatcher = req
                        .getRequestDispatcher(Links.MODERATOR_EDIT_CONTRACT_PAGE);
            }

        }

        dispatcher.forward(req, resp);
    }

    private void getIdModerator(HttpServletRequest req) {
        HttpSession session = req.getSession();
        idModerator = (Integer) session.getAttribute("userId");
    }

    private void updateContractInfo(HttpServletRequest req) {
        prepareDataToUpdate(req);
        if (!checkData()) {
            req.setAttribute("error", "Ops! Can't save. Check data please.");
            return;
        }

        handleConnection();
        try {
            Contract contract = prepareContractToUpdate();
            factoryMySql.createExhibitionContract(connection).setLockContractTable();
            factoryMySql.createExhibitionContract(connection).updateContract(contract);
            changeSuccessful = true;
            LOGGER.info("Moderator id: " + idModerator
                    + " update contract id: " + contract.getId()
                    + " and set new data: " + contract);
        } catch (Exception exception) {
            LOGGER.error(exception);
            changeSuccessful = false;
        } finally {
            try {
                factoryMySql.createExhibitionCenter(connection).unlockTable();
            } catch (DBException e) {
                LOGGER.error(e);
            }

            closeConnection();
        }
    }

    private Contract prepareContractToUpdate() throws ParseException {
        DateFormat format = new SimpleDateFormat(Patterns.DATE_SQL_PATTERN, Locale.ENGLISH);
        Date dateFrom = format.parse(dateFromLine);
        Date dateTo = format.parse(dateToLine);
        return new Contract.Builder()
                .setId(idContract)
                .setExhibitionId(exhibitionId)
                .setExCenterId(exCenterId)
                .setDateFrom(new java.sql.Date(dateFrom.getTime()))
                .setDateTo(new java.sql.Date(dateTo.getTime()))
                .setTicketPrice(new BigDecimal(ticketPriceLine))
                .setWorkTime(workTimeExhibitionLine)
                .setMaxTicketPerDay(Integer.parseInt(maxTicketPerDayLine))
                .build();
    }

    private void prepareDataToUpdate(HttpServletRequest req) {
        dateFromLine = req.getParameter("dateFrom");
        dateToLine = req.getParameter("dateTo");
        ticketPriceLine = req.getParameter("price");
        maxTicketPerDayLine = req.getParameter("maxTicketDay");
        workTimeExhibitionLine = req.getParameter("workTime");
    }

    private boolean checkData() {
        if (!dateValid()) {
            return false;
        }

        if (!priceValid()) {
            return false;
        }

        if (!maxTicketPerDayValid()) {
            return false;
        }

        if (!workTimeValid()) {
            return false;
        }

        return true;
    }

    private boolean workTimeValid() {
        if (workTimeExhibitionLine == null) {
            return false;
        }

        return true;
    }

    private boolean maxTicketPerDayValid() {
        if (maxTicketPerDayLine == null) {
            return false;
        }

        if (!StringUtils.isNumeric(maxTicketPerDayLine)) {
            return false;
        }

        return true;
    }

    private boolean priceValid() {
        if (ticketPriceLine == null) {
            return false;
        }

        if (!ticketPriceLine.matches(Patterns.PRICE_PATTERN)) {

            return false;
        }

        return true;
    }

    /**
     * Checking input data
     *
     * @return true if data valid
     */
    private boolean dateValid() {
        if (dateFromLine == null || dateToLine == null) {
            return false;
        }

        if (!dateToLine.matches(Patterns.DATE_PATTERN)
                || !dateFromLine.matches(Patterns.DATE_PATTERN)) {
            return false;
        }

        if (!isDateInOrder(dateFromLine, dateToLine)) {
            return false;
        }

        return true;
    }

    /**
     * Check if date in right order
     *
     * @param from date ex. start
     * @param to   date ex. end
     * @return true if  date in right order
     */
    private boolean isDateInOrder(String from, String to) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateFrom = df.parse(from);
            Date dateTo = df.parse(to);
            if (dateFrom.compareTo(dateTo) < 0) {
                return true;
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return false;
    }

    /**
     * Get Contract, Exhibition, ExhibitionCenter and set
     * set them to request
     *
     * @param req
     */
    private void getDataForRequest(HttpServletRequest req) {
        handleConnection();

        try {

            if (idContract != null) {
                Contract contract = factoryMySql.createExhibitionContract(connection)
                        .getExhibitionContractById(idContract);
                Exhibition exhibition = factoryMySql.createExhibition(connection)
                        .getExhibitionById(contract
                                .getExhibitionId());
                getLangTagsFroExhibition(exhibition);
                ExhibitionCenter exhibitionCenter = factoryMySql
                        .createExhibitionCenter(connection)
                        .getExhibitionCenterById(contract.getExCenterId());
                getPhonesExhibitionCenter(exhibitionCenter);

                setDataToForm(contract, exhibition, exhibitionCenter, req);

            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection();
        }
    }

    /**
     * Prepare data to put in form in .jsp file
     *
     * @param contract
     * @param exhibition
     * @param exhibitionCenter
     * @param req
     */
    private void setDataToForm(Contract contract, Exhibition exhibition,
                               ExhibitionCenter exhibitionCenter,
                               HttpServletRequest req) {

        req.setAttribute("contract", contract);

        req.setAttribute("exhibition", exhibition);

        req.setAttribute("exhibitionCenter", exhibitionCenter);

        if (exhibitionCenter.getPhone() != null) {
            req.setAttribute("exhibitionCenterPhones", exhibitionCenter.getPhone());
        }

    }

    private void getPhonesExhibitionCenter(ExhibitionCenter exhibitionCenter)
            throws DBException {
        List<String> phonesExhibitionCenter = factoryMySql
                .createExhibitionCenterPhone(connection)
                .getPhones(exhibitionCenter.getId());
        exhibitionCenter.setPhone(phonesExhibitionCenter);
    }

    private void collectDataFromReq(HttpServletRequest req) {
        if (req.getParameter("idEdit") != null) {
            idContract = Integer.parseInt(req.getParameter("idEdit"));
        }
        if (req.getParameter("exhibitionId") != null) {
            exhibitionId = Integer.parseInt(req.getParameter("exhibitionId"));
        }
        if (req.getParameter("exhibitionCenterId") != null) {
            exCenterId = Integer.parseInt(req.getParameter("exhibitionCenterId"));
        }
    }

    private void getLangTagsFroExhibition(Exhibition exhibition) throws DBException {
        Map<String, String> expoLang = factoryMySql
                .createDescriptionTable(connection)
                .getAllDescription(exhibition);
        String langTags = "";
        for (Map.Entry<String, String> entry : expoLang.entrySet()) {
            langTags += entry.getKey() + " ";
        }
        exhibition.setLanguageTags(langTags);
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

    private void handleConnection() {
        try {
            connection = ConnectionPoolMySql.getInstance().getConnection();
            factoryMySql = new FactoryMySql();
        } catch (Exception exception) {
            LOGGER.error(exception);
        }
    }
}
