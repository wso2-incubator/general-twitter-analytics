-- MySQL dump 10.13  Distrib 5.5.49, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: cep_data_db
-- ------------------------------------------------------
-- Server version	5.5.49-0ubuntu0.14.04.1

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
-- Table structure for table `PopularLinks`
--

DROP TABLE IF EXISTS `PopularLinks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PopularLinks` (
  `text` varchar(1000) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `Owner` varchar(100) DEFAULT NULL,
  `Ft` int(11) DEFAULT NULL,
  `Rt` int(11) DEFAULT NULL,
  `HTag` varchar(500) DEFAULT NULL,
  `Retwitter` varchar(200) DEFAULT NULL,
  `OwnerFull` varchar(200) DEFAULT NULL,
  `RetwitterFullName` varchar(200) DEFAULT NULL,
  `created_at` varchar(100) DEFAULT NULL,
  `url` mediumtext,
  KEY `count` (`count`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PopularLinks`
--

LOCK TABLES `PopularLinks` WRITE;
/*!40000 ALTER TABLE `PopularLinks` DISABLE KEYS */;
INSERT INTO `PopularLinks` VALUES (NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(NULL,3,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(NULL,5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `PopularLinks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PopularOnTopic`
--

DROP TABLE IF EXISTS `PopularOnTopic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PopularOnTopic` (
  `TEXT` varchar(255) DEFAULT NULL,
  `COUNT` int(11) DEFAULT NULL,
  `OWNER` varchar(255) DEFAULT NULL,
  `FT` int(11) DEFAULT NULL,
  `RT` int(11) DEFAULT NULL,
  `HTAG` varchar(255) DEFAULT NULL,
  `RETWITTER` varchar(255) DEFAULT NULL,
  `OWNERFULL` varchar(255) DEFAULT NULL,
  `RETWITTERFULLNAME` varchar(255) DEFAULT NULL,
  `CREATED_AT` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PopularOnTopic`
--

LOCK TABLES `PopularOnTopic` WRITE;
/*!40000 ALTER TABLE `PopularOnTopic` DISABLE KEYS */;
INSERT INTO `PopularOnTopic` VALUES (NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(NULL,3,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(NULL,5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `PopularOnTopic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SentimentRate`
--

DROP TABLE IF EXISTS `SentimentRate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SentimentRate` (
  `CDate` varchar(100) DEFAULT NULL,
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `G1_P1` double DEFAULT NULL,
  `G1_P2` double DEFAULT NULL,
  `G2_P1` double DEFAULT NULL,
  `G2_P2` double DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SentimentRate`
--

LOCK TABLES `SentimentRate` WRITE;
/*!40000 ALTER TABLE `SentimentRate` DISABLE KEYS */;
/*!40000 ALTER TABLE `SentimentRate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `WordCloud`
--

DROP TABLE IF EXISTS `WordCloud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `WordCloud` (
  `id` int(11) DEFAULT NULL,
  `G1_P1` text,
  `G1_P2` text,
  `G2_P1` text,
  `G2_P2` text,
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `WordCloud`
--

LOCK TABLES `WordCloud` WRITE;
/*!40000 ALTER TABLE `WordCloud` DISABLE KEYS */;
INSERT INTO `WordCloud` VALUES (1,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `WordCloud` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `googleLatestNews`
--

DROP TABLE IF EXISTS `googleLatestNews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `googleLatestNews` (
  `text` varchar(1000) DEFAULT NULL,
  `Pub_Date` varchar(1000) DEFAULT NULL,
  `Link` varchar(1000) DEFAULT NULL,
  `Discription` varchar(50000) DEFAULT NULL,
  `Owner` varchar(1000) DEFAULT NULL,
  `ID` int(11) NOT NULL,
  `DateTime` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `googleLatestNews`
--

LOCK TABLES `googleLatestNews` WRITE;
/*!40000 ALTER TABLE `googleLatestNews` DISABLE KEYS */;
/*!40000 ALTER TABLE `googleLatestNews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `news`
--

DROP TABLE IF EXISTS `news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `news` (
  `G1_P1` varchar(500) DEFAULT NULL,
  `G1_P2` varchar(500) DEFAULT NULL,
  `G2_P1` varchar(500) DEFAULT NULL,
  `G2_P2` varchar(500) DEFAULT NULL,
  `CDate` varchar(100) DEFAULT NULL,
  `ID` int(11) DEFAULT NULL,
  KEY `count` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `news`
--

LOCK TABLES `news` WRITE;
/*!40000 ALTER TABLE `news` DISABLE KEYS */;
/*!40000 ALTER TABLE `news` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `top`
--

DROP TABLE IF EXISTS `top`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `top` (
  `ID` int(11) DEFAULT NULL,
  `G1_P1` int(11) DEFAULT NULL,
  `G1_P2` int(11) DEFAULT NULL,
  `G2_P1` int(11) DEFAULT NULL,
  `G2_P2` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `top`
--

LOCK TABLES `top` WRITE;
/*!40000 ALTER TABLE `top` DISABLE KEYS */;
INSERT INTO `top` VALUES (1,0,0,0,0);
/*!40000 ALTER TABLE `top` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-08-23  4:46:23
