package dao.mysql;

import dao.interfaces.QueryHelper;

import java.sql.Connection;
import java.util.ResourceBundle;

public class ConnectionManagerMySQL implements QueryHelper {
    private ResourceBundle resource = ResourceBundle.getBundle("QuerriesMySql");

    public ConnectionManagerMySQL(){}

    @Override
    public String getProperty(String s) {
        return resource.getString(s);
    }
}
