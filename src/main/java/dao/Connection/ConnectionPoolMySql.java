package dao.Connection;

import dao.interfaces.ConnectionPoolDao;
import exceptions.DBException;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;
import utility.PasswordHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ResourceBundle;

/**
 * Singleton
 * Serves to get the connection to the database
 */
public class ConnectionPoolMySql implements ConnectionPoolDao {

    private GenericObjectPool connectionPool = null;
    private static final Logger LOGGER = Logger.getLogger(PasswordHandler.class);
    private static final ResourceBundle QUERIES = ResourceBundle.getBundle("DBConnection");
    private volatile static ConnectionPoolMySql instance = null;


    private ConnectionPoolMySql() {
    }

    public static ConnectionPoolMySql getInstance() {
        if (instance == null) {
            synchronized (ConnectionPoolMySql.class) {
                if (instance == null) {
                    instance = new ConnectionPoolMySql();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws DBException {
        try {
            DataSource dataSource = setUp();
            return dataSource.getConnection();
        } catch (Exception exception) {
            LOGGER.error(exception);
            throw new DBException(exception);
        }
    }

    private DataSource setUp() throws Exception {
        Class.forName(QUERIES.getString("mysql.driver")).newInstance();

        connectionPool = new GenericObjectPool();
        connectionPool.setMaxActive(Integer.valueOf(QUERIES.getString("mysql.maxActive")));
        connectionPool.setMaxIdle(Integer.valueOf(QUERIES.getString("mysql.maxIdle")));
        connectionPool.setMinIdle(Integer.valueOf(QUERIES.getString("mysql.minIdle")));

        ConnectionFactory cf = new DriverManagerConnectionFactory(
                prepareURL(),
                QUERIES.getString("mysql.username"),
                QUERIES.getString("mysql.password"));

        PoolableConnectionFactory pcf =
                new PoolableConnectionFactory(cf, connectionPool,
                        null, null, false, true);
        return new PoolingDataSource(connectionPool);
    }

    private String prepareURL() {
        StringBuilder buildUrl = new StringBuilder();
        buildUrl.append(QUERIES.getString("mysql.url")).append("&");
        buildUrl.append(QUERIES.getString("mysql.useJDBCCompliantTimezoneShift")).append("&");
        buildUrl.append(QUERIES.getString("mysql.useLegacyDatetimeCode")).append("&");
        buildUrl.append(QUERIES.getString("mysql.serverTimeZone")).append("&");
        buildUrl.append(QUERIES.getString("mysql.autoReconnect")).append("&");
        buildUrl.append(QUERIES.getString("mysql.useSSL"));
        return buildUrl.toString();
    }
}







