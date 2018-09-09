package dao.interfaces;

import entities.Role;
import entities.User;
import exceptions.DBException;

/**
 * Defines methods for work with role table
 *
 * @author Dmytro Galomko
 */
public interface RoleDao {
    /**
     * Get role for specific user from role table
     *
     * @param id User
     * @return Enum Role
     * @throws DBException
     */
    Role getRoleById(Integer id) throws DBException;

    /**
     * Insert tole for user in to role table
     *
     * @param user entity
     * @param role Enum
     * @throws DBException
     */
    void insertRole(User user, Role role) throws DBException;

    /**
     * Delete role for user from role table
     *
     * @param user entity
     * @throws DBException
     */
    void delete(User user) throws DBException;

    /**
     * Change role for user in role table
     *
     * @param id   if User
     * @param role Enum
     * @throws DBException
     */
    void updateRole(Integer id, Role role) throws DBException;
}
