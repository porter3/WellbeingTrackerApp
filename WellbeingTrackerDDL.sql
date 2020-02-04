DROP DATABASE IF EXISTS WellbeingTracker;
CREATE DATABASE WellbeingTracker;
USE WellbeingTracker;

CREATE TABLE UserAccount(
	UserAccountId INT PRIMARY KEY AUTO_INCREMENT,
    UserName VARCHAR(15) UNIQUE NOT NULL,
    UserPassword VARCHAR(50) NOT NULL,
    FirstName VARCHAR(30) NOT NULL,
    LastName VARCHAR(30) NOT NULL,
    Email VARCHAR(254) NOT NULL,
    CreationTimestamp DATETIME NOT NULL,
    TimeZone VARCHAR(40) NOT NULL
);

CREATE TABLE DayLog(
	DayLogId INT PRIMARY KEY AUTO_INCREMENT,
    UserAccountId INT NOT NULL,
    LogDate DATE NOT NULL,
    CONSTRAINT fk_User_DayLog FOREIGN KEY (UserAccountId)
		REFERENCES UserAccount(UserAccountId)
	-- STRECH: Composite key of DayLogId and UserId (should begin incrementing at 1 for a new user)
);

-- Shouldn't need an isSubjective column. Can determine that by checking if scale/unit is null.
CREATE TABLE MetricType(
	MetricTypeId INT PRIMARY KEY AUTO_INCREMENT,
    UserAccountId INT NOT NULL,
    MetricName VARCHAR(50) NOT NULL,
    Scale INT NULL,
    Unit VARCHAR(50) NULL,
    CONSTRAINT fk_UserAccount_MetricType FOREIGN KEY (UserAccountId)
		REFERENCES UserAccount(UserAccountId)
);

CREATE TABLE MetricSetEntry(
	MetricSetEntryId INT PRIMARY KEY AUTO_INCREMENT,
    DayLogId INT NOT NULL,
    CONSTRAINT fk_DayLog_MetricSetEntry FOREIGN KEY (DayLogId)
		REFERENCES DayLog(DayLogId)
);
    
CREATE TABLE MetricSetChildEntry(
	MetricSetChildEntryId INT PRIMARY KEY AUTO_INCREMENT,
    MetricSetEntryId INT NOT NULL,
    MetricTypeId INT NOT NULL,
    MetricValue INT NOT NULL,
    EntryTime TIME NOT NULL,
    CONSTRAINT fk_MetricSetEntry_MetricSetChildEntry FOREIGN KEY (MetricSetEntryId)
		REFERENCES MetricSetEntry(MetricSetEntryId),
	CONSTRAINT fk_MetricType_MetricSetChildEntry FOREIGN KEY (MetricTypeId)
		REFERENCES MetricType(MetricTypeId)
);

CREATE TABLE StandaloneMetricEntry(
	StandaloneMetricEntryId INT PRIMARY KEY AUTO_INCREMENT,
    DayLogId INT NOT NULL,
    MetricTypeId INT NOT NULL,
    MetricValue INT NOT NULL,
    EntryTime TIME NOT NULL,
    CONSTRAINT fk_DayLog_StandaloneMetricEntry FOREIGN KEY (DayLogId)
		REFERENCES DayLog(DayLogId),
	CONSTRAINT fk_MetricType_StandaloneMetricEntry FOREIGN KEY (MetricTypeId)
		REFERENCES MetricType(MetricTypeId)
);



