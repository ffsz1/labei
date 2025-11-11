package com.xchat.oauth2.service.core.exception;

/**
 * @author liuguofu
 *         on 4/10/15.
 */
public class IllegalParameterException extends ServiceException {
    public IllegalParameterException(String msg, Throwable t) {
        super(msg, t);
        statusCode = StatusCode.ILLEGAL_PARAMETER;
    }

    public IllegalParameterException(String msg) {
        super(msg);
        statusCode = StatusCode.ILLEGAL_PARAMETER;
    }
}
