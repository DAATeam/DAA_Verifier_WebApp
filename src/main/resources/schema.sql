CREATE DATABASE  IF NOT EXISTS `verifier`;
USE `verifier`;

--
-- Table structure for table `book_detail`
--
CREATE TABLE IF NOT EXISTS `signature` (
  `app_id` INTEGER NOT NULL,
  `data` VARCHAR(10000) NOT NULL,
  `expire_time` LONG,
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `history` (
  `id` INTEGER NOT NULL AUTO_INCREMENT,
  `service_id` INTEGER NOT NULL,
  `user_info` VARCHAR(10000),
  `result_verify` BOOLEAN,
  `timestamp` TIMESTAMP DEFAULT NOW(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
