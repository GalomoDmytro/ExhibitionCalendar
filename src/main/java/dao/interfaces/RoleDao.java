package dao.interfaces;

import entities.Role;
import entities.User;
import exceptions.DBException;

public interface RoleDao {
    Role getRoleById(Integer id) throws DBException;
    void insertRole(User user, Role role) throws DBException;
    void delete(User user) throws DBException;
}
