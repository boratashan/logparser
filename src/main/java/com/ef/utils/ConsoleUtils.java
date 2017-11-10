package com.ef.utils;

import static org.apache.commons.lang.StringUtils.repeat;

/**
 * Helper methods for using console.
 * @author  Bora Tashan
 * @version 1.0
 * @since   2017-1-23
 */
public class ConsoleUtils {


    public static void print(String message) {
        System.out.print(message);
    }

    public static void println(String message) {
        System.out.print(String.format("%s\n", message));
    }

    public static void printlnLine() {
        println(repeat("*", 50));
    }

    public static void printLnException(Exception e) {
        println(String.format("[EXCEPTION DURING EXECUTION] %s", e.getMessage()));
    }
}
