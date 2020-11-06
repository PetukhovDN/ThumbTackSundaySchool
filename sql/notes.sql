DROP DATABASE IF EXISTS notes;
CREATE DATABASE `notes`;
USE `notes`;

CREATE TABLE note_user (
    id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    patronymic VARCHAR(50),
	login VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_status ENUM('USER','ADMIN') DEFAULT 'USER',
    deleted_status BIT DEFAULT 0,
    KEY first_name (first_name),
    KEY last_name (last_name),
	KEY creation_time (creation_time),
    KEY user_status (user_status),
    KEY deleted_status (deleted_status),
    UNIQUE KEY login (login))
    ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE session (
    token VARCHAR(36) NOT NULL PRIMARY KEY,
    note_user_id INT(11) NOT NULL,
    session_start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    KEY session_start_time (session_start_time),
    FOREIGN KEY (note_user_id) REFERENCES note_user (id) ON UPDATE CASCADE ON DELETE CASCADE)
    ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE ignore_user (
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	ignore_user_id INT(11),
	ignored_by_user_id INT(11),
    FOREIGN KEY (ignore_user_id) REFERENCES note_user (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (ignored_by_user_id) REFERENCES note_user (id) ON UPDATE CASCADE ON DELETE CASCADE)
	ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE following_user (
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	following_user_id INT(11),
	follower_user_id INT(11),
	FOREIGN KEY (following_user_id) REFERENCES note_user (id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (follower_user_id) REFERENCES note_user (id) ON UPDATE CASCADE ON DELETE CASCADE)
	ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE section (
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    section_name VARCHAR(50) NOT NULL,
    creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    author_id INT(11) NOT NULL,
    KEY section_name (section_name),
    KEY creation_time (creation_time),
    FOREIGN KEY (author_id) REFERENCES note_user (id))
	ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE note (
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    note_head VARCHAR(100) NOT NULL,
    creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	author_id INT(11) NOT NULL,
    section_id INT(11) NOT NULL,
    KEY author_id (author_id),
    KEY section_id (section_id),
    KEY creation_time (creation_time),
    FOREIGN KEY (author_id) REFERENCES note_user (id),
    FOREIGN KEY (section_id) REFERENCES section (id) ON UPDATE CASCADE ON DELETE CASCADE)
	ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE note_revision (
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    revision INT(11) NOT NULL,
    note_id INT(11) NOT NULL,
    note_body VARCHAR(5000) NOT NULL,
    KEY revision (revision),
    UNIQUE KEY note_revision (revision, note_id),
    FOREIGN KEY (note_id) REFERENCES note (id) ON UPDATE CASCADE ON DELETE CASCADE)
    ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE note_comment (
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    comment_text VARCHAR(500) NOT NULL,
    creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    author_id INT(11) NOT NULL,
    note_id INT(11) NOT NULL,
	KEY creation_time (creation_time),
    FOREIGN KEY (author_id) REFERENCES note_user (id),
    FOREIGN KEY (note_id) REFERENCES note (id) ON UPDATE CASCADE ON DELETE CASCADE)
    ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE note_rating (
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    note_id INT(11) NOT NULL,
    author_id INT(11) NOT NULL,
    rating INT(11) DEFAULT 0,
    rating_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY note_rating (note_id,author_id),
    KEY rating (rating),
    KEY rating_time (rating_time),
    FOREIGN KEY (note_id) REFERENCES note (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES note_user (id))
    ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO note_user (id, first_name, last_name, login, password, user_status) VALUES (null, 'admin', 'admin', 'admin', 'password', 'ADMIN');
