package dao.mysql;

import dao.interfaces.DBHelperDao;

import java.sql.Connection;

public class MySqlHelper implements DBHelperDao {

    @Override
    public Connection getConnection() {
        return null;
    }
}
