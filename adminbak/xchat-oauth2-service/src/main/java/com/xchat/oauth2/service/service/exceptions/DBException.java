package com.xchat.oauth2.service.service.exceptions;

/**
 * @author liuguofu
 *         on 3/23/15.
 */
public class DBException extends RuntimeException {

    public DBException() {
        super();
    }

    public DBException(String message) {
        super(message);
    }

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBException(Throwable cause) {
        super(cause);
    }
}
