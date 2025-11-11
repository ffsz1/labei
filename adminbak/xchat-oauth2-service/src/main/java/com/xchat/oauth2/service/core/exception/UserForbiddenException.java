package com.xchat.oauth2.service.core.exception;

/**
 * @author liuguofu
 *         on 4/10/15.
 */
public class UserForbiddenException extends ServiceException {
    public UserForbiddenException(String msg, Throwable t) {
        super(msg, t);
        statusCode = StatusCode.USER_FORBIDDEN_ERROR;
        HttpCode = 200;
    }

    public UserForbiddenException(String msg) {
        super(msg);
        statusCode = StatusCode.USER_FORBIDDEN_ERROR;
        HttpCode = 200;
    }
}
