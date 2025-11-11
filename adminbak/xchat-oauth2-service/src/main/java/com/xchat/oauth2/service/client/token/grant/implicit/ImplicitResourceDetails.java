package com.xchat.oauth2.service.client.token.grant.implicit;

import com.xchat.oauth2.service.client.token.grant.redirect.AbstractRedirectResourceDetails;

/**
 * @author Dave Syer
 */
public class ImplicitResourceDetails extends AbstractRedirectResourceDetails {

	public ImplicitResourceDetails() {
		setGrantType("implicit");
	}

}
