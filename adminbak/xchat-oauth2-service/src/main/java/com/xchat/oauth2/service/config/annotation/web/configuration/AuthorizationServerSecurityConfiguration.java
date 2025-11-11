/*
 * Copyright 2002-2013 the original author or authors.
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
package com.xchat.oauth2.service.config.annotation.web.configuration;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import com.xchat.oauth2.service.config.annotation.configuration.ClientDetailsServiceConfiguration;
import com.xchat.oauth2.service.config.annotation.configurers.ClientDetailsServiceConfigurer;
import com.xchat.oauth2.service.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import com.xchat.oauth2.service.provider.ClientDetailsService;
import com.xchat.oauth2.service.provider.endpoint.FrameworkEndpointHandlerMapping;

/**
 * @author Rob Winch
 * @author Dave Syer
 * 
 */
@Configuration
@Order(0)
@Import({ ClientDetailsServiceConfiguration.class, AuthorizationServerEndpointsConfiguration.class })
public class AuthorizationServerSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private List<com.xchat.oauth2.service.config.annotation.web.configuration.AuthorizationServerConfigurer> configurers = Collections.emptyList();

	@Autowired
	private ClientDetailsService clientDetailsService;

	@Autowired
	private AuthorizationServerEndpointsConfiguration endpoints;

	@Autowired
	public void configure(ClientDetailsServiceConfigurer clientDetails) throws Exception {
		for (com.xchat.oauth2.service.config.annotation.web.configuration.AuthorizationServerConfigurer configurer : configurers) {
			configurer.configure(clientDetails);
		}
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		AuthorizationServerSecurityConfigurer configurer = new AuthorizationServerSecurityConfigurer();
		FrameworkEndpointHandlerMapping handlerMapping = endpoints.oauth2EndpointHandlerMapping();
		http.setSharedObject(FrameworkEndpointHandlerMapping.class, handlerMapping);
		configure(configurer);
		http.apply(configurer);
		String tokenEndpointPath = handlerMapping.getServletPath("/oauth/token");
		String tokenKeyPath = handlerMapping.getServletPath("/oauth/token_key");
		String checkTokenPath = handlerMapping.getServletPath("/oauth/check_token");
		// @formatter:off
		http
        	.authorizeRequests()
            	.antMatchers(tokenEndpointPath).fullyAuthenticated()
            	.antMatchers(tokenKeyPath).access(configurer.getTokenKeyAccess())
            	.antMatchers(checkTokenPath).access(configurer.getCheckTokenAccess())
        .and()
        	.requestMatchers()
            	.antMatchers(tokenEndpointPath, tokenKeyPath, checkTokenPath)
        .and()
        	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
		// @formatter:on
		http.setSharedObject(ClientDetailsService.class, clientDetailsService);
	}

	protected void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		for (com.xchat.oauth2.service.config.annotation.web.configuration.AuthorizationServerConfigurer configurer : configurers) {
			configurer.configure(oauthServer);
		}
	}

}
