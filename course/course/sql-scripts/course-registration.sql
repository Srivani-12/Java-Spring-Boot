-- Use the database
USE `registration`;

-- Drop tables if they exist (to avoid conflicts during recreation)
DROP TABLE IF EXISTS `roles`;
DROP TABLE IF EXISTS `message`;

DROP TABLE IF EXISTS `enrollment`;
DROP TABLE IF EXISTS `course`;
DROP TABLE IF EXISTS `instructor`;
DROP TABLE IF EXISTS `members`;
DROP TABLE IF EXISTS `student`;


-- Members table to store authentication details
CREATE TABLE `members` (
  `user_id` VARCHAR(100) NOT NULL,
  `pw` CHAR(255) NOT NULL,  -- BCrypt encrypted password
  `active` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Roles table to store user roles
CREATE TABLE `roles` (
   `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `user_id` VARCHAR(100) NOT NULL,
  `role` VARCHAR(50) NOT NULL,
  UNIQUE KEY `roles_idx_1` (`user_id`, `role`),
  CONSTRAINT `roles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `members` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Student table
-- Student Table
CREATE TABLE `student` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100) UNIQUE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Instructor table
CREATE TABLE `instructor` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100) UNIQUE NOT NULL,
  `department` VARCHAR(100) NOT NULL,
  CONSTRAINT fk_instructor_member FOREIGN KEY (`email`) REFERENCES `members`(`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Course table
CREATE TABLE `course` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(100) NOT NULL,
  `code` VARCHAR(50) UNIQUE NOT NULL,
  `description` TEXT,                          -- Added: Course description
  `start_date` DATE,                           -- Added: Course start date
  `end_date` DATE,                             -- Added: Course end date
  `instructor_id` BIGINT NOT NULL,
  `capacity` INT NOT NULL,
  `available_seats` INT NOT NULL DEFAULT 0,
  `youtube_link` VARCHAR(255),

  FOREIGN KEY (`instructor_id`) REFERENCES `instructor`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



-- Enrollment table
CREATE TABLE `enrollment` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `student_id` BIGINT NOT NULL,  -- Changed from INT to BIGINT
  `course_id` BIGINT NOT NULL,   -- Changed from INT to BIGINT
  `enrollment_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`student_id`) REFERENCES `student`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`course_id`) REFERENCES `course`(`id`) ON DELETE CASCADE,
  UNIQUE (`student_id`, `course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `message`(
`id` BIGINT AUTO_INCREMENT PRIMARY KEY,
`sender` VARCHAR(255) NOT NULL,
`receiver` VARCHAR(255) NOt NULL,
`content` TEXT NOT NULL,
`seen` TINYINT(1) NOT NULL DEFAULT 0,
`time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY(`sender`)REFERENCES `members`(`user_id`),
FOREIGN KEY(`receiver`) REFERENCES `members`(`user_id`))
ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- Insert data into members table



INSERT INTO `members` (`user_id`, `pw`, `active`)
VALUES
('alice@example.com', '$2a$12$9nuoSnTLkOKT79KWptY2mufoBNJeIWBQ.cxu7X/U/q056Y9uDipTy', 1),
('bob@example.com', '$2a$12$M4FbSxQne48/NAtgk5XmiO0keUebOEL0f4Gg2Zw.VHuCOECM9mRo2', 1),
('admin@example.com', '$2a$12$/Xq7dV.r0AuAt.cKF0SYDeomhLvWkh3yH5WZSS6a6bdTJ9RnT0.b6', 1),
('emily@example.com', '$2a$12$Cs5b4gyrSR3G912ZYusHp.iaofprsKiruP5L8F3OynU/W4DOTM7Di', 1),
('mark@example.com', '$2a$12$O.1qxc8mDCogRvP0OHiPdOCR4k3u/ITnqHIgCqK8.8bjm.95bSEgW', 1);

-- Insert roles for each member
INSERT INTO `roles` (`user_id`, `role`)
VALUES
('alice@example.com', 'ROLE_STUDENT'),
('bob@example.com', 'ROLE_STUDENT'),
('admin@example.com', 'ROLE_ADMIN'),
('emily@example.com', 'ROLE_INSTRUCTOR'),
('mark@example.com', 'ROLE_INSTRUCTOR'),
('mark@example.com', 'ROLE_ADMIN');

-- Insert data into student table
INSERT INTO `student` (`username`, `email`)
VALUES
('Alice', 'alice@example.com'),
('Bob', 'bob@example.com');

-- Insert data into instructor table
INSERT INTO `instructor` (`username`, `email`, `department`)
VALUES
('Dr. Emily Brown', 'emily@example.com', 'Computer Science'),
('Dr. Mark Wilson', 'mark@example.com', 'Mathematics');

INSERT INTO `course`
(`name`, `code`, `description`, `start_date`, `end_date`, `instructor_id`, `capacity`, `available_seats`, `youtube_link`)
VALUES
('Data Structures', 'CS101', 'Learn basic to advanced data structures.', '2025-06-01', '2025-08-30', 1, 50, 50, 'https://www.youtube.com/watch?v=RBSGKlAvoiM'),
('Database Systems', 'CS102', 'Covers relational databases, SQL, and indexing.', '2025-07-01', '2025-09-15', 1, 40, 40, 'https://www.youtube.com/watch?v=HXV3zeQKqGY'),
('Linear Algebra', 'MATH201', 'Matrix operations, vector spaces, and more.', '2025-06-15', '2025-08-15', 2, 35, 35, 'https://www.youtube.com/watch?v=kjBOesZCoqc');


-- Insert enrollment data
INSERT INTO `enrollment` (`student_id`, `course_id`, `enrollment_date`)
VALUES
(1, 1, '2025-03-20'),
(1, 2, '2025-03-21'),
(2, 3, '2025-03-22');

-- Triggers to manage course capacity
DELIMITER //
CREATE TRIGGER `before_enrollment_insert`
BEFORE INSERT ON `enrollment`
FOR EACH ROW
BEGIN
    DECLARE available INT;
    SELECT available_seats INTO available FROM course WHERE id = NEW.course_id;
    IF available <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Course is full. No available seats.';
    ELSE
        UPDATE course SET available_seats = available_seats - 1 WHERE id = NEW.course_id;
    END IF;
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER `after_enrollment_delete`
AFTER DELETE ON `enrollment`
FOR EACH ROW
BEGIN
    UPDATE course SET available_seats = available_seats + 1 WHERE id = OLD.course_id;
END;
//
DELIMITER ;