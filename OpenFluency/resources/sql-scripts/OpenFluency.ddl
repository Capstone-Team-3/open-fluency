CREATE DATABASE IF NOT EXISTS openfluency DEFAULT CHARACTER SET utf8 ;

USE openfluency;

/*
 * MySQL Base table creation for OpenFluence database
 *
 * Host: localhost    Database: open_fluency
 * Server version	5.6.16
*/

SET NAMES utf8 ;
SET SQL_MODE='NO_AUTO_VALUE_ON_ZERO' ;
SET CHARACTER_SET_CLIENT = utf8 ;

--
-- Table structure for table unit
--
DROP TABLE IF EXISTS meaning;
DROP TABLE IF EXISTS reading;
DROP TABLE IF EXISTS unit;

CREATE TABLE unit (
  pk             VARCHAR(250)    NOT NULL, #unicode codepoint string. allow short phrases
  literal        TEXT(250)       NOT NULL, #UTF8 encoding for the unit's character string 
  type           CHAR(8)         NOT NULL, #KANJI|KATAKANA|HIRAGANA|RADICAL|NAME|WORD|PHRASE
  language       CHAR(16)        NOT NULL, #JAPANESE, for now
  radical_num    INT,                      #the "classical" number of this kanji's radical
  grade          INT,
  stroke_count   INT,
  frequency      INT,
  radical_kanji  TEXT,                     #this kanji is a radical, and here is its name
  version        INT,
  PRIMARY KEY (pk)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#CREATE UNIQUE INDEX unit_key1 ON unit (literal(250)); #do later, after removing non-utf8 literals

--
-- Table structure for table meaning
--
DROP TABLE IF EXISTS meaning;

CREATE TABLE meaning (
  pk             INT             NOT NULL AUTO_INCREMENT,
  unit_fk        VARCHAR(250)    NOT NULL, #the meaning is associated with one unit
  meaning        TEXT(250)       NOT NULL, #the item's meaning
  language       CHAR(16)        NOT NULL, #ENGLISH, for now
  version        INT,
  FOREIGN KEY (unit_fk) REFERENCES unit(pk),
  PRIMARY KEY (pk)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE INDEX meaning_key1 ON meaning (unit_fk);

--
-- Table structure for table reading
--
DROP TABLE IF EXISTS reading;

CREATE TABLE reading (
  pk             INT             NOT NULL AUTO_INCREMENT,
  unit_fk        VARCHAR(250)    NOT NULL, #the reading is associated with one unit
  reading        TEXT(250)       NOT NULL, #UTF8 encoding for the reading's character string 
  type           CHAR(8)         NOT NULL, #JA_ON|JA_KUN|PINYIN
  version        INT,
  FOREIGN KEY (unit_fk) REFERENCES unit(pk),
  PRIMARY KEY (pk)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE INDEX reading_key1 ON reading (unit_fk);
