/*
 * Copyright 2012-2013 the original author or authors.
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

package com.xchat.oauth2.service.provider.approval;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import com.xchat.oauth2.service.common.OAuth2AccessToken;
import com.xchat.oauth2.service.provider.OAuth2Authentication;
import com.xchat.oauth2.service.provider.approval.Approval.ApprovalStatus;
import com.xchat.oauth2.service.provider.token.TokenStore;

/**
 * An {@link com.xchat.oauth2.service.provider.approval.ApprovalStore} that works with an existing {@link com.xchat.oauth2.service.provider.token.TokenStore}, extracting implicit {@link com.xchat.oauth2.service.provider.approval.Approval
 * Approvals} from the content of tokens already in the store. Useful interface so that users can list and revoke
 * approvals even if they are not really represented in such a way internally. For full fine-grained control of user
 * approvals don't use a TokenStore at all, and don't use this ApprovalStore with Approval-based
 * {@link com.xchat.oauth2.service.provider.token.AuthorizationServerTokenServices} implementations.
 * 
 * @author Dave Syer
 * 
 */
public class TokenApprovalStore implements com.xchat.oauth2.service.provider.approval.ApprovalStore {

	private TokenStore store;

	/**
	 * @param store the token store to set
	 */
	public void setTokenStore(TokenStore store) {
		this.store = store;
	}

	/**
	 * This implementation is a no-op. We assume that the {@link com.xchat.oauth2.service.provider.token.TokenStore} is populated elsewhere, by (for example) a
	 * token services instance that knows more about granted tokens than we could possibly infer from the approvals.
	 * 
	 * @see com.xchat.oauth2.service.provider.approval.ApprovalStore#addApprovals(java.util.Collection)
	 */
	@Override
	public boolean addApprovals(Collection<com.xchat.oauth2.service.provider.approval.Approval> approvals) {
		return true;
	}

	/**
	 * Revoke all tokens that match the client and user in the approvals supplied.
	 * 
	 * @see com.xchat.oauth2.service.provider.approval.ApprovalStore#revokeApprovals(java.util.Collection)
	 */
	@Override
	public boolean revokeApprovals(Collection<com.xchat.oauth2.service.provider.approval.Approval> approvals) {
		boolean success = true;
		for (com.xchat.oauth2.service.provider.approval.Approval approval : approvals) {
			Collection<OAuth2AccessToken> tokens = store.findTokensByClientIdAndUserName(approval.getClientId(), approval.getUserId());
			for (OAuth2AccessToken token : tokens) {
				OAuth2Authentication authentication = store.readAuthentication(token);
				if (authentication != null
						&& approval.getClientId().equals(authentication.getOAuth2Request().getClientId())) {
					store.removeAccessToken(token);
				}
			}
		}
		return success;
	}

	/**
	 * Extract the implied approvals from any tokens associated with the user and client id supplied.
	 * 
	 * @see com.xchat.oauth2.service.provider.approval.ApprovalStore#getApprovals(String,
	 * String)
	 */
	@Override
	public Collection<com.xchat.oauth2.service.provider.approval.Approval> getApprovals(String userId, String clientId) {
		Collection<com.xchat.oauth2.service.provider.approval.Approval> result = new HashSet<com.xchat.oauth2.service.provider.approval.Approval>();
		Collection<OAuth2AccessToken> tokens = store.findTokensByClientIdAndUserName(clientId, userId);
		for (OAuth2AccessToken token : tokens) {
			OAuth2Authentication authentication = store.readAuthentication(token);
			if (authentication != null) {
				Date expiresAt = token.getExpiration();
				for (String scope : token.getScope()) {
					result.add(new Approval(userId, clientId, scope, expiresAt, ApprovalStatus.APPROVED));
				}
			}
		}
		return result;
	}

}
