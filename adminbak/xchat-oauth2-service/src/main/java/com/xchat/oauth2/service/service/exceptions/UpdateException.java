package com.xchat.oauth2.service.service.exceptions;

/**
 * @author liuguofu
 *         on 3/10/15.
 */
public class UpdateException extends DBException {

    public UpdateException() {
        super();
    }

    public UpdateException(String message) {
        super(message);
    }

    public UpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateException(Throwable cause) {
        super(cause);
    }

}
