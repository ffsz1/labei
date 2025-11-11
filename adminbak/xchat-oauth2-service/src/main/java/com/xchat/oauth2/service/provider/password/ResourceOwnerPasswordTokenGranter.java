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

package com.xchat.oauth2.service.provider.password;

import com.xchat.oauth2.service.common.KeyStore;
import com.xchat.oauth2.service.common.exceptions.InvalidGrantException;
import com.xchat.oauth2.service.common.util.DESUtils;
import com.xchat.oauth2.service.provider.*;
import com.xchat.oauth2.service.provider.token.AbstractTokenGranter;
import com.xchat.oauth2.service.provider.token.AuthorizationServerTokenServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Dave Syer
 */
public class ResourceOwnerPasswordTokenGranter extends AbstractTokenGranter {
    private static final Logger LOG = LoggerFactory.getLogger(ResourceOwnerPasswordTokenGranter.class);

    private static final String GRANT_TYPE = "password";

    private final AuthenticationManager authenticationManager;

    public ResourceOwnerPasswordTokenGranter(AuthenticationManager authenticationManager,
                                             AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        String username = parameters.get("phone");
        String password = parameters.get("password");
        // 加入密码DES解密
        String os = parameters.get("os");
        try {
            password = DESUtils.DESAndBase64Decrypt(password, KeyStore.DES_ENCRYPT_KEY);
        } catch (Exception e) {
            LOG.error("password decrypt error. username:{} msg:{}", username, e);
            throw new BadCredentialsException("password illegal.");
        }

        // Protect from downstream leaks of password
        parameters.remove("password");

        Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            userAuth = authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException ase) {
            //covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
            throw new InvalidGrantException(ase.getMessage());
        } catch (BadCredentialsException e) {
            // If the username/password are wrong the spec says we should send 400/invlid grant
            throw new InvalidGrantException("账号或密码错误!");
        }
        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate user: " + username);
        }

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}
