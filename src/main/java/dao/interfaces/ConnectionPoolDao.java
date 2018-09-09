package dao.interfaces;

import exceptions.DBException;

import java.sql.Connection;
/**
 * Interface realisation implies get connection
 * to specific DB
 *
 * @author Dmytro Galomko
 */
public interface ConnectionPoolDao {
    Connection getConnection() throws DBException;
}
