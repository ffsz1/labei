package com.xchat.oauth2.service.service.exceptions;

/**
 * @author liuguofu
 *         on 3/10/15.
 */
public class DeleteException extends DBException{

    public DeleteException() {
        super();
    }

    public DeleteException(String message) {
        super(message);
    }

    public DeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteException(Throwable cause) {
        super(cause);
    }

}
