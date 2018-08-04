package dao.interfaces;

import exceptions.DBException;

import java.sql.Connection;

public interface ConnectionPoolDao {
    Connection getConnection() throws DBException;
}
