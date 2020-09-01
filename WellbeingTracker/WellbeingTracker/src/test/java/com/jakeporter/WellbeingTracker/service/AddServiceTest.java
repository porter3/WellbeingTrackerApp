/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jakeporter.WellbeingTracker.service;

import com.jakeporter.WellbeingTracker.entities.DayLog;
import com.jakeporter.WellbeingTracker.entities.MetricEntry;
import com.jakeporter.WellbeingTracker.entities.MetricType;
import com.jakeporter.WellbeingTracker.entities.UserAccount;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Time;
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
public class AddServiceTest {
   
    @Autowired
    JdbcTemplate jdbc;
    
    @Autowired
    LookupService lookupService;
    
    @Autowired
    AddService addService;
    
    public AddServiceTest() {
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
     * Test of createNewAccount method, of class AddService.
     */
    @Test
    public void testCreateNewAccount() {
        UserAccount user = new UserAccount();
        user.setUsername("testname");
        user.setPassword("testpassword");
        user.setFirstName("testFirstname");
        user.setLastName("testLastname");
        user.setEmail(("testemail@email.com"));
        user.setCreationTime(LocalDateTime.now());
        user.setTimeZone("EST");
        
        UserAccount userFromDB = addService.createNewAccount(user);
        user.setUserAccountId(1);
        assertEquals(user, userFromDB);
    }

    /**
     * Test of addMetricTypes method, of class AddService.
     */
    @Test
    public void testAddMetricTypes() {
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
        MetricType type1FromDB = addService.addMetricType(type1);
        assertEquals(1, type1FromDB.getMetricTypeId());
        type1.setMetricTypeId(1);
        assertEquals(type1, type1FromDB);
                
        MetricType type2 = new MetricType();
        type2.setMetricName("quantitativeTestMetric");
        type2.setUnit("g");
        type2.setUser(user);
        MetricType type2FromDB = addService.addMetricType(type2);
        assertEquals(2, type2FromDB.getMetricTypeId());
        type2.setMetricTypeId(2);
        assertEquals(type2, type2FromDB);
        
        assertEquals(2, lookupService.getMetricTypesForUser(user.getUserAccountId()).size());
    }

    /**
     * Test of addDayLog method, of class AddService.
     */
    @Test
    public void testAddDayLog() {
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
        DayLog logFromDB = addService.addDayLog(log);
        assertEquals(1, logFromDB.getDayLogId());
        log.setDayLogId(1);
        assertEquals(log, logFromDB);
    }

    /**
     * Test of addMetricEntry method, of class AddService.
     */
    @Test
    public void testAddMetricEntry() {
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
        MetricEntry entryFromDB = addService.addMetricEntry(entry);
        assertEquals(1, entryFromDB.getMetricEntryId());
        entry.setMetricEntryId(1);
        assertEquals(entry, entryFromDB);
        
    }

    /**
     * Test of fillDayLogGaps method, of class AddService.
     */
    @Test
    public void testFillDayLogGaps() {
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
        log.setLogDate(LocalDate.of(2020, Month.JANUARY, 1));
        log = addService.addDayLog(log);
        
        DayLog log1 = new DayLog();
        log1.setUser(user);
        log1.setNotes("more test notes");
        log1.setLogDate(LocalDate.of(2020, Month.JANUARY, 4));
        log1 = addService.addDayLog(log1);
        
        // verify only these dayLogs exist so far
        assertEquals(2, lookupService.getDatesForUser(user.getUserAccountId()).size());
        addService.fillDayLogGaps(user.getUserAccountId());
        assertEquals(4, lookupService.getDatesForUser(user.getUserAccountId()).size());
    }

    
}
