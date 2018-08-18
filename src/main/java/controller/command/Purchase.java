package controller.command;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Purchase implements Command {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.PURCHASE_PAGE);

        if(req.getParameter("cancel") != null) {
            dispatcher = req.getRequestDispatcher(Links.HOME_PAGE);
        } if(req.getParameter("buy") != null) {

        }

        dispatcher.forward(req, resp);
    }
}
