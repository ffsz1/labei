package com.xchat.oauth2.service.client.token.grant.client;

import com.xchat.oauth2.service.client.resource.BaseOAuth2ProtectedResourceDetails;

/**
 * @author Dave Syer
 */
public class ClientCredentialsResourceDetails extends BaseOAuth2ProtectedResourceDetails {
	
	public ClientCredentialsResourceDetails() {
		setGrantType("client_credentials");
	}
	
	@Override
	public boolean isClientOnly() {
		return true;
	}

}
