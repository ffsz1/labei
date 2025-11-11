package com.xchat.oauth2.service.service.exceptions;

/**
 * @author liuguofu
 *         on 3/10/15.
 */
public class InsertException extends DBException {

    public InsertException() {
        super();
    }

    public InsertException(String message) {
        super(message);
    }

    public InsertException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsertException(Throwable cause) {
        super(cause);
    }

}
