-- DML

INSERT INTO products VALUES (1, 'Fender', 'American Ultra', 'ELECTRIC_GUITAR', 4624.85, '', 1);
INSERT INTO products VALUES (2, 'Fender', 'Player', 'ELECTRIC_GUITAR', 2624.85,'', 2);
INSERT INTO products VALUES (3, 'Fender', 'Limited Edition 70th Anniversary Broadcaster Time Capsule','ELECTRIC_GUITAR', 8624.85, '', 1);
INSERT INTO products VALUES (4, 'Ibanez', 'AZES40-MGR', 'ELECTRIC_GUITAR', 4624.85, '', 1);


INSERT INTO users VALUES (1, 'Jim', 'email1', '111111', 'password1','CUSTOMER');
INSERT INTO users VALUES (2, 'Anna', 'email2','22222222', 'password2', 'CUSTOMER');
INSERT INTO users VALUES (3, 'Mark', 'email2', '5555555', 'password2', 'ADMINISTRATOR');
COMMIT;
-- id
-- name
-- email
-- phone
-- password
-- role