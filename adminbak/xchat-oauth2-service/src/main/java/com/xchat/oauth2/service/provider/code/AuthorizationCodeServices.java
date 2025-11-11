package com.xchat.oauth2.service.provider.code;

import com.xchat.oauth2.service.common.exceptions.InvalidGrantException;
import com.xchat.oauth2.service.provider.OAuth2Authentication;

/**
 * Services for issuing and storing authorization codes.
 * 
 * @author Ryan Heaton
 */
public interface AuthorizationCodeServices {

	/**
	 * Create a authorization code for the specified authentications.
	 * 
	 * @param authentication The authentications to store.
	 * @return The generated code.
	 */
	String createAuthorizationCode(OAuth2Authentication authentication);

	/**
	 * Consume a authorization code.
	 * 
	 * @param code The authorization code to consume.
	 * @return The authentications associated with the code.
	 * @throws com.xchat.oauth2.service.common.exceptions.InvalidGrantException If the authorization code is invalid or expired.
	 */
	OAuth2Authentication consumeAuthorizationCode(String code)
			throws InvalidGrantException;

}
