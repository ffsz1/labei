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

package com.xchat.oauth2.service.provider.refresh;

import com.xchat.oauth2.service.common.OAuth2AccessToken;
import com.xchat.oauth2.service.provider.ClientDetails;
import com.xchat.oauth2.service.provider.ClientDetailsService;
import com.xchat.oauth2.service.provider.OAuth2RequestFactory;
import com.xchat.oauth2.service.provider.TokenRequest;
import com.xchat.oauth2.service.provider.token.AbstractTokenGranter;
import com.xchat.oauth2.service.provider.token.AuthorizationServerTokenServices;

/**
 * @author Dave Syer
 * 
 */
public class RefreshTokenGranter extends AbstractTokenGranter {

	private static final String GRANT_TYPE = "refresh_token";

	public RefreshTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
		super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
	}
	
	@Override
	protected OAuth2AccessToken getAccessToken(ClientDetails client, TokenRequest tokenRequest) {
		String refreshToken = tokenRequest.getRequestParameters().get("refresh_token");
		return getTokenServices().refreshAccessToken(refreshToken, tokenRequest);
	}

}
