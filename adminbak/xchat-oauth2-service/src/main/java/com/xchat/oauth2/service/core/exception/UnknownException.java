package com.xchat.oauth2.service.core.exception;

/**
 * @author liuguofu
 *         on 5/11/15.
 */
public class UnknownException extends ServiceException {
    public UnknownException(String msg, Throwable t) {
        super(msg, t);
        statusCode = StatusCode.UNKNOWN;
    }

    public UnknownException(String msg) {
        super(msg);
        statusCode = StatusCode.UNKNOWN;
    }
}
