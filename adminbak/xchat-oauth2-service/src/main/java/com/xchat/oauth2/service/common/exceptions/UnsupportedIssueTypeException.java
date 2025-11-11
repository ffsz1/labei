package com.xchat.oauth2.service.common.exceptions;

/**
 * @author liuguofu
 *         on 3/19/15.
 */
public class UnsupportedIssueTypeException extends OAuth2Exception {

    public UnsupportedIssueTypeException(String msg, Throwable t) {
        super(msg, t);
    }

    public UnsupportedIssueTypeException(String msg) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "unsupported_issue_type";
    }
}
