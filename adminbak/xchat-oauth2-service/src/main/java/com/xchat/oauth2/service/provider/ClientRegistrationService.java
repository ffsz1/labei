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
package com.xchat.oauth2.service.provider;

import java.util.List;

/**
 * Interface for client registration, handling add, update and remove of {@link com.xchat.oauth2.service.provider.ClientDetails} from an Authorization
 * Server.
 * 
 * @author Dave Syer
 * 
 */
public interface ClientRegistrationService {

	void addClientDetails(com.xchat.oauth2.service.provider.ClientDetails clientDetails) throws ClientAlreadyExistsException;

	void updateClientDetails(com.xchat.oauth2.service.provider.ClientDetails clientDetails) throws com.xchat.oauth2.service.provider.NoSuchClientException;

	void updateClientSecret(String clientId, String secret) throws com.xchat.oauth2.service.provider.NoSuchClientException;

	void removeClientDetails(String clientId) throws com.xchat.oauth2.service.provider.NoSuchClientException;
	
	List<ClientDetails> listClientDetails();

}
