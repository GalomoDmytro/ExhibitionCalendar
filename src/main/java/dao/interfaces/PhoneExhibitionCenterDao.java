package dao.interfaces;

import exceptions.DBException;

import java.util.List;

public interface PhoneExhibitionCenterDao {

    List<String> getPhones(String id) throws DBException;
    void insertPhone(String id, String phone) throws DBException;
    void deletePhone(String id, String phone) throws DBException;
}
