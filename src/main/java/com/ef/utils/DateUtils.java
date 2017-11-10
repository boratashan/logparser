package com.ef.utils;


import org.joda.time.DateTime;

import java.util.Date;
/**
 * Helper methods for date time operations.
 * @author  Bora Tashan
 * @version 1.0
 * @since   2017-1-23
 */
public class DateUtils {

    public static int secondsBetween(Date from, Date to) {
        return (int) (Math.abs(from.getTime() - to.getTime()) / 1000);
    }

    public static boolean dateInRange(Date value, Date from, Date to) {
        return (value.compareTo(from) >= 0) && (value.compareTo(to) < 0);

    }

    public static Date increseDateByHour(Date value, int hours) {
        DateTime dateTime = new DateTime(value);
        return dateTime.plusHours(hours).toDate();
    }

    public static Date increseDateByDay(Date value, int days) {
        DateTime dateTime = new DateTime(value);
        return dateTime.plusDays(days).toDate();
    }

    public static java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    public static java.sql.Timestamp convertJavaDateToSqlTimeStamp(java.util.Date date) {
        return new java.sql.Timestamp(date.getTime());
    }
}
