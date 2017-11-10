package com.ef.exceptions;

/**
 * Wrapper Exception to be throwed when application config file is not found.
 * @author  Bora Tashan
 * @version 1.0
 * @since   2017-1-23
 */
public class ConfigFileException extends ParserException {
    public ConfigFileException() {
        super();
    }

    public ConfigFileException(String message) {
        super(message);
    }

    public ConfigFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
