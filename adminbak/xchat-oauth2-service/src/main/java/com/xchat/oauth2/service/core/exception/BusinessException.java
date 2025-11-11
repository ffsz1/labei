package com.xchat.oauth2.service.core.exception;

/**
 * @author liuguofu
 *         on 4/10/15.
 */
public class BusinessException extends ServiceException {
    public BusinessException(String msg, Throwable t) {
        super(msg, t);
        statusCode = StatusCode.BUSINESS_ERROR;
    }

    public BusinessException(String msg) {
        super(msg);
        statusCode = StatusCode.BUSINESS_ERROR;
    }
}
