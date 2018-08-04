package dao.Connection;


import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import javax.sql.DataSource;
import java.sql.Connection;

public class ConnectionPoolMySql {

    static final String DB_URL = "jdbc:mysql://localhost:3306/bookstore?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&useSSL=false"; //----------
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static String URL = "jdbc:mysql://localhost:3306/bookstore?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&useSSL=false";
    private GenericObjectPool connectionPool = null;
    public static final String USERNAME = "root";
    public static final String PASSWORD = "1123581321";

    private static ConnectionPoolMySql instance = null;

    private ConnectionPoolMySql(){}

    public static ConnectionPoolMySql getInstance() {
        if(instance == null) {
            instance = new ConnectionPoolMySql();
        }

        return instance;
    }

    private DataSource setUp() throws Exception {
        Class.forName(DRIVER).newInstance();

        // Creates an instance of GenericObjectPool that holds our
        // pool of connections object.
        connectionPool = new GenericObjectPool();
        connectionPool.setMaxActive(10);
        connectionPool.setMaxIdle(5);
        connectionPool.setMinIdle(3);

        // Creates a connection factory object which will be use by
        // the pool to create the connection object. We passes the
        // JDBC url info, username and password.
        ConnectionFactory cf = new DriverManagerConnectionFactory(
                URL,
                USERNAME,
                PASSWORD);

        // Creates a PoolableConnectionFactory that will wraps the
        // connection object created by the ConnectionFactory to add
        // object pooling functionality.
        PoolableConnectionFactory pcf =
                new PoolableConnectionFactory(cf, connectionPool,
                        null, null, false, true);
        return new PoolingDataSource(connectionPool);
    }

    public Connection getConnection() throws Exception {
        DataSource dataSource = setUp();
        return dataSource.getConnection();
    }

//    private ConnectionPoolMySql() {
//
//    }
//
//    public static ConnectionPoolMySql getInstance() {
//        if (instance == null) {
//            instance = new ConnectionPoolMySql();
//        }
//
//        return instance;
//    }
//
//    private DataSource setUp() throws Exception {
//        Class.forName(DRIVER);
//        GenericObjectPool genericObjectPool = new GenericObjectPool();
//        genericObjectPool.setMaxActive(MAX_TOTAL);
//        genericObjectPool.setMaxIdle(MAX_IDLE);
//
//        ConnectionFactory conFactory = new DriverManagerConnectionFactory(URI, USERNAME, PASSWORD);
//        PoolableConnectionFactory poolableConnectionFactory =
//                new PoolableConnectionFactory(conFactory, genericObjectPool,
//                        null, null, false, true);
//        return new PoolingDataSource(genericObjectPool);
//    }
//
//    public Connection getConnection() {
//        try {
//            DataSource dataSource = setUp();
//            Connection connection = dataSource.getConnection();
//            return connection;
//        } catch (Exception ex) {
//            return null;
//        }
//    }


}







