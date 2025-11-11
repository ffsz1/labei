package com.xchat.oauth2.service.common.exceptions.myexception;

import com.xchat.oauth2.service.common.exceptions.OAuth2Exception;

/**
 * Created by Administrator on 2017/12/19.
 */
public class InvalidAccountException extends OAuth2Exception {
    public InvalidAccountException(String msg, Throwable t) {
        super(msg, t);
    }

    public InvalidAccountException(String msg) {
        super(msg);
    }

    @Override
    public int getHttpErrorCode() {
        return 200;
    }

    @Override
    public String getOAuth2ErrorCode() {
        return OAuth2Exception.ACCOUNT_ERROR;
    }


}
