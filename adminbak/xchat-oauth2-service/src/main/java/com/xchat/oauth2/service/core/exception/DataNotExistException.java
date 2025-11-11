package com.xchat.oauth2.service.core.exception;

/**
 * @author liuguofu
 *         on 4/10/15.
 */
public class DataNotExistException extends ServiceException {
    public DataNotExistException(String msg, Throwable t) {
        super(msg, t);
        statusCode = StatusCode.DATA_NOT_EXIST_ERROR;
        super.HttpCode = 404;
    }

    public DataNotExistException(String msg) {
        super(msg);
        statusCode = StatusCode.DATA_NOT_EXIST_ERROR;
        super.HttpCode = 404;
    }
}
