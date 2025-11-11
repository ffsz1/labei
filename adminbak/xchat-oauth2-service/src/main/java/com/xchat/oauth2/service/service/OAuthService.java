package com.xchat.oauth2.service.service;

import com.xchat.oauth2.service.domain.oauth.OAuthClientDetails;

/**
 * @author liuguofu
 */

public interface OAuthService {

    OAuthClientDetails loadOAuthClientDetails(String clientId);
}
