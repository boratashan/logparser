# logparser

A log parser command line application with java. It demonstrates 

1. Command line argument handling
2. Background task runner with java 8 lambda implementation
3. Fast file reading
4. Containers to analyze log file (Included)
5. Loading log records to MySQL
6. Some helper implementations

To run application
    java -cp "parser.jar" com.ef.Parser --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100 

In order to load logs to Please execute below sql commands to create mysql database with name "LOGTEST"
also please create the user parser with password parser1234

In java code, under resources folder, the is config.properties file which
containes database jdbc url, user and password. If another database and user is demanded
please change the configuration in the config.properties file. Java code reads the
parameter from it to create Jdbc connection.  After this parameters are set, the java application will create the
necessary tables (Logs and Reports) automatically.

CREATE DATABASE  LOGTEST;
CREATE USER IF NOT EXISTS 'parser'@'localhost' IDENTIFIED BY 'parser1234';
GRANT ALL ON LOGTEST.* TO 'parser'@'localhost';
