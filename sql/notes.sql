DROP DATABASE IF EXISTS notes;
CREATE DATABASE `notes`;
USE `notes`;

CREATE TABLE note_user (
    id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    patronymic VARCHAR(50),
	login VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    isAdmin BIT DEFAULT 0,
    isOnline BIT DEFAULT 1,
    isDeleted BIT DEFAULT 0,
    KEY firstName (firstName),
    KEY lastName (lastName),
    KEY isAdmin (isAdmin),
    KEY isOnline (isOnline),
    KEY isDeleted (isDeleted),
    UNIQUE KEY login (login))
    ENGINE=INNODB DEFAULT CHARSET=utf8;
    
CREATE TABLE ignore_user (
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	ignored_user_id INT(11),
	ignoredBy_user_id INT(11),
	ignore_status BIT DEFAULT 0)
	ENGINE=INNODB DEFAULT CHARSET=utf8;
        
CREATE TABLE following_user (
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	following_user_id INT(11),
	followed_by_user_id INT(11),
	follow_status BIT DEFAULT 0)
	ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE section (
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    section_name VARCHAR(50) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    author_id INT(11) NOT NULL,
    FOREIGN KEY (author_id) REFERENCES note_user (id) ON UPDATE CASCADE ON DELETE CASCADE)
	ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE note (
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    note_head VARCHAR(100) NOT NULL,
    note_body VARCHAR(5000) NOT NULL,
    revision INT(11) DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	author_id INT(11) NOT NULL,
    section_id INT(11) NOT NULL,
    KEY author_id (author_id),
    KEY section_id (section_id),
    KEY revision (revision),
    FOREIGN KEY (author_id) REFERENCES note_user (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (section_id) REFERENCES section (id) ON UPDATE CASCADE ON DELETE CASCADE)
	ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE note_comment (
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    comment_text VARCHAR(500) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    author_id INT(11) NOT NULL,
    note_id INT(11) NOT NULL,
	KEY author_id (author_id),
    KEY note_id (note_id),
    FOREIGN KEY (author_id) REFERENCES note_user (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (note_id) REFERENCES note (id) ON UPDATE CASCADE ON DELETE CASCADE)
    ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE note_rating (
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    note_id INT(11) NOT NULL,
    author_id INT(11) NOT NULL,
    rating INT(11) DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY note_rating (note_id,author_id),
    KEY rating (rating),
    FOREIGN KEY (note_id) REFERENCES note (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES note_user (id) ON UPDATE CASCADE ON DELETE CASCADE)
    ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO note_user (id, firstname, lastname, login, password, isAdmin) VALUES (null, 'admin', 'admin', 'admin', 'password', 1);
