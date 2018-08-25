package controller.command;

import controller.filter.AccessSecurityFilter;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/errorHandler")
public class ErrorHandler extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ErrorHandler.class);
    // TODO: Refactor and remove this class

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("Error handler class ------");
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.ERROR_PAGE);
        dispatcher.forward(req, resp);
    }

}
