CREATE DATABASE  IF NOT EXISTS `verifier`;
USE `verifier`;

--
-- Table structure for table `book_detail`
--
CREATE TABLE IF NOT EXISTS `signature` (
  `app_id` INTEGER NOT NULL,
  `data` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
