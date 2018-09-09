package controller.command.util;

import controller.command.Command;
import entities.Role;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Dmytro Galomko
 */
public class LogoutCommand implements Command {
    private static final Logger LOGGER = Logger.getLogger(LogoutCommand.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        getDataFromSession(req);

        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.INDEX_PAGE);
        dispatcher.forward(req, resp);
    }

    private void getDataFromSession(HttpServletRequest req) {
        HttpSession session = req.getSession(true);
        Integer idUser = (Integer) session.getAttribute("userId");
        LOGGER.info("LOG OUT idUser: " + idUser);
        session.setAttribute("role", Role.GUEST);
        session.setAttribute("userId", 1);
    }
}
