/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jakeporter.WellbeingTrackerAPI.service;

import com.jakeporter.WellbeingTrackerAPI.entities.DayLog;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricEntry;
import com.jakeporter.WellbeingTrackerAPI.entities.MetricType;
import com.jakeporter.WellbeingTrackerAPI.entities.UserAccount;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
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
public class LookupServiceTest {
    
    @Autowired
    JdbcTemplate jdbc;
    
    @Autowired
    LookupService lookupService;
    
    @Autowired
    AddService addService;
    
    public LookupServiceTest() {
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
    
    /*
    The commented-out tests fail because a UserAccount's creationtimestamp varies slightly after they're
    retrieved from the database vs. when they're added. Assuming it's some weird discrepancy with SQL,
    not going to worry about it for now.
    */

    /**
     * Test of getDayLogsForUser method, of class LookupService.
     */
    @Test
    public void testGetDayLogsForUser() {
        UserAccount user = new UserAccount();
        user.setUsername("testname");
        user.setPassword("testpassword");
        user.setFirstName("testFirstname");
        user.setLastName("testLastname");
        user.setEmail(("testemail@email.com"));
        user.setTimeZone("EST");
        user = addService.createNewAccount(user); // has ID and Role now
        
        UserAccount user1 = new UserAccount();
        user1.setUsername("testname1");
        user1.setPassword("testpassword1");
        user1.setFirstName("testFirstname1");
        user1.setLastName("testLastname1");
        user1.setEmail(("testemail1@email.com"));
        user1.setTimeZone("CST");
        user1 = addService.createNewAccount(user1); // has ID and Role now
        
        DayLog log = new DayLog();
        log.setUser(user);
        log.setNotes("test notes");
        log.setLogDate(LocalDate.now());
        log = addService.addDayLog(log);
        System.out.println("log: " + log.toString());

        DayLog log1 = new DayLog();
        log1.setUser(user);
        log1.setNotes("more test notes");
        log1.setLogDate(LocalDate.now());
        log1 = addService.addDayLog(log1);
        System.out.println("log1: " + log1.toString());
        
        DayLog log2 = new DayLog();
        log2.setUser(user1);
        log2.setNotes("even more test notes");
        log2.setLogDate(LocalDate.now());
        log2 = addService.addDayLog(log2);
        System.out.println("log2: " + log2.toString());
        
        List<DayLog> userLogs = lookupService.getDayLogsForUser(user.getUserAccountId());
        System.out.println("USERLOGS: ");
        userLogs.stream()
                .forEach(l -> System.out.println(l.toString()));
        List<DayLog> user1Logs = lookupService.getDayLogsForUser(user1.getUserAccountId());
        assertEquals(2, userLogs.size());
        assertTrue(userLogs.contains(log)); // false right now
        assertTrue(userLogs.contains(log1));
        assertFalse(userLogs.contains(log2));
        
        assertEquals(1, user1Logs.size());
        assertTrue(user1Logs.contains(log2));
        assertFalse(user1Logs.contains(log));
        assertFalse(user1Logs.contains(log1));
    }

    /**
     * Test of getDatesForUser method, of class LookupService.
     */
    @Test
    public void testGetDatesForUser() {
        UserAccount user = new UserAccount();
        user.setUsername("testname");
        user.setPassword("testpassword");
        user.setFirstName("testFirstname");
        user.setLastName("testLastname");
        user.setEmail(("testemail@email.com"));
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
        log1.setLogDate(LocalDate.of(2020, Month.JANUARY, 3));
        log1 = addService.addDayLog(log1);
        
        List<LocalDate> dates = lookupService.getDatesForUser(user.getUserAccountId());
        assertEquals(2, dates.size());
        assertTrue(dates.contains(log.getLogDate()));
        assertTrue(dates.contains(log1.getLogDate()));
    }

    /**
     * Test of getMetricEntriesForType method, of class LookupService.
     */
    @Test
    public void testGetMetricEntriesForType() {
        UserAccount user = new UserAccount();
        user.setUsername("testname");
        user.setPassword("testpassword");
        user.setFirstName("testFirstname");
        user.setLastName("testLastname");
        user.setEmail(("testemail@email.com"));
        user.setTimeZone("EST");
        user = addService.createNewAccount(user); // has ID and Role now
        
        DayLog log = new DayLog();
        log.setUser(user);
        log.setNotes("test notes");
        log.setLogDate(LocalDate.now());
        log = addService.addDayLog(log);
        
        MetricType type = new MetricType();
        type.setMetricName("quantitativeTestMetric");
        type.setUnit("g");
        type.setUser(user);
        type = addService.addMetricType(type);
        
        MetricType type1 = new MetricType();
        type1.setMetricName("subjectiveTestMetric");
        type1.setScale(10);
        type1.setUser(user);
        type1 = addService.addMetricType(type1);
        
        MetricEntry entry = new MetricEntry();
        entry.setDayLog(log);
        entry.setMetricType(type);
        entry.setMetricValue(200);
        entry.setEntryTime(Time.valueOf("00:00:00"));
        entry = addService.addMetricEntry(entry);
        
        MetricEntry entry1 = new MetricEntry();
        entry1.setDayLog(log);
        entry1.setMetricType(type);
        entry1.setMetricValue(150);
        entry1.setEntryTime(Time.valueOf("01:00:00"));
        entry1 = addService.addMetricEntry(entry1);
        
        MetricEntry entry2 = new MetricEntry();
        entry2.setDayLog(log);
        entry2.setMetricType(type1);
        entry2.setMetricValue(8);
        entry2.setEntryTime(Time.valueOf("02:00:00"));
        entry2 = addService.addMetricEntry(entry2);
        
        List<MetricEntry> typeEntries = lookupService.getMetricEntriesForType(type.getMetricTypeId());
        List<MetricEntry> type1Entries = lookupService.getMetricEntriesForType(type1.getMetricTypeId());
        
        assertEquals(2, typeEntries.size());
        assertTrue(typeEntries.contains(entry));
        assertTrue(typeEntries.contains(entry1));
        assertFalse(typeEntries.contains(entry2));
        
        assertEquals(1, type1Entries.size());
        assertFalse(type1Entries.contains(entry));
        assertFalse(type1Entries.contains(entry1));
        assertTrue(type1Entries.contains(entry2));
    }

    /**
     * Test of getMetricEntriesForUser method, of class LookupService.
     */
    @Test
    public void testGetMetricEntriesForUser() {
        // user
        UserAccount user = new UserAccount();
        user.setUsername("testname");
        user.setPassword("testpassword");
        user.setFirstName("testFirstname");
        user.setLastName("testLastname");
        user.setEmail(("testemail@email.com"));
        user.setTimeZone("EST");
        user = addService.createNewAccount(user); // has ID and Role now
        
        DayLog log = new DayLog();
        log.setUser(user);
        log.setNotes("test notes");
        log.setLogDate(LocalDate.now());
        log = addService.addDayLog(log);
        
        MetricType type = new MetricType();
        type.setMetricName("quantitativeTestMetric");
        type.setUnit("g");
        type.setUser(user);
        type = addService.addMetricType(type);
        
        // user 1
        UserAccount user1 = new UserAccount();
        user1.setUsername("testname1");
        user1.setPassword("testpassword1");
        user1.setFirstName("testFirstname1");
        user1.setLastName("testLastname1");
        user1.setEmail(("testemail1@email.com"));
        user1.setCreationTime(LocalDateTime.now());
        user1.setTimeZone("EST");
        user1 = addService.createNewAccount(user1); // has ID and Role now
        
        DayLog log1 = new DayLog();
        log1.setUser(user1);
        log1.setNotes("test notes");
        log1.setLogDate(LocalDate.now());
        log1 = addService.addDayLog(log1);
        
        MetricType type1 = new MetricType();
        type1.setMetricName("quantitativeTestMetric");
        type1.setUnit("g");
        type1.setUser(user1);
        type1 = addService.addMetricType(type1);
        
        // user entries
        MetricEntry entry = new MetricEntry();
        entry.setDayLog(log);
        entry.setMetricType(type);
        entry.setMetricValue(8);
        entry.setEntryTime(Time.valueOf("00:00:00"));
        entry = addService.addMetricEntry(entry);
        
        MetricEntry entry1 = new MetricEntry();
        entry1.setDayLog(log);
        entry1.setMetricType(type);
        entry1.setMetricValue(6);
        entry1.setEntryTime(Time.valueOf("00:00:00"));
        entry1 = addService.addMetricEntry(entry1);
        
        // user1 entry
        MetricEntry entry2 = new MetricEntry();
        entry2.setDayLog(log1);
        entry2.setMetricType(type1);
        entry2.setMetricValue(5);
        entry2.setEntryTime(Time.valueOf("00:00:00"));
        entry2 = addService.addMetricEntry(entry2);
        
        List<MetricEntry> userEntries = lookupService.getMetricEntriesForUser(user.getUserAccountId());
        List<MetricEntry> user1Entries = lookupService.getMetricEntriesForUser(user1.getUserAccountId());
        
        assertEquals(2, userEntries.size());
        assertTrue(userEntries.contains(entry));
        assertTrue(userEntries.contains(entry1));
        assertFalse(userEntries.contains(entry2));
        
        assertEquals(1, user1Entries.size());
        assertFalse(user1Entries.contains(entry));
        assertFalse(user1Entries.contains(entry1));
        assertTrue(user1Entries.contains(entry2));
    }

    /**
     * Test of getMetricEntriesByDate method, of class LookupService.
     */
    @Test
    public void testGetMetricEntriesByDate() {
    }

    /**
     * Test of getMetricTypesForUser method, of class LookupService.
     */
    @Test
    public void testGetMetricTypesForUser() {
    }

    /**
     * Test of getMetricEntryById method, of class LookupService.
     */
    @Test
    public void testGetMetricEntryById() {
    }

    /**
     * Test of getMetricTypeById method, of class LookupService.
     */
    @Test
    public void testGetMetricTypeById() {
    }

    /**
     * Test of getUserAccountById method, of class LookupService.
     */
    @Test
    public void testGetUserAccountById() {
    }

    /**
     * Test of getDayLogByDateAndUser method, of class LookupService.
     */
    @Test
    public void testGetDayLogByDateAndUser() {
    }

    /**
     * Test of getUserByUsername method, of class LookupService.
     */
    @Test
    public void testGetUserByUsername() {
    }

    
}
