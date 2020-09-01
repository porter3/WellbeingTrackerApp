package com.jakeporter.WellbeingTracker.data;

import java.util.List;
import com.jakeporter.WellbeingTracker.entities.Role;

/**
 *
 * @author jake
 */
public interface RoleDao {

    Role getRoleById(int id);
    Role getRoleByRole(String role);
    List<Role> getAllRoles();
    void deleteRole(int id);
    void updateRole(Role role);
    Role createRole(Role role);
}
