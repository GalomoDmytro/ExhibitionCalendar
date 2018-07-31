package dao.interfaces;

import entities.Role;
import exceptions.DBException;

public interface RoleDao {
    Role getRoleById(Integer id) throws DBException;
}
