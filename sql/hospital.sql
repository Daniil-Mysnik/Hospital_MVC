DROP DATABASE IF EXISTS hospital;
CREATE DATABASE hospital;
USE hospital;


CREATE TABLE `user` (
    id INT(11) NOT NULL AUTO_INCREMENT,
    firstName VARCHAR(100) NOT NULL,
    lastName VARCHAR(100) NOT NULL,
    patronymic VARCHAR(100),
    login VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    userType ENUM('ADMIN', 'PATIENT', 'DOCTOR') NOT NULL,
    PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `admin` (
    user_id INT(11) NOT NULL,
    position VARCHAR(100) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES `user` (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `patient` (
    user_id INT(11) NOT NULL,
    email VARCHAR(100) NOT NULL,
    address VARCHAR(100) NOT NULL,
    phone VARCHAR(100) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES `user` (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `speciality` (
    id INT(11) NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `room` (
    id INT(11) NOT NULL AUTO_INCREMENT,
    number VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `doctor` (
    user_id INT(11) NOT NULL,
    speciality_id INT(11) NOT NULL,
    room_id INT(11) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES `user` (id) ON DELETE CASCADE,
    FOREIGN KEY (speciality_id) REFERENCES `speciality` (id) ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES `room` (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `daySchedule` (
    id INT(11) NOT NULL AUTO_INCREMENT,
    doctor_id INT(11) NOT NULL,
    date TIMESTAMP,
    timeStart TIME,
    timeEnd TIME,
    PRIMARY KEY (id),
    FOREIGN KEY (doctor_id) REFERENCES `doctor` (user_id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `appointment` (
    id INT(11) NOT NULL AUTO_INCREMENT,
    schedule_id INT(11) NOT NULL,
    time TIME,
    duration INT,
    state ENUM('FREE', 'TICKET', 'COMMISSION') NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (schedule_id) REFERENCES `daySchedule` (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `session` (
    id VARCHAR(100) NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES `user` (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `ticket` (
    id INT(11) NOT NULL AUTO_INCREMENT,
    number VARCHAR(100) NOT NULL,
    patient_id INT(11) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (patient_id) REFERENCES `patient` (user_id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `appointment_ticket` (
    ticket_id INT(11) NOT NULL,
    appointment_id INT(11) NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES `ticket` (id) ON DELETE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES `appointment` (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `commission` (
    id INT(11) NOT NULL AUTO_INCREMENT,
    patient_id INT(11) NOT NULL,
    ticket_id INT(11) NOT NULL,
    date TIMESTAMP NOT NULL,
    time TIME NOT NULL,
    duration INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (patient_id) REFERENCES `patient` (user_id) ON DELETE CASCADE,
    FOREIGN KEY (ticket_id) REFERENCES `ticket` (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;


INSERT INTO `user` values (1, 'Daniil', 'Mysnik', 'Leonidovich', 'admin12345', 'admin12345', 'ADMIN');
INSERT INTO `admin` values (1, 'MAIN');
INSERT INTO `speciality` values (1, 'Oculist');
INSERT INTO `speciality` values (2, 'Therapist');
INSERT INTO `room` values (1, '15a');
INSERT INTO `room` values (2, '1');