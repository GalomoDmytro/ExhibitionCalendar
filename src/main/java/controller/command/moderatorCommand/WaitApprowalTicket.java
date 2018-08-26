package controller.command.moderatorCommand;

import controller.command.Command;
import controller.command.Links;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WaitApprowalTicket implements Command {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.WAIT_APPROVAL_TICKETS_PAGE);

        dispatcher.forward(req, resp);
    }
}
