package dao.interfaces;

import exceptions.DBException;

import java.util.List;

public interface UserPhonesDao {

    List<String>  getPhones(String eMail) throws DBException;
}
