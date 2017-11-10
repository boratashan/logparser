package com.ef.db;

import com.ef.exceptions.DatabaseException;
import com.ef.exceptions.ParserException;
import com.ef.parser.LogFileReader;
import com.ef.utils.ConsoleUtils;
import com.ef.utils.DateUtils;
import com.ef.utils.SingleTaskExecutor;
import com.ef.utils.TaskRunnable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * Seperated thread that reads log and loads all records to database.
 * @author  Bora Tashan
 * @version 1.0
 * @since   2017-1-23
 */
public class DbLogFileLoader implements TaskRunnable {

    SingleTaskExecutor executor;
    private String logFileName;


    public DbLogFileLoader(String logFileName) {
        this.logFileName = logFileName;
        executor = new SingleTaskExecutor();
    }

    @Override
    public boolean isFinished() {
        return executor.isFinished();
    }

    @Override
    public void interrupt() {
        executor.interrupt();
    }

    @Override
    public void join() {
        executor.join();
    }

    @Override
    public void run() {
        executor.execute(status -> {
            try {
                ConsoleUtils.printlnLine();
                ConsoleUtils.println("Transferring log file to mysql...");
                Connection connection = ConnectionFactory.newConnection();
                try {
                    LogFileReader logFileReader = new LogFileReader(logFileName);
                    ConsoleUtils.println("Purifying log table...");
                    connection.prepareStatement("DELETE FROM Logs").execute();
                    ConsoleUtils.println("Purifying log table.[DONE]");

                    ConsoleUtils.println("Transferring log file to mysql...");

                    String query = "INSERT INTO Logs (Date, IP, Request, Status, UserAgent) VALUES (?,?,?,?,?)";
                    PreparedStatement statement = connection.prepareStatement(query);

                    logFileReader.read((logRecord, event) -> {
                        if (status.isInterrupted()) {
                            event.setStopReading(true);
                            return;
                        }
                        statement.setTimestamp(1, DateUtils.convertJavaDateToSqlTimeStamp(logRecord.getDate()));
                        statement.setString(2, logRecord.getIp());
                        statement.setString(3, logRecord.getRequest());
                        statement.setString(4, logRecord.getStatus());
                        statement.setString(5, logRecord.getAgent());
                        statement.execute();
                    });
                    ConsoleUtils.println("Transferring log file to mysql. [DONE]");
                    ConsoleUtils.printlnLine();
                } finally {
                    connection.close();
                }
            } catch (SQLException | DatabaseException e) {
                ConsoleUtils.println("ERROR : Database error, please check database instance!");
                ConsoleUtils.printLnException(e);
                ConsoleUtils.println(e.getCause().getMessage());
            } catch (FileNotFoundException e) {
                ConsoleUtils.println("ERROR : Log file is not found!");
                ConsoleUtils.printLnException(e);
                ConsoleUtils.println(e.getCause().getMessage());
            } catch (IOException e) {
                ConsoleUtils.println("ERROR : IO Error while parsing the log file!");
                ConsoleUtils.printLnException(e);
                ConsoleUtils.println(e.getCause().getMessage());
            } catch (ParserException e) {
                ConsoleUtils.println("ERROR : Can not parse the line record of the log!");
                ConsoleUtils.printLnException(e);
                ConsoleUtils.println(e.getCause().getMessage());
            }
        });

    }


}
