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
package com.xchat.oauth2.service.client.filter.state;

import com.xchat.oauth2.service.client.resource.OAuth2ProtectedResourceDetails;
import com.xchat.oauth2.service.common.util.RandomValueStringGenerator;

/**
 * @author Dave Syer
 *
 */
public class DefaultStateKeyGenerator implements com.xchat.oauth2.service.client.filter.state.StateKeyGenerator {

	private RandomValueStringGenerator generator = new RandomValueStringGenerator();

	public String generateKey(OAuth2ProtectedResourceDetails resource) {
		return generator.generate();
	}

}
