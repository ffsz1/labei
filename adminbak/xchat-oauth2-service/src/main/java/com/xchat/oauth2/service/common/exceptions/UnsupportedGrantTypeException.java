package com.xchat.oauth2.service.common.exceptions;

/**
 * @author Ryan Heaton
 */
@SuppressWarnings("serial")
public class UnsupportedGrantTypeException extends com.xchat.oauth2.service.common.exceptions.OAuth2Exception {

  public UnsupportedGrantTypeException(String msg, Throwable t) {
    super(msg, t);
  }

  public UnsupportedGrantTypeException(String msg) {
    super(msg);
  }

  @Override
  public String getOAuth2ErrorCode() {
    return "unsupported_grant_type";
  }
}
