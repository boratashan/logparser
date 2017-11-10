package com.ef.parser;

import com.ef.exceptions.ParserException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
/**
 * Holds the line data from log file. It parses the raw log line and assigns to corresponded members.
 * @author  Bora Tashan
 * @version 1.0
 * @since   2017-1-23
 */
public class LogRecord {
    private static final String[] cols = new String[]{"date", "ip", "request", "status", "agent"};
    private Date date;
    private String ip;
    private String request;
    private String status;
    private String agent;

    public LogRecord() {

    }

    public LogRecord(Date date, String ip, String request, String status, String agent) {
        this.date = date;
        this.ip = ip;
        this.request = request;
        this.status = status;
        this.agent = agent;
    }

    public static LogRecord fromString(String rawRecord) throws ParserException {
        LogRecord record = new LogRecord();
        try {
            StringTokenizer tokenizer = new StringTokenizer(rawRecord, "|");
            String rec;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            for (String s : cols) {
                if (tokenizer.hasMoreElements()) {
                    rec = tokenizer.nextToken();
                    switch (s) {
                        case "date":
                            record.date = simpleDateFormat.parse(rec);
                            break;
                        case "ip":
                            record.ip = rec;
                            break;
                        case "request":
                            record.request = rec;
                            break;
                        case "status":
                            record.status = rec;
                            break;
                        case "agent":
                            record.agent = rec;
                            break;
                    }
                } else {
                    throw new ParserException(String.format("Line \"%s\" can not parsed. Missing columns!"));
                }
            }
        } catch (ParseException e) {
            throw new ParserException(String.format("Line \"%s\" can not parsed. Datetime parsing error!"));
        }
        return record;
    }

    public Date getDate() {
        return date;
    }

    public String getIp() {
        return ip;
    }

    public String getRequest() {
        return request;
    }

    public String getStatus() {
        return status;
    }

    public String getAgent() {
        return agent;
    }

    @Override
    public String toString() {
        return String.format("Log Record : %s - %s - %s - %s - %s", date, ip, request, status, agent);
    }
}
