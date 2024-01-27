CREATE DATABASE IF NOT EXISTS go;
CREATE USER goapp;
SET PASSWORD FOR goapp = PASSWORD('ilovejava');
GRANT SELECT, INSERT, UPDATE ON go.* TO goapp;

CREATE TABLE IF NOT EXISTS games(
    ID int PRIMARY KEY AUTO_INCREMENT,
    start_time datetime NOT NULL,
    end_time datetime,
    winner enum('B', 'W', 'D')
);

CREATE TABLE IF NOT EXISTS moves(
    ID int PRIMARY KEY AUTO_INCREMENT,
    game int,
    move_number int,
    x int NOT NULL,
    y int NOT NULL,
    player varchar(5) NOT NULL,
    FOREIGN KEY (game) REFERENCES games(ID)
);