-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- Schema timekeeping
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS timekeeping ;

-- -----------------------------------------------------
-- Schema timekeeping
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS timekeeping DEFAULT CHARACTER SET utf8 ;
USE timekeeping ;

-- -----------------------------------------------------
-- Table timekeeping.action_with_request
-- -----------------------------------------------------
DROP TABLE IF EXISTS timekeeping.action_with_request ;

CREATE TABLE IF NOT EXISTS action_with_request (
  action_id INT NOT NULL AUTO_INCREMENT,
  action_description VARCHAR(45) NOT NULL,
  PRIMARY KEY (action_description),
  UNIQUE INDEX action_id_UNIQUE (action_id ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table timekeeping.category_of_activity
-- -----------------------------------------------------
DROP TABLE IF EXISTS category_of_activity ;

CREATE TABLE IF NOT EXISTS category_of_activity (
  category_id BIGINT NOT NULL AUTO_INCREMENT,
  category_name VARCHAR(100) NOT NULL,
  PRIMARY KEY (category_name),
  UNIQUE INDEX category_id_UNIQUE (category_id ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table activity
-- -----------------------------------------------------
DROP TABLE IF EXISTS activity ;

CREATE TABLE IF NOT EXISTS activity (
  activity_id BIGINT NOT NULL AUTO_INCREMENT,
  activity_name VARCHAR(100) NOT NULL,
  category_id BIGINT NOT NULL,
  PRIMARY KEY (activity_name),
  UNIQUE INDEX activity_id_UNIQUE (activity_id ASC) VISIBLE,
  INDEX fk_activity_category_of_activity1 (category_id ASC) VISIBLE,
  CONSTRAINT fk_activity_category_of_activity1
    FOREIGN KEY (category_id)
    REFERENCES category_of_activity (category_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table role
-- -----------------------------------------------------
DROP TABLE IF EXISTS role ;

CREATE TABLE IF NOT EXISTS role (
  role_id INT NOT NULL AUTO_INCREMENT,
  role_description ENUM('administrator', 'system user') NOT NULL,
  PRIMARY KEY (role_description),
  UNIQUE INDEX role_description_UNIQUE (role_description ASC) VISIBLE,
  UNIQUE INDEX role_id_UNIQUE (role_id ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table user_status
-- -----------------------------------------------------
DROP TABLE IF EXISTS user_status ;

CREATE TABLE IF NOT EXISTS user_status (
  status_id INT NOT NULL AUTO_INCREMENT,
  status_name VARCHAR(45) NOT NULL,
  PRIMARY KEY (status_name),
  UNIQUE INDEX status_name_UNIQUE (status_name ASC) VISIBLE,
  UNIQUE INDEX status_id_UNIQUE (status_id ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table user
-- -----------------------------------------------------
DROP TABLE IF EXISTS user ;

CREATE TABLE IF NOT EXISTS user (
  account BIGINT NOT NULL AUTO_INCREMENT,
  login VARCHAR(45) NOT NULL,
  email VARCHAR(45) NOT NULL,
  password VARCHAR(250) NOT NULL,
  first_name VARCHAR(45) NULL DEFAULT NULL,
  last_name VARCHAR(45) NULL DEFAULT NULL,
  role_id INT NOT NULL,
  status_id INT NOT NULL,
  notification VARCHAR(45) NOT NULL,
  PRIMARY KEY (login),
  UNIQUE INDEX account_UNIQUE (account ASC) VISIBLE,
  INDEX fk_user_role1 (role_id ASC) VISIBLE,
  INDEX fk_user_user_status2 (status_id ASC) VISIBLE,
  CONSTRAINT fk_user_role1
    FOREIGN KEY (role_id)
    REFERENCES role (role_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_user_user_status2
    FOREIGN KEY (status_id)
    REFERENCES user_status (status_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table request
-- -----------------------------------------------------
DROP TABLE IF EXISTS requests ;

CREATE TABLE IF NOT EXISTS request (
  request_id BIGINT NOT NULL AUTO_INCREMENT,
  account BIGINT NOT NULL,
  activity_id BIGINT NOT NULL,
  action_id INT NOT NULL,
  request_description VARCHAR(256) NULL DEFAULT NULL,
  PRIMARY KEY (request_id),
  INDEX fk_request_user_idx (account ASC) VISIBLE,
  INDEX fk_request_activity1_idx (activity_id ASC) VISIBLE,
  INDEX fk_request_action_with_request2 (action_id ASC) VISIBLE,
  CONSTRAINT fk_request_action_with_request2
    FOREIGN KEY (action_id)
    REFERENCES action_with_request (action_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_request_activity1
    FOREIGN KEY (activity_id)
    REFERENCES activity (activity_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_request_user
    FOREIGN KEY (account)
    REFERENCES user (account)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table user_activity
-- -----------------------------------------------------
DROP TABLE IF EXISTS user_activity ;

CREATE TABLE IF NOT EXISTS user_activity (
  account BIGINT NOT NULL,
  activity_id BIGINT NOT NULL,
  time_amount INT NULL,
  INDEX fk_user_activity_user_idx (account ASC) VISIBLE,
  INDEX fk_user_activity_activity1_idx (activity_id ASC) VISIBLE,
  PRIMARY KEY (activity_id, account),
  CONSTRAINT fk_user_activity_activity1
    FOREIGN KEY (activity_id)
    REFERENCES activity (activity_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_user_activity_user
    FOREIGN KEY (account)
    REFERENCES user (account)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;