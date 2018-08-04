package temp;

import dao.Connection.ConnectionPoolMySql;
import dao.mysql.FactoryMySql;
import dao.mysql.UserMySql;
import entities.User;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class MyServlet extends HttpServlet {

    static final String DB_URL = "jdbc:mysql://localhost:3306/bookstore?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&useSSL=false"; //----------
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static String URL = "jdbc:mysql://localhost:3306/bookstore?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&useSSL=false";
    private GenericObjectPool connectionPool = null;
    public static final String USERNAME = "root";
    public static final String PASSWORD = "1123581321";



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = null;

        try {

//            ConnectionPoolMySql connectionPoolMySql = new ConnectionPoolMySql();
//            DataSource dataSource = connectionPoolMySql.setUp();
//            conn = dataSource.getConnection();
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
