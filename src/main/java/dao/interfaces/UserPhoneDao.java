package dao.interfaces;

import exceptions.DBException;

import java.util.List;

public interface UserPhoneDao {

    List<String>  getPhones(String eMail) throws DBException;
    void insertPhone(String mail, String phone) throws DBException;
    void deletePhone(String mail, String phone) throws DBException;
}
