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

package com.xchat.oauth2.service.config.annotation.web.configuration;

import com.xchat.oauth2.service.config.annotation.configurers.ClientDetailsServiceConfigurer;
import com.xchat.oauth2.service.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import com.xchat.oauth2.service.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

/**
 * Convenient strategy for configuring an OAUth2 Authorization Server. Beans of this type are applied to the Spring
 * context automatically if you {@link com.xchat.oauth2.service.config.annotation.web.configuration.EnableAuthorizationServer @EnableAuthorizationServer}.
 * 
 * @author Dave Syer
 * 
 */
public interface AuthorizationServerConfigurer {

	/**
	 * Configure the security of the Authorization Server, which means in practical terms the /oauth/token endpoint. The
	 * /oauth/authorize endpoint also needs to be secure, but that is a normal user-facing endpoint and should be
	 * secured the same way as the rest of your UI, so is not covered here. The default settings cover the most common
	 * requirements, following recommendations from the OAuth2 spec, so you don't need to do anything here to get a
	 * basic server up and running.
	 * 
	 * @param security a fluent configurer for security features
	 */
	void configure(AuthorizationServerSecurityConfigurer security) throws Exception;

	/**
	 * Configure the {@link com.xchat.oauth2.service.provider.ClientDetailsService}, e.g. declaring individual clients and their properties. Note that
	 * password grant is not enabled (even if some clients are allowed it) unless an {@link org.springframework.security.authentication.AuthenticationManager} is
	 * supplied to the {@link #configure(com.xchat.oauth2.service.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer)}. At least one client, or a fully
	 * formed custom {@link com.xchat.oauth2.service.provider.ClientDetailsService} must be declared or the server will not start.
	 * 
	 * @param clients the client details configurer
	 */
	void configure(ClientDetailsServiceConfigurer clients) throws Exception;

	/**
	 * Configure the non-security features of the Authorization Server endpoints, like token store, token
	 * customizations, user approvals and grant types. You shouldn't need to do anything by default, unless you need
	 * password grants, in which case you need to provide an {@link org.springframework.security.authentication.AuthenticationManager}.
	 * 
	 * @param endpoints the endpoints configurer
	 */
	void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception;

}
