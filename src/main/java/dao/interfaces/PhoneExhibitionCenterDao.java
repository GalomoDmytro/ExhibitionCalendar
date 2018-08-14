package dao.interfaces;

import exceptions.DBException;

import java.util.List;

public interface PhoneExhibitionCenterDao {

    List<String> getPhones(Integer id) throws DBException;
    void insertPhone(Integer id, String phone) throws DBException;
    void deletePhone(Integer id) throws DBException;
}
