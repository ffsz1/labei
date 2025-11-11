/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.xchat.oauth2.service.config.annotation.web.configurers;

import java.util.*;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;

import com.xchat.oauth2.service.provider.ClientDetailsService;
import com.xchat.oauth2.service.provider.CompositeTokenGranter;
import com.xchat.oauth2.service.provider.OAuth2RequestFactory;
import com.xchat.oauth2.service.provider.OAuth2RequestValidator;
import com.xchat.oauth2.service.provider.TokenGranter;
import com.xchat.oauth2.service.provider.approval.ApprovalStore;
import com.xchat.oauth2.service.provider.approval.ApprovalStoreUserApprovalHandler;
import com.xchat.oauth2.service.provider.approval.TokenApprovalStore;
import com.xchat.oauth2.service.provider.approval.TokenStoreUserApprovalHandler;
import com.xchat.oauth2.service.provider.approval.UserApprovalHandler;
import com.xchat.oauth2.service.provider.client.ClientCredentialsTokenGranter;
import com.xchat.oauth2.service.provider.client.InMemoryClientDetailsService;
import com.xchat.oauth2.service.provider.code.AuthorizationCodeServices;
import com.xchat.oauth2.service.provider.code.AuthorizationCodeTokenGranter;
import com.xchat.oauth2.service.provider.code.InMemoryAuthorizationCodeServices;
import com.xchat.oauth2.service.provider.endpoint.FrameworkEndpointHandlerMapping;
import com.xchat.oauth2.service.provider.error.DefaultWebResponseExceptionTranslator;
import com.xchat.oauth2.service.provider.error.WebResponseExceptionTranslator;
import com.xchat.oauth2.service.provider.implicit.ImplicitTokenGranter;
import com.xchat.oauth2.service.provider.password.ResourceOwnerPasswordTokenGranter;
import com.xchat.oauth2.service.provider.refresh.RefreshTokenGranter;
import com.xchat.oauth2.service.provider.request.DefaultOAuth2RequestFactory;
import com.xchat.oauth2.service.provider.request.DefaultOAuth2RequestValidator;
import com.xchat.oauth2.service.provider.token.AccessTokenConverter;
import com.xchat.oauth2.service.provider.token.AuthorizationServerTokenServices;
import com.xchat.oauth2.service.provider.token.ConsumerTokenServices;
import com.xchat.oauth2.service.provider.token.DefaultAccessTokenConverter;
import com.xchat.oauth2.service.provider.token.DefaultTokenServices;
import com.xchat.oauth2.service.provider.token.ResourceServerTokenServices;
import com.xchat.oauth2.service.provider.token.TokenEnhancer;
import com.xchat.oauth2.service.provider.token.TokenStore;
import com.xchat.oauth2.service.provider.token.store.InMemoryTokenStore;
import com.xchat.oauth2.service.provider.token.store.JwtAccessTokenConverter;
import com.xchat.oauth2.service.provider.token.store.JwtTokenStore;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Configure the properties and enhanced functionality of the Authorization Server endpoints.
 * 
 * @author Rob Winch
 * @author Dave Syer
 * @since 2.0
 */
public final class AuthorizationServerEndpointsConfigurer {

	private AuthorizationServerTokenServices tokenServices;

	private ConsumerTokenServices consumerTokenServices;

	private AuthorizationCodeServices authorizationCodeServices;

	private ResourceServerTokenServices resourceTokenServices;

	private TokenStore tokenStore;

	private TokenEnhancer tokenEnhancer;

	private AccessTokenConverter accessTokenConverter;

	private ApprovalStore approvalStore;

	private TokenGranter tokenGranter;

	private OAuth2RequestFactory requestFactory;

	private OAuth2RequestValidator requestValidator;

	private UserApprovalHandler userApprovalHandler;

	private AuthenticationManager authenticationManager;

	private ClientDetailsService clientDetailsService;

	private String prefix;

	private Map<String, String> patternMap = new HashMap<String, String>();

	private Set<HttpMethod> allowedTokenEndpointRequestMethods = new HashSet<HttpMethod>();

	private FrameworkEndpointHandlerMapping frameworkEndpointHandlerMapping;

	private boolean approvalStoreDisabled;

	private List<Object> interceptors = new ArrayList<Object>();

	private DefaultTokenServices defaultTokenServices;

	private boolean tokenServicesOverride = false;

	private boolean reuseRefreshToken = true;

	private WebResponseExceptionTranslator exceptionTranslator;

	public AuthorizationServerTokenServices getTokenServices() {
		return tokenServices;
	}

	public TokenStore getTokenStore() {
		return tokenStore();
	}

	public TokenEnhancer getTokenEnhancer() {
		return tokenEnhancer;
	}

	public AccessTokenConverter getAccessTokenConverter() {
		return accessTokenConverter;
	}

	public ApprovalStore getApprovalStore() {
		return approvalStore;
	}

	public ClientDetailsService getClientDetailsService() {
		return clientDetailsService;
	}

	public OAuth2RequestFactory getOAuth2RequestFactory() {
		return requestFactory();
	}

	public OAuth2RequestValidator getOAuth2RequestValidator() {
		return requestValidator();
	}

	public UserApprovalHandler getUserApprovalHandler() {
		return userApprovalHandler();
	}

	public AuthorizationServerEndpointsConfigurer tokenStore(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
		return this;
	}

	public AuthorizationServerEndpointsConfigurer tokenEnhancer(TokenEnhancer tokenEnhancer) {
		this.tokenEnhancer = tokenEnhancer;
		return this;
	}

	public AuthorizationServerEndpointsConfigurer reuseRefreshTokens(boolean reuseRefreshToken) {
		this.reuseRefreshToken = reuseRefreshToken;
		return this;
	}

	public AuthorizationServerEndpointsConfigurer accessTokenConverter(AccessTokenConverter accessTokenConverter) {
		this.accessTokenConverter = accessTokenConverter;
		return this;
	}

	public AuthorizationServerEndpointsConfigurer tokenServices(AuthorizationServerTokenServices tokenServices) {
		this.tokenServices = tokenServices;
		this.tokenServicesOverride = true;
		return this;
	}

	public boolean isTokenServicesOverride() {
		return tokenServicesOverride;
	}

	public AuthorizationServerEndpointsConfigurer userApprovalHandler(UserApprovalHandler approvalHandler) {
		this.userApprovalHandler = approvalHandler;
		return this;
	}

	public AuthorizationServerEndpointsConfigurer approvalStore(ApprovalStore approvalStore) {
		if (approvalStoreDisabled) {
			throw new IllegalStateException("ApprovalStore was disabled");
		}
		this.approvalStore = approvalStore;
		return this;
	}

	public AuthorizationServerEndpointsConfigurer approvalStoreDisabled() {
		this.approvalStoreDisabled = true;
		return this;
	}

	public AuthorizationServerEndpointsConfigurer prefix(String prefix) {
		this.prefix = prefix;
		return this;
	}

	public AuthorizationServerEndpointsConfigurer pathMapping(String defaultPath, String customPath) {
		this.patternMap.put(defaultPath, customPath);
		return this;
	}

	public AuthorizationServerEndpointsConfigurer addInterceptor(HandlerInterceptor interceptor) {
		this.interceptors.add(interceptor);
		return this;
	}

	public AuthorizationServerEndpointsConfigurer addInterceptor(WebRequestInterceptor interceptor) {
		this.interceptors.add(interceptor);
		return this;
	}

	public AuthorizationServerEndpointsConfigurer exceptionTranslator(WebResponseExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
		return this;
	}

	/**
	 * The AuthenticationManager for the password grant.
	 * 
	 * @param builder an AuthenticationManager, fully initialized
	 * @return this for a fluent style
	 */
	public AuthorizationServerEndpointsConfigurer authenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		return this;
	}

	public AuthorizationServerEndpointsConfigurer tokenGranter(TokenGranter tokenGranter) {
		this.tokenGranter = tokenGranter;
		return this;
	}

	/**
	 * N.B. this method is not part of the public API. To set up a custom ClientDetailsService please use
	 * {@link com.xchat.oauth2.service.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter#configure(com.xchat.oauth2.service.config.annotation.configurers.ClientDetailsServiceConfigurer)}.
	 */
	public void setClientDetailsService(ClientDetailsService clientDetailsService) {
		this.clientDetailsService = clientDetailsService;
	}

	public AuthorizationServerEndpointsConfigurer requestFactory(OAuth2RequestFactory requestFactory) {
		this.requestFactory = requestFactory;
		return this;
	}

	public AuthorizationServerEndpointsConfigurer requestValidator(OAuth2RequestValidator requestValidator) {
		this.requestValidator = requestValidator;
		return this;
	}

	public AuthorizationServerEndpointsConfigurer authorizationCodeServices(
			AuthorizationCodeServices authorizationCodeServices) {
		this.authorizationCodeServices = authorizationCodeServices;
		return this;
	}

	public AuthorizationServerEndpointsConfigurer allowedTokenEndpointRequestMethods(HttpMethod... requestMethods) {
		Collections.addAll(allowedTokenEndpointRequestMethods, requestMethods);
		return this;
	}

	public ConsumerTokenServices getConsumerTokenServices() {
		return consumerTokenServices();
	}

	public ResourceServerTokenServices getResourceServerTokenServices() {
		return resourceTokenServices();
	}

	public AuthorizationCodeServices getAuthorizationCodeServices() {
		return authorizationCodeServices();
	}

	public Set<HttpMethod> getAllowedTokenEndpointRequestMethods() {
		return allowedTokenEndpointRequestMethods();
	}

	public OAuth2RequestValidator getRequestValidator() {
		return requestValidator();
	}

	public TokenGranter getTokenGranter() {
		return tokenGranter();
	}

	public FrameworkEndpointHandlerMapping getFrameworkEndpointHandlerMapping() {
		return frameworkEndpointHandlerMapping();
	}

	public WebResponseExceptionTranslator getExceptionTranslator() {
		return exceptionTranslator();
	}

	private ResourceServerTokenServices resourceTokenServices() {
		if (resourceTokenServices == null) {
			if (tokenServices instanceof ResourceServerTokenServices) {
				return (ResourceServerTokenServices) tokenServices;
			}
			resourceTokenServices = createDefaultTokenServices();
		}
		return resourceTokenServices;
	}

	private Set<HttpMethod> allowedTokenEndpointRequestMethods() {
		// HTTP POST should be the only allowed endpoint request method by default.
		if (allowedTokenEndpointRequestMethods.isEmpty()) {
			allowedTokenEndpointRequestMethods.add(HttpMethod.POST);
		}
		return allowedTokenEndpointRequestMethods;
	}

	private ConsumerTokenServices consumerTokenServices() {
		if (consumerTokenServices == null) {
			if (tokenServices instanceof ConsumerTokenServices) {
				return (ConsumerTokenServices) tokenServices;
			}
			consumerTokenServices = createDefaultTokenServices();
		}
		return consumerTokenServices;
	}

	private AuthorizationServerTokenServices tokenServices() {
		if (tokenServices != null) {
			return tokenServices;
		}
		this.tokenServices = createDefaultTokenServices();
		return tokenServices;
	}

	public AuthorizationServerTokenServices getDefaultAuthorizationServerTokenServices() {
		if (defaultTokenServices != null) {
			return defaultTokenServices;
		}
		this.defaultTokenServices = createDefaultTokenServices();
		return this.defaultTokenServices;
	}

	private DefaultTokenServices createDefaultTokenServices() {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setTokenStore(tokenStore());
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setReuseRefreshToken(reuseRefreshToken);
		tokenServices.setClientDetailsService(clientDetailsService());
		tokenServices.setTokenEnhancer(tokenEnhancer());
		return tokenServices;
	}

	private TokenEnhancer tokenEnhancer() {
		if (this.tokenEnhancer == null && accessTokenConverter() instanceof JwtAccessTokenConverter) {
			tokenEnhancer = (TokenEnhancer) accessTokenConverter;
		}
		return this.tokenEnhancer;
	}

	private AccessTokenConverter accessTokenConverter() {
		if (this.accessTokenConverter == null) {
			accessTokenConverter = new DefaultAccessTokenConverter();
		}
		return this.accessTokenConverter;
	}

	private TokenStore tokenStore() {
		if (tokenStore == null) {
			if (accessTokenConverter() instanceof JwtAccessTokenConverter) {
				this.tokenStore = new JwtTokenStore((JwtAccessTokenConverter) accessTokenConverter());
			}
			else {
				this.tokenStore = new InMemoryTokenStore();
			}
		}
		return this.tokenStore;
	}

	private ApprovalStore approvalStore() {
		if (approvalStore == null && tokenStore() != null && !isApprovalStoreDisabled()) {
			TokenApprovalStore tokenApprovalStore = new TokenApprovalStore();
			tokenApprovalStore.setTokenStore(tokenStore());
			this.approvalStore = tokenApprovalStore;
		}
		return this.approvalStore;
	}

	private boolean isApprovalStoreDisabled() {
		return approvalStoreDisabled || (tokenStore() instanceof JwtTokenStore);
	}

	private ClientDetailsService clientDetailsService() {
		if (clientDetailsService == null) {
			this.clientDetailsService = new InMemoryClientDetailsService();
		}
		return this.clientDetailsService;
	}

	private UserApprovalHandler userApprovalHandler() {
		if (userApprovalHandler == null) {
			if (approvalStore() != null) {
				ApprovalStoreUserApprovalHandler handler = new ApprovalStoreUserApprovalHandler();
				handler.setApprovalStore(approvalStore());
				handler.setRequestFactory(requestFactory());
				handler.setClientDetailsService(clientDetailsService);
				this.userApprovalHandler = handler;
			}
			else if (tokenStore() != null) {
				TokenStoreUserApprovalHandler userApprovalHandler = new TokenStoreUserApprovalHandler();
				userApprovalHandler.setTokenStore(tokenStore());
				userApprovalHandler.setClientDetailsService(clientDetailsService());
				userApprovalHandler.setRequestFactory(requestFactory());
				this.userApprovalHandler = userApprovalHandler;
			}
			else {
				throw new IllegalStateException("Either a TokenStore or an ApprovalStore must be provided");
			}
		}
		return this.userApprovalHandler;
	}

	private AuthorizationCodeServices authorizationCodeServices() {
		if (authorizationCodeServices == null) {
			authorizationCodeServices = new InMemoryAuthorizationCodeServices();
		}
		return authorizationCodeServices;
	}

	private WebResponseExceptionTranslator exceptionTranslator() {
		if (exceptionTranslator != null) {
			return exceptionTranslator;
		}
		exceptionTranslator = new DefaultWebResponseExceptionTranslator();
		return exceptionTranslator;
	}

	private OAuth2RequestFactory requestFactory() {
		if (requestFactory != null) {
			return requestFactory;
		}
		requestFactory = new DefaultOAuth2RequestFactory(clientDetailsService());
		return requestFactory;
	}

	private OAuth2RequestValidator requestValidator() {
		if (requestValidator != null) {
			return requestValidator;
		}
		requestValidator = new DefaultOAuth2RequestValidator();
		return requestValidator;
	}

	private TokenGranter tokenGranter() {
		if (tokenGranter == null) {
			ClientDetailsService clientDetails = clientDetailsService();
			AuthorizationServerTokenServices tokenServices = tokenServices();
			AuthorizationCodeServices authorizationCodeServices = authorizationCodeServices();
			OAuth2RequestFactory requestFactory = requestFactory();

			List<TokenGranter> tokenGranters = new ArrayList<TokenGranter>();
			tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices,
					clientDetails, requestFactory));
			tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetails, requestFactory));
			ImplicitTokenGranter implicit = new ImplicitTokenGranter(tokenServices, clientDetails, requestFactory);
			tokenGranters.add(implicit);
			tokenGranters.add(new ClientCredentialsTokenGranter(tokenServices, clientDetails, requestFactory));
			if (authenticationManager != null) {
				tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices,
						clientDetails, requestFactory));
			}
			tokenGranter = new CompositeTokenGranter(tokenGranters);
		}
		return tokenGranter;
	}

	private FrameworkEndpointHandlerMapping frameworkEndpointHandlerMapping() {
		if (frameworkEndpointHandlerMapping == null) {
			frameworkEndpointHandlerMapping = new FrameworkEndpointHandlerMapping();
			frameworkEndpointHandlerMapping.setMappings(patternMap);
			frameworkEndpointHandlerMapping.setPrefix(prefix);
			frameworkEndpointHandlerMapping.setInterceptors(interceptors.toArray());
		}
		return frameworkEndpointHandlerMapping;
	}

}
