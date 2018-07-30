package dao.mysql;

import dao.interfaces.UserDao;
import entities.User;

import java.util.List;

public class UserMySql extends MySqlHelper implements UserDao {

    private final String FIELD_ID = "";
    private final String FIELD_NAME = "";
    private final String FIELD_MAIL = "";
    private final String FIELD_FIRST_NAME = "";
    private final String FIELD_LAST_NAME = "";
    private final String FIELD_PASSWORD = "";

//    private final String FIELD_PHONE = "";

    @Override
    public User getById(Integer id) {
        return null;
    }

    @Override
    public User getByName(String name) {
        return null;
    }

    @Override
    public User getByMail(String eMail) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public boolean deleteUser(User user) {
        return false;
    }
}
