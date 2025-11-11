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
package com.xchat.oauth2.service.client.token;

import org.springframework.http.HttpHeaders;
import com.xchat.oauth2.service.client.resource.OAuth2ProtectedResourceDetails;

import org.springframework.util.MultiValueMap;

public interface RequestEnhancer {
	
	void enhance(AccessTokenRequest request, OAuth2ProtectedResourceDetails resource, MultiValueMap<String, String> form,
				 HttpHeaders headers);

}
