package com.xchat.oauth2.service.client.token.grant.code;

import com.xchat.oauth2.service.client.token.grant.redirect.AbstractRedirectResourceDetails;

/**
 * @author Ryan Heaton
 * @author Dave Syer
 */
public class AuthorizationCodeResourceDetails extends AbstractRedirectResourceDetails {

	public AuthorizationCodeResourceDetails() {
		setGrantType("authorization_code");
	}

}
