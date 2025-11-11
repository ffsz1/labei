package com.xchat.oauth2.service.core.exception;

/**
 * @author liuguofu
 *         on 4/10/15.
 */
public class DuplicateException extends ServiceException {
    public DuplicateException(String msg, Throwable t) {
        super(msg, t);
        statusCode = StatusCode.DUPLICATE_EXCEPTION;
        HttpCode = 200;
    }

    public DuplicateException(String msg) {
        super(msg);
        statusCode = StatusCode.DUPLICATE_EXCEPTION;
        HttpCode = 200;
    }
}
