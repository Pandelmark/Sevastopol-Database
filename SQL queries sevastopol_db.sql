CREATE DATABASE sevastopol_db;
USE sevastopol_db;

CREATE TABLE residents (id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY, firstname VARCHAR(30), lastname VARCHAR(30), department VARCHAR(30), stay_time INT, salary INT, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)  ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE USER Ripley IDENTIFIED BY '1234';

SELECT host, user FROM mysql.user;

GRANT ALL PRIVILEGES ON sevastopol_db.* TO Ripley;

SHOW GRANTS FOR Ripley;

INSERT INTO residents (firstname, lastname, department, stay_time, salary) VALUES ('Zachary', 'Wastson', 'ME', 17, 610600);
INSERT INTO residents (firstname, lastname, department, stay_time, salary) VALUES ('Felinda', 'Whitechapel', 'Neurologist', 25, 600000);