CREATE DATABASE IF NOT EXISTS Food;

USE Food;

DROP TABLE IF EXISTS Food.food;

CREATE TABLE Food.food (
	id int(10) NOT NULL AUTO_INCREMENT,
	name varchar(60) NOT NULL,
	quantity varchar(60),
	price DECIMAL(19,4),
	PRIMARY key(id)
);