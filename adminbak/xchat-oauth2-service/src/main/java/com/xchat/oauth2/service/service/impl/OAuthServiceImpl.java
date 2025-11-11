package com.xchat.oauth2.service.service.impl;

import com.xchat.oauth2.service.domain.oauth.OAuthClientDetails;
import com.xchat.oauth2.service.domain.oauth.OAuthRepository;
import com.xchat.oauth2.service.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liuguofu
 */
@Service("oauthService")
public class OAuthServiceImpl implements OAuthService {

    @Autowired
    private OAuthRepository oauthRepository;

    @Override
    public OAuthClientDetails loadOAuthClientDetails(String clientId) {
        return oauthRepository.findOAuthClientDetails(clientId);
    }
}
