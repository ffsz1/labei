package com.xchat.oauth2.service.provider.implicit;

import java.util.concurrent.ConcurrentHashMap;

import com.xchat.oauth2.service.provider.OAuth2Request;
import com.xchat.oauth2.service.provider.TokenRequest;

/**
 * In-memory implementation of the ImplicitGrantService.
 * 
 * @author Amanda Anganes
 *
 */
@SuppressWarnings("deprecation")
public class InMemoryImplicitGrantService implements com.xchat.oauth2.service.provider.implicit.ImplicitGrantService {

	protected final ConcurrentHashMap<TokenRequest, OAuth2Request> requestStore = new ConcurrentHashMap<TokenRequest, OAuth2Request>();
	
	public void store(OAuth2Request originalRequest, TokenRequest tokenRequest) {
		this.requestStore.put(tokenRequest, originalRequest);
	}

	public OAuth2Request remove(TokenRequest tokenRequest) {
		OAuth2Request request = this.requestStore.remove(tokenRequest);
		return request;
	}

}
