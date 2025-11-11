/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xchat.oauth2.service.client.token;

import org.springframework.security.access.AccessDeniedException;
import com.xchat.oauth2.service.client.resource.OAuth2ProtectedResourceDetails;
import com.xchat.oauth2.service.client.resource.UserApprovalRequiredException;
import com.xchat.oauth2.service.client.resource.UserRedirectRequiredException;
import com.xchat.oauth2.service.common.OAuth2AccessToken;
import com.xchat.oauth2.service.common.OAuth2RefreshToken;

/**
 * A strategy which knows how to obtain an access token for a specific resource.
 * 
 * @author Ryan Heaton
 * @author Dave Syer
 */
public interface AccessTokenProvider {

	/**
	 * Obtain a new access token for the specified protected resource.
	 * 
	 * @param details The protected resource for which this provider is to obtain an access token.
	 * @param parameters The parameters of the request giving context for the token details if any.
	 * @return The access token for the specified protected resource. The return value may NOT be null.
	 * @throws com.xchat.oauth2.service.client.resource.UserRedirectRequiredException If the provider requires the current user to be redirected for
	 * authorization.
	 * @throws com.xchat.oauth2.service.client.resource.UserApprovalRequiredException If the provider is ready to issue a token but only if the user approves
	 * @throws org.springframework.security.access.AccessDeniedException If the user denies access to the protected resource.
	 */
	OAuth2AccessToken obtainAccessToken(OAuth2ProtectedResourceDetails details, com.xchat.oauth2.service.client.token.AccessTokenRequest parameters)
			throws UserRedirectRequiredException, UserApprovalRequiredException, AccessDeniedException;

	/**
	 * Whether this provider supports the specified resource.
	 * 
	 * @param resource The resource.
	 * @return Whether this provider supports the specified resource.
	 */
	boolean supportsResource(OAuth2ProtectedResourceDetails resource);

	/**
	 * @param resource the resource for which a token refresh is required
	 * @param refreshToken the refresh token to send
	 * @return an access token
	 */
	OAuth2AccessToken refreshAccessToken(OAuth2ProtectedResourceDetails resource, OAuth2RefreshToken refreshToken,
										 com.xchat.oauth2.service.client.token.AccessTokenRequest request) throws UserRedirectRequiredException;

	/**
	 * @param resource The resource to check
	 * @return true if this provider can refresh an access token
	 */
	boolean supportsRefresh(OAuth2ProtectedResourceDetails resource);
}
