package com.xchat.oauth2.service.domain.oauth;

import com.xchat.oauth2.service.domain.shared.Repository;

public interface OAuthRepository extends Repository {

    OAuthClientDetails findOAuthClientDetails(String clientId);
}
