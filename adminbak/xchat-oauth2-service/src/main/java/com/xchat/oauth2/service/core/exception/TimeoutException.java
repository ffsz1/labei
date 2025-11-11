package com.xchat.oauth2.service.core.exception;

/**
 * @author liuguofu
 *         on 4/10/15.
 */
public class TimeoutException extends ServiceException {

    public TimeoutException(String msg, Throwable t) {
        super(msg, t);
        statusCode = StatusCode.TIMEOUT_ERROR;
    }

    public TimeoutException(String msg) {
        super(msg);
        statusCode = StatusCode.TIMEOUT_ERROR;
    }
}
