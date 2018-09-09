package controller.command;

import dao.Connection.ConnectionPoolMySql;
import dao.interfaces.FactoryDao;
import dao.mysql.FactoryMySql;
import org.apache.log4j.Logger;

import java.sql.Connection;

/**
 * Contains methods common to most servlets
 */
public abstract class ServletHelper {
    protected Connection connection;
    protected FactoryDao factoryDB;

    /**
     * Close connection to DB
     *
     * @param logger
     */
    protected void closeConnection(Logger logger) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception exception) {
            logger.error(exception);
        }
    }

    /**
     * Get connection to DB and create factoryDB
     *
     * @param logger
     */
    protected void handleConnection(Logger logger) {
        try {
            connection = ConnectionPoolMySql.getInstance().getConnection();
            factoryDB = new FactoryMySql();
        } catch (Exception exception) {
            logger.error(exception);
        }
    }
}
