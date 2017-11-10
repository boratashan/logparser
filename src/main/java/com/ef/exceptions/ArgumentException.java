package com.ef.exceptions;
/**
 * Wrapper Exception to be throwed when command line argument problem is occured.
 * @author  Bora Tashan
 * @version 1.0
 * @since   2017-1-23
 */
public class ArgumentException extends ParserException {
    public ArgumentException(String message) {
        super(message);
    }

    public ArgumentException() {
        super();
    }

    public ArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
