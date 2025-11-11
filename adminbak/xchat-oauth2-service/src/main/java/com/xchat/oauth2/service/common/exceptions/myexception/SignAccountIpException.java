package com.xchat.oauth2.service.common.exceptions.myexception;

import com.xchat.oauth2.service.common.exceptions.OAuth2Exception;

/**
 * Created by liuguofu on 2017/10/26.
 */


public class SignAccountIpException extends OAuth2Exception {

    public SignAccountIpException(String msg, Throwable t) {
        super(msg, t);
    }

    public SignAccountIpException(String msg) {
        super(msg);
    }

    @Override
    public int getHttpErrorCode() {
        return 200;
    }

    @Override
    public String getOAuth2ErrorCode() {
        return OAuth2Exception.SIGN_IP_TO_OFTEN;
    }
}
