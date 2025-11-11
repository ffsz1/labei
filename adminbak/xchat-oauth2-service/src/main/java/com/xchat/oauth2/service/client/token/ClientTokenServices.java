/*
 * Copyright 2006-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */


package com.xchat.oauth2.service.client.token;

import org.springframework.security.core.Authentication;
import com.xchat.oauth2.service.client.resource.OAuth2ProtectedResourceDetails;
import com.xchat.oauth2.service.common.OAuth2AccessToken;

/**
 * @author Dave Syer
 * 
 */
public interface ClientTokenServices {

	/**
	 * Retrieve the access token for a given resource and user authentication (my be null).
	 * 
	 * @param resource the resource to be accessed
	 * @param authentication the current user authentication (or null if there is none)
	 * @return an access token if one has been stored, null otherwise
	 */
	OAuth2AccessToken getAccessToken(OAuth2ProtectedResourceDetails resource, Authentication authentication);

	/**
	 * Save or update the access token for this resource and authentication (may be null).
	 * 
	 * @param resource the resource to be accessed
	 * @param authentication the current user authentication (or null if there is none)
	 * @param accessToken an access token to be stored
	 */
	void saveAccessToken(OAuth2ProtectedResourceDetails resource, Authentication authentication,
						 OAuth2AccessToken accessToken);

	/**
	 * Remove the token (if any) that is stored with the provided resource and authentication. If there is no such token
	 * do nothing.
	 * 
	 * @param resource the resource to be accessed
	 * @param authentication the current user authentication (or null if there is none)
	 */
	void removeAccessToken(OAuth2ProtectedResourceDetails resource, Authentication authentication);

}
