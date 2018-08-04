package dao.interfaces;

import exceptions.DBException;

import java.sql.Connection;

public interface ConnectionPool {
    Connection getConnection() throws DBException;
}
