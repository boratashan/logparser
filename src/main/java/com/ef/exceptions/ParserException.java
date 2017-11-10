package com.ef.exceptions;

/**
 * Wrapper Exception to be throwed when log file line is not being parsed.
 * @author  Bora Tashan
 * @version 1.0
 * @since   2017-1-23
 */
public class ParserException extends Exception {
    public ParserException() {
        super();
    }

    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
