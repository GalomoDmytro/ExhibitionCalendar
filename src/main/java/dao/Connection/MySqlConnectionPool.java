package dao.Connection;

import dao.interfaces.ConnectionHelper;
import exceptions.DBException;
import org.apache.commons.dbcp2.*;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySqlConnectionPool implements ConnectionHelper {

    private static final String URI = "jdbc:mysql://localhost:3306/bookstore?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=true";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1123581321";
    private static final int MAX_TOTAL = 100;
    private static final int MAX_IDLE = 50;

    private DataSource dataSource;
    private static volatile MySqlConnectionPool instance;


    private MySqlConnectionPool() {
//        try {
//            Class.forName(DRIVER);
//        } catch (ClassNotFoundException e) {
//        }
        ConnectionFactory connectionFactory =
                new DriverManagerConnectionFactory(URI, USERNAME, PASSWORD);

        PoolableConnectionFactory poolableConnectionFactory =
                new PoolableConnectionFactory(connectionFactory, null);

        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxIdle(MAX_IDLE);
        config.setMaxTotal(MAX_TOTAL);

        ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory, null);
        poolableConnectionFactory.setPool(connectionPool);

        dataSource = new PoolingDataSource<>(connectionPool);
    }

    public static MySqlConnectionPool getInstance() {
        if(instance == null) {
            synchronized (MySqlConnectionPool.class) {
                instance = new MySqlConnectionPool();
            }
        }

        return new MySqlConnectionPool();
    }

    @Override
    public Connection getConnection() throws DBException {
        try {
            return dataSource.getConnection();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }
}
