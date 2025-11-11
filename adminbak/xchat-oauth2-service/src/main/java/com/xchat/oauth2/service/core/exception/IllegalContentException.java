package com.xchat.oauth2.service.core.exception;

/**
 * @author liuguofu
 *         on 4/10/15.
 */
public class IllegalContentException extends ServiceException {
    public IllegalContentException(String msg, Throwable t) {
        super(msg, t);
        statusCode = StatusCode.ILLEGAL_CONTENT_ERROR;
        HttpCode = 200;
    }

    public IllegalContentException(String msg) {
        super(msg);
        statusCode = StatusCode.ILLEGAL_CONTENT_ERROR;
        HttpCode = 200;
    }
}
