package controller;

import controller.command.Command;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet(urlPatterns = {"/*", "admin/*", "moderator/*", "user/*"})
public class Controller extends HttpServlet {

    private ControllerHelper controllerHelper = new ControllerHelper();
    private Command command;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        manageRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        manageRequest(req, resp);
    }

    private void manageRequest(HttpServletRequest req, HttpServletResponse resp)  {
        command = controllerHelper.getCommand(req, resp);

        try {
            command.execute(req, resp);
        } catch (ServletException servletException){

        } catch (IOException ioException) {

        }
    }
}
