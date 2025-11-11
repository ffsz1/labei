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
package com.xchat.oauth2.service.provider.token;

import java.util.Collections;
import java.util.List;

import com.xchat.oauth2.service.common.OAuth2AccessToken;
import com.xchat.oauth2.service.provider.OAuth2Authentication;

/**
 * A composite token enhancer that loops over its delegate enhancers.
 * 
 * @author Dave Syer
 * 
 */
public class TokenEnhancerChain implements com.xchat.oauth2.service.provider.token.TokenEnhancer {

	private List<com.xchat.oauth2.service.provider.token.TokenEnhancer> delegates = Collections.emptyList();

	/**
	 * @param delegates the delegates to set
	 */
	public void setTokenEnhancers(List<com.xchat.oauth2.service.provider.token.TokenEnhancer> delegates) {
		this.delegates = delegates;
	}

	/**
	 * Loop over the {@link #setTokenEnhancers(java.util.List) delegates} passing the result into the next member of the chain.
	 * 
	 * @see com.xchat.oauth2.service.provider.token.TokenEnhancer#enhance(com.xchat.oauth2.service.common.OAuth2AccessToken,
	 * com.xchat.oauth2.service.provider.OAuth2Authentication)
	 */
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		OAuth2AccessToken result = accessToken;
		for (com.xchat.oauth2.service.provider.token.TokenEnhancer enhancer : delegates) {
			result = enhancer.enhance(result, authentication);
		}
		return result;
	}

}
