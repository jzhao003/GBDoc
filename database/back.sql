-- MySQL dump 10.13  Distrib 5.5.61, for osx10.13 (x86_64)
--
-- Host: localhost    Database: gb-doc
-- ------------------------------------------------------
-- Server version	5.5.61

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `doc_filled_field`
--

DROP TABLE IF EXISTS `doc_filled_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `doc_filled_field` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_on` datetime DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  `doc_template_id` bigint(20) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `field_value` text,
  PRIMARY KEY (`id`),
  KEY `doc_template_id` (`doc_template_id`),
  CONSTRAINT `doc_filled_field_ibfk_1` FOREIGN KEY (`doc_template_id`) REFERENCES `doc_template` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doc_filled_field`
--

LOCK TABLES `doc_filled_field` WRITE;
/*!40000 ALTER TABLE `doc_filled_field` DISABLE KEYS */;
/*!40000 ALTER TABLE `doc_filled_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doc_template`
--

DROP TABLE IF EXISTS `doc_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `doc_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_on` datetime DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  `standard_section_id` bigint(20) NOT NULL COMMENT '所属的目录项。',
  `content` text,
  PRIMARY KEY (`id`),
  KEY `standard_section_id` (`standard_section_id`),
  CONSTRAINT `doc_template_ibfk_1` FOREIGN KEY (`standard_section_id`) REFERENCES `standard_section` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doc_template`
--

LOCK TABLES `doc_template` WRITE;
/*!40000 ALTER TABLE `doc_template` DISABLE KEYS */;
INSERT INTO `doc_template` VALUES (1,NULL,NULL,NULL,NULL,52,'fdsdffdsdf');
/*!40000 ALTER TABLE `doc_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `document`
--

DROP TABLE IF EXISTS `document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `document` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_on` datetime DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `folder_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `document`
--

LOCK TABLES `document` WRITE;
/*!40000 ALTER TABLE `document` DISABLE KEYS */;
/*!40000 ALTER TABLE `document` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `documents_set`
--

DROP TABLE IF EXISTS `documents_set`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `documents_set` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_on` datetime DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `standard_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `standard_id` (`standard_id`),
  CONSTRAINT `documents_set_ibfk_1` FOREIGN KEY (`standard_id`) REFERENCES `standard` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `documents_set`
--

LOCK TABLES `documents_set` WRITE;
/*!40000 ALTER TABLE `documents_set` DISABLE KEYS */;
/*!40000 ALTER TABLE `documents_set` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `standard`
--

DROP TABLE IF EXISTS `standard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `standard` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_on` datetime DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `template_location` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `standard`
--

LOCK TABLES `standard` WRITE;
/*!40000 ALTER TABLE `standard` DISABLE KEYS */;
INSERT INTO `standard` VALUES (15,'2018-11-17 16:50:04','2018-11-17 16:50:04',NULL,NULL,'14340标准说明书','12344'),(16,'2018-11-17 16:54:14','2018-11-17 16:54:14',NULL,NULL,'sshsss \n','12344'),(17,'2018-11-17 16:54:14','2018-11-17 16:54:14',NULL,NULL,'sshsss \n','12344'),(18,'2018-11-17 16:54:15','2018-11-17 16:54:15',NULL,NULL,'sshsss \n','12344'),(19,'2018-11-17 16:54:15','2018-11-17 16:54:15',NULL,NULL,'sshsss \n','12344'),(20,'2018-11-17 16:55:30','2018-11-17 16:55:30',NULL,NULL,'sshsss \n','12344'),(21,'2018-11-17 19:27:41','2018-11-17 19:27:41',NULL,NULL,'1','11'),(22,'2018-11-17 20:02:49','2018-11-17 20:02:49',NULL,NULL,'sshsss \nssssssssss','12344'),(23,'2018-11-17 20:03:11','2018-11-17 20:03:11',NULL,NULL,'sshsss \nssssssssss','12344'),(24,'2018-11-17 20:04:43','2018-11-17 20:04:43',NULL,NULL,'sshsss \n','12344'),(25,'2018-11-17 20:04:54','2018-11-17 20:04:54',NULL,NULL,'sshsss \n','12344'),(26,'2018-11-17 20:04:55','2018-11-17 20:04:55',NULL,NULL,'sshsss \n','12344'),(27,'2018-11-17 20:04:55','2018-11-17 20:04:55',NULL,NULL,'sshsss \n','12344'),(28,'2018-11-17 20:04:56','2018-11-17 20:04:56',NULL,NULL,'sshsss \n','12344'),(29,'2018-11-17 20:04:56','2018-11-17 20:04:56',NULL,NULL,'sshsss \n','12344'),(30,'2018-11-17 20:06:12','2018-11-17 20:06:12',NULL,NULL,'sshsss \n','12344'),(31,'2018-11-17 20:06:26','2018-11-17 20:06:26',NULL,NULL,'sshsss \n','12344'),(32,'2018-11-17 20:07:00','2018-11-17 20:07:00',NULL,NULL,'sshsss \n','12344'),(33,'2018-11-17 20:07:28','2018-11-17 20:07:28',NULL,NULL,'sshsss \n','12344'),(34,'2018-11-17 20:07:31','2018-11-17 20:07:31',NULL,NULL,'sshsss \n','12344'),(35,'2018-11-17 20:11:09','2018-11-17 20:11:09',NULL,NULL,'sshsss \n','12344'),(36,'2018-11-17 20:11:41','2018-11-17 20:11:41',NULL,NULL,'sshsss \n','12344'),(37,'2018-11-17 20:17:57','2018-11-17 20:17:57',NULL,NULL,'sshsss \n','12344'),(38,'2018-11-17 20:18:22','2018-11-17 20:18:22',NULL,NULL,'sshsss \n','12344'),(39,'2018-11-17 20:18:23','2018-11-17 20:18:23',NULL,NULL,'sshsss \n','12344'),(40,'2018-11-17 20:18:26','2018-11-17 20:18:26',NULL,NULL,'sshsss \n','12344'),(41,'2018-11-17 20:18:38','2018-11-17 20:18:38',NULL,NULL,'sshsss \n','12344'),(42,'2018-11-17 20:18:50','2018-11-17 20:18:50',NULL,NULL,'sshsss \n','12344'),(43,'2018-11-17 20:23:02','2018-11-17 20:23:02',NULL,NULL,'sshsss \n','12344'),(44,'2018-11-17 20:27:01','2018-11-17 20:27:01',NULL,NULL,'sshsss \n','12344'),(45,'2018-11-17 20:33:26','2018-11-17 20:33:26',NULL,NULL,'sshsss \n','12344'),(46,'2018-11-17 20:33:44','2018-11-17 20:33:44',NULL,NULL,'sshsss \n','12344'),(47,'2018-11-17 20:33:45','2018-11-17 20:33:45',NULL,NULL,'sshsss \n','12344'),(48,'2018-11-17 20:43:27','2018-11-17 20:43:27',NULL,NULL,'sshsss \n','12344'),(49,'2018-11-17 20:46:57','2018-11-17 20:46:57',NULL,NULL,'sshsss \n','12344'),(50,'2018-11-17 20:47:23','2018-11-17 20:47:23',NULL,NULL,'sshsss \n','12344'),(51,'2018-11-17 20:48:38','2018-11-17 20:48:38',NULL,NULL,'sshsss \n','12344'),(52,'2018-11-17 20:50:17','2018-11-17 20:50:17',NULL,NULL,'sshsss \n','12344'),(53,'2018-11-18 11:45:14','2018-11-18 11:45:14',NULL,NULL,'新的文档\n','12344'),(54,'2018-11-18 11:48:51','2018-11-18 11:48:51',NULL,NULL,'啊啊','12344'),(55,'2018-11-18 11:53:08','2018-11-18 11:53:08',NULL,NULL,'把','12344'),(56,'2018-11-18 16:39:04','2018-11-18 16:39:04',NULL,NULL,'00000','12344'),(57,'2018-11-18 16:41:17','2018-11-18 16:41:17',NULL,NULL,'11111\n','12344'),(58,'2018-11-18 17:05:08','2018-11-18 17:05:08',NULL,NULL,'999\n','12344'),(59,'2018-11-18 17:13:14','2018-11-18 17:13:14',NULL,NULL,'sshsss 0000\n','12344'),(60,'2018-11-19 18:01:41','2018-11-19 18:01:41',NULL,NULL,'001010\n','12344'),(61,'2018-11-25 14:12:41','2018-11-25 14:12:41',NULL,NULL,'ssssd','12344');
/*!40000 ALTER TABLE `standard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `standard_section`
--

DROP TABLE IF EXISTS `standard_section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `standard_section` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_on` datetime DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  `section_number` varchar(50) DEFAULT NULL,
  `section_content` varchar(50) DEFAULT NULL,
  `standard_id` bigint(20) DEFAULT NULL,
  `doc_title` varchar(256) DEFAULT NULL,
  `location` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `standard_id` (`standard_id`),
  CONSTRAINT `standard_section_ibfk_1` FOREIGN KEY (`standard_id`) REFERENCES `standard` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `standard_section`
--

LOCK TABLES `standard_section` WRITE;
/*!40000 ALTER TABLE `standard_section` DISABLE KEYS */;
INSERT INTO `standard_section` VALUES (48,'2018-11-19 18:02:21','2018-11-19 18:02:21',NULL,NULL,'1.1.2',' 结束\n',60,NULL,NULL),(49,'2018-11-19 18:02:21','2018-11-19 18:02:21',NULL,NULL,'1.1',' 前言\n',60,NULL,NULL),(50,'2018-11-19 18:02:21','2018-11-19 18:02:21',NULL,NULL,'2.1',' 下一个\n      这是段落\n      段落2\n      段落3\n',60,NULL,NULL),(51,'2018-11-19 18:02:21','2018-11-19 18:02:21',NULL,NULL,'7.1',' 结束',60,NULL,NULL),(52,'2018-11-19 18:02:21','2018-11-19 18:02:21',NULL,NULL,'1.1.1',' 开始\n',60,NULL,NULL),(53,'2018-11-25 14:13:03','2018-11-25 14:13:03',NULL,NULL,'1.1','1.\n\n2\n',61,NULL,NULL),(54,'2018-11-25 14:13:03','2018-11-25 14:13:03',NULL,NULL,'2.1','1.\n',61,NULL,NULL),(55,'2018-11-25 14:13:03','2018-11-25 14:13:03',NULL,NULL,'2.1.1','1.\n',61,NULL,NULL);
/*!40000 ALTER TABLE `standard_section` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `role` varchar(50) DEFAULT NULL,
  `created_on` datetime DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-12-02 19:16:28
