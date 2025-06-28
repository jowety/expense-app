CREATE DATABASE  IF NOT EXISTS `expense_db_test` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `expense_db_test`;
-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: expense_db
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `id` varchar(20) NOT NULL,
  `name` varchar(45) NOT NULL,
  `type` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` varchar(20) NOT NULL,
  `name` varchar(45) NOT NULL,
  `budget` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subcategory`
--

DROP TABLE IF EXISTS `subcategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subcategory` (
  `id` varchar(20) NOT NULL,
  `name` varchar(45) NOT NULL,
  `category_id` varchar(20) NOT NULL,
  `budget` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_subcategory_category_idx` (`category_id`),
  CONSTRAINT `fk_subcategory_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payee`
--

DROP TABLE IF EXISTS `payee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `account_default` varchar(20) DEFAULT NULL,
  `category_default` varchar(20) DEFAULT NULL,
  `subcategory_default` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `fk_payee_account_idx` (`account_default`),
  KEY `fk_payee_category_idx` (`category_default`),
  KEY `fk_payee_subcategory_idx` (`subcategory_default`),
  CONSTRAINT `fk_payee_account` FOREIGN KEY (`account_default`) REFERENCES `account` (`id`),
  CONSTRAINT `fk_payee_category` FOREIGN KEY (`category_default`) REFERENCES `category` (`id`),
  CONSTRAINT `fk_payee_subcategory` FOREIGN KEY (`subcategory_default`) REFERENCES `subcategory` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `expense`
--

DROP TABLE IF EXISTS `expense`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expense` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `account_id` varchar(20) NOT NULL,
  `payee_id` int NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `subcategory_id` varchar(20) NOT NULL,
  `import_desc` varchar(100) DEFAULT NULL,
  `notes` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_expense_account_idx` (`account_id`),
  KEY `fk_expense_payee_idx` (`payee_id`),
  KEY `fk_expense_subcategory_idx` (`subcategory_id`),
  CONSTRAINT `fk_expense_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `fk_expense_payee` FOREIGN KEY (`payee_id`) REFERENCES `payee` (`id`),
  CONSTRAINT `fk_expense_subcategory` FOREIGN KEY (`subcategory_id`) REFERENCES `subcategory` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `expense_view`
--

DROP TABLE IF EXISTS `expense_view`;
/*!50001 DROP VIEW IF EXISTS `expense_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `expense_view` AS SELECT 
 1 AS `id`,
 1 AS `date`,
 1 AS `year`,
 1 AS `month_number`,
 1 AS `month_string`,
 1 AS `account`,
 1 AS `payee`,
 1 AS `category`,
 1 AS `subcategory`,
 1 AS `amount`,
 1 AS `notes`*/;
SET character_set_client = @saved_cs_client;




--
-- Final view structure for view `expense_view`
--

/*!50001 DROP VIEW IF EXISTS `expense_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`jowety`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `expense_view` AS select `expense`.`id` AS `id`,`expense`.`date` AS `date`,year(`expense`.`date`) AS `year`,month(`expense`.`date`) AS `month_number`,monthname(`expense`.`date`) AS `month_string`,`account`.`name` AS `account`,`payee`.`name` AS `payee`,`category`.`name` AS `category`,`subcategory`.`name` AS `subcategory`,`expense`.`amount` AS `amount`,`expense`.`notes` AS `notes` from ((((`expense` join `account` on((`expense`.`account_id` = `account`.`id`))) join `payee` on((`expense`.`payee_id` = `payee`.`id`))) join `subcategory` on((`expense`.`subcategory_id` = `subcategory`.`id`))) join `category` on((`subcategory`.`category_id` = `category`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-21  8:55:32
