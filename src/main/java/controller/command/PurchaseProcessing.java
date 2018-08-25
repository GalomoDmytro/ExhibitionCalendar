package controller.command;

import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import entities.Ticket;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

public class PurchaseProcessing implements Command {

    private Connection connection;
    private FactoryMySql factoryMySql;
    private Ticket ticket;

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.PURCHASE_PROCESSING_PAGE);

        if(!inputDataValid()) {
            dispatcher = req.getRequestDispatcher(Links.CHECKOUT_PAGE);
        }

        sendBankCardProcessing();

        dispatcher.forward(req, resp);
    }

    private boolean inputDataValid() {
        return true;
    }

    private void sendBankCardProcessing() {
        // some payment logic's
        // ...
        bankResponse();
    }

    private void bankResponse() {
        handleConnection();

        try {
            factoryMySql.createTicket(connection).insertTicket(ticket);
        } catch (Exception exception) {

        } finally {
            closeConnection();
        }
    }

    private void handleConnection() {
        try {
            connection = ConnectionPoolMySql.getInstance().getConnection();
            factoryMySql = new FactoryMySql();
        } catch (Exception exception) {
        }
    }

    private void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception exception) {

        }
    }


}
