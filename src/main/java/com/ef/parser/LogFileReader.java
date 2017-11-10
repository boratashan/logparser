package com.ef.parser;

import com.ef.exceptions.ParserException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Generic log file reader which allows to read each line in a delegated event or lambda function.
 * Also provides functionatility to control traversing.
 * @author  Bora Tashan
 * @version 1.0
 * @since   2017-1-23
 */
public class LogFileReader {
    private String logFileName;

    public LogFileReader(String logFileName) throws FileNotFoundException {
        this.logFileName = logFileName;
    }

    public void read(LogFileReadHandler handler) throws IOException, ParserException, SQLException {
        ReadEvent readEvent = new ReadEvent();
        BufferedReader reader = new BufferedReader(new FileReader(logFileName));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                LogRecord record = LogRecord.fromString(line);
                handler.onReadLine(record, readEvent);
                if (readEvent.isStopReading()) {
                    break;
                }
            }
        } finally {
            reader.close();
        }
    }

    public interface LogFileReadHandler {
        public void onReadLine(LogRecord logRecord, ReadEvent readEvent) throws SQLException;
    }

    public class ReadEvent {
        private boolean stopReading;

        public boolean isStopReading() {
            return stopReading;
        }

        public void setStopReading(boolean stopReading) {
            this.stopReading = stopReading;
        }

    }


}
