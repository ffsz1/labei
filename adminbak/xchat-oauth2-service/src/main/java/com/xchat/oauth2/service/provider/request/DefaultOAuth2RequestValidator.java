package com.xchat.oauth2.service.provider.request;

import java.util.Set;

import com.xchat.oauth2.service.common.exceptions.InvalidScopeException;
import com.xchat.oauth2.service.provider.AuthorizationRequest;
import com.xchat.oauth2.service.provider.ClientDetails;
import com.xchat.oauth2.service.provider.OAuth2RequestValidator;
import com.xchat.oauth2.service.provider.TokenRequest;

/**
 * Default implementation of {@link com.xchat.oauth2.service.provider.OAuth2RequestValidator}.
 * 
 * @author Amanda Anganes
 *
 */
public class DefaultOAuth2RequestValidator implements OAuth2RequestValidator {

	public void validateScope(AuthorizationRequest authorizationRequest, ClientDetails client) throws InvalidScopeException {
		validateScope(authorizationRequest.getScope(), client.getScope());
	}

	public void validateScope(TokenRequest tokenRequest, ClientDetails client) throws InvalidScopeException {
		validateScope(tokenRequest.getScope(), client.getScope());
	}
	
	private void validateScope(Set<String> requestScopes, Set<String> clientScopes) {

		if (clientScopes != null && !clientScopes.isEmpty()) {
			for (String scope : requestScopes) {
				if (!clientScopes.contains(scope)) {
					throw new InvalidScopeException("Invalid scope: " + scope, clientScopes);
				}
			}
		}
		
		if (requestScopes.isEmpty()) {
			throw new InvalidScopeException("Empty scope (either the client or the user is not allowed the requested scopes)");
		}
	}

}
