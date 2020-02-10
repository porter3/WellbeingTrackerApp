package com.jakeporter.WellbeingTrackerAPI.service;

import com.jakeporter.WellbeingTrackerAPI.data.UserAccountDao;
import com.jakeporter.WellbeingTrackerAPI.entities.Role;
import com.jakeporter.WellbeingTrackerAPI.entities.UserAccount;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author jake
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    
    @Autowired
    UserAccountDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = userDao.getUserByUsername(username);
        
        Set<GrantedAuthority> grantedAuthorities = new HashSet();
        for (Role role : user.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), grantedAuthorities);
    }

}
