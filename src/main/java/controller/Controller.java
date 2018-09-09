package controller;

import controller.command.Command;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Accepts input from doGet, and doPost.
 * Send Http Servlet request and response to ControllerHelper
 *
 * @author Dmytro Galomko
 */
@WebServlet(urlPatterns = {"/controller"})
public class Controller extends HttpServlet {

    private ControllerHelper controllerHelper = new ControllerHelper();
    private Command command;
    private static final Logger LOGGER = Logger.getLogger(Controller.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        manageRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        manageRequest(req, resp);
    }

    /**
     * Transmits data to the controllerHelper, and execute the result
     *
     * @param req
     * @param resp
     */
    private void manageRequest(HttpServletRequest req, HttpServletResponse resp) {
        command = controllerHelper.getCommand(req, resp);

        try {
            command.execute(req, resp);
        } catch (ServletException servletException) {
            LOGGER.error(servletException);
        } catch (IOException ioException) {
            LOGGER.error(ioException);
        }
    }
}
