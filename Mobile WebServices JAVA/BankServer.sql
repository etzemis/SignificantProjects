SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `BankServer` DEFAULT CHARACTER SET latin1 ;
USE `BankServer` ;

-- -----------------------------------------------------
-- Table `BankServer`.`clientProfile`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `BankServer`.`clientProfile` (
  `userid` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NULL DEFAULT NULL ,
  `surname` VARCHAR(45) NULL DEFAULT NULL ,
  `username` VARCHAR(45) NULL DEFAULT NULL ,
  `password` VARCHAR(45) NULL DEFAULT NULL ,
  PRIMARY KEY (`userid`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BankServer`.`userAccount`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `BankServer`.`userAccount` (
  `accountid` INT NOT NULL AUTO_INCREMENT ,
  `userid` INT NULL DEFAULT NULL ,
  `money` INT NULL DEFAULT NULL ,
  `lastcharge` INT NULL DEFAULT 0 ,
  PRIMARY KEY (`accountid`) )
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
