package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.ServletHelper;
import controller.command.util.Links;
import entities.Exhibition;
import entities.ExhibitionCenter;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Dmytro Galomko
 */
public class CombineExWithExCenter extends ServletHelper implements Command {

    private String idExhibition;
    private String expoCenterId;

    private static final String FROM_EXPO = "fromExpo";
    private static final String FROM_CENTER = "fromCenter";

    private static final Logger LOGGER = Logger
            .getLogger(CombineExWithExCenter.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher dispatcher;

        dispatcher = req.getRequestDispatcher(Links.MODERATOR_COMBO_EXPO_WITH_HALL_PAGE);

        startFormContract(req);

        dispatcher.forward(req, resp);
    }

    private void startFormContract(HttpServletRequest req) {
        if (req.getParameter("expoCenterId") != null) {
            combineWithCenter(req);
        } else {
            combineWithExpo(req);
        }
    }

    private void combineWithExpo(HttpServletRequest req) {
        idExhibition = req.getParameter("idExhibition");
        req.setAttribute("combFrom", FROM_EXPO);
        req.setAttribute("combFromId", idExhibition);

        handleConnection(LOGGER);
        try {
            List<ExhibitionCenter> exhibitionCentersList
                    = factoryDB.createExhibitionCenter(connection)
                    .getAllExhibitionCenter();
            req.setAttribute("list", exhibitionCentersList);
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }
    }

    private void combineWithCenter(HttpServletRequest req) {
        expoCenterId = req.getParameter("expoCenterId");
        req.setAttribute("combFrom", FROM_CENTER);
        req.setAttribute("combFromId", expoCenterId);

        handleConnection(LOGGER);
        try {
            List<Exhibition> exhibitionList
                    = factoryDB.createExhibition(connection).getAllExhibition();
            req.setAttribute("list", exhibitionList);
        } catch (Exception exception) {
            LOGGER.error(exception);
        } finally {
            closeConnection(LOGGER);
        }
    }
}
