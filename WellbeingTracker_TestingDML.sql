USE WellbeingTracker;

INSERT INTO useraccount(username, userpassword, firstname, lastname, email, creationtimestamp, timezone) VALUES
('jakep310', 'thisIsapa55word', 'Jake', 'Porter', 'jakeporter310@gmail.com', '2019-02-04 11:32:00', 'EST');

INSERT INTO `role`(RoleId, RoleName) VALUES
(1, "ROLE_ADMIN"),
(2, "ROLE_USER");

INSERT INTO User_Role(UserAccountId, RoleId) VALUES
(1,1),
(1,2);



SET SQL_SAFE_UPDATES=0;

SELECT * FROM useraccount;

SELECT * FROM useraccount WHERE username = 'jakep310';

UPDATE useraccount SET userpassword = '$2a$10$DVcpt7n.SkMLHXwuFl8xrOzC0ivsY78mn0i2S5dsPshX5dt/X.7ee' WHERE username = 'jakep310';

