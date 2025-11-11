package com.xchat.oauth2.service.common.exceptions.myexception;

import com.xchat.oauth2.service.common.exceptions.OAuth2Exception;

/**
 * Created by Administrator on 2017/12/19.
 */
public class InvalidDeceiveException extends OAuth2Exception {
    public InvalidDeceiveException(String msg, Throwable t) {
        super(msg, t);
    }

    public InvalidDeceiveException(String msg) {
        super(msg);
    }

    @Override
    public int getHttpErrorCode() {
        return 200;
    }

    @Override
    public String getOAuth2ErrorCode() {
        return OAuth2Exception.DECEIVE_ERROR;
    }
}
