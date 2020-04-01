USE WellbeingTrackerPresentation;

INSERT INTO useraccount(username, userpassword, firstname, lastname, email, creationtimestamp, timezone) VALUES
('jakep310', '$2a$10$7CjTHzwJJRjecs57ITJEDeuPPyy.mduJiDMP/sfael7JUnhsX7w/y', 'Jake', 'Porter', 'jakeporter310@gmail.com', '2019-02-04 11:32:00', 'EST');

DELETE FROM user_role WHERE useraccountid = 4;
DELETE FROM useraccount WHERE useraccountid = 4;
SELECT * FROM useraccount;

INSERT INTO metrictype(useraccountid, metricname, scale, unit) VALUES
(1, 'sleep time', 0, 'hours'),
(1, 'protein', 0, 'g'),
(1, 'mood', 10, '');

SELECT * FROM metrictype;

DELETE FROM metrictype WHERE metrictypeid = 5;

INSERT INTO daylog(useraccountid, logdate, notes) VALUES
(1, '2020-02-10', 'random notes'),
(1, '2020-02-11', ''),
(1, '2020-02-12', ''),
(1, '2020-02-13', 'this is today!');

SELECT * FROM daylog WHERE useraccountid = 1 ORDER BY logdate;

UPDATE daylog set notes = 'check dem notesssssssssssss' WHERE daylogid = 216;

INSERT INTO metricentry(daylogid, metrictypeid, metricvalue, entrytime) VALUES
(1, 1, 8, '10:20:12.0000000'),
(1, 2, 200, '10:20:12.0000000'),
(1, 3, 6, '10:20:12.0000000'),
(2, 1, 6, '10:20:12.0000000'),
(2, 2, 130, '10:20:12.0000000'),
(2, 3, 9, '10:20:12.0000000'),
(3, 2, 210, '10:20:12.0000000'),
(3, 3, 6,  '10:20:12.0000000');

SELECT * FROM metricentry;

DELETE FROM metricentry WHERE daylogid = 17;

UPDATE metricentry SET daylogid = 1, metrictypeid = 1, metricvalue = 2, entrytime = '08:20:12.0000000' WHERE metricentryid = 1;

INSERT INTO User_Role(UserAccountId, RoleId) VALUES
(1,1),
(1,2);

INSERT INTO User_Role(UserAccountId, RoleId) VALUES
(2, 2);

SELECT * FROM User_role;

SET SQL_SAFE_UPDATES=0;

SELECT * FROM metricentry INNER JOIN daylog ON metricentry.daylogid = daylog.DayLogId ORDER BY logdate, metricentryid;
