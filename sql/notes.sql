DROP DATABASE IF EXISTS notes;
create DATABASE `notes`;
USE `notes`;

create TABLE note_user (
    id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    patronymic VARCHAR(50),
	login VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    user_creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_status ENUM('USER','ADMIN') DEFAULT 'USER',
    deleted_status BIT DEFAULT 0,
    KEY first_name (first_name),
    KEY last_name (last_name),
	KEY user_creation_time (user_creation_time),
    KEY user_status (user_status),
    KEY deleted_status (deleted_status),
    UNIQUE KEY login (login))
    ENGINE=INNODB DEFAULT CHARSET=utf8;

create TABLE session (
    id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    session_id VARCHAR(36) NOT NULL,
    note_user_id INT(11) NOT NULL,
    session_start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_access_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expiry_time int(11),
    UNIQUE KEY session_id (session_id),
    KEY session_start_time (session_start_time),
    KEY last_access_time (last_access_time),
    FOREIGN KEY (note_user_id) REFERENCES note_user (id) ON update CASCADE ON delete CASCADE)
    ENGINE=INNODB DEFAULT CHARSET=utf8;

create TABLE ignore_user (
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	ignore_user_id INT(11),
	ignored_by_user_id INT(11),
    FOREIGN KEY (ignore_user_id) REFERENCES note_user (id) ON update CASCADE ON delete CASCADE,
    FOREIGN KEY (ignored_by_user_id) REFERENCES note_user (id) ON update CASCADE ON delete CASCADE)
	ENGINE=INNODB DEFAULT CHARSET=utf8;

create TABLE following_user (
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	following_user_id INT(11),
	follower_user_id INT(11),
	FOREIGN KEY (following_user_id) REFERENCES note_user (id) ON update CASCADE ON delete CASCADE,
	FOREIGN KEY (follower_user_id) REFERENCES note_user (id) ON update CASCADE ON delete CASCADE)
	ENGINE=INNODB DEFAULT CHARSET=utf8;

create TABLE section (
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    section_name VARCHAR(50) NOT NULL,
    section_creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    author_id INT(11) NOT NULL,
    KEY section_name (section_name),
    KEY section_creation_time (section_creation_time),
    FOREIGN KEY (author_id) REFERENCES note_user (id))
	ENGINE=INNODB DEFAULT CHARSET=utf8;

create TABLE note (
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    note_head VARCHAR(100) NOT NULL,
    note_creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	author_id INT(11) NOT NULL,
    section_id INT(11) NOT NULL,
    KEY author_id (author_id),
    KEY section_id (section_id),
    KEY note_creation_time (note_creation_time),
    FOREIGN KEY (author_id) REFERENCES note_user (id),
    FOREIGN KEY (section_id) REFERENCES section (id) ON update CASCADE ON delete CASCADE)
	ENGINE=INNODB DEFAULT CHARSET=utf8;

create TABLE note_revision (
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    revision INT(11) NOT NULL,
    note_id INT(11) NOT NULL,
    note_body VARCHAR(5000) NOT NULL,
    KEY revision (revision),
    UNIQUE KEY note_revision (revision, note_id),
    FOREIGN KEY (note_id) REFERENCES note (id) ON update CASCADE ON delete CASCADE)
    ENGINE=INNODB DEFAULT CHARSET=utf8;

create TABLE note_comment (
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    comment_text VARCHAR(500) NOT NULL,
    comment_creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    author_id INT(11) NOT NULL,
    note_id INT(11) NOT NULL,
	KEY comment_creation_time (comment_creation_time),
    FOREIGN KEY (author_id) REFERENCES note_user (id),
    FOREIGN KEY (note_id) REFERENCES note (id) ON update CASCADE ON delete CASCADE)
    ENGINE=INNODB DEFAULT CHARSET=utf8;

create TABLE note_rating (
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    note_id INT(11) NOT NULL,
    author_id INT(11) NOT NULL,
    rating INT(11) DEFAULT 0,
    rating_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY note_rating (note_id,author_id),
    KEY rating (rating),
    KEY rating_time (rating_time),
    FOREIGN KEY (note_id) REFERENCES note (id) ON update CASCADE ON delete CASCADE,
    FOREIGN KEY (author_id) REFERENCES note_user (id))
    ENGINE=INNODB DEFAULT CHARSET=utf8;

insert into note_user (id, first_name, last_name, login, password, user_status) values (null, 'admin', 'admin', 'admin', 'password', 'ADMIN');
