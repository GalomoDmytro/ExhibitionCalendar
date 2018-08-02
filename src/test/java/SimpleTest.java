import dao.Connection.MySqlConnectionPool;
import dao.mysql.FactoryMySql;
import dao.mysql.UserMySql;
import entities.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SimpleTest {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/bookstore?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&useSSL=false\n"; //----------

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "1123581321";

    @Test
    public void myTest() {

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = MySqlConnectionPool.getDbConection().getConnection();
            FactoryMySql factoryMySql = new FactoryMySql();
            UserMySql userMySql = (UserMySql) factoryMySql.createUser(conn);
            User user = userMySql.getById(1);
            System.out.println(user.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
