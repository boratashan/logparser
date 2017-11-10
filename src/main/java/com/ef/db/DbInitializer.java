package com.ef.db;

import com.ef.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Initialize db objects(table, user, sp...) for application.
 * @author  Bora Tashan
 * @version 1.0
 * @since   2017-1-23
 */
public class DbInitializer {

    private Connection connection;

    public DbInitializer() throws DatabaseException {
        connection = ConnectionFactory.newConnection();

    }

    public synchronized void initDbObjects() throws DatabaseException {
        try {
            this.createTables();
        } catch (SQLException e) {
            throw new DatabaseException("Sql error while creating db objects!", e);
        }
    }


    private void createTables() throws SQLException {

        String logTableSql = " create table IF NOT EXISTS Logs ( " +
                "                id bigint auto_increment primary key, " +
                "                Date datetime null, " +
                "                IP varchar(15) null, " +
                "                Request varchar(256) null, " +
                "                Status int null, " +
                "                UserAgent varchar(256) null" +
                ")";

        String reportTableSql = " create table IF NOT EXISTS LogReports ( " +
                "                id bigint auto_increment primary key, " +
                "                ReportID CHAR(36) null, " +
                "                Date datetime null, " +
                "                IP varchar(15) null, " +
                "                ReportText varchar(256) null" +
                ")";

        Statement statement = connection.createStatement();
        statement.addBatch(logTableSql);
        statement.addBatch(reportTableSql);
        int i[] = statement.executeBatch();

    }


}
