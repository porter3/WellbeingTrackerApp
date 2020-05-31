package com.jakeporter.WellbeingTrackerAPI.data;

import com.jakeporter.WellbeingTrackerAPI.data.RoleDaoDBImpl.RoleMapper;
import com.jakeporter.WellbeingTrackerAPI.entities.Role;
import com.jakeporter.WellbeingTrackerAPI.entities.UserAccount;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jake
 */

@Repository
public class UserAccountDaoDBImpl implements UserAccountDao{
    
    @Autowired
    private JdbcTemplate jdbc;
    
    public static final class UserAccountMapper implements RowMapper<UserAccount>{

        @Override
        public UserAccount mapRow(ResultSet rs, int i) throws SQLException {
            UserAccount user = new UserAccount();
            user.setUserAccountId(rs.getInt("useraccountid"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("userpassword"));
            user.setFirstName(rs.getString("firstname"));
            user.setLastName(rs.getString("lastname"));
            user.setEmail(rs.getString("email"));
            user.setCreationTime(Timestamp.valueOf(rs.getString("creationtimestamp")).toLocalDateTime());
            user.setTimeZone(rs.getString("timezone"));
            return user;
        }
    }

    @Override
    @Transactional
    public UserAccount addUserAccount(UserAccount user) {
        final String INSERT_USER = "INSERT INTO useraccount"
                + "(username, userpassword, firstname, lastname, email, creationtimestamp, timezone)"
                + " VALUES(?,?,?,?,?,?,?)";
        LocalDateTime creationTime = LocalDateTime.now().withNano(0);
        Timestamp creationTimestamp = Timestamp.valueOf(creationTime);
        jdbc.update(INSERT_USER, user.getUsername(), user.getPassword(), user.getFirstName(), 
                user.getLastName(), user.getEmail(), creationTimestamp, user.getTimeZone());
        // set user's creation time and ID
        int userId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        user.setUserAccountId(userId);
        creationTimestamp = (jdbc.queryForObject("SELECT creationtimestamp FROM useraccount WHERE useraccountid = ?", Timestamp.class, userId));
        user.setCreationTime(creationTimestamp.toLocalDateTime());
        
        
        // add ROLE_USER for user (hard-coded for a user to be added as a USER and not an ADMIN)
        final String INSERT_USER_ROLE = "INSERT INTO user_role(useraccountid, roleid) VALUES(?,?)";
        jdbc.update(INSERT_USER_ROLE, user.getUserAccountId(), 2);
        Role userRole = new Role();
        userRole.setRoleId(2);
        userRole.setRoleName("ROLE_USER");
        Set<Role> roleList = Set.of(userRole);
        user.setRoles(roleList);
        
        return user;
    }

    @Override
    public UserAccount getUserAccountById(int userId) {
        try{
            final String SELECT_USER_BY_ID = "SELECT * FROM useraccount WHERE useraccountid = ?";
            UserAccount user = jdbc.queryForObject(SELECT_USER_BY_ID, new UserAccountMapper(), userId);
            user.setRoles(getRolesForUser(user.getUserAccountId()));
            return user;
        }
        catch (DataAccessException e){
            return null;
        }
    }

    @Override
    public List<UserAccount> getAllUserAccounts() {
        final String SELECT_ALL_USER_ACCOUNTS = "SELECT * FROM useraccount";
        return jdbc.query(SELECT_ALL_USER_ACCOUNTS, new UserAccountMapper());
    }

    @Override
    public UserAccount editUserAccount(UserAccount updatedUser) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteUserAccount(int userId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public UserAccount getUserByUsername(String username) {
        try{
            final String SELECT_USER_BY_USERNAME = "SELECT * FROM useraccount WHERE username = ?";
            UserAccount user = jdbc.queryForObject(SELECT_USER_BY_USERNAME, new UserAccountMapper(), username);
            user.setRoles(getRolesForUser(user.getUserAccountId()));
            return user;
        }
        catch(DataAccessException e){
            return null;
        }
    }

     private Set<Role> getRolesForUser(int userId) throws DataAccessException {
        final String SELECT_ROLES_FOR_USER = "SELECT * FROM user_role "
                + "JOIN role ON user_role.roleid = role.roleid "
                + "WHERE user_role.useraccountid = ?";
        Set<Role> roles = new HashSet(jdbc.query(SELECT_ROLES_FOR_USER, new RoleMapper(), userId));
        return roles;
    }
}
