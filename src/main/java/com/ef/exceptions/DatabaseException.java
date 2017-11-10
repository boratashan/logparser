package com.ef.exceptions;

/**
 * Wrapper Exception to be throwed when database problem is occured.
 * @author  Bora Tashan
 * @version 1.0
 * @since   2017-1-23
 */
public class DatabaseException extends ParserException {
    public DatabaseException() {
        super();
    }

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
