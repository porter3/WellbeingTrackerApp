DROP DATABASE IF EXISTS WellbeingTracker;
CREATE DATABASE WellbeingTracker;
USE WellbeingTracker;

CREATE TABLE UserAccount(
	UserAccountId INT PRIMARY KEY AUTO_INCREMENT,
    UserName VARCHAR(15) UNIQUE NOT NULL,
    UserPassword VARCHAR(200) NOT NULL,
    FirstName VARCHAR(30) NOT NULL,
    LastName VARCHAR(30) NOT NULL,
    Email VARCHAR(254) NOT NULL,
    CreationTimestamp DATETIME NOT NULL,
    TimeZone VARCHAR(40) NOT NULL
);

CREATE TABLE `role`(
	RoleId INT PRIMARY KEY AUTO_INCREMENT,
    RoleName VARCHAR(30) NOT NULL
);

CREATE TABLE User_Role(
	UserAccountId INT NOT NULL,
    RoleId INT NOT NULL,
    PRIMARY KEY(UserAccountId, RoleId),
    FOREIGN KEY (UserAccountId) REFERENCES UserAccount(UserAccountId),
    FOREIGN KEY (RoleId) REFERENCES `role`(RoleId)
);

CREATE TABLE DayLog(
	DayLogId INT PRIMARY KEY AUTO_INCREMENT,
    UserAccountId INT NOT NULL,
    LogDate DATE NOT NULL,
    Notes VARCHAR(1200) NULL,
    CONSTRAINT fk_UserAccount_DayLog FOREIGN KEY (UserAccountId)
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

CREATE TABLE MetricEntry(
	MetricEntryId INT PRIMARY KEY AUTO_INCREMENT,
    DayLogId INT NOT NULL,
    MetricTypeId INT NOT NULL,
    MetricValue INT NOT NULL,
    EntryTime TIME NOT NULL,
    CONSTRAINT fk_DayLog_SMetricEntry FOREIGN KEY (DayLogId)
		REFERENCES DayLog(DayLogId),
	CONSTRAINT fk_MetricType_MetricEntry FOREIGN KEY (MetricTypeId)
		REFERENCES MetricType(MetricTypeId)
);



