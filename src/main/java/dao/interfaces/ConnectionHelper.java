package dao.interfaces;

import exceptions.DBException;

import java.sql.Connection;

public interface ConnectionHelper {

    Connection getConnection() throws DBException;
}
