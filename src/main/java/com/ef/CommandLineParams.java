package com.ef;

import com.ef.exceptions.ArgumentException;
import com.ef.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Parses commandline arguments and assigns to corresponding member
 * or throw an exception when arguments are missing or invalid.
 *
 * @author Bora Tashan
 * @version 1.0
 * @since 2017-1-23
 */
public class CommandLineParams {

    public static final String PARAM_PREFIX = "--";
    public static final String PARAM_PATH_TO_LOG = "accesslog";
    public static final String PARAM_START_DATE = "startdate";
    public static final String PARAM_DURATION = "duration";
    public static final String PARAM_THRESHOLD = "threshold";
    public static final String PARAM_DELIMETER = "=";
    private static final String PARAM_DATEFORMAT = "yyyy-MM-dd.HH:mm:ss";

    private Date startDate;
    private Duration duration;
    private int threshold;
    private String logFilePath;

    public String getLogFilePath() {
        return logFilePath;
    }

    private Date calculatedEndDate;

    public CommandLineParams(String[] args) throws ArgumentException {
        this.readParams(args);
        this.validateParams();
        switch (this.duration) {
            case DAILY:
                calculatedEndDate = DateUtils.increseDateByDay(startDate, 1);
                break;
            case HOURLY:
                calculatedEndDate = DateUtils.increseDateByHour(startDate, 1);
                break;
        }
    }

    public Date getCalculatedEndDate() {
        return calculatedEndDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Duration getDuration() {
        return duration;
    }

    public int getThreshold() {
        return threshold;
    }

    private void readParams(String[] args) throws ArgumentException {
        for (String p : args) {
            StringTokenizer tokenizer = new StringTokenizer(p, PARAM_DELIMETER);
            String param = tokenizer.nextToken();
            String value;
            if (!tokenizer.hasMoreElements()) {
                throw new IllegalArgumentException(String.format("Argument \"%s\" does not have proper value."));
            } else {
                value = tokenizer.nextToken();
            }
            try {
                if (param.equalsIgnoreCase(PARAM_PREFIX.concat(PARAM_DURATION))) {
                    this.duration = Duration.fromString(value);
                } else if (param.equalsIgnoreCase(PARAM_PREFIX.concat(PARAM_THRESHOLD))) {
                    this.threshold = Integer.parseInt(value);

                } else if (param.equalsIgnoreCase(PARAM_PREFIX.concat(PARAM_START_DATE))) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PARAM_DATEFORMAT);
                    this.startDate = simpleDateFormat.parse(value);
                } else if (param.equalsIgnoreCase(PARAM_PREFIX.concat(PARAM_PATH_TO_LOG))) {
                    this.logFilePath = value;
                }
            } catch (ParseException | NumberFormatException | EnumConstantNotPresentException e) {
                throw new ArgumentException(String.format("Argument \"%s\" parameter value \"%s\"  can not be parsed.", param, value));
            }
        }
    }

    private void validateParams() throws ArgumentException {
        if (this.startDate == null)
            throw new ArgumentException("Missing param start date.");
        if (this.threshold == 0)
            throw new ArgumentException("Missing param threshold.");
        if (this.duration == null)
            throw new ArgumentException("Missing param durating.");
        if (this.logFilePath == null)
            throw new ArgumentException("Missing param accesslog.");
    }
}
