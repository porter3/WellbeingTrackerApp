package com.jakeporter.WellbeingTrackerAPI.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author jake
 */

//@Configuration
//@EnableWebSecurity // enables Spring Security's web security support and provides Spring MVC integration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    UserDetailsService userDetails;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http    
//                .authorizeRequests()
//                    .antMatchers("/admin").hasRole("ADMIN")
//                    .antMatchers("/", "/home").permitAll()
//                    .antMatchers("/css/**", "/js/**", "/fonts/**", "/assets/**").permitAll()
//                    .anyRequest().hasRole("USER")
//                .and()
//                .formLogin()
//                    .loginPage("/login")
//                    .failureUrl("/login?login_error=1")
//                    .permitAll()
//                .and()
//                .logout()
//                    .logoutSuccessUrl("/")
//                    .permitAll();
        http.authorizeRequests().antMatchers("/").permitAll();
    }
    
    @Autowired
    public void configureGlobalInDB(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetails).passwordEncoder(bCryptPasswordEncoder());
    }
    
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
}
