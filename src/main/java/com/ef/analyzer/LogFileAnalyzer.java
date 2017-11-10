package com.ef.analyzer;

import com.ef.db.ConnectionFactory;
import com.ef.exceptions.DatabaseException;
import com.ef.exceptions.ParserException;
import com.ef.parser.LogFileReader;
import com.ef.utils.ConsoleUtils;
import com.ef.utils.DateUtils;
import com.ef.utils.SingleTaskExecutor;
import com.ef.utils.TaskRunnable;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;
/**
 * Seperated thread that reads log file and analyze according to requirement.
 * @author  Bora Tashan
 * @version 1.0
 * @since   2017-1-23
 */
public class LogFileAnalyzer implements TaskRunnable {

    SingleTaskExecutor executor;
    private String logFileName;
    private Date analyzeFrom;
    private Date analyzeTo;
    private int threshold;
    private Connection connection;

    public LogFileAnalyzer(String logFileName, Date analyzeFrom, Date analyzeTo, int threshold) {
        this.analyzeFrom = analyzeFrom;
        this.analyzeTo = analyzeTo;
        this.threshold = threshold;
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
            ConsoleUtils.printlnLine();
            ConsoleUtils.println("Log file analyzing process is started...");
            try {
                LogFileReader logFileReader = new LogFileReader(logFileName);
                LogAnalyzeContainer analyzer = new LogAnalyzeContainer();
                ConsoleUtils.println("Reading log file");
                logFileReader.read((logRecord, event) -> {
                    if (status.isInterrupted()) {
                        event.setStopReading(true);
                        return;
                    }
                    if (DateUtils.dateInRange(logRecord.getDate(), analyzeFrom, analyzeTo)) {
                        analyzer.put(logRecord);
                    }
                });
                if (!(status.isInterrupted())) {
                    connection = ConnectionFactory.newConnection();
                    ConsoleUtils.println("Reading log file is DONE");
                    ConsoleUtils.println("Creating report");
                    ConsoleUtils.println("--------------- R E P O R T ---------------");
                    ConsoleUtils.println(String.format("Analyze From : %s to %s, threshold : %d",
                            this.analyzeFrom.toString(),
                            this.analyzeTo.toString(),
                            this.threshold));
                    List<KeyValuePair<String, Integer>> reportTable = analyzer.getByThreshold(threshold);
                    for (KeyValuePair<String, Integer> pair : reportTable) {
                        ConsoleUtils.println(String.format("IP, Request made ->  %s -  %d", pair.getKey(), pair.getValue()));
                    }
                    ConsoleUtils.println("------------ R E P O R T [DONE] -----------");
                    ConsoleUtils.println("Writing report to database....");
                    this.writeReportToDb(reportTable);
                    ConsoleUtils.println("Writing report to database[DONE].");
                }
                ConsoleUtils.println(String.format("Log file analyzing process is [%s].", status.isInterrupted() ? "INTERRUPTED" : "DONE"));
                ConsoleUtils.printlnLine();
            } catch (ParserException | SQLException | IOException e) {
                ConsoleUtils.printLnException(e);
            }
        });
    }

    private void writeReportToDb(List<KeyValuePair<String, Integer>> report) throws DatabaseException, SQLException {
        Connection connection = ConnectionFactory.newConnection();
        String sql = "INSERT INTO LogReports (ReportID, Date, IP, ReportText) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        String reportID = UUID.randomUUID().toString();
        Timestamp reportDate = DateUtils.convertJavaDateToSqlTimeStamp(new Date());
        for (KeyValuePair<String, Integer> pair : report) {
            statement.setString(1, reportID);
            statement.setTimestamp(2, reportDate);
            statement.setString(3, pair.getKey());
            statement.setString(4, String.format("IP %s is blocked due to accessing the server more than %d (threshold is %d)times between %s and %s",
                    pair.getKey(),
                    pair.getValue(),
                    threshold,
                    analyzeFrom.toString(),
                    analyzeTo.toString()));
            statement.execute();
        }
    }


}
