package controller.command.user;

import controller.command.Command;
import controller.command.ServletHelper;
import controller.command.util.Links;
import entities.*;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet responsible for User home page
 */
public class UserHome extends ServletHelper implements Command {

    private Integer idUser;
    private User user;
    private List<String> phonesList;
    private List<String> ticketList = new ArrayList<>();
    private static final Logger LOGGER = Logger.getLogger(UserHome.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.USER_INFO_PAGE);

        getDataFromSession(req);
        getUserFromDB();
        getTicketDataForUser();
        setDataToReq(req);

        dispatcher.forward(req, resp);
    }

    /**
     * Obtain data about Exhibition, ExhibitionCenter,
     * Contract depend on Tickets. And save some of them for User
     */
    private void getTicketDataForUser() {
        handleConnection(LOGGER);
        try {
            List<Ticket> tickets = factoryDB.createTicket(connection)
                    .getAllTicketsForUser(user);
            for (Ticket ticket : tickets) {
                Contract contract = new Contract();
                Exhibition exhibition = new Exhibition();
                ExhibitionCenter exhibitionCenter = new ExhibitionCenter();
                factoryDB.createExhibitionContract(connection)
                        .prepareCEC(contract, exhibition, exhibitionCenter,
                                ticket.getContractId());

                ticketList.add(createTicketToShowUser(ticket, exhibition, exhibitionCenter));
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }
    }

    /**
     * Chose what data to show user about ticket
     *
     * @param ticket
     * @param exhibition
     * @param exhibitionCenter
     * @return ticket info for User
     */
    private String createTicketToShowUser(Ticket ticket, Exhibition exhibition,
                                          ExhibitionCenter exhibitionCenter) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Title: ").append(exhibition.getTitle()).append("; ");
        stringBuilder.append("ExpoCenter: ")
                .append(exhibitionCenter.getTitle()).append("; ");
        stringBuilder.append("Addr: ")
                .append(exhibitionCenter.getAddress()).append("; ");
        stringBuilder.append("Date to apply: ")
                .append(ticket.getDateToApply()).append("; ");
        return stringBuilder.toString();
    }

    private void setDataToReq(HttpServletRequest request) {
        request.setAttribute("user", user);
        request.setAttribute("phonesList", phonesList);
        request.setAttribute("ticketList", ticketList);
    }

    private void getDataFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        idUser = (Integer) session.getAttribute("userId");
    }

    private void getUserFromDB() {
        handleConnection(LOGGER);
        try {
            user = factoryDB.createUser(connection).getById(idUser);
            phonesList = factoryDB.createUserPhones(connection)
                    .getPhones(user.getMail());
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }
    }

}
