package com.xchat.oauth2.service.provider.token;

import org.springframework.security.core.AuthenticationException;
import com.xchat.oauth2.service.common.OAuth2AccessToken;
import com.xchat.oauth2.service.common.exceptions.InvalidTokenException;
import com.xchat.oauth2.service.provider.OAuth2Authentication;

public interface ResourceServerTokenServices {

	/**
	 * Load the credentials for the specified access token.
	 *
	 * @param accessToken The access token value.
	 * @return The authentication for the access token.
	 * @throws org.springframework.security.core.AuthenticationException If the access token is expired
	 * @throws com.xchat.oauth2.service.common.exceptions.InvalidTokenException if the token isn't valid
	 */
	OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException;

	/**
	 * Retrieve the full access token details from just the value.
	 * 
	 * @param accessToken the token value
	 * @return the full access token with client id etc.
	 */
	OAuth2AccessToken readAccessToken(String accessToken);

}
