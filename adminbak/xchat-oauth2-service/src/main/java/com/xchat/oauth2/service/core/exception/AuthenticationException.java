package com.xchat.oauth2.service.core.exception;

/**
 * @author liuguofu
 *         on 4/10/15.
 */
public class AuthenticationException extends ServiceException {

    public AuthenticationException(String msg, Throwable t) {
        super(msg, t);
        statusCode = StatusCode.UNAUTHENTICATION;
    }

    public AuthenticationException(String msg) {
        super(msg);
        statusCode = StatusCode.UNAUTHENTICATION;
    }
}
