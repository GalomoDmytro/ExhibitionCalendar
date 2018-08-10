package controller.command;

import entities.Role;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutCommand implements Command {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        session.setAttribute("role", Role.GUEST);
        session.setAttribute("userId", null);

        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.HOME_PAGE);
        dispatcher.forward(req, resp);
    }
}
