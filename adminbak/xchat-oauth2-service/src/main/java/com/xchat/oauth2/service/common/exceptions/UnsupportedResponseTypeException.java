package com.xchat.oauth2.service.common.exceptions;


/**
 * @author Ryan Heaton
 */
@SuppressWarnings("serial")
public class UnsupportedResponseTypeException extends com.xchat.oauth2.service.common.exceptions.OAuth2Exception {

  public UnsupportedResponseTypeException(String msg, Throwable t) {
    super(msg, t);
  }

  public UnsupportedResponseTypeException(String msg) {
    super(msg);
  }

  @Override
  public String getOAuth2ErrorCode() {
    return "unsupported_response_type";
  }
}
