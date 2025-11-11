package com.xchat.oauth2.service.common.exceptions;

/**
 * @author Ryan Heaton
 */
@SuppressWarnings("serial")
public class UserDeniedAuthorizationException extends com.xchat.oauth2.service.common.exceptions.OAuth2Exception {

  public UserDeniedAuthorizationException(String msg, Throwable t) {
    super(msg, t);
  }

  public UserDeniedAuthorizationException(String msg) {
    super(msg);
  }

  @Override
  public String getOAuth2ErrorCode() {
    return "access_denied";
  }

}
