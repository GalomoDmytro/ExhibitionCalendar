package dao.Connection;

import dao.interfaces.ConnectionHelper;
import exceptions.DBException;
import org.apache.commons.dbcp2.*;


import java.sql.Connection;
import java.sql.SQLException;

public class MySqlConnection implements ConnectionHelper {

//    private static final String URI = "jdbc:mysql://localhost:3306/bookstore?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=true";
//    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
//    private static final String USERNAME = "root";
//    private static final String PASSWORD = "1123581321";
//    private static final int MAX_TOTAL = 100;
//    private static final int MAX_IDLE = 50;
//
//    private DataSource dataSource;
//    private static volatile MySqlConnection instance;


    //    private MySqlConnection() {
////        try {
////            Class.forName(DRIVER);
////        } catch (ClassNotFoundException e) {
////        }
//        ConnectionFactory connectionFactory =
//                new DriverManagerConnectionFactory(URI, USERNAME, PASSWORD);
//
//        PoolableConnectionFactory poolableConnectionFactory =
//                new PoolableConnectionFactory(connectionFactory, null);
//
//        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
//        config.setMaxIdle(MAX_IDLE);
//        config.setMaxTotal(MAX_TOTAL);
//
//        ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory, null);
//        poolableConnectionFactory.setPool(connectionPool);
//
//        dataSource = new PoolingDataSource<>(connectionPool);
//    }
//
//    public static MySqlConnection getInstance() {
//        if(instance == null) {
//            synchronized (MySqlConnection.class) {
//                instance = new MySqlConnection();
//            }
//        }
//
//        return new MySqlConnection();
//    }
//
//    @Override
//    public Connection getConnection() throws DBException {
//        try {
//            return dataSource.getConnection();
//        } catch (SQLException exception) {
//            throw new DBException(exception);
//        }
//    }
    private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/emp";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root123";
    private static final int CONN_POOL_SIZE = 5;


    private BasicDataSource basicDataSource = new BasicDataSource();
    private MySqlConnection instance;

    private MySqlConnection() {
        basicDataSource.setDriverClassName(DRIVER_CLASS_NAME);
        basicDataSource.setUrl(DB_URL);
        basicDataSource.setUsername(DB_USER);
        basicDataSource.setPassword(DB_PASSWORD);
        basicDataSource.setInitialSize(CONN_POOL_SIZE);
    }

    public MySqlConnection getInstance() {
        if(instance == null) {
            instance = new MySqlConnection();
        }
        return instance;
    }

//    private static class DataSourceHolder {
//        private static final MySqlConnection INSTANCE = new MySqlConnection();
//    }

//    public static MySqlConnection getInstance() {
//        return DataSourceHolder.INSTANCE;
//    }


    @Override
    public Connection getConnection() throws DBException {
        try {
            return basicDataSource.getConnection();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }
    }
}
