package com.jakeporter.WellbeingTrackerAPI.data;

import com.jakeporter.WellbeingTrackerAPI.entities.UserAccount;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jake
 */

@Repository
@Profile("database")
public class UserAccountDaoDBImpl implements UserAccountDao{
    
    @Autowired
    private JdbcTemplate jdbc;
    
    public static final class UserAccountMapper implements RowMapper<UserAccount>{

        @Override
        public UserAccount mapRow(ResultSet rs, int i) throws SQLException {
            UserAccount user = new UserAccount();
            user.setUserAccountId(rs.getInt("useraccountid"));
            user.setUserName(rs.getString("username"));
            user.setFirstName(rs.getString("firstname"));
            user.setLastName(rs.getString("lastname"));
            user.setEmail(rs.getString("email"));
            user.setCreationTime(Timestamp.valueOf(rs.getString("creationtimestamp")).toLocalDateTime());
            user.setTimeZone(rs.getString("timezone"));
            return user;
        }
        
    }

    @Override
    public UserAccount addUserAccount(UserAccount user) {
        final String INSERT_USER = "INSERT INTO useraccount"
                + "(username, userpassword, firstname, lastname, email, creationtimestamp, timezone)"
                + " VALUES(?,?,?,?,?,?,?)";
        LocalDateTime creationTime = LocalDateTime.now().withNano(0);
        Timestamp creationTimestamp = Timestamp.valueOf(creationTime);
        jdbc.update(INSERT_USER, user.getUserName(), user.getPassword(), user.getFirstName(), 
                user.getLastName(), user.getEmail(), creationTimestamp, user.getTimeZone());
        
        // set user's creation time and ID
        user.setCreationTime(creationTime);
        user.setUserAccountId(jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class));
        return user;
    }

    @Override
    public UserAccount getUserAccountById(int userId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    
     

}
