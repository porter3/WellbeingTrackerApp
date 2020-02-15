USE WellbeingTracker;

INSERT INTO useraccount(username, userpassword, firstname, lastname, email, creationtimestamp, timezone) VALUES
('jakep310', '$2a$10$7CjTHzwJJRjecs57ITJEDeuPPyy.mduJiDMP/sfael7JUnhsX7w/y', 'Jake', 'Porter', 'jakeporter310@gmail.com', '2019-02-04 11:32:00', 'EST');

SELECT * FROM useraccount;

INSERT INTO metrictype(useraccountid, metricname, scale, unit) VALUES
(1, 'Hours of Sleep', 0, 'hours'),
(1, 'Protein Intake', 0, 'g'),
(1, 'Mood', 10, '');

SELECT * FROM metrictype;

INSERT INTO daylog(useraccountid, logdate, notes) VALUES
(1, '2020-02-10', 'random notes'),
(1, '2020-02-11', ''),
(1, '2020-02-12', ''),
(1, '2020-02-13', 'this is today!');

SELECT * FROM daylog WHERE useraccountid = 1;

INSERT INTO metricentry(daylogid, metrictypeid, metricvalue, entrytime) VALUES
(1, 1, 8, '10:20:12.0000000'),
(1, 2, 200, '10:20:12.0000000'),
(1, 3, 6, '10:20:12.0000000'),
(2, 1, 6, '10:20:12.0000000'),
(2, 2, 130, '10:20:12.0000000'),
(2, 3, 9, '10:20:12.0000000'),
(3, 2, 210, '10:20:12.0000000'),
(3, 3, 6,  '10:20:12.0000000'),
(4, 2, 40, '08:20:12.0000000');

SELECT * FROM metricentry;

DELETE FROM metricentry WHERE daylogid = 17;

UPDATE metricentry SET daylogid = 1, metrictypeid = 1, metricvalue = 2, entrytime = '08:20:12.0000000' WHERE metricentryid = 1;

INSERT INTO `role`(RoleId, RoleName) VALUES
(1, "ROLE_ADMIN"),
(2, "ROLE_USER");

INSERT INTO User_Role(UserAccountId, RoleId) VALUES
(1,1),
(1,2);

SET SQL_SAFE_UPDATES=0;
