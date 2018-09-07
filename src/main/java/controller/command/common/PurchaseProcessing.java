package controller.command.common;

import controller.command.Command;
import controller.command.util.Links;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PurchaseProcessing implements Command {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.PURCHASE_FINISH_PAGE);

        dispatcher.forward(req, resp);
    }

}
