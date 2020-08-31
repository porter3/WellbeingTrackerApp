/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jakeporter.WellbeingTrackerAPI.service;

import com.jakeporter.WellbeingTrackerAPI.data.DayLogDao;
import com.jakeporter.WellbeingTrackerAPI.entities.DayLog;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricEntry;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricType;
import com.jakeporter.WellbeingTrackerAPI.entities.UserAccount;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author jake
 */

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class DeleteServiceTest {
    
    @Autowired
    JdbcTemplate jdbc;
    
    @Autowired
    DeleteService deleteService;
    
    @Autowired
    AddService addService;
    
    @Autowired
    LookupService lookupService;
    
    @Autowired
    DayLogDao logDao;
    
    public DeleteServiceTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        jdbc.update("DROP TABLE metricentry");
        jdbc.update("DROP TABLE metrictype");
        jdbc.update("DROP TABLE daylog");
        jdbc.update("DROP TABLE user_role");
        jdbc.update("DROP TABLE useraccount");
        
        jdbc.update("CREATE TABLE UserAccount(\n" +
"	UserAccountId INT PRIMARY KEY AUTO_INCREMENT,\n" +
"    UserName VARCHAR(15) UNIQUE NOT NULL,\n" +
"    UserPassword VARCHAR(200) NOT NULL,\n" +
"    FirstName VARCHAR(30) NOT NULL,\n" +
"    LastName VARCHAR(30) NOT NULL,\n" +
"    Email VARCHAR(254) NOT NULL,\n" +
"    CreationTimestamp DATETIME NOT NULL,\n" +
"    TimeZone VARCHAR(40) NOT NULL\n" +
")");
        jdbc.update("CREATE TABLE User_Role(\n" +
"	UserAccountId INT NOT NULL,\n" +
"    RoleId INT NOT NULL,\n" +
"    PRIMARY KEY(UserAccountId, RoleId),\n" +
"    FOREIGN KEY (UserAccountId) REFERENCES UserAccount(UserAccountId),\n" +
"    FOREIGN KEY (RoleId) REFERENCES `role`(RoleId)\n" +
")");
        
        jdbc.update("CREATE TABLE DayLog(\n" +
"	DayLogId INT PRIMARY KEY AUTO_INCREMENT,\n" +
"    UserAccountId INT NOT NULL,\n" +
"    LogDate DATE NOT NULL,\n" +
"    Notes VARCHAR(1200) NULL,\n" +
"    CONSTRAINT fk_UserAccount_DayLog FOREIGN KEY (UserAccountId)\n" +
"		REFERENCES UserAccount(UserAccountId))");
        
        jdbc.update("CREATE TABLE MetricType(\n" +
"	MetricTypeId INT PRIMARY KEY AUTO_INCREMENT,\n" +
"    UserAccountId INT NOT NULL,\n" +
"    MetricName VARCHAR(50) NOT NULL,\n" +
"    Scale INT NULL,\n" +
"    Unit VARCHAR(50) NULL,\n" +
"    CONSTRAINT fk_UserAccount_MetricType FOREIGN KEY (UserAccountId)\n" +
"		REFERENCES UserAccount(UserAccountId)\n" +
")");
        
        jdbc.update("CREATE TABLE MetricEntry(\n" +
"	MetricEntryId INT PRIMARY KEY AUTO_INCREMENT,\n" +
"    DayLogId INT NOT NULL,\n" +
"    MetricTypeId INT NOT NULL,\n" +
"    MetricValue FLOAT NOT NULL,\n" +
"    EntryTime TIME NOT NULL,\n" +
"    CONSTRAINT fk_DayLog_SMetricEntry FOREIGN KEY (DayLogId)\n" +
"		REFERENCES DayLog(DayLogId),\n" +
"	CONSTRAINT fk_MetricType_MetricEntry FOREIGN KEY (MetricTypeId)\n" +
"		REFERENCES MetricType(MetricTypeId)\n" +
")");
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of deleteMetricEntry method, of class DeleteService.
     */
    @Test
    public void testDeleteMetricEntry() {
        UserAccount user = new UserAccount();
        user.setUsername("testname");
        user.setPassword("testpassword");
        user.setFirstName("testFirstname");
        user.setLastName("testLastname");
        user.setEmail(("testemail@email.com"));
        user.setCreationTime(LocalDateTime.now());
        user.setTimeZone("EST");
        user = addService.createNewAccount(user); // has ID and Role now
        
        MetricType type1 = new MetricType();
        type1.setMetricName("subjectiveTestMetric");
        type1.setScale(10);
        type1.setUser(user);
        type1 = addService.addMetricType(type1);
        
        DayLog log = new DayLog();
        log.setUser(user);
        log.setNotes("test notes");
        log.setLogDate(LocalDate.now());
        log = addService.addDayLog(log);
        
        MetricEntry entry = new MetricEntry();
        entry.setDayLog(log);
        entry.setMetricType(type1);
        entry.setMetricValue(8);
        entry.setEntryTime(Time.valueOf("00:00:00"));
        entry = addService.addMetricEntry(entry);
        assertNotNull(entry);
                
        deleteService.deleteMetricEntry(entry.getMetricEntryId());
        entry = lookupService.getMetricEntryById(entry.getMetricEntryId());
        assertNull(entry);
    }

    /**
     * Test of deleteDayLog method, of class DeleteService.
     */
    @Test
    public void testDeleteDayLog() {
        UserAccount user = new UserAccount();
        user.setUsername("testname");
        user.setPassword("testpassword");
        user.setFirstName("testFirstname");
        user.setLastName("testLastname");
        user.setEmail(("testemail@email.com"));
        user.setCreationTime(LocalDateTime.now());
        user.setTimeZone("EST");
        user = addService.createNewAccount(user); // has ID and Role now
        
        DayLog log = new DayLog();
        log.setUser(user);
        log.setNotes("test notes");
        log.setLogDate(LocalDate.now());
        log = addService.addDayLog(log);
        assertNotNull(log);
        
        deleteService.deleteDayLog(log.getDayLogId());
        log = logDao.getDayLogById(log.getDayLogId());
        assertNull(log);
    }

    /**
     * Test of deleteMetricType method, of class DeleteService.
     */
    @Test
    public void testDeleteMetricType() {
        UserAccount user = new UserAccount();
        user.setUsername("testname");
        user.setPassword("testpassword");
        user.setFirstName("testFirstname");
        user.setLastName("testLastname");
        user.setEmail(("testemail@email.com"));
        user.setCreationTime(LocalDateTime.now());
        user.setTimeZone("EST");
        user = addService.createNewAccount(user); // has ID and Role now
        
        MetricType type = new MetricType();
        type.setMetricName("subjectiveTestMetric");
        type.setScale(10);
        type.setUser(user);
        type = addService.addMetricType(type);
        assertNotNull(type);
        
        deleteService.deleteMetricType(type.getMetricTypeId());
        type = lookupService.getMetricTypeById(type.getMetricTypeId());
        assertNull(type);
    }
    
}
