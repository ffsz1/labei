package com.xchat.oauth2.service.client.token.grant.client;

import java.util.Iterator;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import com.xchat.oauth2.service.client.resource.OAuth2AccessDeniedException;
import com.xchat.oauth2.service.client.resource.OAuth2ProtectedResourceDetails;
import com.xchat.oauth2.service.client.resource.UserRedirectRequiredException;
import com.xchat.oauth2.service.client.token.AccessTokenProvider;
import com.xchat.oauth2.service.client.token.AccessTokenRequest;
import com.xchat.oauth2.service.client.token.OAuth2AccessTokenSupport;
import com.xchat.oauth2.service.common.OAuth2RefreshToken;
import com.xchat.oauth2.service.common.OAuth2AccessToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Provider for obtaining an oauth2 access token by using client credentials.
 * 
 * @author Dave Syer
 */
public class ClientCredentialsAccessTokenProvider extends OAuth2AccessTokenSupport implements AccessTokenProvider {

	public boolean supportsResource(OAuth2ProtectedResourceDetails resource) {
		return resource instanceof com.xchat.oauth2.service.client.token.grant.client.ClientCredentialsResourceDetails
				&& "client_credentials".equals(resource.getGrantType());
	}

	public boolean supportsRefresh(OAuth2ProtectedResourceDetails resource) {
		return false;
	}

	public OAuth2AccessToken refreshAccessToken(OAuth2ProtectedResourceDetails resource,
			OAuth2RefreshToken refreshToken, AccessTokenRequest request) throws UserRedirectRequiredException {
		return null;
	}

	public OAuth2AccessToken obtainAccessToken(OAuth2ProtectedResourceDetails details, AccessTokenRequest request)
			throws UserRedirectRequiredException, AccessDeniedException, OAuth2AccessDeniedException {

		com.xchat.oauth2.service.client.token.grant.client.ClientCredentialsResourceDetails resource = (com.xchat.oauth2.service.client.token.grant.client.ClientCredentialsResourceDetails) details;
		return retrieveToken(request, resource, getParametersForTokenRequest(resource), new HttpHeaders());

	}

	private MultiValueMap<String, String> getParametersForTokenRequest(
			com.xchat.oauth2.service.client.token.grant.client.ClientCredentialsResourceDetails resource) {

		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.set("grant_type", "client_credentials");

		if (resource.isScoped()) {

			StringBuilder builder = new StringBuilder();
			List<String> scope = resource.getScope();

			if (scope != null) {
				Iterator<String> scopeIt = scope.iterator();
				while (scopeIt.hasNext()) {
					builder.append(scopeIt.next());
					if (scopeIt.hasNext()) {
						builder.append(' ');
					}
				}
			}

			form.set("scope", builder.toString());
		}

		return form;

	}

}
