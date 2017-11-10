Please execute below sql commands to create database with name "LOGTEST"
also please create the user parser with password parser1234

In java code, under resources folder, the is config.properties file which
containes database jdbc url, user and password. If another database and user is demanded
please change the configuration in the config.properties file. Java code reads the
parameter from it to create Jdbc connection.  After this parameters are set, the java application will create the
necessary tables (Logs and Reports) automatically.

CREATE DATABASE  LOGTEST;
CREATE USER IF NOT EXISTS 'parser'@'localhost' IDENTIFIED BY 'parser1234';
GRANT ALL ON LOGTEST.* TO 'parser'@'localhost';




