/*
 * Copyright 2013-2014 the original author or authors.
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

package com.xchat.oauth2.service.client;

import org.springframework.http.client.ClientHttpRequest;

import com.xchat.oauth2.service.client.http.AccessTokenRequiredException;
import com.xchat.oauth2.service.client.resource.OAuth2ProtectedResourceDetails;
import com.xchat.oauth2.service.common.OAuth2AccessToken;
import org.springframework.util.StringUtils;

/**
 * @author Dave Syer
 *
 */
public class DefaultOAuth2RequestAuthenticator implements com.xchat.oauth2.service.client.OAuth2RequestAuthenticator {

	@Override
	public void authenticate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext clientContext,
			ClientHttpRequest request) {
		OAuth2AccessToken accessToken = clientContext.getAccessToken();
		if (accessToken == null) {
			throw new AccessTokenRequiredException(resource);
		}
		String tokenType = accessToken.getTokenType();
		if (!StringUtils.hasText(tokenType)) {
			tokenType = OAuth2AccessToken.BEARER_TYPE; // we'll assume basic bearer token type if none is specified.
		}
		request.getHeaders().set("Authorization", String.format("%s %s", tokenType, accessToken.getValue()));
	}

}
