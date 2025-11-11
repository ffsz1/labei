package com.xchat.oauth2.web.oauth;

import com.xchat.oauth2.service.domain.oauth.OAuthClientDetails;
import com.xchat.oauth2.service.provider.approval.TokenServicesUserApprovalHandler;
import com.xchat.oauth2.service.service.OAuthService;
import org.springframework.security.core.Authentication;
import com.xchat.oauth2.service.provider.AuthorizationRequest;

/**
 * @author liuguofu
 */
public class OAuthUserApprovalHandler extends TokenServicesUserApprovalHandler {

    private OAuthService oauthService;

    public OAuthUserApprovalHandler() {
    }


    public boolean isApproved(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
        if (super.isApproved(authorizationRequest, userAuthentication)) {
            return true;
        }
        if (!userAuthentication.isAuthenticated()) {
            return false;
        }

        OAuthClientDetails clientDetails = oauthService.loadOAuthClientDetails(authorizationRequest.getClientId());
        return clientDetails != null && clientDetails.trusted();

    }

    public void setoAuthService(OAuthService oAuthService) {
        this.oauthService = oAuthService;
    }
}
