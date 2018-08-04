package temp;

import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import dao.mysql.UserMySql;
import entities.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class MyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = null;

        try {

            conn = ConnectionPoolMySql.getInstance().getConnection();

            FactoryMySql factoryMySql = new FactoryMySql();
            UserMySql userMySql = (UserMySql) factoryMySql.createUser(conn);
            User user = userMySql.getById(1);
            System.out.println(user.toString());
            req.setAttribute("con", user.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            req.setAttribute("con", "EXCEPTION " + ex.toString());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/list.jsp");
        requestDispatcher.forward(req, resp);
    }


}
