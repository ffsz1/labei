package com.xchat.oauth2.service.provider;

import com.xchat.oauth2.service.common.exceptions.InvalidScopeException;

/**
 * Validation interface for OAuth2 requests to the {@link com.xchat.oauth2.service.provider.endpoint.AuthorizationEndpoint} and {@link com.xchat.oauth2.service.provider.endpoint.TokenEndpoint}.
 * 
 * @author Amanda Anganes
 *
 */
public interface OAuth2RequestValidator {

	/**
	 * Ensure that the client has requested a valid set of scopes.
	 * 
	 * @param authorizationRequest the AuthorizationRequest to be validated
	 * @param client the client that is making the request
	 * @throws com.xchat.oauth2.service.common.exceptions.InvalidScopeException if a requested scope is invalid
	 */
	public void validateScope(com.xchat.oauth2.service.provider.AuthorizationRequest authorizationRequest, com.xchat.oauth2.service.provider.ClientDetails client) throws InvalidScopeException;
	
	/**
	 * Ensure that the client has requested a valid set of scopes.
	 * 
	 * @param tokenRequest the TokenRequest to be validated
	 * @param client the client that is making the request
	 * @throws com.xchat.oauth2.service.common.exceptions.InvalidScopeException if a requested scope is invalid
	 */
	public void validateScope(TokenRequest tokenRequest, ClientDetails client) throws InvalidScopeException;
	
}
