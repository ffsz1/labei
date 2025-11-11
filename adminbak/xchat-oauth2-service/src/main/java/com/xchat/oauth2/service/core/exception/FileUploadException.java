package com.xchat.oauth2.service.core.exception;

/**
 * @author liuguofu
 *         on 5/7/15.
 */
public class FileUploadException extends ServiceException {
    public FileUploadException(String msg, Throwable t) {
        super(msg, t);
        statusCode = StatusCode.FILE_UPLOAD_ERROR;
    }

    public FileUploadException(String msg) {
        super(msg);
        statusCode = StatusCode.FILE_UPLOAD_ERROR;
    }
}
